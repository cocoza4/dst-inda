package org.dst.inda.application;

import java.util.Arrays;

import org.dst.inda.application.service.IInDAService;
import org.dst.inda.application.service.impl.InDAService;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		IInDAService indaService = new InDAService();
		indaService.printTopEditedFiles(Arrays.asList("plan1557"));
		
		
	}

}
