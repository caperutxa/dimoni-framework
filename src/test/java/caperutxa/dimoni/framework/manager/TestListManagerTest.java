package caperutxa.dimoni.framework.manager;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import caperutxa.dimoni.framework.config.Configuration;
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
	
	@Test
	public void translateTest() {
		manager = new TestListManager();
		
		String line = manager.translatePathVariables("-s\"Search\" -c\"searchByResort\" -r -foutput -PdestinationFolder=output ${soapui_projects_folder}\\ReadyApi\\XML-soapui-project.xml");
		Assert.assertTrue(line.contains("thisIsTheSoapProjectFolder"));
		
		line = manager.translatePathVariables("-s\"Search\" -c\"searchByResort\" -r -foutput -PdestinationFolder=output ${readyapi_projects_folder}\\ReadyApi\\XML-soapui-project.xml");
		Assert.assertTrue(line.contains("thisIsTheReadyApiProjectFolder"));
		
		line = manager.translatePathVariables("${soapui_projects_folder} -s\"Search\" -c\"searchByResort\" -r -foutput -PdestinationFolder=output ${readyapi_projects_folder}\\ReadyApi\\XML-soapui-project.xml");
		Assert.assertTrue(line.contains("thisIsTheReadyApiProjectFolder"));
		Assert.assertTrue(line.contains("thisIsTheSoapProjectFolder"));
	}
}
