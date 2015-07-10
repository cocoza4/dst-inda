package org.dst.inda.core.model;

import java.util.Date;
import java.util.List;

public class Artifact {
	
	private String id;
	private String category;
	private String assignedTo;
	private Date createdDate;
	private Date closeDate;
	private String description;
	private String planningFolderId;
	private String path;
	private String title;
	private String impactAnalysis;
	private String triage;
	private String rootCause;
	private String stepsToReproduce;
	private String autoTestScripts;
	private List<Comment> comments;
	private List<Attachment> attachments;
	private List<Association> associations;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getAssignedTo() {
		return assignedTo;
	}
	
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getCloseDate() {
		return closeDate;
	}
	
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPlanningFolderId() {
		return planningFolderId;
	}
	
	public void setPlanningFolderId(String planningFolderId) {
		this.planningFolderId = planningFolderId;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getImpactAnalysis() {
		return impactAnalysis;
	}
	
	public void setImpactAnalysis(String impactAnalysis) {
		this.impactAnalysis = impactAnalysis;
	}
	
	public String getTriage() {
		return triage;
	}
	
	public void setTriage(String triage) {
		this.triage = triage;
	}
	
	public String getRootCause() {
		return rootCause;
	}
	
	public void setRootCause(String rootCause) {
		this.rootCause = rootCause;
	}
	
	public String getStepsToReproduce() {
		return stepsToReproduce;
	}
	
	public void setStepsToReproduce(String stepsToReproduce) {
		this.stepsToReproduce = stepsToReproduce;
	}
	
	public String getAutoTestScripts() {
		return autoTestScripts;
	}
	
	public void setAutoTestScripts(String autoTestScripts) {
		this.autoTestScripts = autoTestScripts;
	}
	
	public List<Comment> getComments() {
		return comments;
	}
	
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public List<Attachment> getAttachments() {
		return attachments;
	}
	
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public List<Association> getAssociations() {
		return associations;
	}

	public void setAssociations(List<Association> associations) {
		this.associations = associations;
	}

}
