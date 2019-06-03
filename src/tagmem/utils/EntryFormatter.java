package tagmem.utils;

import java.util.Arrays;
import java.util.List;

import java.util.regex.*;

import tagmem.entity.Entry;
import tagmem.exception.InvalidFormatStringException;

public class EntryFormatter {
	
	private String formatString;
	private static List<String> validSpecifiers = (Arrays.asList("i","n","v","t"));
	private static final String DEFAULT_FORMAT = "%i: %n\n\t%v\n";
	
	//TODO It seems stupid to throw an error from the default constructor. Can this be changed?
	public EntryFormatter() throws InvalidFormatStringException {
		//default format
		this(DEFAULT_FORMAT);
	}
	
	//TODO Logic seems redundant -- empty constructor behaves same as constructor with empty string
	public EntryFormatter(String format) throws InvalidFormatStringException{
		this.formatString = format == ""? DEFAULT_FORMAT : format;
		this.validate();
	}
	
	private void validate() throws InvalidFormatStringException {
		Pattern specifierPattern = Pattern.compile("%(.)");
		Matcher specifierMatcher = specifierPattern.matcher(this.formatString);
		boolean matchExists=false;
		while (specifierMatcher.find()) {
			matchExists=true;
			String match = specifierMatcher.group(1);
			if (!validSpecifiers.contains(match)) {
				throw new InvalidFormatStringException("invalid entry field specifier: %"+match);
			}
		}  
		if (!matchExists){
			throw new InvalidFormatStringException("no specifiers were provided in the format string");
		}
	}
	
	/**
	 * format replaces specifiers in the formatString with the values pulled from the 
	 * given Entry
	 * 
	 * @param entry Entry to format
	 * @return Formatted string based on member boolean keys
	 */
	public String format(Entry entry) {
		return formatString
				.replace("\\n", "\n")	//fix newlines
				.replace("\\t","\t") 	//fix tabs
				.replace("%i",""+entry.getId())
				.replace("%n",entry.getName())
				.replace("%v",entry.getValue())
				.replace("%t", entry.getTags().toString());
	}
	
}
