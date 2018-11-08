package tagmem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Memory {
	
	private List<Entry> entries;
	
	public Memory() {
		//empty constructor
	}
	
	public List<Entry> getEntries() {
		return this.entries;
	}
	
	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
	public void printAll() {
		for (Entry e: this.entries) {
			System.out.println(e.toString());
		}
	}
	
	/**
	 * search iterates through all of the entries in entries and returns the list of entries
	 * with matching tags. 
	 * 
	 * @param searchTags A List of tags (strings) to match
	 * @param strictMatch true means that all tags must match for an entry to be returned; false means at least one tag must match
	 * @return
	 */
	public List<Entry> search(List<String> searchTags,boolean strictMatch){
		List<Entry> matches = new ArrayList<Entry>();
		for (Entry entry: this.entries) {
			boolean doesMatch = strictMatch ? entry.getTags().containsAll(searchTags) : !Collections.disjoint(entry.getTags(),searchTags);
			if (doesMatch) {
				matches.add(entry);
			}
		}
		return matches;
	}
	
	public List<Entry> search(List<String> searchTags){
		return this.search(searchTags,true);
	}

}
