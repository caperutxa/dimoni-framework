package caperutxa.dimoni.framework.manager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import caperutxa.dimoni.framework.config.Configuration;
import caperutxa.dimoni.framework.model.TestModel;

public class TestListManager {
	
	public Map<Integer, TestModel> getTestByComponentFromFile(String path, String component) {
		Map<Integer, TestModel> testList= new LinkedHashMap<Integer, TestModel>();
		List<String> lines = getTestFromFile(path);
		
		for(String l : lines) {
			try {
				String translated = translatePathVariables(l);
				String[] parts = translated.split("#");
				//if(parts[4].toLowerCase().contains(component.toLowerCase()) || parts[5].toLowerCase().contains(component.toLowerCase())) {
				String componentList = parts[4] + "," + parts[5];
				if(filterByComponent(componentList, component)) {
					TestModel t = new TestModel();
					t.setId(Integer.parseInt(parts[0]));
					t.setTestCase(parts[1]);
					t.setTechnology(parts[2]);
					t.setParameters(parts[3]);
					t.setComponents(parts[4] + "," + parts[5]);
					t.setTrigger(parts[6]);
					testList.put(t.getId(), t);
				}
			} catch(Exception e) {
				System.out.println("Error while parsing test line from file : " + l + "    . Error message : " + e.getMessage());
			}
		}
		
		return testList;
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
	 * @param parameter
	 * @return
	 */
	public String translatePathVariables(String line) {
		for(Entry<Object, Object> e : Configuration.frameworkProperties.entrySet()) {
			line = line.replace("${" + e.getKey() + "}", e.getValue().toString());
		} 
		
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
		String[] parts = componentList.split(",");
		for(String s : parts) {
			if(s.toLowerCase().equals(component.toLowerCase()))
				return true;
		}
		
		return false;
	}

}
