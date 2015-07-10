package org.dst.inda.core.parser;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.dst.inda.core.model.Artifact;
import org.xml.sax.SAXException;

public interface Parser {
	Artifact parse(String document) throws ParserConfigurationException, SAXException, IOException;	
}
