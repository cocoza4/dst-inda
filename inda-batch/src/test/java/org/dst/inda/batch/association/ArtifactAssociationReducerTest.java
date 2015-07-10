package org.dst.inda.batch.association;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

public class ArtifactAssociationReducerTest {

	private ReduceDriver<Text, Text, Text, Text> reduceDriver;
	
	@Before
	public void setUp() throws Exception {
		reduceDriver = ReduceDriver.newReduceDriver(new ArtifactAssociationReducer());
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
		pairs.add(new Pair<Text, Text>(artfId, new Text("artf60122")));
		pairs.add(new Pair<Text, Text>(artfId, new Text("artf60123")));
		pairs.add(new Pair<Text, Text>(artfId, new Text("artf60125")));
		
		reduceDriver.withInput(artfId, values);
		reduceDriver.withAllOutput(pairs);
		reduceDriver.runTest();
	}
	
}
