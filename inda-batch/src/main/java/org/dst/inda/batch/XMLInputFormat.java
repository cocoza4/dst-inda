package org.dst.inda.batch;

import java.io.IOException;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

public class XMLInputFormat extends TextInputFormat {

	private static final String START_TAG_KEY = "xmlinput.start";
	private static final String END_TAG_KEY = "xmlinput.end";

	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext context) {
		try {
			return new XmlRecordReader((FileSplit) split, (JobConf) context.getConfiguration());
		} catch (IOException ex) {
			return null;
		}
	}

	private static class XmlRecordReader extends RecordReader<LongWritable, Text> {

		private final byte[] startTag;
		private final byte[] endTag;
		private final long start;
		private final long end;
		private final FSDataInputStream fsin;
		private final DataOutputBuffer buffer = new DataOutputBuffer();
		private LongWritable currentKey;
		private Text currentValue;

		public XmlRecordReader(FileSplit split, JobConf jobConf) throws IOException {
			startTag = jobConf.get(START_TAG_KEY).getBytes("utf-8");
			endTag = jobConf.get(END_TAG_KEY).getBytes("utf-8");

			start = split.getStart();
			end = start + split.getLength();
			Path file = split.getPath();
			FileSystem fs = file.getFileSystem(jobConf);
			fsin = fs.open(split.getPath());
			fsin.seek(start);
		}

		private boolean readUntilMatch(byte[] match, boolean withinBlock) throws IOException {
			int i = 0;
			while (true) {
				int b = fsin.read();

				// end of file
				if (b == -1) {
					return false;
				}

				// save to buffer
				if (withinBlock) {
					buffer.write(b);
				}

				// check if we're matching
				if (b == match[i]) {
					i++;
					if (i >= match.length) {
						return true;
					}
				} else {
					i = 0;
				}

				// see if we've passed the stop point
				if (!withinBlock && i == 0 && fsin.getPos() >= end) {
					return false;
				}

			}
		}

		private boolean next(LongWritable key, Text value) throws IOException {
			if (fsin.getPos() < end && readUntilMatch(startTag, false)) {
				try {
					buffer.write(startTag);
					if (readUntilMatch(endTag, true)) {
						key.set(fsin.getPos());
						value.set(buffer.getData(), 0, buffer.getLength());
						return true;
					}
				} finally {
					buffer.reset();
				}
			}
			return false;
		}

		@Override
		public void close() throws IOException {
			fsin.close();
		}

		@Override
		public LongWritable getCurrentKey() throws IOException, InterruptedException {
			return currentKey;
		}

		@Override
		public Text getCurrentValue() throws IOException, InterruptedException {
			return currentValue;
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			return (fsin.getPos() - start) / (float) (end - start);
		}

		@Override
		public void initialize(InputSplit arg0, TaskAttemptContext arg1) throws IOException, InterruptedException {

		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			currentKey = new LongWritable();
			currentValue = new Text();
			return next(currentKey, currentValue);
		}

	}

}
