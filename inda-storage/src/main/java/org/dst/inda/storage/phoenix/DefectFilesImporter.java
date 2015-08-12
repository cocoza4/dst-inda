package org.dst.inda.storage.phoenix;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.phoenix.mapreduce.PhoenixOutputFormat;
import org.apache.phoenix.mapreduce.util.PhoenixMapReduceUtil;
import org.dst.inda.storage.XMLInputFormat;

public class DefectFilesImporter extends Configured implements Tool {
	
	private static final String DOCUMENT_START_TAG = "<artifact>";
	private static final String DOCUMENT_END_TAG = "</artifact>";
	
	private static final String TABLE_DEFECT_FILES = "DEFECT_FILES";
	
	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: PhoenixDataImporter	<input>");
			return -1;
		}
		
		Configuration conf = getConf();
		conf.set("xmlinput.start", DOCUMENT_START_TAG);
		conf.set("xmlinput.end", DOCUMENT_END_TAG);
		
		Job job = Job.getInstance(conf, getClass().getSimpleName());
		job.setJarByClass(getClass());
		job.setInputFormatClass(XMLInputFormat.class);
		job.setOutputFormatClass(PhoenixOutputFormat.class);
		job.setMapOutputValueClass(DefectFilesWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		job.setMapperClass(DefectFilesImportMapper.class);
		job.setNumReduceTasks(0);
		PhoenixMapReduceUtil.setOutput(job, TABLE_DEFECT_FILES, "ARTIFACT_ID,FILE");
		return job.waitForCompletion(true) ? 0 : 1;
	}
	
	public static void main( String[] args ) throws Exception {
		int exitCode = ToolRunner.run(HBaseConfiguration.create(), new DefectFilesImporter(), args);
		System.exit(exitCode);
    }
}
