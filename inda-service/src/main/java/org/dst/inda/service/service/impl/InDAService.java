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
import org.dst.inda.service.model.CategoryImpact;
import org.dst.inda.service.model.Graph;
import org.dst.inda.service.model.Impact;
import org.dst.inda.service.model.Link;
import org.dst.inda.service.model.Node;
import org.dst.inda.service.service.IInDAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class InDAService implements IInDAService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String SQL_SELECT_DEFECTS = "SELECT * FROM defect_new";
	private static final String SQL_SELECT_DEFECTS_BY_FILE = "SELECT planning_folder_id, defect_new.artifact_id AS artifact_id, "
			+ "defect_new.artifact_title AS artifact_title, category, root_cause, commit_files "
			+ "FROM defect_new INNER JOIN file_defect ON defect_new.artifact_id = file_defect.artifact_id";
    private static final String SQL_SELECT_IMPACTS_BY_ARTF_ID = "SELECT category, file "
            + "FROM defect_new INNER JOIN file_defect ON defect_new.artifact_id = file_defect.artifact_id "
            + "WHERE defect_new.artifact_id = ?";

    private static final int DEFAULT_NODE_SIZE = 5;

    private void updateFrequency(Map<String, Integer> map, String key) {
        Integer count = map.get(key);
        if (count == null) {
            count = 1;
            map.put(key, count);
        } else {
            count++;
        }
        map.put(key, count);
    }

	@Override
	public Graph generateGraphByPlanningFolderIds(List<String> planningFolderIds) {
		Graph graph = new Graph();
        String sql = SQL_SELECT_DEFECTS + this.buildPlanningFolderWhereClause(planningFolderIds);
		List<Artifact> artifacts = jdbcTemplate.query(sql, ARTIFACT_ROW_MAPPER,
				planningFolderIds.toArray());

		Map<String, Integer> associationCountMap = new HashMap<>();
        Map<String, Integer> frequencyMap = new HashMap<>();
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
						Integer count = associationCountMap.get(key);
						if (count == null) {
							count = 1;
							relevantArtifactMap.put(artifactId, artifact);
							relevantArtifactMap.put(associatedArtifact.getId(), associatedArtifact);
						} else {
							count++;
						}
						associationCountMap.put(key, count);
                        this.updateFrequency(frequencyMap, artifactId);
					}
				}
			}

		}
		LinkedHashSet<Node> nodeSet = new LinkedHashSet<>();

		for (String key : associationCountMap.keySet()) {
			String[] artfIds = key.split(",");

			String artfId = artfIds[0];
			String associatedArtfId = artfIds[1];

//            int nodeSize = DEFAULT_NODE_SIZE + associationCountMap.get(key) * 2;
            float nodeSize = DEFAULT_NODE_SIZE + frequencyMap.get(artfId) * 0.1f;
			Node node = this.toNode(relevantArtifactMap.get(artfId), nodeSize);
			nodeSet.add(node);

			Node associatedNode = this.toNode(relevantArtifactMap.get(associatedArtfId), DEFAULT_NODE_SIZE);
			nodeSet.add(associatedNode);
		}
		Map<String, Integer> indexMap = this.buildIndexMap(nodeSet);

		for (Entry<String, Integer> entry : associationCountMap.entrySet()) {

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

    @Override
    public Impact generateImpactsByKey(String planningFolderId, String artifactId) {
        String sql = SQL_SELECT_DEFECTS + " WHERE planning_folder_id = ? AND artifact_id = ?";
		Impact impact = new Impact();
		try {
			Artifact artifact = jdbcTemplate.queryForObject(sql, ARTIFACT_ROW_MAPPER, planningFolderId, artifactId);

			impact.setArtifactId(artifact.getId());
			impact.setArtifactTitle(artifact.getTitle());
			impact.setCommitFiles(artifact.getCommitFiles());

			Map<String, Integer> categoryCountMap = new HashMap<>();
			List<String> commitFiles = artifact.getCommitFiles();
			if (CollectionUtils.isNotEmpty(commitFiles)) {
				List<Artifact> associatedArtifacts = this.selectArtifactsByFiles(commitFiles);
				int total = this.calculateCategoryPercentage(categoryCountMap, artifact.getId(), associatedArtifacts);

				List<CategoryImpact> categoryImpacts = new ArrayList<>();
				for (Entry<String, Integer> entry : categoryCountMap.entrySet()) {
					float percentage = (float) entry.getValue() / total * 100;
					categoryImpacts.add(new CategoryImpact(entry.getKey(), percentage));
				}
				impact.setCategoryImpacts(categoryImpacts);
			}
		} catch (EmptyResultDataAccessException e) {

		}

        return impact;
    }

    private int calculateCategoryPercentage(Map<String, Integer> categoryCountMap, String artfId, List<Artifact> associatedArtifacts) {
        int total = 0;
        for (Artifact associated: associatedArtifacts) {
            if (!artfId.equals(associated.getId())) {
                String category = associated.getCategory();
                Integer count = categoryCountMap.get(category);
                if (count == null) {
                    count = 1;
                } else {
                    count++;
                }
                categoryCountMap.put(category, count);
                total++;
            }
        }
        return total;
    }

    private Node toNode(Artifact artf, float size) {
		Node node = new Node();
		node.setArtifactId(artf.getId());
		node.setCategory(artf.getCategory());
		node.setPlanningFolderId(artf.getPlanningFolderId());
		node.setRootCause(artf.getRootCause());
        node.setSize(size);

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

    private String buildPlanningFolderWhereClause(List<String> files) {
        StringBuilder sb = new StringBuilder(" WHERE ");
        for (int i = 0; i < files.size(); i++) {

            if (i + 1 == files.size()) {
                sb.append("planning_folder_id = ?");
            } else {
                sb.append("planning_folder_id = ? OR ");
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
			artifact.setTitle(rs.getString("artifact_title"));
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
