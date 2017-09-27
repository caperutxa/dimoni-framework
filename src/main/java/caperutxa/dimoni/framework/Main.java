package caperutxa.dimoni.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import caperutxa.dimoni.framework.executer.TestReport;
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
		TestReport report = new TestReport();
		return report.prepareTestResults(Configuration.getListOfTests());
	}
	
	/**
	 * Send mail with end test results
	 * 
	 * @param content
	 */
	static void sendMail(String content) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
		String fileName = "logs/summary_" + sdf.format(new Date());
		writeDownSummaryToFile(content, fileName);

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

			String[] recipients = to.split(",");
			for(String sendTo : recipients) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
			}
			message.setSubject("Test results");
			//message.setContent(content, "text/html");
			message.setFrom(new InternetAddress(from));

			Multipart multi = new MimeMultipart();
			MimeBodyPart body = new MimeBodyPart();
			body.setContent(content, "text/html");
			multi.addBodyPart(body);
			MimeBodyPart attach = new MimeBodyPart();
			DataSource source = new FileDataSource(fileName);
			attach.setDataHandler(new DataHandler(source));
			attach.setFileName("TestSummary.html");
			multi.addBodyPart(attach);

			message.setContent(multi);

			Transport.send(message);
	        System.out.println("Message sent successfully");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	static void writeDownSummaryToFile(String out, String outputFile) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter( outputFile );
			pw.println(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			pw.close();
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
