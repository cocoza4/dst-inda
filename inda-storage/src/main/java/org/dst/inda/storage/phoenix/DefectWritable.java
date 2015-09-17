package org.dst.inda.storage.phoenix;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hadoop.mapreduce.lib.db.DBWritable;

public class DefectWritable implements DBWritable {
	
	private String planningFolderId;
	private String artifactId;
	private String artifactTitle;
	private String rootCause;
	private String category;
	private String[] commitFiles;

	public String getPlanningFolderId() {
		return planningFolderId;
	}

	public void setPlanningFolderId(String planningFolderId) {
		this.planningFolderId = planningFolderId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getArtifactTitle() {
		return artifactTitle;
	}

	public void setArtifactTitle(String artifactTitle) {
		this.artifactTitle = artifactTitle;
	}

	public String getRootCause() {
		return rootCause;
	}

	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String[] getCommitFiles() {
		return commitFiles;
	}

	public void setCommitFiles(String[] commitFiles) {
		this.commitFiles = commitFiles;
	}

	@Override
	public void write(PreparedStatement pstmt) throws SQLException {
		
		Array commitFilesArr = pstmt.getConnection().createArrayOf("VARCHAR", commitFiles);
		int idx = 1;
		pstmt.setString(idx++, planningFolderId);
		pstmt.setString(idx++, artifactId);
		pstmt.setString(idx++, artifactTitle);
		pstmt.setString(idx++, category);
		pstmt.setString(idx++, rootCause);
		pstmt.setArray(idx, commitFilesArr);
		
	}

	@Override
	public void readFields(ResultSet rs) throws SQLException {
		planningFolderId = rs.getString("planning_folder_id");
		artifactId = rs.getString("artifact_id");
		artifactTitle = rs.getString("artifact_title");
		category = rs.getString("category");
		rootCause = rs.getString("root_cause");
		Array commitFilesArr = rs.getArray("commit_files");
		commitFiles = (String[])commitFilesArr.getArray();
		
	}
}
