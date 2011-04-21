package com.thalesgroup.variablesextractor;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.thalesgroup.variablesextractor.util.ExtractionException;

public class PropertiesFileExtractorTest {

    @Test
    public void testExtractVariables() throws ExtractionException {
        String file = this.getClass().getResource("test.properties").getPath().substring(1);
        PropertiesFileExtractor ext = new PropertiesFileExtractor(file);
        Map<String,String> vars = ext.extractVariables();
        assertEquals("Jacky", vars.get("name"));
        assertEquals("3.5.7", vars.get("version"));
        assertEquals("201104211725", vars.get("timestamp"));
        assertEquals("zip", vars.get("extension"));
        assertEquals("10000", vars.get("size"));
        assertEquals("blue", vars.get("color"));
    }

}
