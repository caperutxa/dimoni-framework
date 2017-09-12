package caperutxa.dimoni.framework.manager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import caperutxa.dimoni.framework.config.Configuration;
import caperutxa.dimoni.framework.model.TestModel;

public class TestListManager {

	/**
	 * Extract the tests that match the component
	 *
	 * The component could be a list of components,
	 * then once is detected break the loop
	 *
	 * @param path
	 * @param component
	 * @return
	 */
	public Map<Integer, TestModel> getTestByComponentFromFile(String path, String component) {
		Map<Integer, TestModel> testList= new LinkedHashMap<Integer, TestModel>();
		List<String> lines = getTestFromFile(path);
		
		for(String l : lines) {
			try {
				String[] parts = l.split("#");
				StringBuilder builder = new StringBuilder(parts[4]);
				if(5 < parts.length)
					builder.append(",").append(parts[5]);
				if(6 < parts.length)
					builder.append(",").append(parts[6]);
				String componentList = builder.toString();

				String[] componentSplit = component.split(",");
				for(String splitted : componentSplit) {
					if (filterByComponent(componentList, splitted.trim())) {
						TestModel t = translateAndGetTestModel(l);
						testList.put(t.getId(), t);
						break;
					}
				}
			} catch(Exception e) {
				System.out.println("Error while parsing test line from file : " + l);
				System.out.println("Error message : " + e.getMessage());
				System.out.println(e.toString());
			}
		}
		
		return testList;
	}

	/**
	 * Extract test with a given id
	 *
	 * @param path
	 * @param id of the test that is desired to run
	 * @return
	 */
	public Map<Integer, TestModel> getTestByIdFromFile(String path, int id) {
		Map<Integer, TestModel> testList= new LinkedHashMap<Integer, TestModel>();
		List<String> lines = getTestFromFile(path);

		for(String l : lines) {
			try {
				String[] parts = l.split("#");
				int listId = Integer.parseInt(parts[0]);

				if(listId == id) {
					TestModel t = translateAndGetTestModel(l);
					testList.put(t.getId(), t);
				}
			} catch(Exception e) {
				System.out.println("Error while parsing test line from file : " + l);
				System.out.println("Error message : " + e.getMessage());
				System.out.println(e.toString());
			}
		}

		return testList;
	}

	TestModel translateAndGetTestModel(String line) {
		TestModel t = new TestModel();

		String translated = translatePathVariables(line);
		String[] partsTranslated = translated.split("#");

		StringBuilder builder = new StringBuilder(partsTranslated[4]);
		if(5 < partsTranslated.length)
			builder.append(",").append(partsTranslated[5]);
		if(6 < partsTranslated.length)
			builder.append(",").append(partsTranslated[6]);
		String componentList = builder.toString();

		t.setId(Integer.parseInt(partsTranslated[0]));
		t.setTestCase(partsTranslated[1]);
		t.setTechnology(partsTranslated[2]);
		t.setParameters(partsTranslated[3]);
		t.setComponents(componentList);

		return t;
	}


	
	List<String> getTestFromFile(String path) {
		List<String> list = new LinkedList<String>();
		
		try {
			list = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * The test list should contain a relative path to the projects folder
	 * then we need to translate that
	 * 
	 * This is because no one needs to place the projects in the same folder
	 * 
	 * @param line
	 * @return
	 */
	public String translatePathVariables(String line) {
		Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(line);
		
		while (matcher.find()) {
			line = translateGroup(line, matcher.group(1));
		}
		
		return line;
	}
	
	/**
	 * Translate the pattern using any properties file defined
	 * @param line
	 * @param group
	 * @return
	 */
	String translateGroup(String line, String group) {
		if(null != Configuration.frameworkProperties.getProperty(group)) {
			line = line.replace("${" + group + "}", Configuration.frameworkProperties.getProperty(group));
			return line;
		}
		
		String environmentGroup = Configuration.environment + "_" + group;
		if(null != Configuration.environmentProperties.getProperty(environmentGroup)) {
			line = line.replace("${" + group + "}", Configuration.environmentProperties.getProperty(environmentGroup));
			return line;
		}
		
		System.out.println("WARNING : Line does not translated : " + line);
		return line;
	}
	
	/**
	 * The test list uses the secondary components separated by commas
	 * This method use as parameter the main component and secondary components joined by commas
	 * then split and look for equals compare
	 * 
	 * @param componentList
	 * @return true if the component is in the string
	 */
	boolean filterByComponent(String componentList, String component) {
		if(component.toLowerCase().equals("all"))
			return true;
		
		String[] parts = componentList.split(",");
		for(String s : parts) {
			if(s.trim().toLowerCase().equals(component.toLowerCase()))
				return true;
		}
		
		return false;
	}

}
