package org.inda.batch.extractor.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.inda.batch.extractor.Extractor;
import org.inda.batch.model.Artifact;
import org.junit.Before;
import org.junit.Test;

public class TeamForgeExtractorTest {
	
	private Extractor extractor;
	
	private Artifact artf;
			
	@Before
	public void setUp() throws Exception {
		extractor = new TeamForgeExtractor();
		
		artf = new Artifact();
		artf.setTitle("Custom files not working on search form in New portal - BPSD-4337 , artf60122 , artf60123 , artf60125");
	}
	
	@Test
	public void testExtractArtifactId() throws Exception {
		List<String> artfIds = extractor.extractArtifactId(artf);
		
		assertEquals(3, artfIds.size());
		
		for (String id: artfIds) {
			System.out.println(id);
		}
	}
	

}
