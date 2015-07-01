package org.inda.batch.model;

import java.util.Date;

public class Comment {
	
	private String id;
	private String createdByFullName;
	private String createdBy;
	private Date createdDate;
	private String description;
	private String transactionId;
	
	public Comment() {}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedByFullName() {
		return createdByFullName;
	}
	
	public void setCreatedByFullName(String createdByFullName) {
		this.createdByFullName = createdByFullName;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", createdByFullName=" + createdByFullName + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", description=" + description + ", transactionId=" + transactionId
				+ "]";
	}

}
