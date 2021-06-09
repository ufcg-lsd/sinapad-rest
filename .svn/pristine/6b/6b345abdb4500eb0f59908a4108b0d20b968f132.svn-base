package br.lncc.sinapad.rest.monitoring;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import br.lncc.sinapad.core.data.FileData;
import br.lncc.sinapad.core.data.ResultData;
import br.lncc.sinapad.core.data.ResultData.Status;
import br.lncc.sinapad.core.exception.NotAuthorizedException;
import br.lncc.sinapad.core.exception.ServiceException;
import br.lncc.sinapad.core.exception.UnexpectedException;
import br.lncc.sinapad.core.service.file.FileService;
import br.lncc.sinapad.core.service.monitoring.JobMonitoringService;
import br.lncc.sinapad.rest.RESTOperation;
import br.lncc.sinapad.rest.data.FileResult;
import br.lncc.sinapad.rest.data.ListStatusResult;
import br.lncc.sinapad.rest.data.Result;
import br.lncc.sinapad.rest.data.StatusResult;
import br.lncc.sinapad.rest.exception.ServiceNotFoundException;
import br.lncc.sinapad.rest.utils.RESTResultCodes;
import br.lncc.sinapad.rest.utils.RESTUtils;

@Path("/job-monitoring")
public class JobMonitoringRESTOperation extends RESTOperation {

	private static Logger logger = Logger.getLogger(JobMonitoringRESTOperation.class);

	private static Comparator<StatusResult> comparator = new Comparator<StatusResult>() {
		@Override
		public int compare(StatusResult s1, StatusResult s2) {
			Date d1 = s1.getStartDate();
			Date d2 = s2.getStartDate();
			if (d1 != null && d2 != null) {
				return d1.compareTo(d2);
			}
			return 0;
		}
	};

	/**
	 * 
	 * Lists all results in a given period
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param from
	 *          (optional) The minimum start date of the job, or null for dates
	 *          from any period. The used format is 'yyyyMMdd'
	 * @param to
	 *          (optional) The maximum start date of the job, or null for dates to
	 *          any period. The used format is 'yyyyMMdd'
	 * @return A result containing the list of statuses and the code of the
	 *         result. <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if the format of one the
	 *         dates(from or to) is not null and is incorrect, or if some required
	 *         parameter is null; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */

	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list")
	public ListStatusResult list(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("from") String from, @FormParam("to") String to, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			FileService fileService = RESTOperation.getFileService(service);
			JobMonitoringService jobMonitoringService = RESTOperation.getJobMonitoringService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty())) {
					return new ListStatusResult(RESTResultCodes.USER_ERROR);
				}

				if (!fileService.exists(uuid, project, null, null)) {
					return new ListStatusResult(RESTResultCodes.PROJECT_NOT_FOUND);
				}
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			Date dateFrom = null;
			if (from == null) {
				from = "";
			}

			if (to == null) {
				to = "";
			}

			if (!from.equals("")) {
				try {
					dateFrom = formatter.parse(from);
				} catch (ParseException e) {
					return new ListStatusResult(RESTResultCodes.USER_ERROR);
				}
			}

			Date dateTo = null;
			if (!to.equals("")) {
				try {
					dateTo = formatter.parse(to);
				} catch (ParseException e) {
					return new ListStatusResult(RESTResultCodes.USER_ERROR);
				}
			}
			List<ResultData> list = jobMonitoringService.list(uuid, project, dateFrom, dateTo);
			if (list != null) {
				List<StatusResult> result = RESTUtils.convertListResultDataToListStatusResult(list);
				ListStatusResult results = new ListStatusResult(RESTResultCodes.OK);
				Collections.sort(result, comparator);
				results.setElements(result);
				return results;
			}
		} catch (NotAuthorizedException e) {
			return new ListStatusResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new ListStatusResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return new ListStatusResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Gets the job status
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param jobId
	 *          The id of the job
	 * @return The status of the job and the code of the result <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required parameter is
	 *         null; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid; <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#FILE_DOES_NOT_EXIST} if the file does not
	 *         exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/status")
	public StatusResult status(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("jobId") String jobId, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);
			FileService fileService = RESTOperation.getFileService(service);
			JobMonitoringService jobMonitoringService = RESTOperation.getJobMonitoringService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (jobId == null || jobId.isEmpty())) {
					return new StatusResult(RESTResultCodes.USER_ERROR);
				}

				if (!fileService.exists(uuid, project, null, null)) {
					return new StatusResult(RESTResultCodes.PROJECT_NOT_FOUND);
				}
			}

			Status data = jobMonitoringService.status(uuid, project, jobId);
			StatusResult result = RESTUtils.convertJobIdAndStatusToStatusResult(jobId, data);
			result.setCode(RESTResultCodes.OK);
			return result;
		} catch (NotAuthorizedException e) {
			return new StatusResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new StatusResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
			return new StatusResult(RESTResultCodes.JOB_DOES_NOT_EXIST);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return new StatusResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Gets the job result
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param jobId
	 *          The id of the job
	 * @return A result containing the status of the job and the code of the
	 *         result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required parameter is
	 *         null; <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid; <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#FILE_DOES_NOT_EXIST} if the file does not
	 *         exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/get")
	public StatusResult get(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("jobId") String jobId, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			FileService fileService = RESTOperation.getFileService(service);
			JobMonitoringService jobMonitoringService = RESTOperation.getJobMonitoringService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (jobId == null || jobId.isEmpty())) {
					return new StatusResult(RESTResultCodes.USER_ERROR);
				}

				if (!fileService.exists(uuid, project, null, null)) {
					return new StatusResult(RESTResultCodes.PROJECT_NOT_FOUND);
				}
			}

			ResultData data = jobMonitoringService.get(uuid, project, jobId);
			if (data != null) {
				StatusResult result = RESTUtils.convertResultDataToStatusResult(data);
				result.setCode(RESTResultCodes.OK);
				return result;
			}
			return new StatusResult(RESTResultCodes.JOB_DOES_NOT_EXIST);
		} catch (NotAuthorizedException e) {
			return new StatusResult(RESTResultCodes.NOT_AUTHORIZED);
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
			return new StatusResult(RESTResultCodes.SERVICE_NOT_FOUND);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
			return new StatusResult(RESTResultCodes.JOB_DOES_NOT_EXIST);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return new StatusResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Deletes a job result
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param jobId
	 *          The id of the job.
	 * @return A result containing the code of the result. <br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid; <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required parameter is
	 *         null; <br>
	 *         - {@link RESTResultCodes#JOB_DOES_NOT_EXIST} if the job result does
	 *         not exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/delete")
	public Result delete(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("jobId") String jobId, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			FileService fileService = RESTOperation.getFileService(service);
			JobMonitoringService jobMonitoringService = RESTOperation.getJobMonitoringService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (jobId == null || jobId.isEmpty())) {
					return new StatusResult(RESTResultCodes.USER_ERROR);
				}

				if (!fileService.exists(uuid, project, null, null)) {
					return new StatusResult(RESTResultCodes.PROJECT_NOT_FOUND);
				}
			}

			boolean result = jobMonitoringService.delete(uuid, project, jobId);
			if (result) {
				return new Result(RESTResultCodes.OK);
			}
			return new Result(RESTResultCodes.JOB_DOES_NOT_EXIST);
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
			return new StatusResult(RESTResultCodes.JOB_DOES_NOT_EXIST);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return new Result(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Gets the log of the job.
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param service
	 *          The service name.
	 * @param project
	 *          A project owned by the user.
	 * @param jobId
	 *          The id of the job.
	 * @return The log of the job <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         -{@link RESTResultCodes#NO_CONTENT} if the log can not be returned; <br>
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/log")
	public Response log(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("jobId") String jobId, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			JobMonitoringService jobMonitoringService = RESTOperation.getJobMonitoringService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (jobId == null || jobId.isEmpty())) {
					return Response.noContent().build();
				}
			}
			InputStream is = null;

			is = jobMonitoringService.log(uuid, project, jobId);

			if (is == null) {
				return Response.noContent().build();
			}
			return Response.ok(is, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = " + jobId + ".log").build();
		} catch (NotAuthorizedException e) {
			return Response.noContent().build();
		} catch (ServiceNotFoundException e) {
			RESTUtils.handleException(e, logger);
		} catch (ServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UnexpectedException e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		} catch (Exception e) {
			RESTOperation.initServices();
			RESTUtils.handleException(e, logger);
		}
		return Response.noContent().build();
	}

	/**
	 * 
	 * Gets the immutable file history used and returned by the job
	 * 
	 * @param uuid
	 *          The universally unique identifier of the user
	 * @param service
	 *          The service name. The service name.
	 * @param project
	 *          A project owned by the user
	 * @param jobId
	 *          The id of the job
	 * @param parents
	 *          (optional) The parents directory. Null if there is no parents
	 * @param file
	 *          (optional) The file name. Null if there is not file name
	 * @return A result containing the file representation of the history
	 *         directory of the job and the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required parameter is
	 *         null;<br>
	 *         - {@link RESTResultCodes#NOT_AUTHORIZED} if the user is not valid; <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if the file was found; <br>
	 *         {@link RESTResultCodes#USER_ERROR} if the job is not valid; <br>
	 *         - {@link RESTResultCodes#FILE_DOES_NOT_EXIST} if the file does not
	 *         exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs. - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service
	 *         is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/history")
	public FileResult history(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("jobId") String jobId, @FormParam("parents") String parents, @FormParam("file") String file, @FormParam("skipValidation") String skipValidation) {
		try {
			boolean validate = !Boolean.parseBoolean(skipValidation);

			FileService fileService = RESTOperation.getFileService(service);
			JobMonitoringService jobMonitoringService = RESTOperation.getJobMonitoringService(service);

			if (validate) {
				if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (jobId == null || jobId.isEmpty())) {
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
			String[] parentsArray = RESTUtils.convertAbsolutePathToArray(parents, true, false);

			// Calling the method
			FileData data = jobMonitoringService.history(uuid, project, jobId, parentsArray, file);
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
	 * Downloads a file from the immutable file history
	 * 
	 * @param uuid
	 *          The universally unique identifier of the user
	 * @param service
	 *          The service name. The service name.
	 * @param project
	 *          A project owned by the user
	 * @param jobId
	 *          The if of the job
	 * @param parents
	 *          (optional) The parents directory. Null if there is no parents
	 * @param file
	 *          The file name
	 * @return The file content or null if the file was not found and a result
	 *         containing the code of the result. <br>
	 *         - {@link RESTResultCodes#OK} if the file download was successful; <br>
	 *         -{@link RESTResultCodes#NO_CONTENT} if the file was not found; <br>
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/download")
	public Response download(@FormParam("service") String service, @FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("jobId") String jobId, @FormParam("parents") String parents, @FormParam("file") String file, @FormParam("skipValidation") String skipValidation) {
		InputStream is = null;
		try {
			JobMonitoringService jobMonitoringService = RESTOperation.getJobMonitoringService(service);
			if (parents != null && parents.isEmpty()) {
				parents = null;
			}

			String[] parentsArray = RESTUtils.convertAbsolutePathToArray(parents, true, false);

			// Calling the method
			is = jobMonitoringService.download(uuid, project, jobId, parentsArray, file);
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
