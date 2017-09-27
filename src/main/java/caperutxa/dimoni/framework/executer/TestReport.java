package caperutxa.dimoni.framework.executer;

import caperutxa.dimoni.framework.model.TestModel;
import caperutxa.dimoni.framework.model.TestSummary;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestReport {

	Date startTest;
	Date endTest;
	/**
	 * The reporting consist into
	 * 1 - Summary
	 * 2 - Table with cases
	 *
	 * @param list
	 * @return
	 */
	public String prepareTestResults(List<TestModel> list) {
		StringBuilder content = new StringBuilder();
		Map<String,TestSummary> summary = new LinkedHashMap<String, TestSummary>();
		Map<String,TestSummary> summaryByComponent = new LinkedHashMap<String, TestSummary>();
		summary.put("total", new TestSummary("total"));

		System.out.println("Prepare test results");

		content.append("<hr /><h3>Test list</h3><table class=\"table table-hover table-condensed\"><tr><td>Id</td><td>Main component</td><td>Start</td><td>End</td><td>Technology</td><td>Parameters</td><td></td></tr>");
		for(TestModel m : list) {
			addTestToSummary(summary, m);
			addTestToSummaryByComponent(summaryByComponent, m);
			content.append(addTestFeaturesToTable(m));
		}
		content.append("</table>");

		System.out.println("Prepare summary");
		StringBuilder summaryHeader = prepareTheSummaryHeader();
		StringBuilder summaryTableByComponent = prepareTheSummary(summaryByComponent, "summaryByComponent");
		StringBuilder summaryTable = prepareTheSummary(summary, "summaryByTechnology");

		StringBuilder resultContent = new StringBuilder()
				.append(getHeader())
				.append(summaryHeader)
				.append("<div class=\"row\">")
				.append(summaryTableByComponent)
				.append(summaryTable)
				.append("</div>")
				.append(content)
				.append(getFooter());

		return resultContent.toString();
	}

	StringBuilder getHeader() {
		StringBuilder s = new StringBuilder()
				.append("<html><head>\n")
				.append("<title>Test summary</title>\n")
				.append("<link rel=\"icon\" type=\"image/png\" sizes=\"96x96\" href=\"http://icon-icons.com/icons2/586/PNG/128/robot-with-plug_icon-icons.com_55259.png\">")
				.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">")
				.append("<script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>")
				.append("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js\" integrity=\"sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4\" crossorigin=\"anonymous\"></script>")
				.append("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js\" integrity=\"sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1\" crossorigin=\"anonymous\"></script>")
				.append("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.0/Chart.bundle.min.js\"></script>")
				.append("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.0/Chart.min.js\"></script>")
				.append("\n</head>\n<body>")
				.append("<div class=\"container-fluid\">");

		return s;
	}

	StringBuilder getFooter() {
		StringBuilder s = new StringBuilder()
				.append("</div></body></html>");

		return s;
	}

	/**
	 *
	 * @return
	 */
	StringBuilder prepareTheSummaryHeader() {
		StringBuilder s = new StringBuilder()
				.append("<div class=\"row\"><div class=\"col-md-12\" style=\"margin-bottom:15px\"></div></div>")
				.append("<div class=\"row\">")
				.append("<div class=\"col-md-12\">")
				.append("<div class=\"jumbotron\">")
				.append("<p class=\"h1\">Test summary</p>")
				.append("<p><span class=\"text-muted small\">Start at ").append(startTest).append("</span></p>")
				.append("<p><span class=\"text-muted small\">Ends at ").append(endTest).append("</span></p>")
				.append("</div></div></div>");

		return s;
	}

	/**
	 * Prepare the chart that will appear in the results
	 * @param map
	 * @return
	 */
	StringBuilder prepareTheSummary(Map<String, TestSummary> map, String id) {
		StringBuilder summaryTable= new StringBuilder()
				.append("<div class=\"col-md-5\" id=\"div").append(id).append("\">");

		summaryTable.append("<canvas id=\"").append(id).append("\"></canvas>");

		//summaryTable.append("<h1>Test summary</h1><table><tr><td>Technology</td><td>total</td><td>Success</td><td>Failed</td><td>Success / Failed</td></tr>");
		StringBuilder labels = new StringBuilder();
		StringBuilder successData = new StringBuilder();
		StringBuilder failedData = new StringBuilder();
		StringBuilder backgroundSuccess = new StringBuilder();
		StringBuilder backgroundBorderSuccess = new StringBuilder();
		StringBuilder backgroundFailed = new StringBuilder();
		StringBuilder backgroundBorderFailed = new StringBuilder();
		int counter = 0;
		for(Map.Entry<String, TestSummary> entry : map.entrySet()) {
			if(0 < counter) {
				labels.append(",");
				successData.append(",");
				failedData.append(",");
				backgroundSuccess.append(",");
				backgroundFailed.append(",");
				backgroundBorderSuccess.append(",");
				backgroundBorderFailed.append(",");
			}
			//summaryTable.append(addSummaryToTable(entry.getValue()));
			labels.append("\"").append(entry.getValue().getType()).append("\"");
			successData.append(entry.getValue().getSuccess());
			failedData.append(entry.getValue().getFailed());
			backgroundSuccess.append("'rgba(75, 192, 192, 0.2)'");
			backgroundBorderSuccess.append("'rgba(75, 192, 192, 1)'");
			backgroundFailed.append("'rgba(255, 99, 132, 0.2)'");
			backgroundBorderFailed.append("'rgba(255,99,132,1)'");

			counter++;
		}
		//summaryTable.append("</table>");

		summaryTable.append("<script>");

		summaryTable.append("var ctx").append(id).append(" = document.getElementById(\"").append(id).append("\").getContext('2d');")
				.append("ctx").append(id).append(".canvas.width = $(\"#div").append(id).append("\").width();")
				.append("ctx").append(id).append(".canvas.height = 300;")
				.append("var myChart").append(id).append(" = new Chart(ctx").append(id).append(" , { type: 'bar', data: {")
				.append("labels: [").append(labels).append("],")
				.append("datasets: [{ label: 'Success', data: [").append(successData)
					.append("],backgroundColor: [").append(backgroundSuccess)
					.append("],borderColor: [").append(backgroundBorderSuccess)
					.append("],borderWidth: 1")
				.append("},{")
					.append("label: 'Failed', data: [").append(failedData)
					.append("],backgroundColor: [").append(backgroundFailed)
					.append("],borderColor: [").append(backgroundBorderFailed)
					.append("],borderWidth: 1")
				.append("}]},options: {scales: {yAxes: [{ticks: {beginAtZero:true}}]}}});");

		summaryTable.append("</script>");

		summaryTable.append("</div>");

		return summaryTable;
	}

	/**
	 * Added by technology
	 *
	 * @param test
	 */
	void addTestToSummary(Map<String,TestSummary> summary, TestModel test) {
		summary.get("total").updateOnSuccess(test.isResult());

		if(!summary.containsKey(test.getTechnology())) {
			summary.put(test.getTechnology(), new TestSummary(test.getTechnology()));
		}

		summary.get(test.getTechnology()).updateOnSuccess(test.isResult());

		if(null == startTest || test.getStart().before(startTest)) {
			startTest = test.getStart();
		}

		if(null == endTest || test.getEnd().after(endTest)) {
			endTest = test.getEnd();
		}
	}

	/**
	 * Add by component
	 *	Note that the total and the times are already updated in the addTestToSummaryByComponent method
	 * @param summary
	 * @param test
	 */
	void addTestToSummaryByComponent(Map<String,TestSummary> summary, TestModel test) {
		if(!summary.containsKey(test.getMainComponent())) {
			summary.put(test.getMainComponent(), new TestSummary(test.getMainComponent()));
		}

		summary.get(test.getMainComponent()).updateOnSuccess(test.isResult());
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
				.append("</td><td>");

		if(m.isResult()) {
			content.append("<span class=\"glyphicon glyphicon-ok\" style=\"color:green\" aria-hidden=\"true\"></span> ");
		} else {
			content.append("<span class=\"glyphicon glyphicon-remove\" style=\"color:red\" aria-hidden=\"true\"></span> ");
		}

		content.append(m.getMainComponent())
				.append("</td><td>")
				.append(m.getStartDateWithFormat())
				.append("</td><td>")
				.append(m.getEndDateWithFormat())
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
