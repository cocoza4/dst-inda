package org.dst.inda.service;

import java.io.File;
import java.util.Arrays;

import org.codehaus.jackson.map.ObjectMapper;
import org.dst.inda.service.model.Graph;
import org.dst.inda.service.service.IInDAService;
import org.dst.inda.service.service.impl.InDAService;

public class Main {

	private static void writeFile(String file, Graph graph) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(file), graph);
	}

	public static void main(String[] args) throws Exception {

		IInDAService indaService = new InDAService();
		
		long start = System.currentTimeMillis();
		Graph graph = indaService.generateGraphByPlanningFolderIds(Arrays.asList("plan1557"));
		long end = System.currentTimeMillis();
		System.out.println("time elapsed: " + (end - start));
		System.out.println("nodes: " + graph.getNodes().size());
		System.out.println("links: " + graph.getLinks().size());
		writeFile("/home/cocoza4/Desktop/d3js/planningFolder.json", graph);
	}

}
