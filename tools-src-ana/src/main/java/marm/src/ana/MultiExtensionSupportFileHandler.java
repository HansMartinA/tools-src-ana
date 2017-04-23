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

/**
 * An abstract FileHandler for supporting multiple extensions.
 * For subclasses, it's not recommended to override the getExtension method because it's handled by this class.
 * Instead, overriding the clone method is necessary.
 * 
 * @author Martin Armbruster
 * @version 1.0
 * @since 1.2
 */
public abstract class MultiExtensionSupportFileHandler implements SrcFileHandler, Cloneable
{
	/**
	 * Saves the supported extension of this instance.
	 */
	private String extension = "";
	
	/**
	 * Sets the extension supported by this instance.
	 * 
	 * @param extension the extension supported by this instance.
	 */
	void setExtension(String extension)
	{
		this.extension = extension;
	}
	
	@Override
	public String getExtension()
	{
		return extension;
	}
	
	/**
	 * Clones this instance and returns a copy.
	 * Subclasses should override this method because it always returns null.
	 */
	@Override
	public MultiExtensionSupportFileHandler clone()
	{
		return null;
	}
}