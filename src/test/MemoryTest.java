package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import tagmem.Entry;
import tagmem.Memory;
import tagmem.dao.json.JSONMemoryDao;

public class MemoryTest extends TestCase{
	
	//TODO add unit tests for add and remove
	
	public void testSearch() {
		JSONMemoryDao dao = new JSONMemoryDao("testFiles/test.json");
		Memory m = dao.load();
		
		ArrayList<String> searchTags = new ArrayList<String>(Arrays.asList("test","two"));
		
		List<Entry> results = m.search(searchTags);
		
		assertEquals(1,results.size());
		
		Entry result = results.get(0);
		assertEquals(2, result.getId());
	}
	
	public void testStrictMatching() {
		JSONMemoryDao dao = new JSONMemoryDao("testFiles/test.json");
		Memory m = dao.load();
		
		ArrayList<String> searchTags = new ArrayList<String>(Arrays.asList("list","two"));
		
		List<Entry> results = m.search(searchTags,true);
		List<Entry> defaultResults = m.search(searchTags);
		
		assertEquals(results,defaultResults);
		
		assertEquals(1,results.size());
	}
	
	public void testNonStrictMatching() {
		JSONMemoryDao dao = new JSONMemoryDao("testFiles/test.json");
		Memory m = dao.load();
		
		ArrayList<String> searchTags = new ArrayList<String>(Arrays.asList("list","two"));
		
		List<Entry> results = m.search(searchTags,false);
		
		assertEquals(2,results.size());
	}
	

}
