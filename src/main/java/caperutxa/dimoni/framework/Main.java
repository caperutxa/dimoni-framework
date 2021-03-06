package caperutxa.dimoni.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;

import caperutxa.dimoni.framework.config.Configuration;
import caperutxa.dimoni.framework.executer.TestExecution;
import caperutxa.dimoni.framework.model.TestModel;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// get default configuration
		Configuration.defaultFrameworkConfiguration();
		// parse parameters
		Configuration.parseParameters(args);
		// get test source and test list
		Configuration.getTestList();
		
		// run tests (allow multithread)
		TestExecution runner = new TestExecution();
		runner.runTest();
		
		// get results
		String mailContent = prepareResults();
		
		// send mail summary (only if any test arise)
		logResults(mailContent);
		sendMail(mailContent);
		
		System.out.println("End");
	}
	
	/**
	 * Get test list and prepare a mail
	 * 
	 * @return
	 */
	static String prepareResults() {
		StringBuilder content = new StringBuilder();
		List<TestModel> list = Configuration.getListOfTests();
		
		for(TestModel m : list) {
			System.out.println("Preparing results for test id = " + m.getId());
			
			content.append(System.lineSeparator());
			
			content.append("Test ").append(m.getId())
					.append(", components=[").append(m.getComponents())
					.append("], Test case ").append(m.getTechnology())
					.append(", ").append(m.getTechnology())
					.append(", parameters [ ").append(m.getParameters()).append(" ]");
			
			content.append(System.lineSeparator());
			
			content.append("Start at ").append(m.getStart())
				.append(" , end at").append(m.getEnd());
			
			content.append(System.lineSeparator());
			
			content.append("Result : ").append(m.isResult());
			
			if(null != m.getErrorMessage()) {
				content.append(System.lineSeparator()).append(m.getErrorMessage());
			}
			
			if(null != m.getFailedError()) {
				content.append(System.lineSeparator()).append(m.getFailedError());
			}
			
			content.append(System.lineSeparator());
		}
		
		System.out.println("Summary");
		System.out.println(content.toString());
		
		return content.toString();
	}
	
	/**
	 * Send mail with end test results
	 * 
	 * @param content
	 */
	static void sendMail(String content) {
		if(!Configuration.frameworkProperties.getProperty("mail_send").equals("true"))
			return;
		
		String to = Configuration.frameworkProperties.getProperty("mail_to");
		String from = Configuration.frameworkProperties.getProperty("mail_from");
		String host = Configuration.frameworkProperties.getProperty("mail_host");
		String port = Configuration.frameworkProperties.getProperty("mail_port");
		
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", port);
		properties.setProperty("mail.smtp.auth", "false");
		properties.setProperty("mail.smtp.ssl.trust", host);
		properties.setProperty("mail.smtp.starttls.enable", "true");
		
		Session session = Session.getDefaultInstance(properties);
		
		try {
			MimeMessage message = new MimeMessage(session);
			
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Test results");
			message.setText(content);
			message.setFrom(new InternetAddress(from));
			
			Transport.send(message);
	        System.out.println("Message sent successfully");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Log results to file with relative path logs
	 * @param content
	 */
	static void logResults(String content) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss_SSS");
		String rightNow = dateFormat.format(new Date());
		File target = new File("logs/testResults" + rightNow + ".log");
		File parent = target.getParentFile();
		try {
			if (!parent.exists() && !parent.mkdirs()) {
				throw new IllegalStateException("Couldn't create dir: " + parent);
			}
			FileUtils.write(target, content, "UTF-8");
		} catch (IOException e) {
			System.out.println("Unable to log the test results into file. " + e.getMessage());
		}
	}

}
