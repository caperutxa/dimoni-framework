package caperutxa.dimoni.framework.manager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import caperutxa.dimoni.framework.model.TestModel;

public class TestListManager {
	
	public List<TestModel> getTestByComponentFromFile(String path, String component) {
		List<TestModel> testList = new LinkedList<TestModel>();
		List<String> lines = getTestFromFile(path);
		
		for(String l : lines) {
			try {
				String[] parts = l.split("#");
				if(parts[3].toLowerCase().contains(component.toLowerCase()) || parts[4].toLowerCase().contains(component.toLowerCase())) {
					TestModel t = new TestModel();
					t.setTestCase(parts[0]);
					t.setTechnology(parts[1]);
					t.setParameters(parts[2]);
					t.setComponents(parts[3] + "," + parts[4]);
					t.setTrigger(parts[5]);
					testList.add(t);
				}
			} catch(Exception e) {
				System.out.println("Error while parsing test line from file : " + l + "    " + e.getMessage());
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

}
