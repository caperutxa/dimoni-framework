package caperutxa.dimoni.framework.model;

/**
 *
 */
public class TestSummary {

	public TestSummary(String type) {
		this.type = type;
	}

	int totalTest;
	int failed;
	int success;
	String type;

	public void addTotal(int i) {
		totalTest += i;
	}

	public void addSuccess(int i) {
		success += i;
	}

	public void addFailed(int i) {
		failed += i;
	}

	public void updateOnSuccess(boolean success) {
		addTotal(1);
		if(success) {
			addSuccess(1);
		} else {
			addFailed(1);
		}
	}

	public double getSuccessPercentage() {
		return 100 * ((double)success / (double)totalTest);
	}

	public double getFailedPercentage() {
		return 100 * ((double)failed / (double)totalTest);
	}

	/* Getters and setters */

	public int getTotalTest() {
		return totalTest;
	}

	public void setTotalTest(int totalTest) {
		this.totalTest = totalTest;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
