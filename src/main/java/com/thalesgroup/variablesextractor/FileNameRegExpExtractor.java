/************************************************************************************
 * Copyright (c) 2004-2011,  Thales Corporate Services SAS                          *
 * Author: Robin Jarry                                                              *
 *                                                                                  *
 * The MIT License                                                                  *
 *                                                                                  *
 * Permission is hereby granted, free of charge, to any person obtaining a copy     *
 * of this software and associated documentation files (the "Software"), to deal    *
 * in the Software without restriction, including without limitation the rights     *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell        *
 * copies of the Software, and to permit persons to whom the Software is            *
 * furnished to do so, subject to the following conditions:                         *
 *                                                                                  *
 * The above copyright notice and this permission notice shall be included in       *
 * all copies or substantial portions of the Software.                              *
 *                                                                                  *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR       *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,         *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE      *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER           *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,    *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN        *
 * THE SOFTWARE.                                                                    *
 ************************************************************************************/
package com.thalesgroup.variablesextractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.selectors.SelectorUtils;

import com.thalesgroup.variablesextractor.util.ExtractionException;
import com.thalesgroup.variablesextractor.util.MultipleFilesMatchedException;
import com.thalesgroup.variablesextractor.util.NamedMatcher;
import com.thalesgroup.variablesextractor.util.NamedPattern;
import com.thalesgroup.variablesextractor.util.Util;

public class FileNameRegExpExtractor implements VariableExtractor {

    private final String file;
    private final String pattern;
    private final String baseDir;
    private final int flags;
    
    public FileNameRegExpExtractor(String file, String pattern) {
        this(file, pattern, null, 0);
    }
    
    public FileNameRegExpExtractor(String file, String pattern, int flags) {
        this(file, pattern, null, flags);
    }
    
    public FileNameRegExpExtractor(String file, String pattern, String baseDir) {
        this(file, pattern, baseDir, 0);
    }
    
    public FileNameRegExpExtractor(String file, String pattern, String baseDir, int flags) {
        this.file = file;
        this.pattern = pattern;
        this.baseDir = baseDir;
        this.flags = flags;
    }

    @Override
    public Map<String, String> extractVariables() throws ExtractionException {
        try {
            String filename = getExpandedFileName();
            NamedPattern compiledPattern = NamedPattern.compile(pattern, flags);
            NamedMatcher matcher = compiledPattern.matcher(filename);

            if (matcher.find()) {
                return matcher.namedGroups();
            } else {
                return new LinkedHashMap<String, String>();
            }
        } catch (FileNotFoundException e) {
            throw new ExtractionException("File not found: " + e.getMessage());
        } catch (MultipleFilesMatchedException e) {
            throw new ExtractionException("Multiple files match with the given pattern: " + e.getMessage());
        } catch (PatternSyntaxException e) {
            throw new ExtractionException("Invalid regexp pattern: " + e.getMessage(), e);
        }
    }

    private String getExpandedFileName() throws FileNotFoundException,
            MultipleFilesMatchedException
    {
        DirectoryScanner ds = new DirectoryScanner();
        String baseDir = guessBaseDirFromFile();
        String file = this.file;
        if (baseDir == null && this.baseDir != null) {
            baseDir = this.baseDir;
        } else {
            baseDir = "./";
        }
        if (file.startsWith(baseDir)) {
            file = file.substring(baseDir.length());
            while (file.startsWith(File.separator)) {
                file = file.substring(1);
            }
        }
        ds.setBasedir(baseDir);
        ds.setIncludes(new String[] { file });
        ds.scan();
        String[] files = ds.getIncludedFiles();
        if (files.length == 1) {
            File f = new File(files[0]);
            return f.getName();
        } else if (files.length == 0) {
            throw new FileNotFoundException(baseDir + file);
        } else {
            throw new MultipleFilesMatchedException(baseDir + file);
        }
    }

    private String guessBaseDirFromFile() {
        String nonWildcardBase = SelectorUtils.rtrimWildcardTokens(this.file);
        return Util.cleanString(nonWildcardBase);
    }
}
