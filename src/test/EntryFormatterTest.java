package test;

import java.util.Arrays;

import org.junit.Test;

import junit.framework.TestCase;
import tagmem.Entry;
import tagmem.utils.EntryFormatter;

public class EntryFormatterTest extends TestCase{
	
	@Test
	public void testDefault() {
		Entry e = new Entry(12,"Favorite Insect","Dragonfly",Arrays.asList("favorite","insect","dragonfly","odonata"));
		EntryFormatter ef = new EntryFormatter("");
		
		assertEquals(ef.format(e),"12: Favorite Insect\n\tDragonfly");
	}
	
	@Test
	public void testFullGranular() {
		Entry e = new Entry(12,"Favorite Insect","Dragonfly",Arrays.asList("favorite","insect","dragonfly","odonata"));
		EntryFormatter ef = new EntryFormatter("invt");
		
		String result = ef.format(e);
		
		assert(result.contains("12"));
		assert(result.contains("Favorite Insect"));
		assert(result.contains("Dragonfly"));
		assert(result.contains(Arrays.asList("favorite","insect","dragonfly","odonata").toString()));
	}
	
	//TODO test various levels of granular (nv, ivt,...)
	
	@Test
	public void testPlayground() {
		//always passes - just an easy way to try out different formats
		Entry e = new Entry(12,"Favorite Insect","Dragonfly",Arrays.asList("favorite","insect","dragonfly","odonata"));
		Entry e2 = new Entry(15, "Favorite Food","Pizza", Arrays.asList("favorite","food","pizza"));
		
		for (Entry entry: Arrays.asList(e,e2)) {
			EntryFormatter ef = new EntryFormatter("invt");
			
			String result = ef.format(entry);
			System.out.println(result);
		}
	}

}
