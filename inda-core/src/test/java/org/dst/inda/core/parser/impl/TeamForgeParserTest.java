package org.dst.inda.core.parser.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dst.inda.core.model.Artifact;
import org.dst.inda.core.model.Association;
import org.dst.inda.core.model.Comment;
import org.dst.inda.core.parser.Parser;
import org.dst.inda.core.parser.impl.TeamForgeParser;
import org.junit.BeforeClass;
import org.junit.Test;

public class TeamForgeParserTest {

	private static Artifact artf;

	@BeforeClass
	public static void beforeClass() throws Exception {

		Parser parser = new TeamForgeParser();
		
		File file = new File("src/test/resources/artifact.xml");
		String doc = FileUtils.readFileToString(file);
		
		artf = parser.parse(doc);
	}

	@Test
	public void testParse() throws Exception {

		String expectedArtfTitle = "Custom files not working on search form in New portal - BPSD-4337";
		String expectedImpactAnalysis = "Open Search form in New portal";
		String expectedCategory = "new portal";
		String expectedAssignedTo = "dt85330";
		String expectedDescription = "A custom file for search form is not loaded when opening "
				+ "the search form. In New Portal, it only works correctly when opening an object. "
				+ "In Classic Portal, it works correctly both search and work form.";
		String expectedPlanningFolderId = "plan1557";
		String expectedRootCause = "Base Code Changes";

		assertEquals("artf60121", artf.getId());
		assertEquals("new portal", artf.getCategory());
		assertEquals(StringUtils.deleteWhitespace(expectedArtfTitle), StringUtils.deleteWhitespace(artf.getTitle()));
		assertEquals(expectedImpactAnalysis, artf.getImpactAnalysis());
		assertEquals(expectedCategory, artf.getCategory());
		assertEquals(expectedAssignedTo, artf.getAssignedTo());
		assertEquals(StringUtils.deleteWhitespace(expectedDescription),
				StringUtils.deleteWhitespace(artf.getDescription()));
		assertEquals(expectedPlanningFolderId, artf.getPlanningFolderId());
		assertEquals(expectedRootCause, artf.getRootCause());

	}

	@Test
	public void testParse_associations() throws Exception {

		String expectedArtfTitle = "Custom files not working on search form in New portal - BPSD-4337";
		String expectedDescription = "[artf60120]: fix unable to load custom files on Search form";

		List<Association> associations = artf.getAssociations();
		assertEquals(1, associations.size());

		Association model = associations.get(0);
		assertEquals("artf60120", model.getArtfId());
		assertEquals(StringUtils.deleteWhitespace(expectedArtfTitle),
				StringUtils.deleteWhitespace(model.getArtfTitle()));
		assertEquals(StringUtils.deleteWhitespace(expectedDescription),
				StringUtils.deleteWhitespace(model.getDescription()));

	}

	@Test
	public void testParse_comments() throws Exception {

		String expectedCreatedBy = "dt70527";
		String expectedDescription = "code review. no issue.";
		String expectedCreatedByFullName = "Nattachai Werasiriwatn";

		List<Comment> comments = artf.getComments();
		assertEquals(6, comments.size());

		Comment comment = comments.get(0);
		assertEquals(expectedCreatedBy, comment.getCreatedBy());
		assertEquals(expectedDescription, comment.getDescription());
		assertEquals(expectedCreatedByFullName, comment.getCreatedByFullName());
	}
	
	@Test
	public void testParse_commits() throws Exception {

		List<String> commitFiles = artf.getCommitFiles();
		assertEquals(3, commitFiles.size());

		assertEquals("AWD/trunk/src/web/awd/common/widgets/awd.search/awd.search.js", commitFiles.get(0));
		assertEquals("AWD/trunk/src/web/awd/common/widgets/awd.search/awd.searchForm.js", commitFiles.get(1));
		assertEquals("AWD/trunk/src/web/awd/qUnitTests/widgets/js/searchUnitTest.js", commitFiles.get(2));
	}

}
