package org.inda.batch.mapreduce;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.inda.batch.extractor.Extractor;
import org.inda.batch.extractor.impl.TeamForgeExtractor;
import org.inda.batch.model.Artifact;
import org.inda.batch.parser.Parser;
import org.inda.batch.parser.impl.TeamForgeParser;
import org.xml.sax.SAXException;

public class TeamForgeMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	private Parser parser;
	private Extractor extractor;

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		parser = new TeamForgeParser();
		extractor = new TeamForgeExtractor();
	}

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String document = value.toString();
		
		try {
			Artifact artf = parser.parse(document);
			Text artfId = new Text(artf.getId());
			
			for (String referingArtfId: extractor.extractArtifactId(artf)) {
				context.write(new Text(referingArtfId), artfId);
			}
			
			context.write(artfId, new Text());
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
		
	}

}
