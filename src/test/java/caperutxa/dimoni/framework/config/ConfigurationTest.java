package caperutxa.dimoni.framework.config;

import org.junit.Test;
import org.junit.Assert;

public class ConfigurationTest {

	@Test
	public void parseParametersTest() {
		String[] args = { "-component=GUI",
				"-environment=stage",
				"-testscenario=bookroom",
				"-trigger=smoke",
				"-custom=customTest"};
		
		Configuration.parseParameters(args);
		
		Assert.assertEquals("GUI", Configuration.component);
		Assert.assertEquals("stage", Configuration.environment);
		Assert.assertEquals("bookroom", Configuration.testScenario);
		Assert.assertEquals("smoke", Configuration.trigger);
		Assert.assertEquals("customTest", Configuration.custom);
	}
	
	@Test
	public void parseParametersCaseInsensitiveTest() {
		String[] args = { "-Component=GUI",
				"-Environment=stage",
				"-testScenario=bookroom",
				"-trigger=smoke",
				"-Custom=customTest"};
		
		Configuration.parseParameters(args);
		
		Assert.assertEquals("GUI", Configuration.component);
		Assert.assertEquals("stage", Configuration.environment);
		Assert.assertEquals("bookroom", Configuration.testScenario);
		Assert.assertEquals("smoke", Configuration.trigger);
		Assert.assertEquals("customTest", Configuration.custom);
	}
}
