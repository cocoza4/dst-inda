package org.dst.inda.application.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dst.inda.application.service.IInDAService;
import org.dst.inda.application.utils.ConnectionUtils;

import dnl.utils.text.table.TextTable;

public class InDAService implements IInDAService {

	private String[] columnNames = { "Planning Folder", "File", "# of modifactions" };

	private Connection conn = ConnectionUtils.getConnection();

	private String SQL_SELECT_TOP_EDITED_FILES = "SELECT planning_folder_id, file, count(file) as c FROM defect d JOIN defect_files df ON d.artifact_id = df.artifact_id"
			+ " WHERE planning_folder_id = ? GROUP BY planning_folder_id, file";

	@Override
	public void printTopEditedFiles(List<String> planningFolderIds) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_TOP_EDITED_FILES);
		pstmt.setString(1, planningFolderIds.get(0));
		ResultSet rs = pstmt.executeQuery();

		List<Object[]> arrs = new ArrayList<>();
		while (rs.next()) {
			Object[] arr = new Object[] { rs.getString("planning_folder_id"), rs.getString("file"), rs.getInt("c") };
			arrs.add(arr);
		}
		
		Object[][] data = new Object[arrs.size()][];
		
		for (int i = 0; i < arrs.size(); i++) {
			data[i] = arrs.get(i);
		}
				
		TextTable tt = new TextTable(columnNames, data);
		// this adds the numbering on the left
		tt.setAddRowNumbering(true);
		// sort by the first column
		tt.setSort(0);
		tt.printTable();

		pstmt.close();
		conn.close();

	}

}
