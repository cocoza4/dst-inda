package org.dst.inda.application.service;

import java.sql.SQLException;
import java.util.List;

public interface IInDAService {
	void printTopEditedFiles(List<String> planningFolderIds) throws SQLException;
}