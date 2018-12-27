package tagmem.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tagmem.Entry;

public class EntryFormatter {
	
	static String ID_KEY = "i";
	static String NAME_KEY = "n";
	static String VALUE_KEY = "v";
	static String TAGS_KEY = "t";
	static List<String> ALL_KEYS = new ArrayList<String>(Arrays.asList(ID_KEY,NAME_KEY,VALUE_KEY,TAGS_KEY));
	
	private boolean showID;
	private boolean showName;
	private boolean showValue;
	private boolean showTags;
	private boolean pretty;
	private boolean isValid;
	private String badArgs;
	
	public EntryFormatter() {
		this("");
	}
	
	public EntryFormatter(String format) {
		this.pretty = format.isEmpty();
		this.showID = format.contains(EntryFormatter.ID_KEY);
		this.showName = format.contains(EntryFormatter.NAME_KEY);
		this.showValue = format.contains(EntryFormatter.VALUE_KEY);
		this.showTags = format.contains(EntryFormatter.TAGS_KEY);
		this.badArgs = "";
		
		this.validate(format);
	}
	
	public void validate(String formatString) {
		for (String key:EntryFormatter.ALL_KEYS) {
			formatString = formatString.replaceAll(key, "");
		}
		this.isValid =  (formatString.length()==0);
		if (!this.isValid) {
			this.badArgs = formatString;
		}
	}
	
	public boolean isValid() {
		return this.isValid;
	}
	
	public String getBadArgs() {
		return this.badArgs;
	}
	
	/**
	 * formatPretty will disregard the set of boolean keys and just show id, name, and value
	 * 
	 * @param e Entry to format
	 * @return Formatted string in the format "{ID}: {Name}\n\t {Value}
	 */
	public String formatPretty(Entry e) {
		String formatString = "%d: %s\n\t%s\n";
		return String.format(formatString, e.getId(),e.getName(),e.getValue());
	}
	
	/**
	 * format uses the set of boolean keys to determine which Entry fields to include in the
	 * formatted result string. 
	 * 
	 * Each field is separated from the next by \n\t
	 * 
	 * @param e Entry to format
	 * @return Formatted string based on member boolean keys
	 */
	public String format(Entry e) {
		if (this.pretty) {
			return this.formatPretty(e);
		} 
		
		return (String.format("%s%s%s%s", 
				(this.showID ? 		("ID: "+e.getId()+"\n\t") : ""),
				(this.showName ?	("Name: "+e.getName()+"\n\t") : ""),
				(this.showValue ? 	("Value: "+e.getValue()+"\n\t") : ""),
				(this.showTags ? 	("Tags: "+e.getTags().toString()+"\n\t") : "")
				));
	}
	
}
