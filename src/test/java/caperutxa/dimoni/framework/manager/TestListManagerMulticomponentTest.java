package caperutxa.dimoni.framework.manager;

import caperutxa.dimoni.framework.config.Configuration;
import caperutxa.dimoni.framework.model.TestModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Allow multicomponent run
 */
@RunWith(Parameterized.class)
public class TestListManagerMulticomponentTest {

	TestListManager manager;

	String testFile = "src/test/resources/testconfiguration/testlist.csv";

	String expectedComponent;
	int[] expectedIds;
	boolean result;

	public TestListManagerMulticomponentTest(String expected, int[] expectedIds, boolean result) {
		this.expectedComponent = expected;
		this.expectedIds = expectedIds;
		this.result = result;
	}

	@Before
	public void before() throws FileNotFoundException, IOException {
		Configuration.frameworkPropertiesFile = "src/test/resources/properties/frameworkTest.properties";
		Configuration.defaultFrameworkConfiguration();

		manager = new TestListManager();
	}

	@Parameterized.Parameters(name = "{index}: component = {0} ==> {2}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ "GUI,critical", new int[]{2,3,6,7,8,9,10,12,13,14}, true },
				{ "smoke,language", new int[]{2,3,4,5,7,11}, true },
				{ "nonexisting1,nonexisting2", new int[]{0}, false },
				{ " filter_spaces , language, bussines", new int[]{3,4,8}, true},
				{ "one_alone", new int[]{0}, false},
				{ "bussines", new int[]{8}, true}
		});
	}

	@Test
	public void getTestListFromFileTest() {
		boolean found = false;
		File file = new File(testFile);

		Map<Integer, TestModel> testList = manager.getTestByComponentFromFile(file.getAbsolutePath(), expectedComponent);

		for(Map.Entry<Integer, TestModel> m : testList.entrySet()) {
			boolean loopAssert = false;
			for(int i : expectedIds) {
				if(i == m.getValue().getId()) {
					loopAssert = true;
					break;
				}
			}
			//System.out.println(m.getValue().getId() + " expected " + result);
			Assert.assertEquals(result, loopAssert);
			found = true;
		}
		Assert.assertEquals(result, found); // At least one result
	}
}
