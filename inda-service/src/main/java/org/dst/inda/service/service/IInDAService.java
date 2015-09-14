package org.dst.inda.service.service;

import java.util.List;

import org.dst.inda.service.model.Graph;
import org.dst.inda.service.model.Impact;

public interface IInDAService {
	Graph generateGraphByPlanningFolderIds(List<String> planningFolderIds);
	Impact generateImpactsByKey(String planningFolderId, String artifactId);
}