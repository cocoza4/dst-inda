package org.dst.inda.storage.hbase;

import org.apache.hadoop.hbase.util.Bytes;

public abstract class RowKeyConverter {
	
	private static final int PLANNING_FOLDER_ID_LENGTH = 12;
	private static final int ARTIFACT_ID_LENGTH = 12;
	
	public static byte[] generateRowKey(String planningFolderId, String artfId) {
		
		byte[] firstBytes = Bytes.toBytes(planningFolderId);
		byte[] secondBytes = Bytes.toBytes(artfId);
		
		byte[] rowKey = new byte[PLANNING_FOLDER_ID_LENGTH + ARTIFACT_ID_LENGTH];
		
		Bytes.putBytes(rowKey, 0, firstBytes, 0, firstBytes.length);
		Bytes.putBytes(rowKey, ARTIFACT_ID_LENGTH, secondBytes, 0, secondBytes.length);
		return rowKey;
	}
	
	public static void main(String[] args) {
		byte[] rowKey = RowKeyConverter.generateRowKey("plan1557", "artf60120");
		System.out.println(rowKey);
	}

}