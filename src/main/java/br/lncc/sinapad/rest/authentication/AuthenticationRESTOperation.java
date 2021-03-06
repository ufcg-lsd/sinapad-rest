package br.lncc.sinapad.rest.authentication;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import br.lncc.sinapad.core.data.UserData;
import br.lncc.sinapad.core.exception.NotAuthorizedException;
import br.lncc.sinapad.core.exception.ServiceException;
import br.lncc.sinapad.core.exception.UnexpectedException;
import br.lncc.sinapad.core.service.authentication.AuthenticationService;
import br.lncc.sinapad.core.service.authentication.AuthenticationService.Domain;
import br.lncc.sinapad.rest.RESTOperation;
import br.lncc.sinapad.rest.data.AuthenticationResult;
import br.lncc.sinapad.rest.data.Result;
import br.lncc.sinapad.rest.data.UserResult;
import br.lncc.sinapad.rest.exception.ServiceNotFoundException;
import br.lncc.sinapad.rest.utils.RESTResultCodes;
import br.lncc.sinapad.rest.utils.RESTUtils;

@Path("/authentication")
public class AuthenticationRESTOperation extends RESTOperation {

	private static Logger logger = Logger.getLogger(AuthenticationRESTOperation.class);
    
	/**
	 * 
	 * Authenticate an use given an userName and password
	 * 
	 * @param username
	 *          The name of the user;
	 * @param password
	 *          The password of the user
	 * @param service
	 *          The service name.
	 * @return A result containing the uuid of the authenticated user and the code
	 *         of the result. <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if username or password is
	 *         null; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/login-ldap")
	public AuthenticationResult loginLDAP(@FormParam("service") String service, @FormParam("username") String username, @FormParam("password") String password) {
		
		logger.debug("loginLDAP (service" + service + ") for user: " + username + " ....");
		String messageFailedResult = "loginLDAP result: failed!";
		String messageSuccessResult = "loginLDAP result: success!";
		
		try {
			AuthenticationService authenticationService = RESTOperation.getAuthenticationService(service);
			if (username == null || username.isEmpty()) {
				return new AuthenticationResult(RESTResultCodes.USER_ERROR);
			}

			if (password == null || password.isEmpty()) {
				return new AuthenticationResult(RESTResultCodes.USER_ERROR);
			}

			String uuid = authenticationService.login(username, password, Domain.LDAP);
			if (uuid != null) {
				AuthenticationResult result = new AuthenticationResult();
				result.setUuid(uuid);
				result.setCode(RESTResultCodes.OK);
				logger.debug(messageSuccessResult);
				return result;
			} else {
				logger.debug(messageFailedResult);
				return new AuthenticationResult(RESTResultCodes.NOT_AUTHORIZED);
			}
		} catch (ServiceNotFoundException e) {
			logger.debug(messageFailedResult);
			return new AuthenticationResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (NotAuthorizedException e) {
			logger.debug(messageFailedResult);
			return new AuthenticationResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceException e) {
			logger.debug(messageFailedResult);
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			logger.debug(messageFailedResult);
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			logger.debug(messageFailedResult);
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		logger.debug(messageSuccessResult);
		return new AuthenticationResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Authenticate an user given an userName and a certification
	 * 
	 * @param username
	 *          A valid userName
	 * @param file
	 *          A stream containing the certificate data
	 * @param content
	 *          The file content.
	 * @param service
	 *          The service name.
	 * @return A result containing the uuid of the authenticated user and the code
	 *         of the result. <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if username or password is
	 *         null; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/login-rsa")
	public AuthenticationResult loginRSA(@FormDataParam("service") String service, @FormDataParam("username") String username, @FormDataParam("file") InputStream file, @FormDataParam("file") FormDataContentDisposition content) {
		
		logger.debug("loginRSA (service: " + service + ") for user: " + username);
		String messageFailedResult = "loginRSA result: failed!";
		String messageSuccessResult = "loginRSA result: success!";
		
		try {
			AuthenticationService authenticationService = RESTOperation.getAuthenticationService(service);
			if (username == null || username.isEmpty()) {
				return new AuthenticationResult(RESTResultCodes.USER_ERROR);
			}

			String uuid = authenticationService.login(username, file, Domain.RSA);
			if (uuid != null) {
				AuthenticationResult result = new AuthenticationResult();
				result.setUuid(uuid);
				result.setCode(RESTResultCodes.OK);
				logger.debug(messageSuccessResult);
				return result;
			} else {
				logger.debug(messageFailedResult);
				return new AuthenticationResult(RESTResultCodes.NOT_AUTHORIZED);
			}
		} catch (NotAuthorizedException e) {
			logger.debug(messageFailedResult);
			return new AuthenticationResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			logger.debug(messageFailedResult);
			return new AuthenticationResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (ServiceException e) {
			logger.debug(messageFailedResult);
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			logger.debug(messageFailedResult);
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			logger.debug(messageFailedResult);
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		logger.debug(messageSuccessResult);
		return new AuthenticationResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Authenticate an user given a certification
	 * 
	 * @param file
	 *          A stream containing the certificate data
	 * @param content
	 *          The file content.
	 * @param service
	 *          The service name.
	 * @return A result containing the uuid of the authenticated user and the code
	 *         of the result. <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if username or password is
	 *         null; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/login-voms")
	public AuthenticationResult loginVOMS(@FormDataParam("service") String service, @FormDataParam("file") InputStream file, @FormDataParam("file") FormDataContentDisposition content) {
		try {
			AuthenticationService authenticationService = RESTOperation.getAuthenticationService(service);
			String uuid = authenticationService.login(null, file, Domain.VOMS);
			if (uuid != null) {
				AuthenticationResult result = new AuthenticationResult();
				result.setUuid(uuid);
				result.setCode(RESTResultCodes.OK);
				return result;
			} else {
				return new AuthenticationResult(RESTResultCodes.NOT_AUTHORIZED);
			}
		} catch (ServiceNotFoundException e) {
			return new AuthenticationResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (NotAuthorizedException e) {
			return new AuthenticationResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return new AuthenticationResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Logout an user
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @return A result containing the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if the uuid is null; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not logged; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the
	 *         service is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/logout")
	public Result logout(@FormParam("service") String service, @FormParam("uuid") String uuid) {
		try {
			AuthenticationService authenticationService = RESTOperation.getAuthenticationService(service);
			if (uuid != null) {
				authenticationService.logout(uuid);
				return new Result(RESTResultCodes.OK);
			}
			return new Result(RESTResultCodes.USER_ERROR);
		} catch (ServiceNotFoundException e) {
			return new AuthenticationResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (NotAuthorizedException e) {
			return new AuthenticationResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return new Result(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Returns some information about the user
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @return A result containing the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if the uuid is null; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not logged; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/info")
	public UserResult info(@FormParam("service") String service, @FormParam("uuid") String uuid) {
		try {
			AuthenticationService authenticationService = RESTOperation.getAuthenticationService(service);
			if (uuid != null) {
				UserData userData = authenticationService.info(uuid);
				if (userData != null) {
					UserResult result = RESTUtils.convertUserDataToUserResult(userData);
					// result.setCode(RESTResultCodes.OK);
					return result;
				}
				return new UserResult(RESTResultCodes.NOT_AUTHORIZED);
			}
			return new UserResult(RESTResultCodes.USER_ERROR);
		} catch (ServiceNotFoundException e) {
			return new UserResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (NotAuthorizedException e) {
			return new UserResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return new UserResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}
}
