package org.dst.inda.storage.phoenix;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.dst.inda.core.model.Artifact;
import org.dst.inda.core.parser.Parser;
import org.dst.inda.core.parser.impl.TeamForgeParser;
import org.xml.sax.SAXException;

public class DefectImportMapper<K> extends Mapper<LongWritable, Text, K, DefectWritable> {
	
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
				List<String> commitFiles = artf.getCommitFiles();
				DefectWritable defect = new DefectWritable();
				defect.setPlanningFolderId(artf.getPlanningFolderId());
				defect.setArtifactId(artf.getId());
				defect.setArtifactTitle(artf.getTitle());
				defect.setRootCause(artf.getRootCause());
				defect.setCategory(artf.getCategory());
				defect.setCommitFiles(commitFiles.toArray(new String[commitFiles.size()]));
				context.write(null, defect);
			}
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}
}
