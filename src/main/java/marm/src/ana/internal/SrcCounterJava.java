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

import java.util.regex.Pattern;

/**
 * A file handler that counts all and actual lines of code in java source files.
 * Actual lines of code follow this definition: these are lines not containing whitespaces, commentaries or braces
 * with or without commentaries only.  
 * 
 * @author Martin Armbruster
 * @version 1.1
 * @since 1.0
 */
public class SrcCounterJava extends SrcCounter
{
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
	
	@Override
	public void reset()
	{
		super.reset();
		lineCounter = 0;
	}

	@Override
	public void handleLine(String line)
	{
		if(!whitespaces.matcher(line).matches()&&!singleCommentary.matcher(line).matches()
				&&!commentaries.matcher(line).matches())
		{
			lineCounter++;
		}
	}
}