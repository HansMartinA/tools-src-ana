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

package marm.src.ana.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import marm.src.ana.MultiExtensionSupportFileHandler;

/**
 * A file handler that counts lines of code for source code files.
 * It provides an interface to use the counter for any files and to analyze every line separately.
 * Besides, an instance of this class can be used for counting all lines of code in source code files.
 * 
 * @author Martin Armbruster
 * @version 1.1
 * @since 1.1
 */
public class SrcCounter extends MultiExtensionSupportFileHandler
{
	/**
	 * Number of total scanned files.
	 */
	private int scannedFiles;
	/**
	 * Number of total digits in all scanned files.
	 */
	private int digitCounter;
	/**
	 * Number of lines of code in all scanned files.
	 */
	private int completeLineCounter;
	
	/**
	 * Returns the number of total scanned files by this instance.
	 * 
	 * @return the number of total scanned files.
	 */
	public int getScannedFiles()
	{
		return scannedFiles;
	}
	
	/**
	 * Returns the number of digits in all scanned files.
	 * 
	 * @return the number of digits in all scanned files.
	 */
	public int getDigitCount()
	{
		return digitCounter;
	}
	
	/**
	 * Returns the number of total lines of code in all scanned files.
	 * 
	 * @return the number of total lines of code in all scanned files.
	 */
	public int getCompleteSrcLines()
	{
		return completeLineCounter;
	}
	
	/**
	 * Resets this instance for analyzing another directory.
	 */
	@Override
	public void reset()
	{
		scannedFiles = 0;
		digitCounter = 0;
		completeLineCounter = 0;
	}

	/**
	 * Analyzes a source code file.
	 * 
	 * @param f the file.
	 */
	@Override
	public void handleFile(File f)
	{
		scannedFiles++;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line = reader.readLine();
			while(line!=null)
			{ 
				completeLineCounter++;
				digitCounter += line.length();
				handleLine(line);
				line = reader.readLine();
			}
			reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles a line of a source code file.
	 * Subclasses should override this method to perform a separate action on every line, e. g. counting special lines.
	 * 
	 * @param line the line to be handled.
	 */
	protected void handleLine(String line)
	{
	}
	
	@Override
	public SrcCounter clone()
	{
		return new SrcCounter();
	}
}