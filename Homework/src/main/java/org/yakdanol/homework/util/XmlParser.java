package org.yakdanol.homework.util;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

@Slf4j
public class XmlParser {

    public static Document parseXml(InputStream xmlData) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(xmlData);
        } catch (Exception e) {
            log.error("Error parsing XML data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to parse XML data", e);
        }
    }
}
