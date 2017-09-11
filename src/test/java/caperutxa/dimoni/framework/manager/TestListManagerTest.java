package caperutxa.dimoni.framework.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import caperutxa.dimoni.framework.model.TestModel;
import org.junit.Test;

import caperutxa.dimoni.framework.config.Configuration;
import org.junit.Assert;
import org.junit.Before;

public class TestListManagerTest {
	
	TestListManager manager;
	
	String testFile = "src/test/resources/testconfiguration/testlist.csv";
	
	@Before
	public void before() throws FileNotFoundException, IOException {
		Configuration.environment = "testa";
		Configuration.frameworkPropertiesFile = "src/test/resources/properties/frameworkTest.properties";
		Configuration.environmentPropertiesFile = "src/test/resources/properties/environmentTest.properties";
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
		
		line = manager.translatePathVariables("${soapui_projects_folder} -s\"Search\" -c\"searchByResort\" -r -foutput -PdestinationFolder=output ${api}\\ReadyApi\\XML-soapui-project.xml");
		Assert.assertTrue(line.contains("thisIsTheSoapProjectFolder"));
		Assert.assertTrue(line.contains("http://api.testa.com"));
		
		line = manager.translatePathVariables("${do_not_translate} -s\"Search\" -c\"searchByResort\" -r -foutput -PdestinationFolder=output ${api}\\ReadyApi\\XML-soapui-project.xml");
		Assert.assertTrue(line.contains("${do_not_translate}"));
		Assert.assertTrue(line.contains("http://api.testa.com"));
		
		line = manager.translatePathVariables("1#Smoke internal api#soapui#-s\"Smoke\" -c\"SearchAndBook\" -r -foutput -PdestinationFolder=output -Penvironment=${databaseinstancename} -Pendpoint=${internal_api_pos} -PendpointXMLAPI=${xmlapi_postget_post_static} -PagentId=102 ${soapui_projects_folder}\\ReadyApi\\XML-InternalApiPos-soapui-project.xml#InternalApiPos#XMLAPI,provisional#critical,transfer");
		Assert.assertTrue(line.contains("internalurl"));
		Assert.assertTrue(line.contains("thisisstatic"));
		Assert.assertTrue(line.contains("develop_a"));
	}

	@Test
	public void getTestByIdFromFileTest() {
		manager = new TestListManager();

		File file = new File(testFile);
		int id = 1;
		Map<Integer, TestModel> testList = manager.getTestByIdFromFile(file.getAbsolutePath(), id);

		int counter = 0;
		for(Map.Entry<Integer, TestModel> m : testList.entrySet()) {
			Assert.assertTrue(id == m.getValue().getId());
			counter++;
		}
		Assert.assertTrue(1 == counter); // At Only one result

		id = 10;
		testList = manager.getTestByIdFromFile(file.getAbsolutePath(), id);

		counter = 0;
		for(Map.Entry<Integer, TestModel> m : testList.entrySet()) {
			Assert.assertTrue(id == m.getValue().getId());
			counter++;
		}
		Assert.assertTrue(1 == counter); // At Only one result

		id = 999;
		testList = manager.getTestByIdFromFile(file.getAbsolutePath(), id);

		counter = 0;
		for(Map.Entry<Integer, TestModel> m : testList.entrySet()) {
			Assert.assertTrue(id == m.getValue().getId());
			counter++;
		}
		Assert.assertTrue(0 == counter); // At Only one result
	}
}
