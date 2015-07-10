package org.dst.inda.batch;

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
import org.dst.inda.batch.association.ArtifactAssociationMapper;
import org.dst.inda.batch.association.ArtifactAssociationReducer;

public class BatchDriver extends Configured implements Tool {
	
	private static final String DOCUMENT_START_TAG = "<artifact>";
	private static final String DOCUMENT_END_TAG = "</artifact>";
	
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
		conf.set("xmlinput.start", DOCUMENT_START_TAG);
		conf.set("xmlinput.end", DOCUMENT_END_TAG);
		
		Job job = Job.getInstance(conf, "TeamForge Processing");
		job.setJarByClass(getClass());
		
		Path input = new Path(args[0]);
		Path output = new Path(args[1]);
		
		FileInputFormat.addInputPath(job, input);
		FileOutputFormat.setOutputPath(job, output);
		
		// delete output path if exists
		output.getFileSystem(conf).delete(output, true);
		
		job.setMapperClass(ArtifactAssociationMapper.class);
		job.setReducerClass(ArtifactAssociationReducer.class);
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
