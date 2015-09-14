package org.dst.inda.webapp.resource;

import java.util.Arrays;

import org.dst.inda.service.model.Graph;
import org.dst.inda.service.model.Impact;
import org.dst.inda.service.service.IInDAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/defects")
public class DefectResource {
	
	@Autowired
	private IInDAService indaService;
	
	@RequestMapping(value = "/analytics", method = RequestMethod.GET)
    public Graph getAllByPlanningFolderIds(@RequestParam(value = "planningfolders") String[] planningFolders) {

        if (planningFolders.length == 0) {
        	return new Graph();
        }

        for (String s: planningFolders) {
            System.out.println(s);
        }

        return indaService.generateGraphByPlanningFolderIds(Arrays.asList(planningFolders));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Impact getAllByPlanningFolderIdAndArtifactId(@RequestParam(value = "planningfolder") String planningFolder,
                                                       @RequestParam(value = "artifact") String artifact) {

        return indaService.generateImpactsByKey(planningFolder, artifact);
    }
	

}
