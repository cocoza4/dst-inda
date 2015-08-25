package org.dst.inda.service.model;

import java.util.List;

public class Artifact {
	
	private String id;
	private String planningFolderId;
	private String category;
	private String rootCause;
	private List<String> commitFiles;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlanningFolderId() {
		return planningFolderId;
	}

	public void setPlanningFolderId(String planningFolderId) {
		this.planningFolderId = planningFolderId;
	}

	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getRootCause() {
		return rootCause;
	}
	
	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}
	
	public List<String> getCommitFiles() {
		return commitFiles;
	}
	
	public void setCommitFiles(List<String> commitFiles) {
		this.commitFiles = commitFiles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((commitFiles == null) ? 0 : commitFiles.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((planningFolderId == null) ? 0 : planningFolderId.hashCode());
		result = prime * result + ((rootCause == null) ? 0 : rootCause.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artifact other = (Artifact) obj;
		if (this.id.equals(other.getId())) {
			return true;
		}
		return false;
	}
	
}
