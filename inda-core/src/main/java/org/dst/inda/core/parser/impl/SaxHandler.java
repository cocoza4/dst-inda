package org.dst.inda.core.parser.impl;

import java.util.ArrayList;
import java.util.Stack;

import org.dst.inda.core.model.Artifact;
import org.dst.inda.core.model.Association;
import org.dst.inda.core.model.Comment;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler extends DefaultHandler {

	private Stack<String> elementStack = new Stack<String>();
	private StringBuilder sb = new StringBuilder();
	
	private Artifact artf;
	private Comment comment;
	private Association association;

	public Artifact getArtifact() {
		return artf;
	}

	@Override
	public void startDocument() {
		artf = new Artifact();
		artf.setComments(new ArrayList<Comment>());
		artf.setAssociations(new ArrayList<Association>());
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
		} else if (qName.equalsIgnoreCase("association")) {
			association = new Association();
			artf.getAssociations().add(association);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		String currentParent = this.currentParentElem();
		String current = this.currentElem();

		if (currentParent != null) {
			String value = sb.toString().trim();
//			System.out.println("parent: " + currentParent + ", current: " + current + ", value: " + value);
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
				} else if (current.equalsIgnoreCase("planningFolderId")) {
					artf.setPlanningFolderId(value);
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
			} else if (currentParent.equalsIgnoreCase("association")) {
				if (current.equalsIgnoreCase("originId")) {
					association.setArtfId(value);
				} else if (current.equalsIgnoreCase("originTitle")) {
					association.setArtfTitle(value);
				} else if (current.equalsIgnoreCase("description")) {
					association.setDescription(value);
				}
			}
		}
		sb.setLength(0);
		this.elementStack.pop();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		sb.append(new String(ch, start, length));
	}

}
