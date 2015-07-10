package org.dst.inda.batch;

import static org.junit.Assert.assertEquals;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.dst.inda.batch.BatchDriver;
import org.junit.Test;

public class BatchDriverTest {
	
	@Test
	public void test() throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "file:///");
		conf.set("mapreduce.framework.name", "local");
		conf.setInt("mapreduce.task.io.sort.mb", 1);

		Path input = new Path("src/test/resources/input");
		Path output = new Path("src/test/resources/output");
		FileSystem fs = FileSystem.getLocal(conf);

		fs.delete(output, true); // delete old output
		
		String[] args = new String[]{input.toString(), output.toString()};
		
		BatchDriver driver = new BatchDriver();
		driver.setConf(conf);

		int exitCode = driver.run(args);
		
		assertEquals(exitCode, 0);
		
	}

}
