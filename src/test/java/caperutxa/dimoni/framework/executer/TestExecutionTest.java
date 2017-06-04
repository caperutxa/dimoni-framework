package caperutxa.dimoni.framework.executer;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import caperutxa.dimoni.framework.config.Configuration;

public class TestExecutionTest {

	TestExecution executer;
	
	String[] args = {"-component=XMLAPI",
			"-environment=stage",
			"-technology=selenium"
			};
	
	@Before
	public void before() throws FileNotFoundException, IOException {
		executer = new TestExecution();
		Configuration.frameworkPropertiesFile = "src/test/resources/properties/frameworkTest.properties";
		Configuration.defaultFrameworkConfiguration();
		Configuration.frameworkProperties.setProperty("test_list_file", "c:/Temp/automation/testlistSample.txt");
		Configuration.parseParameters(args);
		Configuration.getTestList();
	}
	
	/**
	 * The code is commented intentionally
	 * just check that the set-up is correct
	 * 
	 * The test is an integration test
	 * because it needs external installation to run tests
	 */
	@Test
	public void runTestTest() {
		//executer.runTest();
	}
	
	/**
	 * The code is commented intentionally
	 * just check that the set-up is correct
	 * 
	 * The test is an integration test
	 * because it needs external installation to run tests
	 */
	@Test
	public void fullProcess() throws Exception {
		//String[] args = { "-component=XMLAPI" };
		//Main.main(args);
	}
}
