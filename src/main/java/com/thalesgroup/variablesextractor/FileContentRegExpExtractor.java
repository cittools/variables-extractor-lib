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
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.apache.tools.ant.util.FileUtils;

import com.thalesgroup.variablesextractor.util.ExtractionException;
import com.thalesgroup.variablesextractor.util.NamedMatcher;
import com.thalesgroup.variablesextractor.util.NamedPattern;

public class FileContentRegExpExtractor implements VariableExtractor {

    private final String file;
    private final String pattern;
    private final int flags;
    
    public FileContentRegExpExtractor(String file, String pattern) {
        this(file, pattern, 0);
    }
    public FileContentRegExpExtractor(String file, String pattern, int flags) {
        this.file = file;
        this.pattern = pattern;
        this.flags = flags;
    }
    

    @Override
    public Map<String, String> extractVariables() throws ExtractionException {
        try {
            File f = new File(this.file);
            String buffer = FileUtils.readFully(new FileReader(f));
            NamedPattern compiledPattern = NamedPattern.compile(pattern, this.flags);
            NamedMatcher matcher = compiledPattern.matcher(buffer);
            
            if (matcher.find()) {
                return matcher.namedGroups();
            } else {
                return new LinkedHashMap<String, String>();
            }
        } catch (FileNotFoundException e) {
            throw new ExtractionException("File not found: " + file, e);
        } catch (IOException e) {
            throw new ExtractionException("Error reading file: " + file, e);
        } catch (PatternSyntaxException e) {
            throw new ExtractionException("Invalid regexp pattern: " + e.getMessage(), e);
        }
    }

}
