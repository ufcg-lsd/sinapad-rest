package br.lncc.sinapad.rest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;
import br.lncc.sinapad.core.exception.NotAuthorizedException;
import br.lncc.sinapad.core.exception.ServiceException;
import br.lncc.sinapad.core.exception.UnexpectedException;
import br.lncc.sinapad.core.service.file.FileService;
import br.lncc.sinapad.rest.authentication.AuthenticationRESTOperation;
import br.lncc.sinapad.rest.data.AuthenticationResult;
import br.lncc.sinapad.rest.data.Result;
import br.lncc.sinapad.rest.exception.ServiceNotFoundException;
import br.lncc.sinapad.rest.utils.RESTUtils;

public class RestOperationTest extends TestCase {

	public static final String SERVICE = "service";
	public static final String USERNAME_VALID = "username.valid";
	public static final String USERNAME_INVALID = "username.invalid";
	public static final String PASSWORD_VALID = "password.valid";
	public static final String PASSWORD_INVALID = "password.invalid";
	public static final String PROJECT_VALID = "project.valid";
	public static final String PROJECT_INVALID = "project.invalid";
	public static final String APPLICATION_VALID = "application.valid";
	public static final String APPLICATION_INVALID = "application.invalid";
	public static final String VERSION_VALID = "version.valid";
	public static final String VERSION_INVALID = "version.invalid";
	public static final String[] PARAMETERS = { "application.args.key.", "application.args.value.", "application.args.type.", "application.args.content." };

	public static String service;
	public static String usernameValid;
	public static String usernameInvalid;
	public static String passwordValid;
	public static String passwordInvalid;
	public static String projectValid;
	public static String projectInvalid;
	public static String applicationValid;
	public static String applicationInvalid;
	public static String versionValid;
	public static String versionInvalid;
	protected static Result result;
	protected static String userUUIDValid;
	protected static Properties properties = new Properties();

	static {
		try {
			InputStream is = RESTOperation.class.getResourceAsStream("/rest_test.properties");
			properties.load(is);
			service = properties.getProperty(SERVICE);
			usernameValid = properties.getProperty(USERNAME_VALID);
			usernameInvalid = properties.getProperty(USERNAME_INVALID);
			passwordValid = properties.getProperty(PASSWORD_VALID);
			passwordInvalid = properties.getProperty(PASSWORD_INVALID);
			projectValid = properties.getProperty(PROJECT_VALID);
			projectInvalid = properties.getProperty(PROJECT_INVALID);
			applicationValid = properties.getProperty(APPLICATION_VALID);
			applicationInvalid = properties.getProperty(APPLICATION_INVALID);
			versionValid = properties.getProperty(VERSION_VALID);
			versionInvalid = properties.getProperty(VERSION_INVALID);
			userUUIDValid = doLogin();

		} catch (Exception e) {
			throw new RuntimeException("Could not configure the tests", e);
		}
	}

	private static String doLogin() {
		AuthenticationRESTOperation authentication = new AuthenticationRESTOperation();
		AuthenticationResult result = authentication.loginLDAP(service, usernameValid, passwordValid);
		String id = result.getUuid();
		return id;
	}

	protected static String loadParameters() {
		try {
			FileService fileService = RESTOperation.getFileService(service);
			int i = 0;
			String parameters = "";
			while (properties.containsKey("application.args.key." + i)) {
				String key = properties.getProperty("application.args.key." + i);
				String value = properties.getProperty("application.args.value." + i);
				String type = properties.getProperty("application.args.type." + i);

				parameters += key + "::" + value + ";";
				String content = properties.getProperty("application.args.content." + i);

				if ("FILE".equals(type)) {

					createFile(fileService, value, content);
				}

				if ("DIR".equals(type)) {
					createDirs(fileService, value);
				}

				i++;

			}
			// removes the last ';'
			parameters = parameters.substring(0, parameters.length() - 1);
			return parameters;
		} catch (Exception e) {
			assertTrue(false);
		}
		return null;
	}

	private static void createDirs(FileService fileService, String value) throws ServiceException, NotAuthorizedException, UnexpectedException {
		String[] parents = RESTUtils.convertAbsolutePathToArray(value, false, false);
		String path = "";
		for (int index = 0; index <= parents.length - 1; index++) {
			try {
				path = path + "/" + parents[index - 1];
				String[] dir = RESTUtils.convertAbsolutePathToArray(path, true, false);
				fileService.create(userUUIDValid, projectValid, dir, parents[index], true);
			} catch (ArrayIndexOutOfBoundsException e) {
				if (!fileService.exists(userUUIDValid, projectValid, null, parents[index])) {
					fileService.create(userUUIDValid, projectValid, null, parents[index], true);
				}
			}
		}
	}

	private static void createFile(FileService fileService, String value, String content) throws IOException, ServiceException, NotAuthorizedException, UnexpectedException {
		String fileName = RESTUtils.retrieveFileNameFromAbsolutePath(value);
		File file = new File(fileName);

		if (content != null) {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(content);
			out.close();
		}

		String[] parents = RESTUtils.convertAbsolutePathToArray(value, false, true);
		String path = "";
		for (int index = 0; index <= parents.length - 1; index++) {
			try {
				path = path + "/" + parents[index - 1];
				String[] dir = RESTUtils.convertAbsolutePathToArray(path, true, false);
				fileService.create(userUUIDValid, projectValid, dir, parents[index], true);
			} catch (ArrayIndexOutOfBoundsException e) {
				fileService.create(userUUIDValid, projectValid, null, parents[index], true);
			}
		}
		fileService.upload(userUUIDValid, projectValid, parents, fileName, file, true);
	}

	protected void clearParameters() throws ServiceException, NotAuthorizedException, ServiceNotFoundException, UnexpectedException {
		FileService fileService = RESTOperation.getFileService(service);
		int i = 0;
		while (properties.containsKey("application.args.key." + i)) {
			String value = properties.getProperty("application.args.value." + i);
			String type = properties.getProperty("application.args.type." + i);
			if ("FILE".equals(type) || "DIR".equals(type)) {
				String[] parents = RESTUtils.convertAbsolutePathToArray(value, false, false);
				fileService.delete(userUUIDValid, projectValid, null, parents[0]);
			}
			i++;
		}
	}

	public void test() {
		// avoid warnings due to junit test case implementation
	}
}
