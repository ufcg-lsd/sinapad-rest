package br.lncc.sinapad.rest.service;

import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import br.lncc.sinapad.rest.RESTOperation;
import br.lncc.sinapad.rest.data.ListServiceResult;
import br.lncc.sinapad.rest.data.ServiceResult;
import br.lncc.sinapad.rest.utils.RESTResultCodes;

@Path("/service")
public class ServiceRESTOperation extends RESTOperation {

	private static Logger logger = Logger.getLogger(ServiceRESTOperation.class);

	/**
	 * 
	 * List the services that are available on REST *
	 * 
	 * @return A list containing the name of all services available for the user
	 *         of the result. <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs.
	 *         - {@link RESTResultCodes#SERVICE_NOT_FOUND} if the service is not valid.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list")
	public ListServiceResult list() {
		Set<String> services = RESTOperation.getServices();
		Iterator<String> i = services.iterator();
		ListServiceResult results = new ListServiceResult();
		while (i.hasNext()) {
			String service = i.next();
			ServiceResult result = new ServiceResult();
			result.setName(service);
			result.setCode(RESTResultCodes.OK);
			results.addElement(result);
		}
		results.setCode(RESTResultCodes.OK);
		return results;
	}
}
