/* #######################################################
 * #####    Source Code Analyzer - The MIT-License    ####
 * #######################################################
 *
 * Copyright (C) 2017, Martin Armbruster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package marm.src.ana;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import marm.src.ana.internal.Replace;
import marm.src.ana.internal.ReplaceJava2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the ReplaceJava classes.
 * 
 * @author Martin Armbruster
 * @version 1.1
 * @since 1.0
 */
public class ReplaceJavaTest
{
	/**
	 * Saves the used SourceAnalyzer instance.
	 */
	private SourceAnalyzer analyzer;
	/**
	 * Saves the location of the test directory.
	 */
	private File testDir;
	/**
	 * Temporary file for a LoremIpsum java source code file for time measurement.
	 */
	private File loremIpsumFile;
	
	/**
	 * Setups all objects for testing.
	 */
	@Before
	public void setUp()
	{
		analyzer = new SourceAnalyzer();
		testDir = new File("target"+File.separator+"test-classes");
		try
		{
			loremIpsumFile = File.createTempFile("LoremIpsum", ".java", new File(testDir, "replaceTest3"));
			BufferedWriter bw = new BufferedWriter(new FileWriter(loremIpsumFile));
			String line = readFile(testDir.getPath()+File.separator+"replace3"+File.separator+"LoremIpsum.txt")+"\n";
			for(int i=0; i<10000; i++)
			{
				bw.write(line);
			}
			bw.close();
		}
		catch(IOException ioExc)
		{
			ioExc.printStackTrace();
		}
	}
	
	/**
	 * Tests the time of the SourceAnalyzer without any file handler.
	 */
	@Test
	public void testAnalyzerTime()
	{
		long time = System.nanoTime();
		analyzer.analyze(new File(testDir, "eplaceTest1"));
		System.out.println("Time for the analyzer: "+(System.nanoTime()-time));
		assertEquals(readFile(testDir.getPath()+File.separator+"expectedResults"+File.separator
				+"TestClassBeforeReplacing.java"), readFile(testDir.getPath()+File.separator+"noReplace"+File.separator
				+"TestClass.java"));
	}
	
	/**
	 * Tests the ReplaceJava class with time measurement.
	 */
	@Test
	public void testReplaceWithTime()
	{
		HashMap<String, String> replacements = new HashMap<String, String>();
		replacements.put("2017", "7102");
		replacements.put("Martin", "");
		replacements.put("//xxxx", "/**\n\t * This method does something.\n\t */");
		replacements.put("throw new RuntimeException[(]\\\"Not implemented!\\\"[)];", "print(\"Ah\");");
		analyzer.addSrcFileHandler(new Replace(replacements), ".java");
		long time = System.nanoTime();
		analyzer.analyze(new File(testDir, "replaceTest1"));
		System.out.println("Time for the replacing using regular expressions: "+(System.nanoTime()-time));
		assertEquals(readFile(testDir.getPath()+File.separator+"expectedResults"+File.separator
				+"TestClassAfterReplacing.java"), readFile(testDir.getPath()+File.separator+"replaceTest1"
				+File.separator+"TestClass.java"));
	}
	
	/**
	 * Tests the ReplaceJava2 class with time measurement.
	 */
	@Test
	public void testReplace2WithTime()
	{
		HashMap<String, String> replacements = new HashMap<String, String>();
		replacements.put("2017", "7102");
		replacements.put("Martin", "");
		replacements.put("//xxxx", "/**\n\t * This method does something.\n\t */");
		replacements.put("throw new RuntimeException(\"Not implemented!\");", "print(\"Ah\");");
		analyzer.addSrcFileHandler(new ReplaceJava2(replacements));
		long time = System.nanoTime();
		analyzer.analyze(new File("target\\test-classes\\replaceTest2"));
		System.out.println("Time for the replacing using the Aho-Corasick algorithm: "+(System.nanoTime()-time));
		assertEquals(readFile(testDir.getPath()+File.separator+"expectedResults"+File.separator
				+"TestClassAfterReplacing.java"), readFile(testDir.getPath()+File.separator+"replaceTest2"
				+File.separator+"TestClass.java"));
	}
	
	/**
	 * Measures time for the ReplaceJava class with a huge file content.
	 */
	@Test
	public void testReplaceWithLoremIpsum()
	{
		HashMap<String, String> replacements = new HashMap<>();
		replacements.put("dolor", "Something else.");
		replacements.put("Lorem ipsum", "Blablub");
		replacements.put("sit", "Lorem ipsum");
		replacements.put("no", "Test 5149");
		replacements.put(" ", "   ");
		replacements.put("At", "at");
		replacements.put("sed", "aber");
		replacements.put("eir", "eier");
		replacements.put(",", "");
		replacements.put("et", "und");
		analyzer.addSrcFileHandler(new Replace(replacements), ".java");
		long time = System.nanoTime();
		analyzer.analyze(new File(testDir, "replaceTest3"));
		System.out.println("Time for the LoremIpsum replacing using regular expressions: "+(System.nanoTime()-time));
	}
	
	/**
	 * Measures time for the ReplaceJava2 class with a huge file content.
	 */
	@Test(timeout = 30000)
	public void testReplace2WithLoremIpsum()
	{
		HashMap<String, String> replacements = new HashMap<>();
		replacements.put("dolor", "Something else.");
		replacements.put("Lorem ipsum", "Blablub");
		replacements.put("sit", "Lorem ipsum");
		replacements.put("no", "Test 5149");
		replacements.put(" ", "   ");
		replacements.put("At", "at");
		replacements.put("sed", "aber");
		replacements.put("eir", "eier");
		replacements.put(",", "");
		replacements.put("et", "und");
		analyzer.addSrcFileHandler(new ReplaceJava2(replacements));
		long time = System.nanoTime();
		analyzer.analyze(new File(testDir, "replaceTest3"));
		System.out.println("Time for the LoremIpsum replacing using the Aho-Corasick algorithm: "
				+(System.nanoTime()-time));
	}
	
	/**
	 * Reads the content of a file.
	 * 
	 * @param file path of the file.
	 * @return the content of the file as a String.
	 */
	private String readFile(String file)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();
			while(line != null)
			{
				builder.append(line);
				builder.append("\n");
				line = reader.readLine();
			}
			reader.close();
			return builder.toString();
		}
		catch(IOException ioExc)
		{
			return "";
		}
	}
	
	/**
	 * Cleans everything.
	 */
	@After
	public void tearDown()
	{
		if(loremIpsumFile!=null)
		{
			loremIpsumFile.delete();
		}
	}
}