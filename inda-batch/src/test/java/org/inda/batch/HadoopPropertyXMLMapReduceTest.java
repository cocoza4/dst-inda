package org.inda.batch;

import static org.junit.Assert.assertEquals;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.inda.batch.mapreduce.HadoopPropertyXMLMapReduce;
import org.junit.Test;

public class HadoopPropertyXMLMapReduceTest {
	
	@Test
	public void test() throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "file:///");
		conf.set("mapreduce.framework.name", "local");
		conf.setInt("mapreduce.task.io.sort.mb", 1);

		Path input = new Path("/home/cocoza4/Desktop/inda");
		Path output = new Path("src/test/resources/output");
		FileSystem fs = FileSystem.getLocal(conf);

		fs.delete(output, true); // delete old output

		int exitCode = HadoopPropertyXMLMapReduce.runJob(input.toString(), output.toString());
		
		assertEquals(exitCode, 0);
		
	}

}
