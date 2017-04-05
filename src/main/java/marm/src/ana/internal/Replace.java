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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import marm.src.ana.MultiExtensionSupportFileHandler;

/**
 * Provides an general class for replacing strings in source code files with other strings.
 * This implementation uses regular expressions for the strings and replaces every string one after another.
 * 
 * @author Martin Armbruster
 * @version 1.1
 * @since 1.1
 */
public class Replace extends MultiExtensionSupportFileHandler
{
	/**
	 * Stores the mapping between the strings to be replaced and the strings to be inserted.
	 */
	private Map<String, String> replaceStrings;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param regexReplaceMapping the mapping between the strings to be replaced and the strings to be inserted.
	 */
	public Replace(Map<String, String> regexReplaceMapping)
	{
		replaceStrings = regexReplaceMapping;
	}

	/**
	 * Handles a source code file.
	 * 
	 * @param f the source code file in which all replaces happens.
	 */
	@Override
	public void handleFile(File f)
	{
		try
		{
			// Reads the whole file.
			BufferedReader reader = new BufferedReader(new FileReader(f));
			StringBuilder builder = new StringBuilder();
			String line = reader.readLine();
			while(line!=null)
			{ 
				builder.append(line);
				builder.append("\n");
				line = reader.readLine();
			}
			reader.close();
			// Replaces all strings.
			String endResult = builder.toString();
			for(Map.Entry<String, String> ent : replaceStrings.entrySet())
			{
				endResult = endResult.replaceAll(ent.getKey(), ent.getValue());
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write(endResult);
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void reset()
	{
	}
	
	@Override
	public Replace clone()
	{
		return new Replace(replaceStrings);
	}
}