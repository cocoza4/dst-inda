package org.inda.batch.mapreduce;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class HadoopPropertyXMLMapReduce {

	public static class Map extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String document = value.toString();
			System.out.println("'" + document + "'");
			try {
				XMLStreamReader reader = XMLInputFactory.newInstance()
						.createXMLStreamReader(new ByteArrayInputStream(document.getBytes()));
				String propertyName = "";
				String propertyValue = "";
				String currentElement = "";
				while (reader.hasNext()) {
					int code = reader.next();
					switch (code) {
					case START_ELEMENT:
						currentElement = reader.getLocalName();
						break;
					case CHARACTERS:
						if (currentElement.equalsIgnoreCase("name")) {
							propertyName += reader.getText();
						} else if (currentElement.equalsIgnoreCase("value")) {
							propertyValue += reader.getText();
						}
						break;
					}
				}
				reader.close();
				context.write(new Text(propertyName.trim()), new Text(propertyValue.trim()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		runJob(args[0], args[1]);
	}

	public static int runJob(String input, String output) throws Exception {
		Configuration conf = new Configuration();
		conf.set("key.value.separator.in.input.line", " ");
		conf.set("xmlinput.start", "<artifact>");
		conf.set("xmlinput.end", "</artifact>");

		Job job = Job.getInstance(conf,"xml processing");
		job.setJarByClass(HadoopPropertyXMLMapReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapperClass(Map.class);
		job.setInputFormatClass(XMLInputFormat.class);
		job.setNumReduceTasks(0);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		Path outPath = new Path(output);
		FileOutputFormat.setOutputPath(job, outPath);
		outPath.getFileSystem(conf).delete(outPath, true);

		return job.waitForCompletion(true) ? 0 : 1;
	}
}
