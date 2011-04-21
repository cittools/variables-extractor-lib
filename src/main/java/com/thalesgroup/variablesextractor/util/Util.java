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
package com.thalesgroup.variablesextractor.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Util {

    public static String cleanString(String s) {
        if (s == null || s.length() == 0) {
            return null;
        } else {
            return s.trim();
        }
    }

    public static String readFile(File f) throws IOException {
        final char[] buffer = new char[8192];
        int bufferLength = 0;
        StringBuffer textBuffer = new StringBuffer();
        Reader reader = new FileReader(f);
        while (bufferLength != -1) {
            bufferLength = reader.read(buffer);
            if (bufferLength > 0) {
                textBuffer.append(new String(buffer, 0, bufferLength));
            }
        }
        reader.close();
        return textBuffer.toString();
    }

}
