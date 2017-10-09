package caperutxa.dimoni.framework;

import caperutxa.dimoni.framework.config.Configuration;
import caperutxa.dimoni.framework.executer.TestReport;
import caperutxa.dimoni.framework.model.TestModel;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MainTest {

	TestReport report;

	@Before
	public void before() throws IOException {
		Configuration.environment = "testa";
		Configuration.frameworkPropertiesFile = "src/test/resources/properties/frameworkTest.properties";
		Configuration.environmentPropertiesFile = "src/test/resources/properties/environmentTest.properties";
		Configuration.defaultFrameworkConfiguration();
		Configuration.frameworkProperties.setProperty("test_list_file", "src/test/resources/testconfiguration/testReportSample.csv");
		report = new TestReport();
	}

	/*
	 * This method is intentionally commented
	 */
	/*
	@Test
	public void sendMailTest() {
		String mailContent = "Test mail with multiple recipients";
		Configuration.component = "all";
		Configuration.getTestList();
		setSuccessOrFailAtRandom(Configuration.getListOfTests());
		report.prepareResultsForLogAndMail(Configuration.getListOfTests());

		Main.sendMail(report.getMailContent(), report.getMailAttached());
	}
	*/

	/**
	 * Declare the test success or fail at random
	 * @param testList
	 */
	void setSuccessOrFailAtRandom(List<TestModel> testList) {
		for(TestModel model : testList) {
			model.setResult(Math.random() < 0.6);
			model.setStart(new Date());
			model.setEnd(new Date());
		}
	}

}
