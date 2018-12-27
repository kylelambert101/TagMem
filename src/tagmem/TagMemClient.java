package tagmem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.*;

import tagmem.dao.json.JSONMemoryDao;
import tagmem.utils.EntryFormatter;

public class TagMemClient {
	
	/* General TODOs
	 * - hide command
	 * - associate tag command (maybe move out to a script)
	 * - expand unit tests to cover add/remove/view features
	 * - Fix bug in view (formatter?) - tags are showing up with too many brackets
	 */
	
	private JSONMemoryDao memoryDao;
	private Memory memory;
	private static Options options;
	private static CommandLineParser parser;
	private static HelpFormatter formatter;
	
	private static final String VERSION = "1.0.0";
	//TODO More elegant way to store version
	
	/* AUTOSAVE = whether client will automatically save new entries */
	private final boolean AUTOSAVE = true;
	
	//configure command line options
	static {
		options = new Options();
		
		Option dataFilePath = new Option("d","dataFilePath",true,"Data file to use");
		dataFilePath.setRequired(false);//switched to false so that version won't require datafile
		
		Option interactiveMode = new Option("i","interactive",false,"Start interactive mode");
		interactiveMode.setRequired(false);
		
		Option searchMode = new Option("s","search",false,"Run single-time search");
		searchMode.setRequired(false);
		
		Option formatFlag = new Option("f","format",true,"Search result format (i|n|v|t)");
		formatFlag.setRequired(false);
		
		Option matchFlag = new Option("m","match",true,"Match style (all|any)");
		matchFlag.setRequired(false);
		
		Option viewFlag = new Option("c","view",true,"View (get it, \"c\" like \"see\"???)");
		viewFlag.setRequired(false);
		
		Option addMode = new Option("a","add",false,"Run quickadd");
		addMode.setRequired(false);
		
		Option removeMode = new Option("r","remove",true,"Remove");
		removeMode.setRequired(false);
		
		Option verboseFlag = new Option("v","verbose",false,"Include extra information for debugging");
		verboseFlag.setRequired(false);
		
		Option versionFlag = new Option("z","version",false,"Print TagMem.jar version");
		versionFlag.setRequired(false);
		
		options.addOption(dataFilePath);
		options.addOption(interactiveMode);
		options.addOption(searchMode);
		options.addOption(formatFlag);
		options.addOption(matchFlag);
		options.addOption(viewFlag);
		options.addOption(addMode);
		options.addOption(removeMode);
		options.addOption(verboseFlag);
		options.addOption(versionFlag);
		
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
	
	public Entry getEntryById(Integer id) throws EntryNotFoundException {
		return this.memory.getEntryById(id);
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
	
	public void removeEntry(Entry e) throws EntryNotFoundException {
		this.removeEntry(e.getId());
	}
	
	public void removeEntry(Integer id) throws EntryNotFoundException {
		this.memory.remove(id);
		if (this.AUTOSAVE) {
			this.memoryDao.save(memory);
		}
	}
	
	private boolean getApproval(Integer id) throws EntryNotFoundException {
		Entry e = this.memory.getEntryById(id);
		System.out.println("Are you sure you want to remove the following entry? (y/n)");
		System.out.println("\n"+e.toString()+"\n");
		System.out.print(">>");
		Scanner iScan = new Scanner(System.in);
		String response = iScan.nextLine().toLowerCase();
		iScan.close();
		return (response.equals("y") || response.equals("yes"));
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
        
        // version flag can just print and quit
        if (cmd.hasOption("z")) {/*version*/
			TagMemClient.printVersion();
			System.exit(0);
		} else {
			if (!cmd.hasOption("d")) {
				System.out.println("Please specify a database file with the -d flag");
				formatter.printHelp("TagMem", options);
				System.exit(1);;
			}
		}
		
		TagMemClient cli = new TagMemClient(cmd.getOptionValue("dataFilePath"));
		
		String [] otherArgs = cmd.getArgs();

		StringBuffer warning = new StringBuffer();
		StringBuffer info = new StringBuffer();
		
		//TODO get way to prevent multiple incompatible options from being specified
		// like noncompatibleoptions = (i,s,c,r,a) and if multiple exist, return with error

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
			//put otherargs back together for quick add
			String addArguments = String.join(" ", otherArgs);
			Entry e = cli.parseEntryFromQuickAddString(addArguments);
			if (e == null) {
				addToBuffer(warning,"Unable to parse quick-add string: \""+addArguments+"\"");
				addToBuffer(warning,"Please provide entry data in the following format: \n<name>: <value> [<space-separated tag list>]");
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
		} else if (cmd.hasOption("r")) {/*remove*/
			//only other parameter should be the entry id. 
			String removeParam = cmd.getOptionValue("r");
			Integer idToRemove;
			try {
				idToRemove = Integer.parseInt(removeParam);
				addToBuffer(info,"Entry ID to remove: "+idToRemove);
				boolean approved = cli.getApproval(idToRemove);
				if (approved) {
					cli.removeEntry(idToRemove);
					addToBuffer(info,"Entry "+idToRemove+" removed");
					System.out.println("Removed Entry "+idToRemove);
				} else {
					addToBuffer(info,"Entry "+idToRemove+" removal aborted");
					System.out.println("Entry removal aborted");
				}
				
			} catch (NumberFormatException e) {//if an int wasn't specified
				addToBuffer(warning,"ID to remove must be an integer");
			} catch(EntryNotFoundException e) {
				addToBuffer(warning,"Error during removal: "+e.getMessage());
			}
			/* verbose */
			if (cmd.hasOption("v")) {
				System.out.println(info.toString());
			}
			
		} else if (cmd.hasOption("c")) {
			Entry entry;
			EntryFormatter formatter = new EntryFormatter("invt");
			String viewParam = cmd.getOptionValue("c");
			Integer idToView;
			try {
				idToView = Integer.parseInt(viewParam);
				addToBuffer(info,"Entry ID to view: "+idToView);
				entry = cli.getEntryById(idToView);
				System.out.println(formatter.format(entry));
			} catch (NumberFormatException e) {//if an int wasn't specified
				addToBuffer(warning,"Entry ID must be an integer");
			} catch(EntryNotFoundException e) {
				addToBuffer(warning, e.getMessage());
			}
			/* verbose */
			if (cmd.hasOption("v")) {
				System.out.println(info.toString());
			}
		} 
		printWarning(warning);
		
	}
	
	private static void printVersion() {
		System.out.println();
		System.out.println("TagMem Library Version "+VERSION);
		System.out.println("Kyle Lambert 2018");
		System.out.println();
	}
	
}
