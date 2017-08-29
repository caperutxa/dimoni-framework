package caperutxa.dimoni.framework.executer;

import caperutxa.dimoni.framework.model.TestModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TestReportTest {

	TestReport report;

	@Before
	public void before() {
		report = new TestReport();
	}

	@Test
	public void showTestReportResults() {
		List<TestModel> testList = addTestToTheList();
		String out = report.prepareTestResults(testList);

		System.out.println(out);
		Assert.assertTrue(out.contains("<td>total</td><td>20</td>"));
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
