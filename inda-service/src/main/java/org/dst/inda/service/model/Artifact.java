package org.dst.inda.service.model;

import java.util.List;

public class Artifact {
	
	private String id;
	private String title;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Artifact artifact = (Artifact) o;

		if (!id.equals(artifact.id)) return false;
		return planningFolderId.equals(artifact.planningFolderId);

	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + planningFolderId.hashCode();
		return result;
	}

    @Override
    public String toString() {
        return "Artifact{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", planningFolderId='" + planningFolderId + '\'' +
                ", category='" + category + '\'' +
                ", rootCause='" + rootCause + '\'' +
                ", commitFiles=" + commitFiles +
                '}';
    }
}
