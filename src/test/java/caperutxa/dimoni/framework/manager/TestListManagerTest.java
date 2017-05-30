package caperutxa.dimoni.framework.manager;

import java.io.File;

import org.junit.Test;

public class TestListManagerTest {
	
	TestListManager manager;
	
	String testFile = "src/test/resources/testconfiguration/testlist.csv";
	
	/**
	 * Smoke test
	 */
	@Test
	public void getTestListFromFileTest() {	
		File file = new File(testFile);
		String component = "GUI";
		//manager.getTestByComponentFromFile(file.getAbsolutePath(), component);
		manager.getTestFromFile(file.getAbsolutePath());
	}
}
