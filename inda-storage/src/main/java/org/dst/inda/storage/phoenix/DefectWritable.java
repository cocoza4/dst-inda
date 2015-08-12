package org.dst.inda.storage.phoenix;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

public class DefectWritable implements DBWritable, Writable {
	
	private String planningFolderId;
	private String artifactId;
	private String rootCause;
	private String category;
	private String[] associations;

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

	@Override
	public void write(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, planningFolderId);
		pstmt.setString(2, artifactId);
		pstmt.setString(3, category);
		pstmt.setString(4, rootCause);
		
	}

	@Override
	public void readFields(ResultSet rs) throws SQLException {
		planningFolderId = rs.getString("PLANNING_FOLDER_ID");
		artifactId = rs.getString("ARTIFACT_ID");
		category = rs.getString("CATEGORY");
		rootCause = rs.getString("ROOT_CAUSE");
//		Array associationArray = rs.getArray("associations");
		
	}

	@Override
	public String toString() {
		return "DefectWritable [planningFolderId=" + planningFolderId + ", artifactId=" + artifactId + ", rootCause="
				+ rootCause + ", category=" + category + "]";
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
