package caperutxa.dimoni.framework.config;

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
	
	public static String custom;
	
	/**
	 * -component=
	 * -environment=
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
				component = a.substring(11);
				System.out.println("Component deteted : " + component);
			} else if(a.toLowerCase().startsWith("-custom=")) {
				custom = a.substring(8);
				System.out.println("Custom test deteted : " + custom);
			} else if(a.toLowerCase().startsWith("-environment=")) {
				environment = a.substring(13);
				System.out.println("Environment detailed : " + environment);
			} else if(a.toLowerCase().startsWith("-testscenario=")) {
				testScenario = a.substring(14);
				System.out.println("Test case to run : " + testScenario);
			} else if(a.toLowerCase().startsWith("-trigger=")) {
				trigger = a.substring(9);
				System.out.println("Trigger : " + trigger);
			}
		}
	}
}
