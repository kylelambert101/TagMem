package tagmem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.*;

import tagmem.dao.json.JSONMemoryDao;
import tagmem.utils.EntryFormatter;

public class TagMemClient {
	
	private JSONMemoryDao memoryDao;
	private Memory memory;
	private static Options options;
	private static CommandLineParser parser;
	private static HelpFormatter formatter;
	
	/* AUTOSAVE = whether client will automatically save new entries */
	private final boolean AUTOSAVE = true;
	
	//configure command line options
	static {
		options = new Options();
		
		Option dataFilePath = new Option("d","dataFilePath",true,"Data file to use");
		dataFilePath.setRequired(true);
		
		Option interactiveMode = new Option("i","interactive",false,"Start interactive mode");
		interactiveMode.setRequired(false);
		
		Option searchMode = new Option("s","search",false,"Run single-time search");
		searchMode.setRequired(false);
		
		Option formatFlag = new Option("f","format",true,"Search result format (i|n|v|t)");
		formatFlag.setRequired(false);
		
		Option matchFlag = new Option("m","match",true,"Match style (all|any)");
		matchFlag.setRequired(false);
		
		Option addMode = new Option("a","add",false,"Run quickadd");
		addMode.setRequired(false);
		/*
		Option addName = new Option("n","name",true,"Name for quick-add");
		addName.setRequired(false);
		
		Option addValue = new Option("v","value",true,"Value for quick-add");
		addValue.setRequired(false);
		
		Option addTags = new Option("t","tags",true,"Tags for quick-add");
		addTags.setRequired(false);
		*/
		
		Option verboseFlag = new Option("v","verbose",false,"Include extra information for debugging");
		verboseFlag.setRequired(false);
		
		options.addOption(dataFilePath);
		options.addOption(interactiveMode);
		options.addOption(searchMode);
		options.addOption(formatFlag);
		options.addOption(matchFlag);
		options.addOption(addMode);
		/*
		options.addOption(addName);
		options.addOption(addValue);
		options.addOption(addTags);
		*/
		options.addOption(verboseFlag);
		
		parser = new DefaultParser();
        formatter = new HelpFormatter();
	}
	
	public TagMemClient(String dataFilePath) {
		this.memoryDao = new JSONMemoryDao(dataFilePath);
		this.memory = this.memoryDao.load();
	}
	
	public String search(List<String> tags, EntryFormatter entryFormatter, boolean strictMatch) {
		List<Entry> results = this.memory.search(tags,strictMatch);
		StringBuffer toReturn = new StringBuffer();
		for (Entry e: results) {
			toReturn.append(entryFormatter.format(e));
			toReturn.append("\n");
		}
		if (results.size() == 0) {
			toReturn.append("--None--");
			toReturn.append("\n");
		}
		return toReturn.toString();
	}
	
	private Entry parseEntryFromQuickAddString(String args) {
		Pattern quickAddPattern = Pattern.compile("(.+): ?(.+) \\[(.+)\\]");
		Matcher quickAddMatcher = 	quickAddPattern.matcher(args);
		if (!quickAddMatcher.find()) {
			return null;
		}
		String name = quickAddMatcher.group(1);
		String value = quickAddMatcher.group(2);
		String tagString = quickAddMatcher.group(3);
		String[] tags = tagString.split(" ");
		return new Entry(this.memory.getNextEntryId(), name, value, Arrays.asList(tags));
	}
	
	public void addEntry(Entry e) {
		this.memory.add(e);
		if (this.AUTOSAVE) {
			this.memoryDao.save(memory);
		}
	}
	
	public static void addToBuffer(StringBuffer buffer,String message) {
		buffer.append(message+"\n");
	}
	
	public static void printSearchResults(String results) {
		System.out.println("Search Results");
		System.out.println("==============");
		System.out.println("");
		System.out.println(results);
	}
	
	public static void printWarning(StringBuffer warning) {
		if (warning.length()>0) {
			System.out.println("###########################");
			System.out.println("ERRORS FROM THE CURRENT RUN");
			System.out.println("");
			System.out.println(warning.toString());
			System.out.println("###########################");
			System.out.println("");
		}
	}
	
	public static void main(String [] args) {

        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("TagMem", options);

            System.exit(1);
        }
		
		TagMemClient cli = new TagMemClient(cmd.getOptionValue("dataFilePath"));
		
		String [] otherArgs = cmd.getArgs();

		StringBuffer warning = new StringBuffer();
		StringBuffer info = new StringBuffer();

		System.out.println("");
		if (cmd.hasOption("i")) {
			System.out.println("Welcome to interactive mode!");
			//TODO interactive mode
		} else if (cmd.hasOption("s")) { /*search mode*/
			
			String format = (cmd.hasOption("f")) ? cmd.getOptionValue("f") : "";
			addToBuffer(info,"Using format: "+(format == "" ? "default" : format));
			
			EntryFormatter entryFormatter = new EntryFormatter(format);
			if (!entryFormatter.isValid()) {
				String format_mesg = "Non-coding format keys were specified: "+entryFormatter.getBadArgs();
				addToBuffer(warning,format_mesg);
				addToBuffer(info,format_mesg);
			}
			
			boolean strictMatch;
			String matchStyle = "default";
			if (cmd.hasOption("m")) {
				 matchStyle = cmd.getOptionValue("m");
				if (matchStyle.equals("all")) {
					strictMatch = true;
				} else if (matchStyle.equals("any")) {
					strictMatch = false;
				} else {
					String match_mesg = "Match style not recognized: \""+matchStyle+"\"";
					addToBuffer(warning,match_mesg);
					addToBuffer(info,match_mesg);
					strictMatch = true;
				}
			} else {
				strictMatch = true;
			}
			addToBuffer(info,"Using match style: "+matchStyle);
			
			/* verbose */
			if (cmd.hasOption("v")) {
				System.out.println(info.toString());
			}
			String results = cli.search(new ArrayList<String>(Arrays.asList(otherArgs)),entryFormatter,strictMatch);
			printSearchResults(results);
			
		} else if (cmd.hasOption("a")) { /*quick-add mode*/
			/*
			if (!(cmd.hasOption("n") && cmd.hasOption("v") && cmd.hasOption("t"))){
				addToBuffer(warning,"Name, Value, and Tags are required for quick-add");
			}
			String name = cmd.getOptionValue("n");
			String value = cmd.getOptionValue("v");
			String tagString = cmd.getOptionValue("t");
			if (verbose) {
				addToBuffer(info,"Parsed values for quick-add:");
				addToBuffer(info,"Name: "+name);
				addToBuffer(info,"Value: "+value);
				addToBuffer(info,"Tags: "+tagString);
			}
			*/
			//put otherargs back together for quick add
			String addArguments = String.join(" ", otherArgs);
			Entry e = cli.parseEntryFromQuickAddString(addArguments);
			if (e == null) {
				addToBuffer(warning,"Unable to parse quick-add string: \""+addArguments+"\"");
			} else {
				addToBuffer(info,"Parsed values for quick-add:");
				addToBuffer(info,"Name: "+e.getName());
				addToBuffer(info,"Value: "+e.getValue());
				addToBuffer(info,"Tags: "+e.getTags().toString());
				
				cli.addEntry(e);
				System.out.println("Added new entry: "+e.toString());
			}	
			/* verbose */
			if (cmd.hasOption("v")) {
				System.out.println(info.toString());
			}
		}
		printWarning(warning);
		
	}
	
}
