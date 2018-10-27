package tagmem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli.*;

import tagmem.dao.json.JSONMemoryDao;
import tagmem.utils.EntryFormatter;

public class TagMemClient {
	
	private Memory memory;
	
	public TagMemClient(String dataFilePath) {
		JSONMemoryDao dao = new JSONMemoryDao(dataFilePath);
		this.memory = dao.load();
	}
	
	public String search(List<String> tags, EntryFormatter entryFormatter) {
		List<Entry> results = this.memory.search(tags);
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
	
	public static void main(String [] args) {
		
		Options options = new Options();
		
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
		
		options.addOption(dataFilePath);
		options.addOption(interactiveMode);
		options.addOption(searchMode);
		options.addOption(formatFlag);
		options.addOption(matchFlag);
		
		CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("TagMem", options);

            System.exit(1);
        }
		
		TagMemClient cli = new TagMemClient(cmd.getOptionValue("dataFilePath"));
		
		//should be tags to search? if in search mode. 
		String [] otherArgs = cmd.getArgs();

		System.out.println("");
		if (cmd.hasOption("i")) {
			System.out.println("Welcome to interactive mode!");
			//TODO interactive mode
		} else if (cmd.hasOption("s")) {
			System.out.println("Welcome to search mode!");
			//TODO search mode
			
			String format = (cmd.hasOption("f")) ? cmd.getOptionValue("f") : "";
			EntryFormatter entryFormatter = new EntryFormatter(format);
			//TODO verbose flag to say what format and match style are used
			
			if (cmd.hasOption("m")) {
				String matchStyle = cmd.getOptionValue("m");
				System.out.println("Using match style: "+matchStyle);
				//TODO match style
			} else {
				System.out.println("Using default match style");
				//TODO default match style
			}
			
			System.out.println("Search Results");
			System.out.println("==============");
			System.out.println("");
			System.out.println(cli.search(new ArrayList<String>(Arrays.asList(otherArgs)),entryFormatter));
		}
		
	}
	
}
