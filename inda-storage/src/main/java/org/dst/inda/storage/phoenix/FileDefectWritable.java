package org.dst.inda.storage.phoenix;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hadoop.mapreduce.lib.db.DBWritable;

public class FileDefectWritable  implements DBWritable {
	
	private String artifactId;
	private String file;

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public void write(PreparedStatement pstmt) throws SQLException {
		pstmt.setString(1, file);
		pstmt.setString(2, artifactId);
	}

	@Override
	public void readFields(ResultSet rs) throws SQLException {
		artifactId = rs.getString("ARTIFACT_ID");
		file = rs.getString("FILE");
	}

}