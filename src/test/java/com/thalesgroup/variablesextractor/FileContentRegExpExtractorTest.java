package com.thalesgroup.variablesextractor;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

import com.thalesgroup.variablesextractor.util.ExtractionException;

public class FileContentRegExpExtractorTest {

    @Test
    public void testExtractVariables() throws ExtractionException {
        
        String file = this.getClass().getResource("test.xml").getPath().substring(1);
        String pattern = ".*<name>(?P<NAME>.+)</name>.*" +
        		"<version>(?P<VERSION>.+)</version>.*" +
        		"<nature>(?P<NATURE>.+)</nature>.*";
        FileContentRegExpExtractor ext = new FileContentRegExpExtractor(file, pattern, Pattern.DOTALL);
        Map<String,String> vars = ext.extractVariables();
        assertEquals("Jacqueline", vars.get("NAME"));
        assertEquals("2.5.8", vars.get("VERSION"));
        assertEquals("64-bit", vars.get("NATURE"));
    }

}
