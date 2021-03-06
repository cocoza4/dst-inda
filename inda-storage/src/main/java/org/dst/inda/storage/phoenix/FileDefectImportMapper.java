package org.dst.inda.storage.phoenix;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.dst.inda.core.model.Artifact;
import org.dst.inda.core.parser.Parser;
import org.dst.inda.core.parser.impl.TeamForgeParser;
import org.xml.sax.SAXException;

public class FileDefectImportMapper <K> extends Mapper<LongWritable, Text, K, FileDefectWritable> {
	
	static enum IllFormedDefect {
		NO_PLANNING_FOLDER
	}
	
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
			
			if (StringUtils.isBlank(artf.getPlanningFolderId())) {
				context.getCounter(IllFormedDefect.NO_PLANNING_FOLDER).increment(1L);
			} else {
				for (String commitFile: artf.getCommitFiles()) {
					FileDefectWritable fileDefect = new FileDefectWritable();
					fileDefect.setArtifactId(artf.getId());
					fileDefect.setFile(commitFile);
					context.write(null, fileDefect);
				}
			}
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}
}
