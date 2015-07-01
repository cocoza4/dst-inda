package org.inda.batch.parser.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.inda.batch.model.Artifact;
import org.inda.batch.parser.Parser;
import org.junit.Before;
import org.junit.Test;

public class TeamForgeParserTest {

	private Parser parser;

	private String doc;
	
	@Before
	public void setUp() throws Exception {

		parser = new TeamForgeParser();

		File file = new File("src/test/resources/artifact.xml");
		
		doc = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		
	}

	@Test
	public void testParse() throws Exception {
		
		Artifact artf = parser.parse(doc);
		assertEquals("artf60120", artf.getId());
		assertEquals("new portal", artf.getCategory());
//		assertEquals(expectedDescritpion, artf.getDescription());
		assertEquals("Custom files not working on search form in New portal - BPSD-4337", artf.getTitle());
		assertEquals("Open Search form in New portal", artf.getImpactAnalysis());
		
		assertEquals(6, artf.getComments().size());
		
//		for (Comment comment: artf.getComments()) {
//			System.out.println(comment);
//		}
		
//		System.out.println(artf.getComments().size());
		
		String token = "artf60121";
		System.out.println(token.matches("artf[0-9]{5}"));
		
		
	}

}
