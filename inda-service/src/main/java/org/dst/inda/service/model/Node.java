package org.dst.inda.service.model;

public class Node {
	
	private String planningFolderId;
	private String artifactId;
	private String artifactTitle;
	private String category;
	private Float size;
	private String rootCause;
	
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

	public Float getSize() {
		return size;
	}

	public void setSize(Float size) {
		this.size = size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
		result = prime * result + ((planningFolderId == null) ? 0 : planningFolderId.hashCode());
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
		Node other = (Node) obj;
		if (this.planningFolderId.equals(other.getPlanningFolderId()) &&
				this.artifactId.equals(other.getArtifactId())) {
			return true;
		}
		return false;
	}
	
}
