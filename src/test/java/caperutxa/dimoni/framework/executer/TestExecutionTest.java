package caperutxa.dimoni.framework.executer;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.iq80.snappy.Main;
import org.junit.Before;
import org.junit.Test;

import caperutxa.dimoni.framework.config.Configuration;

public class TestExecutionTest {

	TestExecution executer;
	
	String[] args = {"-component=XMLAPI",
			"-environment=stage",
			//"-testscenario=bookroom",
			"-technology=selenium",
			//"-trigger=smoke",
			//"-custom=customTest"
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
	
	@Test
	public void runTestTest() {
		//executer.runTest();
	}
	
	@Test
	public void fullProcess() throws Exception {
		String[] args = { "-component=XMLAPI" };
		//Main.main(args);
	}
}
