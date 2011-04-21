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
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.thalesgroup.variablesextractor.util.ExtractionException;

public class PropertiesFileExtractor implements VariableExtractor {

    private final String propertiesFile;
    private final List<String> restrictedNames;

    public PropertiesFileExtractor(String file) {
        this(file, null);
    }
    
    public PropertiesFileExtractor(String file, List<String> restrictedNames) {
        this.propertiesFile = file;
        this.restrictedNames = restrictedNames;
    }

    @Override
    public Map<String, String> extractVariables() throws ExtractionException {
        try {
            Properties properties = new Properties();
            Reader reader = new FileReader(new File(propertiesFile));
            properties.load(reader);
            reader.close();
            
            Map<String, String> vars = new LinkedHashMap<String, String>();

            for (String propName : properties.stringPropertyNames()) {
                if (restrictedNames != null && restrictedNames.size() != 0) {
                    if (restrictedNames.contains(propName)) {
                        vars.put(propName, properties.getProperty(propName));
                    }
                } else {
                    vars.put(propName, properties.getProperty(propName));
                }
            }
            
            return vars;
            
        } catch (FileNotFoundException e) {
            throw new ExtractionException("File not found: " + propertiesFile, e);
        } catch (IOException e) {
            throw new ExtractionException("Error reading file: " + propertiesFile, e);
        }
    }
}
