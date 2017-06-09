package caperutxa.dimoni.framework.config;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;

public class ConfigurationTest {

	@Test
	public void parseParametersTest() {
		String[] args = { "-component=GUI",
				"-environment=stage",
				"-testscenario=bookroom",
				"-technology=selenium",
				"-trigger=smoke",
				"-custom=customTest"};
		
		Configuration.parseParameters(args);
		
		Assert.assertEquals("gui", Configuration.component);
		Assert.assertEquals("stage", Configuration.environment);
		Assert.assertEquals("selenium", Configuration.technology);
		Assert.assertEquals("bookroom", Configuration.testScenario);
		Assert.assertEquals("smoke", Configuration.trigger);
		Assert.assertEquals("customtest", Configuration.custom);
	}
	
	@Test
	public void parseParametersCaseInsensitiveTest() {
		String[] args = { "-Component=GUI",
				"-Environment=stage",
				"-testScenario=bookroom",
				"-technology=SoapUI",
				"-trigger=smoke",
				"-Custom=customTest"};
		
		Configuration.parseParameters(args);
		
		Assert.assertEquals("gui", Configuration.component);
		Assert.assertEquals("stage", Configuration.environment);
		Assert.assertEquals("soapui", Configuration.technology);
		Assert.assertEquals("bookroom", Configuration.testScenario);
		Assert.assertEquals("smoke", Configuration.trigger);
		Assert.assertEquals("customtest", Configuration.custom);
	}
	
	@Test
	public void loadDefaultConfigurationTest() throws FileNotFoundException, IOException {
		Configuration.frameworkPropertiesFile = "src/test/resources/properties/frameworkTest.properties";
		Configuration.defaultFrameworkConfiguration();
		
		Assert.assertEquals("test", Configuration.frameworkProperties.getProperty("default_environment"));
		Assert.assertEquals("file", Configuration.frameworkProperties.getProperty("test_list_source"));
		Assert.assertEquals("src/test/resources/testconfiguration/testlist.csv", Configuration.frameworkProperties.getProperty("test_list_file"));
	}
	
	@Test
	public void parametersThatModifyProperties() throws FileNotFoundException, IOException {
		String[] args = { "-testlistfile=This is the new test list file",
				"-mailSend=true" };
		Configuration.frameworkPropertiesFile = "src/test/resources/properties/frameworkTest.properties";
		Configuration.defaultFrameworkConfiguration();
		Configuration.parseParameters(args);
		
		Assert.assertEquals("This is the new test list file", Configuration.frameworkProperties.getProperty("test_list_file"));
		Assert.assertEquals("true", Configuration.frameworkProperties.getProperty("mail_send"));
	}
	
}
