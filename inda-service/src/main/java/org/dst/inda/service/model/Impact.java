package org.dst.inda.service.model;

import java.util.List;

public class Impact {

    private String artifactId;
    private String artifactTitle;
    private List<String> commitFiles;
    private List<CategoryImpact> categoryImpacts;

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

    public List<String> getCommitFiles() {
        return commitFiles;
    }

    public void setCommitFiles(List<String> commitFiles) {
        this.commitFiles = commitFiles;
    }

    public List<CategoryImpact> getCategoryImpacts() {
        return categoryImpacts;
    }

    public void setCategoryImpacts(List<CategoryImpact> categoryImpacts) {
        this.categoryImpacts = categoryImpacts;
    }
}
