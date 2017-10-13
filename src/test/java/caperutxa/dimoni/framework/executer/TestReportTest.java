package caperutxa.dimoni.framework.executer;

import caperutxa.dimoni.framework.config.Configuration;
import caperutxa.dimoni.framework.model.TestModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class TestReportTest {

	TestReport report;

	@Before
	public void before() {
		Configuration.frameworkProperties = new Properties();
		Configuration.frameworkProperties.setProperty("test_list_file", "src/test/resources/testconfiguration/testReportSample.csv");
		report = new TestReport();
	}

	@Test
	public void showTestReportResults() {
		List<TestModel> testList = addTestToTheList();
		String out = report.prepareTestResults("Test list", testList);

		writeDownToFile(out, "logs/testReport1.html");
		//Nothing to validate
	}

	@Test
	public void createTestResults() {
		Configuration.component = "all";
		Configuration.getTestList();
		setSuccessOrFailAtRandom(Configuration.getListOfTests());
		Configuration.componentsApplied = "Test title";
		String out = report.prepareTestResults(Configuration.componentsApplied, Configuration.getListOfTests());
		String contentOut = report.createContentMail(Configuration.componentsApplied);
		report.getMailSubject(Configuration.componentsApplied);

		writeDownToFile(out, "logs/testReport2.html");
		writeDownToFile(contentOut, "logs/testContentReport2.html");

		// Nothing to validate
	}

	void writeDownToFile(String out, String outpurFile) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter( outpurFile );
			pw.println(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			pw.close();
		}
	}

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

	List<TestModel> addTestToTheList() {
		List<TestModel> testList = new LinkedList<TestModel>();

		for(int i = 0; i < 7; i++) {
			TestModel test = new TestModel();
			test.setId(i);
			test.setResult(true);
			test.setTechnology("Tech1");
			test.setParameters("Parameters");
			test.setComponents("Components");
			test.setErrorMessage("No error");
			test.setStart(new Date());
			test.setEnd(new Date());
			testList.add(test);
		}

		for(int i = 0; i < 3; i++) {
			TestModel test = new TestModel();
			test.setId(i);
			test.setResult(false);
			test.setTechnology("Tech1");
			test.setParameters("Parameters");
			test.setComponents("Components");
			test.setFailedError("Failed");
			test.setStart(new Date());
			test.setEnd(new Date());
			testList.add(test);
		}

		for(int i = 0; i < 7; i++) {
			TestModel test = new TestModel();
			test.setId(i);
			test.setResult(false);
			test.setTechnology("Tech2");
			test.setParameters("Parameters");
			test.setComponents("Components");
			test.setErrorMessage("No error");
			test.setStart(new Date());
			test.setEnd(new Date());
			testList.add(test);
		}

		for(int i = 0; i < 3; i++) {
			TestModel test = new TestModel();
			test.setId(i);
			test.setResult(true);
			test.setTechnology("Tech2");
			test.setParameters("Parameters");
			test.setComponents("Components");
			test.setFailedError("Failed");
			test.setStart(new Date());
			test.setEnd(new Date());
			testList.add(test);
		}

		return testList;
	}
}
