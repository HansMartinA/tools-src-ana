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
import java.util.Collection;
import java.util.Map;
import marm.src.ana.SrcFileHandler;
import org.ahocorasick.trie.Token;
import org.ahocorasick.trie.Trie;

/**
 * Replaces strings in a java source code file with other strings.
 * This class uses the Aho-Corasick algorithm for replacing.
 * 
 * @author Martin Armbruster
 * @version 1.0
 * @since 1.0
 */
public class ReplaceJava2 implements SrcFileHandler
{
	/**
	 * Stores the trie with the strings to be replaced used later for the Aho-Corasick algorithm.
	 */
	private Trie keys;
	/**
	 * Stores the mapping between the strings to be replaced and the strings to be inserted.
	 */
	private Map<String, String> replaceStrings;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param stringReplaceMapping the mapping between the strings to be replaced and the strings to be inserted.
	 */
	public ReplaceJava2(Map<String, String> stringReplaceMapping)
	{
		replaceStrings = stringReplaceMapping;
		keys = Trie.builder().ignoreOverlaps().addKeywords(stringReplaceMapping.keySet()).build();
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

	/**
	 * Handles a java source code file.
	 * 
	 * @param f the java source code file in which all replaces happens.
	 */
	@Override
	public void handleFile(File f)
	{
		try
		{
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
			Collection<Token> tokens = keys.tokenize(builder.toString());
			builder.delete(0, builder.length());
			for(Token token : tokens)
			{
				if(token.isMatch())
				{
					builder.append(replaceStrings.get(token.getFragment()));
				}
				else
				{
					builder.append(token.getFragment());
				}
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write(builder.toString());
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}