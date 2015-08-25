package org.dst.inda.service.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	
	private List<Node> nodes;
	private List<Link> links;
	
	public Graph() {
		nodes = new ArrayList<>();
		links = new ArrayList<>();
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
	
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
	
	public List<Link> getLinks() {
		return links;
	}
	
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	public void addNode(Node node) {
		this.nodes.add(node);
	}
	
	public void addNodes(List<Node> nodes) {
		this.nodes.addAll(nodes);
	}
	
	public void addLink(Link link) {
		this.links.add(link);
	}
	
}
