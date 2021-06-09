package br.lncc.sinapad.rest.monitoring;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import br.lncc.sinapad.core.data.ResourceData;
import br.lncc.sinapad.core.exception.NotAuthorizedException;
import br.lncc.sinapad.core.exception.ServiceException;
import br.lncc.sinapad.core.exception.UnexpectedException;
import br.lncc.sinapad.core.service.monitoring.ResourceMonitoringService;
import br.lncc.sinapad.rest.RESTOperation;
import br.lncc.sinapad.rest.data.ListResourceResult;
import br.lncc.sinapad.rest.data.ResourceResult;
import br.lncc.sinapad.rest.data.StatusResult;
import br.lncc.sinapad.rest.exception.ServiceNotFoundException;
import br.lncc.sinapad.rest.utils.RESTResultCodes;
import br.lncc.sinapad.rest.utils.RESTUtils;

@Path("/resource-monitoring")
public class ResourceMonitoringRESTOperation extends RESTOperation {

	private static Logger logger = Logger.getLogger("resource-monitoring");

	/**
	 * 
	 * List all the resources available for the user.
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @return A result containing the list of the resources available for the
	 *         user and the code of the result. <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if the uuid is null; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list")
	public ListResourceResult list(@FormParam("service") String service, @FormParam("uuid") String uuid) {
		try {
			ResourceMonitoringService resourceMonitoringService = RESTOperation.getResourceMonitoringService(service);
			if (uuid == null || uuid.isEmpty()) {
				return new ListResourceResult(RESTResultCodes.USER_ERROR);
			}

			List<ResourceData> resources;
			resources = resourceMonitoringService.list(uuid);

			List<ResourceResult> result = RESTUtils.convertResourceDataListToResourceResultList(resources);
			ListResourceResult listResult = new ListResourceResult(RESTResultCodes.OK);
			listResult.setElements(result);
			return listResult;
		} catch (NotAuthorizedException e) {
			return new ListResourceResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new ListResourceResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return new ListResourceResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Gets a resource information
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param resource
	 *          The name of the resource.
	 * @return A result containing the resource information and the code of the
	 *         result. <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#USER_ERROR if the resource or the uuid is
	 *         null; <br> - {@link RESTResultCodes#FILE_DOES_NOT_EXIST if the resource
	 *         does not exist; <br> - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if
	 *         some error occurs.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/get")
	public ResourceResult get(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("resource") String resource) {
		try {
			ResourceMonitoringService resourceMonitoringService = RESTOperation.getResourceMonitoringService(service);
			if ((uuid == null || uuid.isEmpty()) || (resource == null || resource.isEmpty())) {
				return new ResourceResult(RESTResultCodes.USER_ERROR);
			}

			ResourceData resourceData = resourceMonitoringService.get(uuid, resource);
			if (resourceData != null) {
				ResourceResult result = RESTUtils.convertResourceDataToResourceResult(resourceData);
				result.setCode(RESTResultCodes.OK);
				return result;
			}
			return new ResourceResult(RESTResultCodes.FILE_DOES_NOT_EXIST);
		} catch (NotAuthorizedException e) {
			return new ResourceResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new ResourceResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return new ResourceResult(RESTResultCodes.INTERNAL_SERVER_ERROR);

	}

}
