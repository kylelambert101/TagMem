package test;


import java.util.Scanner;

import org.junit.Test;

import junit.framework.TestCase;

public class TagMemClientTest extends TestCase {

	@Test
	public static void testRun() {
		//TODO test something -- anything at all
		Scanner iScan = new Scanner(System.in);
		String answer = iScan.nextLine().toLowerCase();
		System.out.println(answer);
		System.out.println(answer.equals("y") || answer == "yes");
	}

}
