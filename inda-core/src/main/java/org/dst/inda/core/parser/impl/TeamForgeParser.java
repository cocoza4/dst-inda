package org.dst.inda.core.parser.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dst.inda.core.model.Artifact;
import org.dst.inda.core.parser.Parser;
import org.xml.sax.SAXException;

public class TeamForgeParser implements Parser {
	
	public TeamForgeParser() {}

	@Override
	public Artifact parse(String document) throws ParserConfigurationException, SAXException, IOException {
		
		InputStream stream = new ByteArrayInputStream(document.getBytes(StandardCharsets.UTF_8));
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		SaxHandler handler = new SaxHandler();
		saxParser.parse(stream, handler);
		
		return handler.getArtifact();
	}

}
