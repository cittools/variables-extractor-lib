package com.thalesgroup.variablesextractor;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.thalesgroup.variablesextractor.util.ExtractionException;

public class FileNameRegExpExtractorTest {

    @Test
    public void testExtractVariables() throws ExtractionException {
        String file = "Toto-*.jar";
        String pattern = "(?P<NAME>\\w+)-(?P<VERSION>.+)\\.(?P<TIMESTAMP>.+)\\.(?P<FILEEXT>\\w+)";
        String baseDir = this.getClass().getResource(".").getPath().substring(1);
        FileNameRegExpExtractor ext = new FileNameRegExpExtractor(file, pattern, baseDir);
        Map<String,String> vars = ext.extractVariables();
        assertEquals("Toto", vars.get("NAME"));
        assertEquals("5.7.4", vars.get("VERSION"));
        assertEquals("201104211725", vars.get("TIMESTAMP"));
        assertEquals("jar", vars.get("FILEEXT"));
    }

}
