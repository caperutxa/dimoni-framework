package caperutxa.dimoni.framework.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import caperutxa.dimoni.framework.config.Configuration;
import caperutxa.dimoni.framework.model.TestModel;
import org.junit.Assert;
import org.junit.Before;

public class TestListManagerTest {
	
	TestListManager manager;
	
	String testFile = "src/test/resources/testconfiguration/testlist.csv";
	
	@Before
	public void before() throws FileNotFoundException, IOException {
		Configuration.frameworkPropertiesFile = "src/test/resources/properties/frameworkTest.properties";
		Configuration.defaultFrameworkConfiguration();
	}
	
	/**
	 * Smoke test
	 */
	@Test
	public void getTestListFromFileTest() {	
		manager = new TestListManager();
		boolean found = false;
		
		File file = new File(testFile);
		String component = "GUI";
		Map<Integer, TestModel> testList = manager.getTestByComponentFromFile(file.getAbsolutePath(), component);
		
		for(Map.Entry<Integer, TestModel> m : testList.entrySet()) {
			Assert.assertTrue(m.getValue().getComponents().contains(component));
			Assert.assertTrue(!m.getValue().getComponents().contains(component+"s"));
			found = true;
		}
		Assert.assertTrue(found); // At least one result
		
		component = "api";
		testList = manager.getTestByComponentFromFile(file.getAbsolutePath(), component);
		found = false;
		
		for(Map.Entry<Integer, TestModel> m : testList.entrySet()) {
			Assert.assertTrue(m.getValue().getComponents().contains(component));
			Assert.assertTrue(!m.getValue().getComponents().contains(component+"s"));
			found = true;
		}
		Assert.assertTrue(found);
		
		component = "deepapi";
		testList = manager.getTestByComponentFromFile(file.getAbsolutePath(), component);
		found = false;
		
		for(Map.Entry<Integer, TestModel> m : testList.entrySet()) {
			Assert.assertTrue(m.getValue().getComponents().contains(component));
			Assert.assertTrue(!m.getValue().getComponents().contains(component+"s"));
			found = true;
		}
		Assert.assertTrue(found);
	}
	
	@Test
	public void translateTest() {
		manager = new TestListManager();
		
		String line = manager.translatePathVariables("-s\"Search\" -c\"searchByResort\" -r -foutput -PdestinationFolder=output ${soapui_projects_folder}\\ReadyApi\\XML-soapui-project.xml");
		Assert.assertTrue(line.contains("thisIsTheSoapProjectFolder"));
		
		line = manager.translatePathVariables("-s\"Search\" -c\"searchByResort\" -r -foutput -PdestinationFolder=output ${readyapi_projects_folder}\\ReadyApi\\XML-soapui-project.xml");
		Assert.assertTrue(line.contains("thisIsTheReadyApiProjectFolder"));
		
	}
}
