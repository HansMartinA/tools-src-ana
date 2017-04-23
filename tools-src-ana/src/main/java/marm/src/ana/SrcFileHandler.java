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

/**
 * SrcFileHandler instances are used to identify and analyze source code files.
 * The interface provides uniform access to different implementations without limiting their possibilities. 
 * 
 * @author Martin Armbruster
 * @version 1.0
 * @since 1.0
 */
public interface SrcFileHandler
{
	/**
	 * Returns a String used to identify a file as a source code file.
	 * The identification is based on the file extension: if it's equal to the String
	 * returned by this method, the file will be analyzed.
	 * Legal extensions include e. g. "java" or ".java" for Java source code files or
	 * "Handler.java" for Java source code files which file names end with "Handler".
	 * 
	 * @return the extension for source code file identification.
	 */
	String getExtension();
	
	/**
	 * Resets the SrcFileHandler to the state after instantiation.
	 */
	void reset();
	
	/**
	 * Analyzes a source code file.
	 * 
	 * @param f the source code file.
	 */
	void handleFile(File f);
}