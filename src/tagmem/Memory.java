package tagmem;

import java.util.ArrayList;
import java.util.List;

public class Memory {
	
	private List<Entry> entries;
	
	public Memory() {
		//empty constructor
	}
	
	public List<Entry> getEntries() {
		return this.entries;
	}
	
	public void setEntries(List entries) {
		this.entries = entries;
	}
	
	public void printAll() {
		for (Entry e: this.entries) {
			System.out.println(e.toString());
		}
	}
	
	public List<Entry> search(List<String> searchTags){
		List<Entry> matches = new ArrayList<Entry>();
		for (Entry entry: this.entries) {
			if (entry.getTags().containsAll(searchTags)) {
				matches.add(entry);
			}
		}
		return matches;
	}

}
