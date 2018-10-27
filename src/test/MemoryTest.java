package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import tagmem.Entry;
import tagmem.Memory;
import tagmem.dao.json.JSONMemoryDao;

public class MemoryTest extends TestCase{
	
	public void testSearch() {
		JSONMemoryDao dao = new JSONMemoryDao("testFiles/test.json");
		Memory m = dao.load();
		
		ArrayList<String> searchTags = new ArrayList<String>(Arrays.asList("test","two"));
		
		List<Entry> results = m.search(searchTags);
		
		assertEquals(1,results.size());
		
		Entry result = results.get(0);
		assertEquals(2, result.getId());
	}

}
