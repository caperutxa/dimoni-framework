package caperutxa.dimoni.framework.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;

import caperutxa.dimoni.framework.config.Configuration;
import caperutxa.dimoni.framework.model.TestModel;

@RunWith(Parameterized.class)
public class TestListManagerParametrizedTest {
	
	TestListManager manager;
	
	String testFile = "src/test/resources/testconfiguration/testlist.csv";
	
	String expectedComponent;
	
	public TestListManagerParametrizedTest(String expected) {
		this.expectedComponent = expected;
	}
	
	@Before
	public void before() throws FileNotFoundException, IOException {
		Configuration.frameworkPropertiesFile = "src/test/resources/properties/frameworkTest.properties";
		Configuration.defaultFrameworkConfiguration();
		
		manager = new TestListManager();
	}
	
	@Parameters(name = "{index}: component = '{0}'")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {     
                 { "GUI" }, { "api" }, { "deepapi" }
           });
    }
    
	@Test
	public void getTestListFromFileTest() {
		boolean found = false;
		File file = new File(testFile);

		Map<Integer, TestModel> testList = manager.getTestByComponentFromFile(file.getAbsolutePath(), expectedComponent);

		for(Map.Entry<Integer, TestModel> m : testList.entrySet()) {
			Assert.assertTrue(m.getValue().getComponents().contains(expectedComponent));
			Assert.assertTrue(!m.getValue().getComponents().contains(expectedComponent+"s"));
			found = true;
		}
		Assert.assertTrue(found); // At least one result
	}

}
