package org.dst.inda.core.model;

public class Attachment {

	private String id;
	private String createdBy;
	private String createdByFullName;
	private String fileName;
	private String fileSize;
	private String mimeType;
	private String rawField;
	private String storedField;
	private String transactionId;
	
	public Attachment() {}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedByFullName() {
		return createdByFullName;
	}
	public void setCreatedByFullName(String createdByFullName) {
		this.createdByFullName = createdByFullName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getRawField() {
		return rawField;
	}
	public void setRawField(String rawField) {
		this.rawField = rawField;
	}
	public String getStoredField() {
		return storedField;
	}
	public void setStoredField(String storedField) {
		this.storedField = storedField;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
