package org.dst.inda.storage.hbase;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.dst.inda.core.model.Artifact;
import org.dst.inda.core.model.Association;
import org.dst.inda.core.parser.Parser;
import org.dst.inda.core.parser.impl.TeamForgeParser;
import org.xml.sax.SAXException;

public class DataImporterMapper<K> extends Mapper<LongWritable, Text, K, Put> {
	
	private static final byte[] CF_DATA = Bytes.toBytes("data");
	private static final byte[] Q_CATEGORY = Bytes.toBytes("category");
	private static final byte[] Q_ROOT_CAUSE = Bytes.toBytes("rootCause");
	
	private static final byte[] CF_ASSOCIATION = Bytes.toBytes("association");
	private static final byte[] Q_ASSOCIATIOIN_ID = Bytes.toBytes("artifactId");

	private Parser parser;

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		parser = new TeamForgeParser();
	}

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String document = value.toString();

		try {
			Artifact artf = parser.parse(document);
			byte[] category = Bytes.toBytes(artf.getCategory());
			byte[] rowKey = RowKeyConverter.generateRowKey(artf.getPlanningFolderId(), artf.getId());
			byte[] rootCause = Bytes.toBytes(artf.getRootCause());

			Put p = new Put(rowKey);
			p.addColumn(CF_DATA, Q_CATEGORY, category);
			p.addColumn(CF_DATA, Q_ROOT_CAUSE, rootCause);
			
			for (Association assoc: artf.getAssociations()) {
				byte[] associationId = Bytes.toBytes(assoc.getArtfId());
				p.addColumn(CF_ASSOCIATION, Q_ASSOCIATIOIN_ID, associationId);
			}
			
			context.write(null, p);
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}

}
