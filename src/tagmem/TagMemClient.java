package tagmem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli.*;

import tagmem.dao.json.JSONMemoryDao;
import tagmem.utils.EntryFormatter;

public class TagMemClient {
	
	private Memory memory;
	private static Options options;
	private static CommandLineParser parser;
	private static HelpFormatter formatter;
	
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
		
		Option verboseFlag = new Option("v","verbose",false,"Include extra information for debugging");
		verboseFlag.setRequired(false);
		
		options.addOption(dataFilePath);
		options.addOption(interactiveMode);
		options.addOption(searchMode);
		options.addOption(formatFlag);
		options.addOption(matchFlag);
		options.addOption(verboseFlag);
		
		parser = new DefaultParser();
        formatter = new HelpFormatter();
	}
	
	public TagMemClient(String dataFilePath) {
		JSONMemoryDao dao = new JSONMemoryDao(dataFilePath);
		this.memory = dao.load();
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
		boolean verbose = cmd.hasOption("v");

		System.out.println("");
		if (cmd.hasOption("i")) {
			System.out.println("Welcome to interactive mode!");
			//TODO interactive mode
		} else if (cmd.hasOption("s")) {
			
			String format = (cmd.hasOption("f")) ? cmd.getOptionValue("f") : "";
			if (verbose) {
				addToBuffer(info,"Using format: "+format);
			}
			EntryFormatter entryFormatter = new EntryFormatter(format);
			if (!entryFormatter.isValid()) {
				String format_mesg = "Non-coding format keys were specified: "+entryFormatter.getBadArgs();
				addToBuffer(warning,format_mesg);
				if(verbose) {
					addToBuffer(info,format_mesg);
				}
			}
			//TODO verbose flag to say what format and match style are used
			
			boolean strictMatch;
			
			if (cmd.hasOption("m")) {
				String matchStyle = cmd.getOptionValue("m");
				if (verbose) {
					addToBuffer(info,"Using match style: "+matchStyle);
				}
				if (matchStyle.equals("all")) {
					strictMatch = true;
				} else if (matchStyle.equals("any")) {
					strictMatch = false;
				} else {
					String match_mesg = "Match style not recognized: \""+matchStyle+"\"";
					addToBuffer(warning,match_mesg);
					if (verbose) {
						addToBuffer(info,match_mesg);
					}
					strictMatch = true;
				}
			} else {
				strictMatch = true;
			}
			if (verbose) {
				System.out.println(info.toString());
			}
			String results = cli.search(new ArrayList<String>(Arrays.asList(otherArgs)),entryFormatter,strictMatch);
			printSearchResults(results);
			
			printWarning(warning);
			
		}
		
	}
	
}
