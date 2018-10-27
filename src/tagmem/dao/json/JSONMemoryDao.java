package tagmem.dao.json;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import tagmem.Entry;
import tagmem.Memory;
import tagmem.dao.MemoryDao;

public class JSONMemoryDao implements MemoryDao {
	
	private String dataFilePath;
	private final String ENTRY_KEY = "entries";
	private final String ID_KEY = "id";
	private final String NAME_KEY = "name";
	private final String VALUE_KEY = "value";
	private final String TAGLIST_KEY = "taglist";
	
	public JSONMemoryDao(String filePath) {
		this.dataFilePath = filePath;
	}
	
	public Memory load() {
		JSONParser parser = new JSONParser();
		
		JSONObject jsonObject;
		
		//TODO is there a better way to handle these exceptions?
		try {
			jsonObject = (JSONObject) parser.parse(new FileReader(this.dataFilePath));
		} catch (IOException ie) {
			ie.printStackTrace();
			return null;
		} catch (ParseException pe) {
			pe.printStackTrace();
			return null;
		} 
		
		/*
		 * jsonObject should be an array of individual entry objects, so assume that is the case. 
		 */
		//TODO catch errors if that assumption isn't correct
		
		JSONArray entryArray = (JSONArray) jsonObject.get(ENTRY_KEY);
		ArrayList<Entry> entries = new ArrayList<Entry>();
		
		for (Object o: entryArray) {
			JSONObject jo = (JSONObject) o;
			
			//TODO put the string identifiers for each field in a static map somewhere so they're easy to change if the json format changes
			//parse id
			int id = Integer.parseInt(jo.get(ID_KEY).toString());
			
			//parse name
			String name = jo.get(NAME_KEY).toString();
			
			//parse value
			String value = jo.get(VALUE_KEY).toString();
			
			//parse tag list
			JSONArray tagList = (JSONArray) jo.get(TAGLIST_KEY);
			ArrayList<String> tags = new ArrayList<String>();
			for (Object t:tagList) {
				String tag = t.toString();
				tags.add(tag);
			}
			Entry e = new Entry(id,name,value,tags);
			entries.add(e);
		}
		
		Memory memory = new Memory();
		
		memory.setEntries(entries);
		
		return memory;
	}
	
	@SuppressWarnings("unchecked")
	public void save(Memory memory,String saveFilePath) {
		// JSONObject that will be written to the file
		JSONObject topLevelObject = new JSONObject();
		
		// JSONArray to hold entries
		JSONArray array = new JSONArray();
		
		/* Iterate through memory and add JSONObjects to JSONArray array */
		for (Entry e: memory.getEntries()) {
			JSONObject object = new JSONObject();
			object.put(ID_KEY, e.getId());
			object.put(NAME_KEY, e.getName());
			object.put(VALUE_KEY, e.getValue());
			JSONArray tagArray = new JSONArray();
			for (String tag:e.getTags()) {
				tagArray.add(tag);
			}
			object.put(TAGLIST_KEY, tagArray);
			
			array.add(object);
		}//end for each Entry e
		
		topLevelObject.put(ENTRY_KEY, array);
		
		try(FileWriter writer = new FileWriter(saveFilePath)) {
			writer.write(topLevelObject.toJSONString());
			writer.flush();
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save(Memory memory) {
		this.save(memory,this.dataFilePath);
	}

}
