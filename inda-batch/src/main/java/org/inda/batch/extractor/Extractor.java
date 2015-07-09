package org.inda.batch.extractor;

import java.util.List;

import org.inda.batch.model.Artifact;

public interface Extractor {
	List<String> extractArtifactId(Artifact artf);
}
