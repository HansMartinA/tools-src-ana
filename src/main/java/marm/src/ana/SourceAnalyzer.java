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
import java.util.ArrayList;

/**
 * Searches for source code files in a given directory and analyzes them with special handlers.
 * 
 * @author Martin Armbruster
 * @version 1.0
 * @since 1.0
 */
public class SourceAnalyzer
{
	/**
	 * Saves all added handlers.
	 */
	private ArrayList<SrcFileHandler> handlers;
	/**
	 * Counter for all found files in the given directory.
	 */
	private int fileCounter;
	
	/**
	 * Creates a new instance of the SourceAnalyzer without registered handlers.
	 */
	public SourceAnalyzer()
	{
		handlers = new ArrayList<SrcFileHandler>();
		reset();
	}
	
	/**
	 * Adds and registers a new SrcFileHandler instance to this SourceAnalyzer. 
	 * 
	 * @param toAdd the SrcFileHandler instance to add.
	 */
	public void addSrcFileHandler(SrcFileHandler toAdd)
	{
		handlers.add(toAdd);
	}
	
	/**
	 * Resets this SourceAnalyzer instance and all registered handlers to the state after instantiation.
	 * Between two analyzes, it's necessary to call this method.
	 * Otherwise, the new analysis interferes with the previous result and can cause unexpected behavior and results. 
	 */
	public void reset()
	{
		fileCounter = 0;
		for(SrcFileHandler handler : handlers)
		{
			handler.reset();
		}
	}
	
	/**
	 * Searches for source code files in a given directory and analyzes them.
	 * The identification of files as source code files and the analysis are handled by registered SrcFileHandler
	 * instances.
	 * In case two or more SrcFileHandler instances identify one specific file as a source code file, it's ensured that
	 * all instances analyzes that file. 
	 * 
	 * @param f the directory that will be searched.
	 */
	public void analyze(File f)
	{
		File[] files = f.listFiles();
		if(files==null)
		{
			return;
		}
		for(int i=0; i<files.length; i++)
		{
			if(files[i].isFile())
			{
				handleFile(files[i]);
			}
			else if(files[i].isDirectory())
			{
				analyze(files[i]);
			}
		}
	}
	
	/**
	 * Handles a real file. If it's identified as a source code file, it will be analyzed.
	 * 
	 * @param f the file.
	 */
	private void handleFile(File f)
	{
		fileCounter++;
		for(int i=0; i<handlers.size(); i++)
		{
			SrcFileHandler cur = handlers.get(i);
			if(f.getAbsolutePath().endsWith(cur.getExtension()))
			{
				cur.handleFile(f);
			}
		}
	}
	
	/**
	 * Returns the number of total files found in the directory.
	 * 
	 * @return the number of total files found.
	 */
	public int getCounter()
	{
		return fileCounter;
	}
	
	/**
	 * Returns a registered SrcFileHandler instance that looks for a special extension.
	 * If two or more instances look for the extension, the first found instance will be returned.
	 * 
	 * @param extension the extension for that the SrcFileHandler instance is looking.
	 * @return the first found registered SrcFileHandler that looks for the extension or null if no instance is found.
	 */
	public SrcFileHandler getFileHandler(String extension)
	{
		for(int i=0; i<handlers.size(); i++)
		{
			SrcFileHandler h = handlers.get(i);
			if(h.getExtension().equals(extension))
			{
				return h;
			}
		}
		return null;
	}
}