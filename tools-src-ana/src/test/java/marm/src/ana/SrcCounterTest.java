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

import java.io.File;
import marm.src.ana.internal.SrcCounter;
import marm.src.ana.internal.SrcCounterJC;
import marm.src.ana.internal.SrcCounterXML;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the source counters.
 * 
 * @author Martin Armbruster
 * @version 1.0
 * @since 2.0
 */
public class SrcCounterTest
{
	/**
	 * Test instance of the SourceAnalyzer.
	 */
	private SourceAnalyzer ana;
	
	/**
	 * Sets up everything for testing.
	 */
	@Before
	public void setUp()
	{
		ana = new SourceAnalyzer();
	}
	
	/**
	 * Tests the SrcCounter class.
	 */
	@Test
	public void testSrcCounter()
	{
		int expectedCount = 46+35+32+38+39+53+10;
		ana.addSrcFileHandler(new SrcCounter(), "");
		ana.analyze(new File("target"+File.separator+"test-classes"+File.separator+"testSrcCounter"));
		SrcCounter actualCounter = (SrcCounter)ana.getFileHandler("");
		assertEquals(expectedCount, actualCounter.getCompleteSrcLines());
	}
	
	/**
	 * Tests the SrcCounterJC class with different files.
	 */
	@Test
	public void testSrcCounterJC()
	{
		ana.addSrcFileHandler(new SrcCounterJC(), ".java", ".c", ".h", ".cpp", ".cs");
		ana.analyze(new File("target"+File.separator+"test-classes"+File.separator+"testSrcCounter"));
		SrcCounterJC jc = (SrcCounterJC)ana.getFileHandler(".java");
		assertEquals(46, jc.getCompleteSrcLines());
		assertEquals(3, jc.getSrcLines());
		jc = (SrcCounterJC)ana.getFileHandler(".h");
		assertEquals(35+39, jc.getCompleteSrcLines());
		assertEquals(14, jc.getSrcLines());
		assertEquals(2, jc.getScannedFiles());
		jc = (SrcCounterJC)ana.getFileHandler(".cpp");
		assertEquals(53, jc.getCompleteSrcLines());
		assertEquals(10, jc.getSrcLines());
		jc = (SrcCounterJC)ana.getFileHandler(".c");
		assertEquals(38, jc.getCompleteSrcLines());
		assertEquals(5, jc.getSrcLines());
		jc = (SrcCounterJC)ana.getFileHandler(".cs");
		assertEquals(32, jc.getCompleteSrcLines());
		assertEquals(5, jc.getSrcLines());
	}
	
	/**
	 * Tests the SrcCounterXML class.
	 */
	@Test
	public void testSrcCounterXML()
	{
		ana.addSrcFileHandler(new SrcCounterXML(), ".xml");
		ana.analyze(new File("target"+File.separator+"test-classes"+File.separator+"testSrcCounter"));
		SrcCounterXML xml = (SrcCounterXML)ana.getFileHandler(".xml");
		assertEquals(10, xml.getCompleteSrcLines());
		assertEquals(6, xml.getSrcLines());
	}
}