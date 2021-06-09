package br.lncc.sinapad.rest.wrapper;

import br.lncc.sinapad.core.service.application.ApplicationService;
import br.lncc.sinapad.core.service.application.converter.ApplicationConverterService;
import br.lncc.sinapad.core.service.authentication.AuthenticationService;
import br.lncc.sinapad.core.service.configuration.ConfigurationService;
import br.lncc.sinapad.core.service.file.FileService;
import br.lncc.sinapad.core.service.monitoring.JobMonitoringService;
import br.lncc.sinapad.core.service.monitoring.ResourceMonitoringService;
import br.lncc.sinapad.core.service.submission.JobSubmissionService;

public class MultipleServiceWrapper {

	private ConfigurationService configurationService;
	private ApplicationService applicationService;
	private ApplicationConverterService applicationConverterService;
	private AuthenticationService authenticationService;
	private FileService fileService;
	private JobMonitoringService jobMonitoringService;
	private ResourceMonitoringService resourceMonitoringService;
	private JobSubmissionService jobSubmissionService;

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public ApplicationConverterService getApplicationConverterService() {
		return applicationConverterService;
	}

	public void setApplicationConverterService(ApplicationConverterService applicationConverterService) {
		this.applicationConverterService = applicationConverterService;
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public JobMonitoringService getJobMonitoringService() {
		return jobMonitoringService;
	}

	public void setJobMonitoringService(JobMonitoringService jobMonitoringService) {
		this.jobMonitoringService = jobMonitoringService;
	}

	public ResourceMonitoringService getResourceMonitoringService() {
		return resourceMonitoringService;
	}

	public void setResourceMonitoringService(ResourceMonitoringService resourceMonitoringService) {
		this.resourceMonitoringService = resourceMonitoringService;
	}

	public JobSubmissionService getJobSubmissionService() {
		return jobSubmissionService;
	}

	public void setJobSubmissionService(JobSubmissionService jobSubmissionService) {
		this.jobSubmissionService = jobSubmissionService;
	}
}
