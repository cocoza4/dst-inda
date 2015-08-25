package org.dst.inda.service.service.impl;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dst.inda.service.model.Artifact;
import org.dst.inda.service.model.Graph;
import org.dst.inda.service.model.Link;
import org.dst.inda.service.model.Node;
import org.dst.inda.service.service.IInDAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class InDAService implements IInDAService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String SQL_SELECT_DEFECTS_BY_PLANNING_FOLDER_ID = "SELECT * FROM defect_new WHERE planning_folder_id = ?";
	private static final String SQL_SELECT_DEFECTS_BY_FILE = "SELECT planning_folder_id, defect_new.artifact_id AS artifact_id, category, root_cause, commit_files "
			+ "FROM defect_new INNER JOIN file_defect ON defect_new.artifact_id = file_defect.artifact_id";

	@Override
	public Graph generateGraphByPlanningFolderIds(List<String> planningFolderIds) {
		Graph graph = new Graph();
		List<Artifact> artifacts = jdbcTemplate.query(SQL_SELECT_DEFECTS_BY_PLANNING_FOLDER_ID, ARTIFACT_ROW_MAPPER,
				planningFolderIds.toArray());

		Map<String, Integer> countMap = new HashMap<>();
		Map<String, Artifact> artifactMap = this.buildArtifactMap(artifacts);

		Map<String, Artifact> relevantArtifactMap = new HashMap<>();

		for (Artifact artifact : artifactMap.values()) {
			String artifactId = artifact.getId();

			List<String> commitFiles = artifact.getCommitFiles();
			if (CollectionUtils.isNotEmpty(commitFiles)) {
				List<Artifact> associatedArtifacts = this.selectArtifactsByFiles(commitFiles);

				for (Artifact associatedArtifact : associatedArtifacts) {
					if (!artifactId.equals(associatedArtifact.getId())) {
						String key = artifactId + "," + associatedArtifact.getId();
						Integer count = countMap.get(key);
						if (count == null) {
							count = 1;
							countMap.put(key, count);
							relevantArtifactMap.put(artifactId, artifact);
							relevantArtifactMap.put(associatedArtifact.getId(), associatedArtifact);
						} else {
							count++;
						}
					}
				}
			}

		}
		LinkedHashSet<Node> nodeSet = new LinkedHashSet<>();

		for (String key : countMap.keySet()) {
			String[] artfIds = key.split(",");

			String artfId = artfIds[0];
			String associatedArtfId = artfIds[1];

			Node node = this.toNode(relevantArtifactMap.get(artfId));
			nodeSet.add(node);

			Node associatedNode = this.toNode(relevantArtifactMap.get(associatedArtfId));
			nodeSet.add(associatedNode);
		}
		Map<String, Integer> indexMap = this.buildIndexMap(nodeSet);

		for (Entry<String, Integer> entry : countMap.entrySet()) {

			String[] artfIds = entry.getKey().split(",");

			String artfId = artfIds[0];
			String associatedArtfId = artfIds[1];

			int count = entry.getValue();
			int source = indexMap.get(artfId);
			int target = indexMap.get(associatedArtfId);
			graph.addLink(new Link(source, target, count));
		}
		graph.addNodes(new ArrayList<>(nodeSet));

		return graph;
	}

	private Node toNode(Artifact artf) {
		Node node = new Node();
		node.setArtifactId(artf.getId());
		node.setCategory(artf.getCategory());
		node.setPlanningFolderId(artf.getPlanningFolderId());
		node.setRootCause(artf.getRootCause());

		if (StringUtils.isBlank(node.getCategory())) {
			node.setCategory("undefined");
		}
		if (StringUtils.isBlank(node.getRootCause())) {
			node.setRootCause("undefined");
		}
		return node;
	}

	private Map<String, Integer> buildIndexMap(Collection<Node> nodes) {
		Iterator<Node> it = nodes.iterator();
		Map<String, Integer> indexMap = new HashMap<>();
		int index = 0;
		while (it.hasNext()) {
			Node node = it.next();
			indexMap.put(node.getArtifactId(), index++);
		}
		return indexMap;
	}

	private Map<String, Artifact> buildArtifactMap(List<Artifact> artifacts) {
		Map<String, Artifact> artifactMap = new HashMap<>();
		for (Artifact artf : artifacts) {
			artifactMap.put(artf.getId(), artf);
		}
		return artifactMap;
	}

	private String buildWhereClause(List<String> files) {
		StringBuilder sb = new StringBuilder(" WHERE ");
		for (int i = 0; i < files.size(); i++) {

			if (i + 1 == files.size()) {
				sb.append("file = ?");
			} else {
				sb.append("file = ? OR ");
			}
		}
		return sb.toString();
	}

	private List<Artifact> selectArtifactsByFiles(List<String> files) {
		String sql = SQL_SELECT_DEFECTS_BY_FILE + this.buildWhereClause(files);
		List<Artifact> artifacts = jdbcTemplate.query(sql, ARTIFACT_ROW_MAPPER, files.toArray());
		return artifacts;
	}

	private static final RowMapper<Artifact> ARTIFACT_ROW_MAPPER = new RowMapper<Artifact>() {
		@Override
		public Artifact mapRow(ResultSet rs, int rowNum) throws SQLException {
			Artifact artifact = new Artifact();
			artifact.setCategory(rs.getString("category"));
			artifact.setId(rs.getString("artifact_id"));
			artifact.setPlanningFolderId(rs.getString("planning_folder_id"));
			Array commitFilesArr = rs.getArray("commit_files");
			if (commitFilesArr != null) {
				String[] commitFiles = (String[]) commitFilesArr.getArray();
				Set<String> set = new HashSet<>(Arrays.asList((commitFiles)));
				artifact.setCommitFiles(new ArrayList<>(set));
			}
			return artifact;
		}
	};

}
