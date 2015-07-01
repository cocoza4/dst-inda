package org.inda.batch.mapreduce;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

public class TeamForgeReducerTest {

	private ReduceDriver<Text, Text, Text, Text> reduceDriver;
	
	@Before
	public void setUp() throws Exception {
		reduceDriver = ReduceDriver.newReduceDriver(new TeamForgeReducer());
	}
	
	@Test
	public void testArtifact_noReference() throws IOException, InterruptedException {
		
		Text key = new Text("artf60128");
		List<Text> values = Arrays.asList(new Text());
		
		reduceDriver.withInput(key, values);
		reduceDriver.withOutput(key, new Text());
		reduceDriver.runTest();
	}
	
	@Test
	public void testArtifact_referringToOthers() throws IOException, InterruptedException {
		
		Text artfId = new Text("artf60121");
		
		List<Text> values = new ArrayList<>();
		values.add(new Text("artf60122"));
		values.add(new Text("artf60123"));
		values.add(new Text("artf60125"));
		
		List<Pair<Text, Text>> pairs = new ArrayList<>();
		pairs.add(new Pair<Text, Text>(new Text("artf60122"), artfId));
		pairs.add(new Pair<Text, Text>(new Text("artf60123"), artfId));
		pairs.add(new Pair<Text, Text>(new Text("artf60125"), artfId));
		pairs.add(new Pair<Text, Text>(new Text("artf60121"), new Text()));
		
		reduceDriver.withInput(artfId, values);
		reduceDriver.withAllOutput(pairs);
		reduceDriver.runTest();
	}
	
}
