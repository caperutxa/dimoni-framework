package caperutxa.dimoni.framework.executer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import caperutxa.dimoni.framework.config.Configuration;
import caperutxa.dimoni.framework.model.TestModel;

public class TestExecution {

	public void runTest() {
		int i = 0;
		Runtime runtime = Runtime.getRuntime();
		
		while(true) {
			System.out.println("This is my test i=" + i);
			TestModel test = Configuration.getNextTestToRun();
			
			if(null == test)
				break;
			
			test.setStart(new Date());
			
			try {
				
				
				System.out.println("Run by console = " + Configuration.frameworkProperties.getProperty(test.getTechnology()));
				System.out.println("Test = " + test.getId() + " - " + test.getTestCase());
				StringBuilder command = new StringBuilder().append(Configuration.frameworkProperties.getProperty(test.getTechnology())).append(" ")
						.append(test.getParameters());
				System.out.println("Prepare command = " + command.toString());
				
				Process pr = runtime.exec(command.toString());
				BufferedReader reader = new BufferedReader( new InputStreamReader(pr.getInputStream()) );
				String line;
				while((line = reader.readLine()) != null) 
                { 
                    System.out.println(line);
                }
				
				int exitVal = pr.waitFor();
				System.out.println("Exit value = " + exitVal);
				
				if(0 == exitVal) {
					test.setResult(true);
				} else {
					test.setResult(false);
				}
				
			} catch (IOException | InterruptedException e) {
				StringBuilder errorMessage = new StringBuilder().append("Unable to run test. id=").append(test.getId())
						.append(" , test case=").append(test.getTestCase())
						.append(" , technology=").append(test.getTechnology())
						.append(" , parameters=[").append(test.getParameters())
						.append("] , components=").append(test.getComponents())
						.append(", trigger=").append(test.getTrigger());
				System.out.println(errorMessage);
				System.out.println(e.getMessage());
				//e.printStackTrace();
				
				test.setResult(false);
				test.setFailedError(e.getMessage());
			}
			
			test.setEnd(new Date());
			i++;
		}
	}
}
