package test;

import java.util.Arrays;

import org.junit.Test;

import junit.framework.TestCase;
import tagmem.entity.Entry;
import tagmem.exception.InvalidFormatStringException;
import tagmem.utils.EntryFormatter;

public class EntryFormatterTest extends TestCase{
	
	@Test
	public void testDefault() throws InvalidFormatStringException {
		Entry e = new Entry(12,"Favorite Insect","Dragonfly",Arrays.asList("favorite","insect","dragonfly","odonata"));
		EntryFormatter ef = new EntryFormatter();
		
		assertEquals(ef.format(e),"12: Favorite Insect\n\tDragonfly\n");
	}
	
	@Test
	public void testFullGranular() throws InvalidFormatStringException {
		Entry e = new Entry(12,"Favorite Insect","Dragonfly",Arrays.asList("favorite","insect","dragonfly","odonata"));
		EntryFormatter ef = new EntryFormatter("%i\n%n\n%v\n%t");
		
		String result = ef.format(e);
		
		assert(result.contains("12"));
		assert(result.contains("Favorite Insect"));
		assert(result.contains("Dragonfly"));
		assert(result.contains(Arrays.asList("favorite","insect","dragonfly","odonata").toString()));
	}
	
	//TODO test various levels of granular
	
	@Test
	public void testPlayground() throws InvalidFormatStringException {
		//always passes - just an easy way to try out different formats
		Entry e = new Entry(12,"Favorite Insect","Dragonfly",Arrays.asList("favorite","insect","dragonfly","odonata"));
		Entry e2 = new Entry(15, "Favorite Food","Pizza", Arrays.asList("favorite","food","pizza"));
		
		for (Entry entry: Arrays.asList(e,e2)) {
			//System.out.println(new EntryFormatter("invt").format(entry));
			EntryFormatter ef = new EntryFormatter();
			
			String result = ef.format(entry);
			System.out.println(result);
			assert(result.equals(result));
		}
	}
	
	@Test
	public void testValid() throws InvalidFormatStringException {
		EntryFormatter ef = new EntryFormatter("%i\n%n\n%v\n%t");
		//will throw error if invalid, which will fail test. 
	}
	
	@Test
	public void testInvalidSpecifier() {
		Exception ex = null;
		try {
			EntryFormatter ef = new EntryFormatter("%kyle");
		} catch (InvalidFormatStringException e) {
			ex = e;
			assertEquals(e.getMessage(),"invalid entry field specifier: %k");
		}
		assertTrue(ex != null);
	}
	
	@Test
	public void testInvalidNoSpecifiers() {
		Exception ex = null;
		try {
			EntryFormatter ef = new EntryFormatter("kyle");
		} catch (InvalidFormatStringException e) {
			ex = e;
			assertEquals(e.getMessage(),"no specifiers were provided in the format string");
		}
		assertTrue(ex != null);
	}
	
	@Test
	public void testDollarSign() throws InvalidFormatStringException {
		//This was needed because EntryFormatter originally used replaceAll, which doesn't like dollar signs
		Entry e = new Entry(37,"Compound Bow - Purple - $399","Compound Bow - Purple - $399",
				Arrays.asList("compound",
				"bow",
				"wishlist",
				"wish",
				"list",
				"birthday"));
		
		EntryFormatter ef = new EntryFormatter("%i: %n");
		//System.out.println(ef.format(e));
		assertEquals(ef.format(e),"37: Compound Bow - Purple - $399");
	}
	
	@Test
	public void testNewLineAndTab() throws InvalidFormatStringException {
		Entry e = new Entry(12,"Favorite Insect","Dragonfly",Arrays.asList("favorite","insect","dragonfly","odonata"));
		EntryFormatter ef = new EntryFormatter("%i\n\t%n");
		assertEquals(ef.format(e),"12\n\tFavorite Insect");
	}

}
