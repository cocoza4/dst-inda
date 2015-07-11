package org.dst.inda.storage;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.dst.inda.storage.hbase.DataImporterMapper;

public class DataImporter extends Configured implements Tool {
	
	private static final String DOCUMENT_START_TAG = "<artifact>";
	private static final String DOCUMENT_END_TAG = "</artifact>";
	
	private static final String TABLE_DEFECT = "defect"; 
	
	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: DataImporter	<input>");
			return -1;
		}
		
		Configuration conf = getConf();
		conf.set("xmlinput.start", DOCUMENT_START_TAG);
		conf.set("xmlinput.end", DOCUMENT_END_TAG);
		conf.set(TableOutputFormat.OUTPUT_TABLE, TABLE_DEFECT);
		
		Job job = Job.getInstance(conf, getClass().getSimpleName());
		job.setJarByClass(getClass());
		job.setInputFormatClass(XMLInputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		job.setMapperClass(DataImporterMapper.class);
		job.setNumReduceTasks(0);
		job.setOutputFormatClass(TableOutputFormat.class);
		return job.waitForCompletion(true) ? 0 : 1;
	}
	
	public static void main( String[] args ) throws Exception {
		int exitCode = ToolRunner.run(HBaseConfiguration.create(), new DataImporter(), args);
		System.exit(exitCode);
    }
	
}
