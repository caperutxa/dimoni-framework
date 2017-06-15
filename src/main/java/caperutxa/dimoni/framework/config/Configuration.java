package caperutxa.dimoni.framework.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import caperutxa.dimoni.framework.manager.TestListManager;
import caperutxa.dimoni.framework.model.TestModel;

/**
 * The test configuration run should be done by a current configuration
 * 
 * @author caperutxa
 *
 */
public class Configuration {

	public static String component;
	public static String environment;
	public static String testScenario;
	public static String trigger;
	public static String technology;
	
	public static String custom;
	
	static int currentTestIndex = 0;
	static List<TestModel> listOfTests;
	public static Map<Integer, TestModel> testMap;
	
	public static Properties frameworkProperties;
	public static String frameworkPropertiesFile = "config/framework.properties";
	
	public static Properties environmentProperties;
	public static String environmentPropertiesFile = "config/framework.properties";
	
	/**
	 * To allow threading only one list and index should remain
	 * Then use this method to access the next test to run
	 * @return
	 */
	public static TestModel getNextTestToRun() {
		if(currentTestIndex == listOfTests.size())
			return null;
		
		TestModel m = listOfTests.get(currentTestIndex);
		currentTestIndex++;
		return m;
	}
	
	/**
	 * -component=
	 * -environment=
	 * -mailsend=
	 * -technology=
	 * -testlistfile=
	 * -testscenario=
	 * -trigger=
	 * 
	 * -custom= is unclear right now
	 * 
	 * @param args
	 */
	public static void parseParameters(String[] args) {
		
		for(String a : args) {
			if(a.toLowerCase().startsWith("-component=")) {
				component = a.substring(11).toLowerCase();
				System.out.println("Component deteted : " + component);
			} else if(a.toLowerCase().startsWith("-custom=")) {
				custom = a.substring(8).toLowerCase();
				System.out.println("Custom test deteted : " + custom);
			} else if(a.toLowerCase().startsWith("-environment=")) {
				environment = a.substring(13).toLowerCase();
				System.out.println("Environment detailed : " + environment);
			} else if(a.toLowerCase().startsWith("-mailsend=")) {
				frameworkProperties.setProperty("mail_send", a.substring(10));
				System.out.println("Send mail : " + a.substring(10));
			} else if(a.toLowerCase().startsWith("-technology=")) {
				technology = a.substring(12).toLowerCase();
				System.out.println("Technology to look for : " + technology);
			} else if(a.toLowerCase().startsWith("-testlistfile=")) {
				frameworkProperties.setProperty("test_list_file", a.substring(14));
				System.out.println("File for test list : " + a.substring(14));
			} else if(a.toLowerCase().startsWith("-testscenario=")) {
				testScenario = a.substring(14).toLowerCase();
				System.out.println("Test case to run : " + testScenario);
			} else if(a.toLowerCase().startsWith("-trigger=")) {
				trigger = a.substring(9).toLowerCase();
				System.out.println("Trigger : " + trigger);
			}
		}
	}
	
	/**
	 * Load the default configuration for the framework
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void defaultFrameworkConfiguration() throws FileNotFoundException, IOException {
		frameworkProperties = new Properties();
		frameworkProperties.load(new FileInputStream(frameworkPropertiesFile));
		
		environmentProperties = new Properties();
		environmentProperties.load(new FileInputStream(environmentPropertiesFile));
	}
	
	/**
	 * Fill the test list depending of the current configuration
	 * 
	 * At the end, transform the map to list
	 * The system will use that list
	 * 
	 */
	public static void getTestList() {
		testMap = new LinkedHashMap<Integer, TestModel>();
		TestListManager manager = new TestListManager();
		
		if(null != component) {
			combineTestLists(manager.getTestByComponentFromFile(frameworkProperties.getProperty("test_list_file"), component));
		}
		
		transformTestMapToList();
	}
	
	/**
	 * If any key inside the map exist it is overridden by the new one
	 * use that feature to merge lists
	 * 
	 * @param list
	 */
	static void combineTestLists(Map<Integer, TestModel> list) {
		for(Map.Entry<Integer, TestModel> entry : list.entrySet()) {
			testMap.put(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Allow threading and use a list instead of map
	 * and initialize the current index
	 */
	static void transformTestMapToList() {
		listOfTests = new LinkedList<TestModel>();
		currentTestIndex = 0;
		
		for(Map.Entry<Integer, TestModel> entry : testMap.entrySet()) {
			listOfTests.add(entry.getValue());
		}
	}
	
	public static List<TestModel> getListOfTests() {
		return listOfTests;
	}
}
