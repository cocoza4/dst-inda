package org.dst.inda.service.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;

import org.codehaus.jackson.map.ObjectMapper;
import org.dst.inda.service.model.Graph;
import org.dst.inda.service.service.IInDAService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/service-context.xml")
public class InDAServiceTest {
	
	@Autowired
	private IInDAService indaService;
	
	private static void writeFile(String file, Graph graph) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(file), graph);
	}
	
	@Test
    public void testInsert() throws Exception {
		long start = System.currentTimeMillis();
		Graph graph = indaService.generateGraphByPlanningFolderIds(Arrays.asList("plan1557"));
		long end = System.currentTimeMillis();
		System.out.println("time elapsed: " + (end - start));
		System.out.println("nodes: " + graph.getNodes().size());
		System.out.println("links: " + graph.getLinks().size());
		writeFile("/home/cocoza4/Desktop/d3js/planningFolder.json", graph);
    }

}
