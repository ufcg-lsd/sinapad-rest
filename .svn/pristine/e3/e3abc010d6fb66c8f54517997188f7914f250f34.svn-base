package br.lncc.sinapad.rest.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlRootElement
public class JobSubmissonResult extends Result {

	/**
	 * The id of the job.
	 */
	private String jobId;
	private List<String> missingArgs;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
	@XmlElementWrapper
	@XmlElement(name = "arg")
	public List<String> getMissingArgs() {
		return missingArgs;
	}

	public void setMissingArgs(List<String> missingArgs) {
		this.missingArgs = missingArgs;
	}

	public JobSubmissonResult() {
		this(RESTResultCodes.OK);
	}

	public JobSubmissonResult(int code) {
		super(code);
	}

}
