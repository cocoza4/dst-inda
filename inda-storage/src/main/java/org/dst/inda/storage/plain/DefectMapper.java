package org.dst.inda.storage.plain;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.dst.inda.core.model.Artifact;
import org.dst.inda.core.parser.Parser;
import org.dst.inda.core.parser.impl.TeamForgeParser;
import org.xml.sax.SAXException;

public class DefectMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
	
	private Parser parser;
	
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		parser = new TeamForgeParser();
	}
	
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String document = value.toString();
		try {
			Artifact artf = parser.parse(document);
			String line = artf.getPlanningFolderId() + "\t" + artf.getId() + "\t" + artf.getCategory() + "\t" + artf.getRootCause();
			context.write(new Text(line), NullWritable.get());
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}
}
