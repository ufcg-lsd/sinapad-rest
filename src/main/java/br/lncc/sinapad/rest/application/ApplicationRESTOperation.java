package br.lncc.sinapad.rest.application;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import br.lncc.sinapad.core.data.ApplicationData;
import br.lncc.sinapad.core.exception.NotAuthorizedException;
import br.lncc.sinapad.core.exception.ServiceException;
import br.lncc.sinapad.core.exception.UnexpectedException;
import br.lncc.sinapad.core.service.application.ApplicationService;
import br.lncc.sinapad.rest.RESTOperation;
import br.lncc.sinapad.rest.data.ApplicationResult;
import br.lncc.sinapad.rest.data.ListApplicationResult;
import br.lncc.sinapad.rest.exception.ServiceNotFoundException;
import br.lncc.sinapad.rest.utils.RESTResultCodes;
import br.lncc.sinapad.rest.utils.RESTUtils;

@Path("/application")
public class ApplicationRESTOperation extends RESTOperation {

	private static Logger logger = Logger.getLogger(ApplicationRESTOperation.class);

	/**
	 * 
	 * List all the applications available for the user
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @return A result containing the list of the applications available for the
	 *         user and the code of the result. <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if the uuid is null; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list")
	public ListApplicationResult list(@FormParam("service") String service, @FormParam("uuid") String uuid) {
		try {
			if (uuid != null) {
				ApplicationService applicationService = RESTOperation.getApplicationService(service);
				List<ApplicationData> data = applicationService.list(uuid);
				List<ApplicationResult> result = RESTUtils.convertApplicationDataListToApplicationResultList(data);

				ListApplicationResult listResult = new ListApplicationResult(RESTResultCodes.OK);
				listResult.setElements(result);
				return listResult;
			}
			return new ListApplicationResult(RESTResultCodes.USER_ERROR);
		} catch (NotAuthorizedException e) {
			return new ListApplicationResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			return new ListApplicationResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return new ListApplicationResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Get an specific application given its name
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param application
	 *          The name of the application.
	 * @return A result containing the application and the code of the result. <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid; <br>
	 *         - {@link RESTResultCodes#OK} if the application was found; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if the application inputed is
	 *         not valid or some required parameter is null; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/get")
	public ApplicationResult get(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("application") String application) {
		try {
			ApplicationService applicationService = RESTOperation.getApplicationService(service);
			if (!((uuid == null || uuid.isEmpty()) || (application == null || application.isEmpty()))) {
				ApplicationData data = applicationService.get(uuid, application);
				if (data != null) {
					ApplicationResult result = RESTUtils.convertApplicationDataToApplicationResult(data);
					result.setCode(RESTResultCodes.OK);
					return result;
				}
			}
			return new ApplicationResult(RESTResultCodes.USER_ERROR);
		} catch (NotAuthorizedException e) {
			return new ApplicationResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			return new ApplicationResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return new ApplicationResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Get the configuration of an specific application given its name and
	 * version.
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param application
	 *          The name of the application.
	 * @param version
	 *          The version of the application.
	 * @return A streaming representing the application configuration file <br>
	 *         - {@link RESTResultCodes#OK} if the file download was successful; <br>
	 *         -{@link RESTResultCodes#NO_CONTENT} if the file was not found or
	 *         some error occur; <br>
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/config")
	public Response config(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("application") String application, @FormParam("version") String version) {
		InputStream is = null;
		try {
			ApplicationService applicationService = RESTOperation.getApplicationService(service);
			if (uuid == null) {
				return Response.noContent().build();
			}

			if (application == null || application.isEmpty()) {
				return Response.noContent().build();
			}

			if (version == null || version.isEmpty()) {
				return Response.noContent().build();
			}

			is = applicationService.config(uuid, application, version);
		} catch (NotAuthorizedException e) {
			RESTUtils.handleException(e, logger);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		if (is == null) {
			return Response.noContent().build();
		}
		return Response.ok(is, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = " + application).build();
	}

}
