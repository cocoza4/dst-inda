package org.dst.inda.core.extractor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.dst.inda.core.extractor.Extractor;
import org.dst.inda.core.model.Artifact;

public class TeamForgeExtractor implements Extractor {
	
	private List<String> extractArtifactId(String document) {
		
		List<String> artfIds = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(document);
		
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.matches(".*artf[0-9]{5}.*")) {
				artfIds.add(token);
			}
		}
		
		return artfIds;
	}

	@Override
	public List<String> extractArtifactId(Artifact artf) {
		String title = artf.getTitle();
		return this.extractArtifactId(title);
	}

}
