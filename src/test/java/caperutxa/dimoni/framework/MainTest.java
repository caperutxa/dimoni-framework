package caperutxa.dimoni.framework;

import caperutxa.dimoni.framework.config.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MainTest {

	@Before
	public void before() throws IOException {
		Configuration.environment = "testa";
		Configuration.frameworkPropertiesFile = "src/test/resources/properties/frameworkTest.properties";
		Configuration.environmentPropertiesFile = "src/test/resources/properties/environmentTest.properties";
		Configuration.defaultFrameworkConfiguration();
	}

	/*
	 * This method is intentionally commented
	@Test
	public void sendMailTest() {
		String mailContent = "Test mail with multiple recipients";

		Main.sendMail(mailContent);
	}
	*/
}
