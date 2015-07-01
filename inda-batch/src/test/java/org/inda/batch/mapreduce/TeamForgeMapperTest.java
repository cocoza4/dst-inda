package org.inda.batch.mapreduce;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

public class TeamForgeMapperTest {
	
	private MapDriver<LongWritable, Text, Text, Text> mapDriver;
	
	@Before
	public void setUp() throws Exception {
		mapDriver = MapDriver.newMapDriver(new TeamForgeMapper());
	}
	
	@Test
	public void testArtifact_noReference() throws IOException, InterruptedException {
		
		File file = new File("src/test/resources/input/artifact-8.xml");		
		String doc = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		
		Text value = new Text(doc);
		Pair<Text, Text> output = new Pair<Text, Text>(new Text("artf60128"), new Text());
		
		mapDriver.withInput(new LongWritable(0), value);
		mapDriver.withOutput(output);
		mapDriver.runTest();
	}

	@Test
	public void testArtifact_referringToOthers() throws IOException, InterruptedException {
		
		File file = new File("src/test/resources/input/artifact-1.xml");		
		String doc = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		
		Text value = new Text(doc);
		Text artfId = new Text("artf60121");
		List<Pair<Text, Text>> pairs = new ArrayList<>();
		pairs.add(new Pair<Text, Text>(new Text("artf60122"), artfId));
		pairs.add(new Pair<Text, Text>(new Text("artf60123"), artfId));
		pairs.add(new Pair<Text, Text>(new Text("artf60125"), artfId));
		pairs.add(new Pair<Text, Text>(new Text(artfId), new Text()));
		
		mapDriver.withInput(new LongWritable(0), value);
		mapDriver.withAllOutput(pairs);
		mapDriver.runTest();
	}
	
}
