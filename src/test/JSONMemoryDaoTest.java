package test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import junit.framework.TestCase;
import tagmem.Entry;
import tagmem.Memory;
import tagmem.dao.json.JSONMemoryDao;

public class JSONMemoryDaoTest extends TestCase{
	
	public void testLoad() {
		JSONMemoryDao dao = new JSONMemoryDao("testFiles/test.json");
		Memory m = dao.load();
		Entry e = m.getEntries().get(0);
		
		assertEquals(1,e.getId());
		assertEquals("Test Name", e.getName());
		assertEquals("Test Value",e.getValue());
		assertEquals(3,e.getTags().size());
		assert(e.getTags().contains("test"));
		assert(e.getTags().contains("tag"));
		assert(e.getTags().contains("list"));
	}
	
	/*
	 * Alright, this is dumb. To test save, load the test.json file and save it to save_test.json
	 * Then, read both files, remove all whitespace (because saving doesn't write newlines or anything)
	 * and then sort the characters in that String to nullify dissimilarities in the order in which 
	 * object fields were written. Compare the result to see if the saved files were the same. 
	 * 
	 *  Again, this isn't reeeeeally a true similarity test because if the two files have the same 
	 *  letter contents, then they will still evaluate to true. But it's close - whatever. 
	 */
	public void testSave() throws IOException {
		//Also depends on load working properly
		String inFilePath = "testFiles/test.json";
		String outFilePath = "testFiles/save_test.json";
		
		JSONMemoryDao dao = new JSONMemoryDao(inFilePath);
		Memory m = dao.load();
		
		dao.save(m,outFilePath);
		
		String originalFile = "";
		String newFile = "";
		
		Scanner originalScanner = new Scanner(new File(inFilePath));
		while (originalScanner.hasNextLine()) {
			String l = originalScanner.nextLine();
			originalFile += l;
		}
		char [] originalArray = originalFile.replaceAll("\\s","").toCharArray();
		Arrays.sort(originalArray);
		originalFile = new String(originalArray);
		originalScanner.close();
		
		Scanner newScanner = new Scanner(new File(outFilePath));
		while (newScanner.hasNextLine()) {
			String line = newScanner.nextLine();
			newFile  += line;
		}
		char [] newArray = newFile.replaceAll("\\s","").toCharArray();
		Arrays.sort(newArray);
		newFile = new String(newArray);
		newScanner.close();
		
		assert(originalFile.equals(newFile));
	}

}
