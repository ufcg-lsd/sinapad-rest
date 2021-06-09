package br.lncc.sinapad.rest.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import br.lncc.sinapad.core.data.FileData;
import br.lncc.sinapad.core.exception.NotAuthorizedException;
import br.lncc.sinapad.core.exception.ServiceException;
import br.lncc.sinapad.core.exception.UnexpectedException;
import br.lncc.sinapad.core.service.file.FileService;
import br.lncc.sinapad.rest.RESTOperation;
import br.lncc.sinapad.rest.data.FileResult;
import br.lncc.sinapad.rest.data.Result;
import br.lncc.sinapad.rest.exception.ServiceNotFoundException;
import br.lncc.sinapad.rest.utils.RESTResultCodes;
import br.lncc.sinapad.rest.utils.RESTUtils;

@Path("/file")
public class FileRESTOperation extends RESTOperation {

	private static Logger logger = Logger.getLogger("file");

	/**
	 * 
	 * Finds a file representation
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param parents
	 *          (optional) The parents directory. Null if there is no parents.
	 *          Parents are separated using the '/' character.
	 * @param file
	 *          (optional) The file name. Null if there is not file name.
	 * @return A result containing the file information and the code of the
	 *         result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if the uuid or the project is
	 *         null; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid;
	 *         <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if the file was found; <br>
	 *         - {@link RESTResultCodes#FILE_DOES_NOT_EXIST} if the file does not
	 *         exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/find")
	public FileResult find(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("parents") String parents, @FormParam("file") String file, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			FileService fileService = RESTOperation.getFileService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty())) {
					return new FileResult(RESTResultCodes.USER_ERROR);
				}

				// Checking if the project is valid
				if (!fileService.exists(uuid, project, null, null)) {
					return new FileResult(RESTResultCodes.PROJECT_NOT_FOUND);
				}
			}

			if (parents != null && parents.isEmpty()) {
				parents = null;
			}

			if (file != null && file.isEmpty()) {
				file = null;
			}

			// Calling the method
			FileData data = fileService.find(uuid, project, RESTUtils.convertAbsolutePathToArray(parents, false, false), file);
			if (data != null) {
				FileResult result = RESTUtils.convertFileDataToFileResult(data);
				result.setCode(RESTResultCodes.OK);
				return result;
			} else {
				return new FileResult(RESTResultCodes.FILE_DOES_NOT_EXIST);
			}
		} catch (NotAuthorizedException e) {
			return new FileResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new FileResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return new FileResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Checks if a file exists.
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param parents
	 *          (optional) The parents directory. Null if there is no parents.
	 *          Parents are separated using the '/' character.
	 * @param file
	 *          (optional) The file name. Null if there is not file name.
	 * @return A result containing the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if the uuid or the project is
	 *         null; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid;
	 *         <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if the file exists; <br>
	 *         - {@link RESTResultCodes#FILE_DOES_NOT_EXIST} if the file does not
	 *         exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/exists")
	public Result exists(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("parents") String parents, @FormParam("file") String file, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			FileService fileService = RESTOperation.getFileService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty())) {
					return new Result(RESTResultCodes.USER_ERROR);
				}

				// Checking if the project is valid
				if (!fileService.exists(uuid, project, null, null)) {
					return new Result(RESTResultCodes.PROJECT_NOT_FOUND);
				}
			}

			if (parents != null && parents.isEmpty()) {
				parents = null;
			}

			if (file != null && file.isEmpty()) {
				file = null;
			}

			// Calling the method
			if (fileService.exists(uuid, project, RESTUtils.convertAbsolutePathToArray(parents, false, false), file)) {
				return new Result(RESTResultCodes.OK);
			} else {
				return new Result(RESTResultCodes.FILE_DOES_NOT_EXIST);
			}
		} catch (NotAuthorizedException e) {
			return new Result(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new Result(RESTResultCodes.SERVICE_NOT_FOUND);
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
	 * Deletes a file or directory.
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param parents
	 *          (optional) The parents directory. Null if there is no parents.
	 *          Parents are separated using the '/' character.
	 * @param file
	 *          The file name.
	 * @return A result containing the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required parameter is
	 *         null; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid;
	 *         <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if the file was successfully deleted;
	 *         <br>
	 *         - {@link RESTResultCodes#FILE_DOES_NOT_EXIST} if the file does not
	 *         exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/delete")
	public Result delete(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("parents") String parents, @FormParam("file") String file, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			FileService fileService = RESTOperation.getFileService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (file == null || file.isEmpty())) {
					return new Result(RESTResultCodes.USER_ERROR);
				}

				// Checking if the project is valid
				if (!fileService.exists(uuid, project, null, null)) {
					return new Result(RESTResultCodes.PROJECT_NOT_FOUND);
				}
			}

			if (parents != null && parents.isEmpty()) {
				parents = null;
			}

			// If the file/directory exists, call the method "delete"
			if (fileService.exists(uuid, project, RESTUtils.convertAbsolutePathToArray(parents, false, false), file)) {
				fileService.delete(uuid, project, RESTUtils.convertAbsolutePathToArray(parents, false, false), file);

				return new Result(RESTResultCodes.OK);
			} else {
				return new Result(RESTResultCodes.FILE_DOES_NOT_EXIST);
			}
		} catch (NotAuthorizedException e) {
			return new Result(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new Result(RESTResultCodes.SERVICE_NOT_FOUND);
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
	 * Creates a new file or directory.
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param parents
	 *          (optional) The parents directory. Null if there is no parents.
	 *          Parents are separated using the '/' character.
	 * @param file
	 *          The file name.
	 * @param directory
	 *          True if it is a directory or false if it is a file.
	 * @return A result containing the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required parameter is
	 *         null; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid;
	 *         <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if the file was successfully created;
	 *         <br>
	 *         - {@link RESTResultCodes#FILE_ALREADY_EXISTS} if the file already
	 *         exists; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/create")
	public Result create(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("parents") String parents, @FormParam("file") String file, @FormParam("directory") String directory, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			FileService fileService = RESTOperation.getFileService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (file == null || file.isEmpty()) || (directory == null || directory.isEmpty())) {
					return new Result(RESTResultCodes.USER_ERROR);
				}

				// Checking if the project is valid
				if (!fileService.exists(uuid, project, null, null)) {
					return new Result(RESTResultCodes.PROJECT_NOT_FOUND);
				}
			}

			if (parents != null && parents.isEmpty()) {
				parents = null;
			}

			// Checking if the file/directory already exists
			if (validate) {
				if (fileService.exists(uuid, project, RESTUtils.convertAbsolutePathToArray(parents, false, false), file)) {
					return new Result(RESTResultCodes.FILE_ALREADY_EXISTS);
				}
			}
			// Calling the method
			boolean isDirectory = Boolean.parseBoolean(directory);
			if (fileService.create(uuid, project, RESTUtils.convertAbsolutePathToArray(parents, false, false), file, isDirectory)) {
				return new Result(RESTResultCodes.OK);
			}
		} catch (NotAuthorizedException e) {
			return new Result(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new Result(RESTResultCodes.SERVICE_NOT_FOUND);
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
	 * Moves a file.
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param parentsFrom
	 *          (optional) The parents directory where the file is. Null if there
	 *          is no parents. Parents are separated using the '/' character.
	 * @param fileName
	 *          The name of the file.
	 * @param parentsTo
	 *          (optional) The parents directory where the file will be placed.
	 *          Null if there is no parents. Parents are separated using the '/'
	 *          character.
	 * @return A result containing the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required parameter is
	 *         null; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid;
	 *         <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if the file was successfully moved;
	 *         <br>
	 *         - {@link RESTResultCodes#FILE_DOES_NOT_EXIST} if the file does not
	 *         exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/move")
	public Result move(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("parentsTo") String parentsTo, @FormParam("parentsFrom") String parentsFrom, @FormParam("fileName") String fileName, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			FileService fileService = RESTOperation.getFileService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (fileName == null || fileName.isEmpty())) {
					return new Result(RESTResultCodes.USER_ERROR);
				}

				// Checking if the project is valid
				if (!fileService.exists(uuid, project, null, null)) {
					return new Result(RESTResultCodes.PROJECT_NOT_FOUND);
				}
			}

			if (parentsTo != null && parentsTo.isEmpty()) {
				parentsTo = null;
			}

			if (parentsFrom != null && parentsFrom.isEmpty()) {
				parentsFrom = null;
			}

			// If the file/directory exists, call the method "move"
			if (!validate || fileService.exists(uuid, project, RESTUtils.convertAbsolutePathToArray(parentsFrom, false, false), fileName)) {
				fileService.move(uuid, project, RESTUtils.convertAbsolutePathToArray(parentsTo, false, false), RESTUtils.convertAbsolutePathToArray(parentsFrom, false, false), fileName);

				return new Result(RESTResultCodes.OK);
			} else {
				return new Result(RESTResultCodes.FILE_DOES_NOT_EXIST);
			}
		} catch (NotAuthorizedException e) {
			return new Result(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new Result(RESTResultCodes.SERVICE_NOT_FOUND);
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
	 * Copies a file.
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param source
	 *          The source of the file.
	 * @param destination
	 *          The destination of the copy.
	 * @return A result containing the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if the uuid or the project is
	 *         null; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid;
	 *         <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if the file was successfully copied;
	 *         <br>
	 *         - {@link RESTResultCodes#FILE_DOES_NOT_EXIST} if the file does not
	 *         exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/copy")
	public Result copy(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("source") String source, @FormParam("destination") String destination, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			FileService fileService = RESTOperation.getFileService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty())) {
					return new Result(RESTResultCodes.USER_ERROR);
				}

				// Checking if the project is valid.
				if (!fileService.exists(uuid, project, null, null)) {
					return new Result(RESTResultCodes.PROJECT_NOT_FOUND);
				}
			}

			// Converting Strings
			String[] sourceParents = RESTUtils.convertAbsolutePathToArray(source, false, true);
			String fileName = RESTUtils.retrieveFileNameFromAbsolutePath(source);
			String[] destinationParents = RESTUtils.convertAbsolutePathToArray(destination, false, true);

			// Calling the method
			if (!validate || fileService.exists(uuid, project, sourceParents, fileName)) {
				fileService.copy(uuid, project, destinationParents, sourceParents, fileName);
				return new Result(RESTResultCodes.OK);
			}
			return new Result(RESTResultCodes.FILE_DOES_NOT_EXIST);
		} catch (NotAuthorizedException e) {
			return new Result(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new Result(RESTResultCodes.SERVICE_NOT_FOUND);
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
	 * Renames a file
	 * 
	 * @param uuid
	 *          The universally unique identifier of the user
	 * @param service
	 *          The service name. The service name.
	 * @param project
	 *          A project owned by the user
	 * @param parents
	 *          (optional) The parents directory. Null if there is no parents
	 * @param oldName
	 *          The old name of the file
	 * @param newName
	 *          The new name of the file
	 * @return A result containing the file information and the code of the
	 *         result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some require parameter is
	 *         null; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid;
	 *         <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if the file was found; <br>
	 *         - {@link RESTResultCodes#FILE_DOES_NOT_EXIST} if the file does not
	 *         exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/rename")
	public Result rename(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("parents") String parents, @FormParam("oldName") String oldName, @FormParam("newName") String newName, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			FileService fileService = RESTOperation.getFileService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (oldName == null || oldName.isEmpty()) || (newName == null || newName.isEmpty())) {
					return new FileResult(RESTResultCodes.USER_ERROR);
				}

				// Checking if the project is valid
				if (!fileService.exists(uuid, project, null, null)) {
					return new FileResult(RESTResultCodes.PROJECT_NOT_FOUND);
				}

				if (!fileService.exists(uuid, project, RESTUtils.convertAbsolutePathToArray(parents, false, false), oldName)) {
					return new FileResult(RESTResultCodes.FILE_DOES_NOT_EXIST);
				}
			}

			if (parents != null && parents.isEmpty()) {
				parents = null;
			}

			// Calling the method
			if (fileService.rename(uuid, project, RESTUtils.convertAbsolutePathToArray(parents, false, false), oldName, newName)) {
				return new Result(RESTResultCodes.OK);
			}
		} catch (NotAuthorizedException e) {
			return new Result(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new Result(RESTResultCodes.SERVICE_NOT_FOUND);
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
	 * Uploads a file.
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param parents
	 *          (optional) The parents directory. Null if there is no parents.
	 *          Parents are separated using the '/' character.
	 * @param file
	 *          The file.
	 * @param content
	 *          The file content.
	 * @param override
	 *          True if the file already exists and want to override.
	 * @return A result containing the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required parameter is
	 *         null; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid;
	 *         <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if the file was uploaded; <br>
	 *         - {@link RESTResultCodes#FILE_ALREADY_EXISTS} if the file already
	 *         exists and 'override' is false; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/upload")
	public Result upload(@FormDataParam("service") String service, @FormDataParam("uuid") String uuid, @FormDataParam("project") String project, @FormDataParam("parents") String parents, @FormDataParam("file") InputStream file, @FormDataParam("file") FormDataContentDisposition content, @FormDataParam("override") String override, @FormDataParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			FileService fileService = RESTOperation.getFileService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (file == null) || (content == null) || (override == null || override.isEmpty())) {
					return new Result(RESTResultCodes.USER_ERROR);
				}

				// Checking if the project is valid
				if (!fileService.exists(uuid, project, null, null)) {
					return new Result(RESTResultCodes.PROJECT_NOT_FOUND);
				}
			}

			if (parents != null && parents.isEmpty()) {
				parents = null;
			}

			// Getting the name of the file
			String fileName = content.getFileName();
			fileName = RESTUtils.retrieveFileNameFromAbsolutePath(fileName);

			boolean result = fileService.upload(uuid, project, RESTUtils.convertAbsolutePathToArray(parents, false, false), fileName, file, Boolean.parseBoolean(override));
			if (result) {
				return new Result(RESTResultCodes.OK);
			}
			return new Result(RESTResultCodes.FILE_ALREADY_EXISTS);
		} catch (NotAuthorizedException e) {
			return new Result(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new Result(RESTResultCodes.SERVICE_NOT_FOUND);
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
	 * Downloads a file.
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param parents
	 *          (optional) The parents directory. Null if there is no parents.
	 *          Parents are separated using the '/' character.
	 * @param file
	 *          The file name.
	 * @return A streaming for download the file and a result containing the code
	 *         of the result. <br>
	 *         - {@link RESTResultCodes#OK} if the file download was successful;
	 *         <br>
	 *         -{@link RESTResultCodes#NO_CONTENT} if the file was not found; <br>
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/download")
	public Response download(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("parents") String parents, @FormParam("file") String file, @FormParam("skipValidation") String skipValidation) {
		InputStream is = null;
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			final FileService fileService = RESTOperation.getFileService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (file == null || file.isEmpty())) {
					return Response.noContent().build();
				}
			}

			if (parents != null && parents.isEmpty()) {
				parents = null;
			}

			String[] parentsArray = RESTUtils.convertAbsolutePathToArray(parents, false, false);
			if (validate) {
				if (!fileService.exists(uuid, project, parentsArray, file)) {
					return Response.noContent().build();
				}
			}

			if (validate) {
				final FileData fileData = fileService.find(uuid, project, parentsArray, file);
				if (fileData.isDirectory()) {
					// create final variables for the thread access
					final String finalUuid = uuid;
					final String finalproject = project;
					is = new PipedInputStream();
					final PipedOutputStream os = new PipedOutputStream((PipedInputStream) is);
					new Thread(new Runnable() {
						private void zip(ZipOutputStream zos, FileData directory) {
							byte[] buffer = new byte[1024000];
							for (FileData file : directory.getChildren()) {
								String absolutePath = file.getAbsolutePath();
								String[] parentsArr = RESTUtils.convertAbsolutePathToArray(absolutePath, true, true);
								String fileName = RESTUtils.retrieveFileNameFromAbsolutePath(absolutePath);
								try {
									FileData child = fileService.find(finalUuid, finalproject, parentsArr, fileName);
									String parentsPath = RESTUtils.convertArrayToAbsolutePath(parentsArr);
									// check if parentsPath is empty before adding a separator
									parentsPath = !parentsPath.isEmpty() ? parentsPath + "/" : parentsPath;
									if (child.isDirectory()) {
										ZipEntry ze = new ZipEntry(parentsPath + fileName + "/");
										zos.putNextEntry(ze);
										zip(zos, child);
										zos.closeEntry();
									} else {
										InputStream is = fileService.download(finalUuid, finalproject, parentsArr, fileName);
										ZipEntry ze = new ZipEntry(parentsPath + fileName);
										zos.putNextEntry(ze);
										int len;
										while ((len = is.read(buffer)) > 0) {
											zos.write(buffer, 0, len);
										}
										is.close();
										zos.closeEntry();
									}
								} catch (Exception e) {
									// do nothing
								}
							}
						}

						public void run() {
							try {
								ZipOutputStream zos = new ZipOutputStream(os);
								zip(zos, fileData);
								zos.close();
							} catch (Exception e) {
								// do nothing
							} finally {
								try {
									os.close();
								} catch (IOException e) {
									// do nothing
								}
							}
						}
					}).start();
					file = file + ".zip";
				} else {
					is = fileService.download(uuid, project, parentsArray, file);
				}
			} else {
				is = fileService.download(uuid, project, parentsArray, file);
			}
		} catch (ServiceNotFoundException e) {
			return Response.noContent().build();
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		if (is == null) {
			return Response.noContent().build();
		}
		return Response.ok(is, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = " + file).build();
	}
}
