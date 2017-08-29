package caperutxa.dimoni.framework.executer;

import caperutxa.dimoni.framework.config.Configuration;
import caperutxa.dimoni.framework.model.TestModel;
import caperutxa.dimoni.framework.model.TestSummary;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestReport {

	/**
	 * The reporting consist into
	 * 1 - Summary
	 * 2 - Table with cases
	 *
	 * @param list
	 * @return
	 */
	public String prepareTestResults(List<TestModel> list) {
		StringBuilder summaryTable = new StringBuilder();
		StringBuilder content = new StringBuilder();
		Map<String,TestSummary> summary = new LinkedHashMap<String, TestSummary>();
		summary.put("total", new TestSummary("total"));

		System.out.println("Prepare test results");

		content.append("<h3>Test list</h3><table><tr><td>Id</td><td>Success</td><td>Start</td><td>End</td><td>Technology</td><td>Parameters</td><td></td></tr>");
		for(TestModel m : list) {
			addTestToSummary(summary, m);
			content.append("<tr>").append(addTestFeaturesToTable(m)).append("</tr>");
		}
		content.append("</table>");

		System.out.println("Prepare summary");
		summaryTable.append("<h1>Test summary</h1><table><tr><td>Technology</td><td>total</td><td>Success</td><td>Failed</td><td>Success / Failed</td></tr>");
		for(Map.Entry<String, TestSummary> entry : summary.entrySet()) {
			summaryTable.append(addSummaryToTable(entry.getValue()));
		}
		summaryTable.append("</table>");

		StringBuilder resultContent = new StringBuilder()
				.append(getHeader())
				.append(summaryTable)
				.append(content)
				.append(getFooter());

		return resultContent.toString();
	}

	String getHeader() {
		return "<html><body>";
	}

	String getFooter() {
		return "</body></html>";
	}

	/**
	 *
	 * @param test
	 */
	void addTestToSummary(Map<String,TestSummary> summary, TestModel test) {
		summary.get("total").updateOnSuccess(test.isResult());

		if(!summary.containsKey(test.getTechnology())) {
			summary.put(test.getTechnology(), new TestSummary(test.getTechnology()));
		}

		summary.get(test.getTechnology()).updateOnSuccess(test.isResult());
	}

	/**
	 * summaryTable.append("<table><tr><td>Technology</td><td>total</td><td>Success</td><td>Failed</td><td>Success / Failed</td></tr>");
	 *
	 * @param s
	 * @return
	 */
	String addSummaryToTable(TestSummary s) {
		StringBuilder summary = new StringBuilder()
				.append("<tr><td>")
				.append(s.getType())
				.append("</td><td>")
				.append(s.getTotalTest())
				.append("</td><td>")
				.append(s.getSuccessPercentage()).append(" %")
				.append("</td><td>")
				.append(s.getFailedPercentage()).append(" %")
				.append("</td><td>")
				.append(s.getSuccess()).append(" / ").append(s.getFailed())
				.append("</td></tr>");

		return summary.toString();
	}
	/**
	 * Add to results table
	 *
	 * <tr><td>Id</td><td>Success</td><td>Start</td><td>End</td><td>Technology</td><td>Parameters</td><td>Others</td></tr>;
	 *
	 * @param m
	 * @return
	 */
	String addTestFeaturesToTable(TestModel m) {
		StringBuilder content = new StringBuilder();

		content.append("<tr>")
				.append("<td>")
				.append(m.getId())
				.append("</td><td>")
				.append(m.isResult())
				.append("</td><td>")
				.append(m.getStart())
				.append("</td><td>")
				.append(m.getEnd())
				.append("</td><td>")
				.append(m.getTechnology())
				.append("</td><td>")
				.append(m.getParameters())
				.append("</td><td>");

		if(null != m.getErrorMessage()) {
			content.append(m.getErrorMessage());
		}
		if(null != m.getFailedError()) {
			content.append(m.getFailedError());
		}

		content.append("</td>")
				.append("</tr>");

		return content.toString();
	}

}
