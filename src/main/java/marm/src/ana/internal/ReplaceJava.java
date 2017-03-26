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

import java.util.Map;

/**
 * Replaces strings following regular expressions in a java source code file with other strings.
 * This class replaces every string one after another.
 * 
 * @author Martin Armbruster
 * @version 1.0
 * @since 1.0
 */
public class ReplaceJava extends Replace
{
	/**
	 * Creates a new instance.
	 * 
	 * @param regexReplaceMapping the mapping between the strings to be replaced and the strings to be inserted.
	 */
	public ReplaceJava(Map<String, String> regexReplaceMapping)
	{
		super(regexReplaceMapping);
	}
	
	/**
	 * Returns the extension for identifying java source code files.
	 * 
	 * @return the extension for identifying java source code files.
	 */
	@Override
	public String getExtension()
	{
		return "java";
	}
	
	/**
	 * Resets the instance for another directory analysis. This does nothing here.
	 */
	@Override
	public void reset()
	{
	}
}