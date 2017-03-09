/* ################################
 * #####    The MIT-License    ####
 * ################################
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
import java.util.regex.Pattern;
import marm.src.ana.SrcFileHandler;

/**
 * A file handler that counts all and actual lines of code in java source files.
 * Actual lines of code follow this definition: these are lines not containing whitespaces, commentaries or braces
 * with or without commentaries only.  
 * 
 * @author Martin Armbruster
 * @version 1.0
 */
public class SrcCounterJava implements SrcFileHandler
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
	 * Number of actual lines of code in all scanned files.
	 */
	private int lineCounter;
	/**
	 * Pattern to identify lines containing whitespaces only.
	 */
	private Pattern whitespaces;
	/**
	 * Pattern to identify lines containing braces with or without commentaries only.
	 */
	private Pattern singleCommentary;
	/**
	 * Pattern to identify lines containing commentaries only.
	 */
	private Pattern commentaries;
	
	/**
	 * Creates a new instance.
	 */
	public SrcCounterJava()
	{
		whitespaces = Pattern.compile("\\s*[{]?\\s*[}]?\\s*[/][/].*");
		singleCommentary = Pattern.compile("\\s*[{]?\\s*[}]?\\s*");
		commentaries = Pattern.compile("\\s*[/]?\\*.*");
		reset();
	}
	
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
	 * Returns the number of actual lines of code in all scanned files.
	 *  
	 * @return the number of actual lindes of code in all scanned files.
	 */
	public int getSrcLines()
	{
		return lineCounter;
	}

	/**
	 * Returns the extension to identify java source code files.
	 * 
	 * @return the extension to identify java source code files.
	 */
	@Override
	public String getExtension()
	{
		return "java";
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
		lineCounter = 0;
	}

	/**
	 * Analyzes a java source code file.
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
				if(!whitespaces.matcher(line).matches()&&!singleCommentary.matcher(line).matches()
						&&!commentaries.matcher(line).matches())
				{
					lineCounter++;
				}
				line = reader.readLine();
			}
			reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}