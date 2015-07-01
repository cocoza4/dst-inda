package org.inda.batch.parser.impl;

import java.util.ArrayList;
import java.util.Stack;

import org.inda.batch.model.Artifact;
import org.inda.batch.model.Comment;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler extends DefaultHandler {

	private Stack<String> elementStack = new Stack<String>();

	private Artifact artf;
	private Comment comment;
	
	public Artifact getArtifact() {
		return artf;
	}
	
	@Override
	public void startDocument() {
		artf = new Artifact();
		artf.setComments(new ArrayList<Comment>());
	}

	private String currentParentElem() {
		if (this.elementStack.size() < 2) {
			return null;
		}
		return this.elementStack.get(this.elementStack.size() - 2);
	}

	private String currentElem() {
		return this.elementStack.peek();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		this.elementStack.push(qName);
		
		if (qName.equalsIgnoreCase("comment")) {
			comment = new Comment();
			artf.getComments().add(comment);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		this.elementStack.pop();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {

		String value = new String(ch, start, length).trim();
		if (value.length() == 0) {
			return;
		}

		String currentParent = this.currentParentElem();
		String current = this.currentElem();
		
		if (currentParent.equalsIgnoreCase("artifact")) {
			
			if (current.equalsIgnoreCase("id")) {
				artf.setId(value);
			} else if (current.equalsIgnoreCase("assignedTo")) {
				artf.setAssignedTo(value);
			} else if (current.equalsIgnoreCase("category")) {
				artf.setCategory(value);
			} else if (current.equalsIgnoreCase("createdDate")) {
				artf.setCreatedDate(null); // TODO: fix this
			} else if (current.equalsIgnoreCase("closeDate")) {
				artf.setCloseDate(null); // TODO: fix this
			} else if (current.equalsIgnoreCase("description")) {
				artf.setDescription(value);
			} else if (current.equalsIgnoreCase("folderId")) {
				artf.setFolderId(value);
			} else if (current.equalsIgnoreCase("path")) {
				artf.setPath(value);
			} else if (current.equalsIgnoreCase("title")) {
				artf.setTitle(value);
			} else if (current.equalsIgnoreCase("impactAnalysis")) {
				artf.setImpactAnalysis(value);
			} else if (current.equalsIgnoreCase("triage")) {
				artf.setTriage(value);
			} else if (current.equalsIgnoreCase("rootCause")) {
				artf.setRootCause(value);
			}
		} else if (currentParent.equalsIgnoreCase("comment")) {

			if (current.equalsIgnoreCase("createdBy")) {
				comment.setCreatedBy(value);
			} else if (current.equalsIgnoreCase("createdByFullname")) {
				comment.setCreatedByFullName(value);
			} else if (current.equalsIgnoreCase("description")) {
				comment.setDescription(value);
			}
			
		}

	}

}
