package org.dst.inda.core.extractor;

import java.util.List;

import org.dst.inda.core.model.Artifact;

public interface Extractor {
	List<String> extractArtifactId(Artifact artf);
}
