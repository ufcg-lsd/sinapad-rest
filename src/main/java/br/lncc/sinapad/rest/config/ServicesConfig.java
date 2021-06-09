package br.lncc.sinapad.rest.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import br.lncc.sinapad.rest.utils.RESTUtils;

@XmlRootElement(name = "services")
public class ServicesConfig implements Serializable {

	private static Logger logger = Logger.getLogger(ServicesConfig.class);

	public ServicesConfig(String xml) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ServicesConfig.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			InputStream is = null;
			File file = new File(xml);
			if (file.exists()) {
				is = new FileInputStream(file);
			} else {
				is = getClass().getResourceAsStream(xml);
			}
			ServicesConfig config = (ServicesConfig) jaxbUnmarshaller.unmarshal(is);

			this.setServices(config.getServices());
		} catch (JAXBException e) {
			RESTUtils.handleException(e, logger);
		} catch (FileNotFoundException e) {
			RESTUtils.handleException(e, logger);
		}
	}

	public ServicesConfig() {

	}

	@XmlElement(name = "service", type = Service.class)
	private List<Service> services;

	public List<Service> getServices() {
		return services;
	}

	@XmlTransient
	public void setServices(List<Service> services) {
		this.services = services;
	}

	public static class Service {
		private String id;
		private String configurationService;
		private String applicationService;
		private String applicationConverterService;
		private String authenticationService;
		private String fileService;
		private String jobMonitoringService;
		private String resourceMonitoringService;
		private String jobSubmissionService;

		public String getId() {
			return id;
		}

		@XmlElement(name = "id")
		public void setId(String id) {
			this.id = id;
		}

		public String getConfigurationService() {
			return configurationService;
		}

		@XmlElement(name = "configuration-service")
		public void setConfigurationService(String configurationService) {
			this.configurationService = configurationService;
		}

		public String getApplicationService() {
			return applicationService;
		}

		@XmlElement(name = "application-service")
		public void setApplicationService(String applicationService) {
			this.applicationService = applicationService;
		}

		public String getApplicationConverterService() {
			return applicationConverterService;
		}

		@XmlElement(name = "application-converter-service")
		public void setApplicationConverterService(String applicationConverterService) {
			this.applicationConverterService = applicationConverterService;
		}

		public String getAuthenticationService() {
			return authenticationService;
		}

		@XmlElement(name = "authentication-service")
		public void setAuthenticationService(String authenticationService) {
			this.authenticationService = authenticationService;
		}

		public String getFileService() {
			return fileService;
		}

		@XmlElement(name = "file-service")
		public void setFileService(String fileService) {
			this.fileService = fileService;
		}

		public String getJobMonitoringService() {
			return jobMonitoringService;
		}

		@XmlElement(name = "job-monitoring-service")
		public void setJobMonitoringService(String jobMonitoringService) {
			this.jobMonitoringService = jobMonitoringService;
		}

		public String getResourceMonitoringService() {
			return resourceMonitoringService;
		}

		@XmlElement(name = "resource-monitoring-service")
		public void setResourceMonitoringService(String resourceMonitoringService) {
			this.resourceMonitoringService = resourceMonitoringService;
		}

		public String getJobSubmissionService() {
			return jobSubmissionService;
		}

		@XmlElement(name = "job-submission-service")
		public void setJobSubmissionService(String jobSubmissionService) {
			this.jobSubmissionService = jobSubmissionService;
		}

	}

}
