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
 * A file handler that counts all and actual lines of code in XML-based files.
 * Actual lines of code follow this definition: these are lines not containing whitespaces or commentaries only.
 * 
 * @author Martin Armbruster
 * @version 1.0
 * @since 2.0
 */
public class SrcCounterXML extends SrcCounter
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
	 * Pattern to identify the beginning of comments.
	 */
	private Pattern beginComment;
	/**
	 * Pattern to identify lines in which comments end and that have other content not belonging to the comment.
	 */
	private Pattern endContentComment;
	/**
	 * Pattern to identify the end of comments.
	 */
	private Pattern endComment;
	/**
	 * Indicates whether a comment has began or not.
	 */
	private boolean inComment;
	
	/**
	 * Creates a new instance.
	 */
	public SrcCounterXML()
	{
		inComment = false;
		whitespaces = Pattern.compile("\\s*");
		beginComment = Pattern.compile(".*<!--.*");
		endContentComment = Pattern.compile(".*-->\\s*\\S+\\s*");
		endComment = Pattern.compile(".*-->.*");
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
	
	@Override
	public void reset()
	{
		super.reset();
		lineCounter = 0;
		inComment = false;
	}

	@Override
	public void handleLine(String line)
	{
		if(!whitespaces.matcher(line).matches()&&(!inComment || inComment&&endContentComment.matcher(line).matches()))
		{
			lineCounter++;
		}
		if(beginComment.matcher(line).matches())
		{
			inComment = true;
		}
		if(endComment.matcher(line).matches())
		{
			inComment = false;
		}
	}
	
	@Override
	public SrcCounterXML clone()
	{
		return new SrcCounterXML();
	}
}