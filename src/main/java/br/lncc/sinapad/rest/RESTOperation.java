package br.lncc.sinapad.rest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import br.lncc.sinapad.core.service.application.ApplicationService;
import br.lncc.sinapad.core.service.application.converter.ApplicationConverterService;
import br.lncc.sinapad.core.service.authentication.AuthenticationService;
import br.lncc.sinapad.core.service.configuration.ConfigurationService;
import br.lncc.sinapad.core.service.file.FileService;
import br.lncc.sinapad.core.service.monitoring.JobMonitoringService;
import br.lncc.sinapad.core.service.monitoring.ResourceMonitoringService;
import br.lncc.sinapad.core.service.submission.JobSubmissionService;
import br.lncc.sinapad.rest.config.ServicesConfig;
import br.lncc.sinapad.rest.config.ServicesConfig.Service;
import br.lncc.sinapad.rest.exception.ServiceNotFoundException;
import br.lncc.sinapad.rest.wrapper.MultipleServiceWrapper;

public abstract class RESTOperation {
	protected static Map<String, MultipleServiceWrapper> services = new HashMap<String, MultipleServiceWrapper>();

	private static long lasInit = 0;
	// one minute for trying to init the services
	private static long initInterval = 60000;
	
	
	public synchronized static void initServices() {
		InputStream is = RESTOperation.class.getResourceAsStream("/rest.properties");
		try {
			long current = System.currentTimeMillis();
			if ((lasInit + initInterval) > current) {
				return;
			}
			lasInit = current;
			
			Properties properties = new Properties();
			properties.load(is);

			ServicesConfig servicesConfig = new ServicesConfig("/services.xml");
			List<Service> impls = servicesConfig.getServices();
			for (Service impl : impls) {
				String id = impl.getId();
				MultipleServiceWrapper wrapper = new MultipleServiceWrapper();

				ConfigurationService configurationService = (ConfigurationService) Class.forName(impl.getConfigurationService()).newInstance();
				configurationService.configure(properties);
				wrapper.setConfigurationService(configurationService);

				ApplicationService applicationService = (ApplicationService) Class.forName(impl.getApplicationService()).newInstance();
				wrapper.setApplicationService(applicationService);
				
				ApplicationConverterService applicationConverterService = (ApplicationConverterService) Class.forName(impl.getApplicationConverterService()).newInstance();
				wrapper.setApplicationConverterService(applicationConverterService);

				FileService fileService = (FileService) Class.forName(impl.getFileService()).newInstance();
				wrapper.setFileService(fileService);

				JobMonitoringService jobMonitoringService = (JobMonitoringService) Class.forName(impl.getJobMonitoringService()).newInstance();
				wrapper.setJobMonitoringService(jobMonitoringService);

				ResourceMonitoringService resourceMonitoringService = (ResourceMonitoringService) Class.forName(impl.getResourceMonitoringService()).newInstance();
				wrapper.setResourceMonitoringService(resourceMonitoringService);

				JobSubmissionService jobSubmissionService = (JobSubmissionService) Class.forName(impl.getJobSubmissionService()).newInstance();
				wrapper.setJobSubmissionService(jobSubmissionService);

				AuthenticationService authenticationService = (AuthenticationService) Class.forName(impl.getAuthenticationService()).newInstance();
				wrapper.setAuthenticationService(authenticationService);

				services.put(id, wrapper);
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not configure the services", e);
		}
	}

	static {
		initServices();
	}

	protected static MultipleServiceWrapper getServiceWrapper(String serviceName) {
		return services.get(serviceName.replaceAll("^[\"']+|[\"']+$", ""));
	}

	public static Set<String> getServices() {
		return services.keySet();
	}

	public static AuthenticationService getAuthenticationService(String service) throws ServiceNotFoundException {
		MultipleServiceWrapper wrapper = getServiceWrapper(service);
		if (wrapper != null) {
			return wrapper.getAuthenticationService();
		}
		throw new ServiceNotFoundException("Could not find a authentication service for the identification [" + service + "]");
	}

	public static ApplicationService getApplicationService(String service) throws ServiceNotFoundException {
		MultipleServiceWrapper wrapper = getServiceWrapper(service);
		if (wrapper != null) {
			return wrapper.getApplicationService();
		}
		throw new ServiceNotFoundException("Could not find a application service for the identification [" + service + "]");
	}
	
	public static ApplicationConverterService getApplicationConverterService(String service) throws ServiceNotFoundException {
		MultipleServiceWrapper wrapper = getServiceWrapper(service);
		if (wrapper != null) {
			return wrapper.getApplicationConverterService();
		}
		throw new ServiceNotFoundException("Could not find a application converter service for the identification [" + service + "]");
	}

	public static FileService getFileService(String service) throws ServiceNotFoundException {
		MultipleServiceWrapper wrapper = getServiceWrapper(service);
		if (wrapper != null) {
			return wrapper.getFileService();
		}
		throw new ServiceNotFoundException("Could not find a file service for the identification [" + service + "]");
	}

	public static JobMonitoringService getJobMonitoringService(String service) throws ServiceNotFoundException {
		MultipleServiceWrapper wrapper = getServiceWrapper(service);
		if (wrapper != null) {
			return wrapper.getJobMonitoringService();
		}
		throw new ServiceNotFoundException("Could not find a job monitoring service for the identification [" + service + "]");
	}

	public static ResourceMonitoringService getResourceMonitoringService(String service) throws ServiceNotFoundException {
		MultipleServiceWrapper wrapper = getServiceWrapper(service);
		if (wrapper != null) {
			return wrapper.getResourceMonitoringService();
		}
		throw new ServiceNotFoundException("Could not find a resource monitoring service for the identification [" + service + "]");
	}

	public static JobSubmissionService getJobSubmissionService(String service) throws ServiceNotFoundException {
		MultipleServiceWrapper wrapper = getServiceWrapper(service);
		if (wrapper != null) {
			return wrapper.getJobSubmissionService();
		}
		throw new ServiceNotFoundException("Could not find a job submission service for the identification [" + service + "]");
	}
}
