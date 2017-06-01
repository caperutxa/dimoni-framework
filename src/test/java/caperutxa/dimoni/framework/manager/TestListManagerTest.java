package caperutxa.dimoni.framework.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import caperutxa.dimoni.framework.config.Configuration;
import caperutxa.dimoni.framework.model.TestModel;
import org.junit.Assert;

public class TestListManagerTest {
	
	TestListManager manager;
	
	String testFile = "src/test/resources/testconfiguration/testlist.csv";
	
	/**
	 * Smoke test
	 */
	@Test
	public void getTestListFromFileTest() {	
		manager = new TestListManager();
		
		File file = new File(testFile);
		String component = "GUI";
		Map<Integer, TestModel> testList = manager.getTestByComponentFromFile(file.getAbsolutePath(), component);
		
		for(Map.Entry<Integer, TestModel> m : testList.entrySet()) {
			Assert.assertTrue(m.getValue().getComponents().contains(component));
		}
		
		component = "api";
		testList = manager.getTestByComponentFromFile(file.getAbsolutePath(), component);
		
		for(Map.Entry<Integer, TestModel> m : testList.entrySet()) {
			Assert.assertTrue(m.getValue().getComponents().contains(component));
		}
	}
	
	@Test
	public void translateTest() throws FileNotFoundException, IOException {
		manager = new TestListManager();
		Configuration.frameworkPropertiesFile = "src/test/resources/properties/frameworkTest.properties";
		Configuration.defaultFrameworkConfiguration();
		
		String line = manager.translatePathVariables("-s\"Search\" -c\"searchByResort\" -r -foutput -PdestinationFolder=output ${soapui_projects_folder}\\ReadyApi\\XML-soapui-project.xml");
		Assert.assertTrue(line.contains("thisIsTheSoapProjectFolder"));
		
		line = manager.translatePathVariables("-s\"Search\" -c\"searchByResort\" -r -foutput -PdestinationFolder=output ${readyapi_projects_folder}\\ReadyApi\\XML-soapui-project.xml");
		Assert.assertTrue(line.contains("thisIsTheReadyApiProjectFolder"));
		
	}
}
