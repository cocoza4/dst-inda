package org.inda.batch;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.inda.batch.mapreduce.TeamForgeMapper;
import org.inda.batch.mapreduce.TeamForgeReducer;
import org.inda.batch.mapreduce.XMLInputFormat;

public class BatchDriver extends Configured implements Tool {
	
	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.printf("Usage: %s [generic options] <input> <output>\n", 
					getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		
		Configuration conf = getConf();
//		conf.set("key.value.separator.in.input.line", "\t");
		conf.set("xmlinput.start", "<artifact>");
		conf.set("xmlinput.end", "</artifact>");
		
		Job job = Job.getInstance(conf, "TeamForge Processing");
		job.setJarByClass(getClass());
		
		Path input = new Path(args[0]);
		Path output = new Path(args[1]);
		
		FileInputFormat.addInputPath(job, input);
		FileOutputFormat.setOutputPath(job, output);
		
		// delete output path if exists
		output.getFileSystem(conf).delete(output, true);
		
		job.setMapperClass(TeamForgeMapper.class);
		job.setReducerClass(TeamForgeReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setInputFormatClass(XMLInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		
		return job.waitForCompletion(true) ? 0 : 1;
	}
	
	
    public static void main(String[] args) throws Exception {
    	int	exitCode = ToolRunner.run(new BatchDriver(), args);
		System.exit(exitCode);
    }

	
}
