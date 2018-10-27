package tagmem;

import java.util.Collection;

public class Entry {
	private int id;
	private String name;
	private String value;
	private Collection<String> tags;
	
	public Entry(int id,String name, String value, Collection<String> tags) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.tags = tags;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Collection<String> getTags() {
		return tags;
	}
	public void setTags(Collection<String> tags) {
		this.tags = tags;
	}
	public void addTag(String tag) {
		this.tags.add(tag);
	}
	public void removeTag(String tag) {
		this.tags.remove(tag);
	}
	public String toString() {
		return ("("+this.id+") "+this.name+": "+this.value+" ["+this.tags.toString()+"]");
	}

}
