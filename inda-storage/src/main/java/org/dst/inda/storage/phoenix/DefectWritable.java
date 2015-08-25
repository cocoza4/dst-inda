package org.dst.inda.storage.phoenix;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hadoop.mapreduce.lib.db.DBWritable;

public class DefectWritable implements DBWritable {
	
	private String planningFolderId;
	private String artifactId;
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
		
		pstmt.setString(1, planningFolderId);
		pstmt.setString(2, artifactId);
		pstmt.setString(3, category);
		pstmt.setString(4, rootCause);
		pstmt.setArray(5, commitFilesArr);
		
	}

	@Override
	public void readFields(ResultSet rs) throws SQLException {
		planningFolderId = rs.getString("planning_folder_id");
		artifactId = rs.getString("artifact_id");
		category = rs.getString("category");
		rootCause = rs.getString("root_cause");
		Array commitFilesArr = rs.getArray("commit_files");
		commitFiles = (String[])commitFilesArr.getArray();
		
	}
}
