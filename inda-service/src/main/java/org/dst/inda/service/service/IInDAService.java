package org.dst.inda.service.service;

import java.util.List;

import org.dst.inda.service.model.Graph;

public interface IInDAService {
	Graph generateGraphByPlanningFolderIds(List<String> planningFolderIds);
}