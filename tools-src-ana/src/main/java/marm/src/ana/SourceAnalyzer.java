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
import java.util.Collection;

/**
 * Searches for source code files in a given directory and analyzes them with special handlers.
 * 
 * @author Martin Armbruster
 * @version 1.3
 * @since 1.0
 */
public class SourceAnalyzer
{
	/**
	 * Saves all regular expressions to identify directories and files for ignoring.
	 */
	private ArrayList<String> ignoredFiles;
	/**
	 * Saves all regular expressions to identify files for exclusive analysis.
	 */
	private ArrayList<String> includedFiles;
	/**
	 * Indicates the behavior for a file considered to be ignored and included for analysis.
	 * When it's true, the file is included. When it's false, the file is ignored. 
	 */
	private boolean includeBeforeIgnore;
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
		ignoredFiles = new ArrayList<String>();
		includedFiles = new ArrayList<String>();
		includeBeforeIgnore = true;
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
	 * Adds and registers a MultiExtensionSupportFileHandler instance for supporting specified file extensions. 
	 * 
	 * @param handler the FileHandler instance to add.
	 * @param extensions the extensions that will be supported by handler. 
	 */
	public void addSrcFileHandler(MultiExtensionSupportFileHandler handler, String... extensions)
	{
		for(String ext : extensions)
		{
			MultiExtensionSupportFileHandler h = handler.clone();
			h.setExtension(ext);
			handlers.add(h);
		}
	}
	
	/**
	 * Adds an regular expression for ignoring directories or files.
	 * All found directories and files are compared to all registered expressions following ".*"+regex.
	 * 
	 * @param regex the regular expression to be added for ignoring directories or files.
	 */
	public void addIgnoreFile(String regex)
	{
		ignoredFiles.add(regex);
	}
	
	/**
	 * Adds an regular expression for exclusive analysis of matching files.
	 * All found files are compared to all expressions following ".*"+regex.
	 * When no expressions are added, all files are included.
	 * 
	 * @param regex the regular expression to be added.
	 */
	public void addIncludeFile(String regex)
	{
		includedFiles.add(regex);
	}
	
	/**
	 * When a file is considered to be ignored and included for analysis at the same time, the IncludeBeforeIgnore
	 * policy applies when it's activated. So, the file is included for analysis. Otherwise, such a file is ignored.
	 * Default is that the IncludeBeforeIgnore policy applies.
	 * 
	 * @param s true when the IncludeBeforeIgnore policy should apply. false otherwise.
	 */
	public void setIncludeBeforeIgnore(boolean s)
	{
		includeBeforeIgnore = s;
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
		loop: for(int i=0; i<files.length; i++)
		{
			boolean shouldInclude = false;
			if(includedFiles.size()==0)
			{
				shouldInclude = true;
			}
			for(int j=0; j<includedFiles.size(); j++)
			{
				if(files[i].getAbsolutePath().matches(".*"+includedFiles.get(j)))
				{
					shouldInclude = true;
					break;
				}
			}
			if(!shouldInclude)
			{
				continue loop;
			}
			if(shouldInclude&&includeBeforeIgnore)
			{
				for(int j=0; j<ignoredFiles.size(); j++)
				{
					if(files[i].getAbsolutePath().matches(".*"+ignoredFiles.get(j)))
					{
						continue loop;
					}
				}
			}
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
	
	/**
	 * Returns registered SrcFileHandler instances looking exactly for a special extension.
	 * 
	 * @param extension the extension for which the SrcFileHandler instances are searched.
	 * @return all registered SrcFileHandler instances looking exactly for the extension. When no instance is found,
	 * the collection is empty.
	 */
	public Collection<SrcFileHandler> getExactFileHandlers(String extension)
	{
		ArrayList<SrcFileHandler> result = new ArrayList<SrcFileHandler>();
		for(SrcFileHandler h : handlers)
		{
			if(h.getExtension().equals(extension))
			{
				result.add(h);
			}
		}
		return result;
	}
	
	/**
	 * Returns registered SrcFileHandler instances looking for extensions ending with a special extension.
	 * 
	 * @param extension the special extension.
	 * @return all registered SrcFileHandler instances looking for extensions ending with extension.
	 */
	public Collection<SrcFileHandler> getFileHandlers(String extension)
	{
		ArrayList<SrcFileHandler> result = new ArrayList<SrcFileHandler>();
		for(SrcFileHandler h : handlers)
		{
			if(h.getExtension().endsWith(extension))
			{
				result.add(h);
			}
		}
		return result;
	}
}