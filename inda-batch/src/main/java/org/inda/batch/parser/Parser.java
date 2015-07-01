package org.inda.batch.parser;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.inda.batch.model.Artifact;
import org.xml.sax.SAXException;

public interface Parser {
	Artifact parse(String document) throws ParserConfigurationException, SAXException, IOException;	
}
