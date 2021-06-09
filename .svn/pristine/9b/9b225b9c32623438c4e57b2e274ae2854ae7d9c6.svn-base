package br.lncc.sinapad.rest.data;

import javax.xml.bind.annotation.XmlRootElement;

import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlRootElement
public class UserResult extends Result {
	/**
	 * The username.
	 */
	private String username;

	public UserResult() {
		this(RESTResultCodes.OK);
	}

	public UserResult(int code) {
		super(code);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * The fullname of the user.
	 */
	private String fullName;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * The email of the user.
	 */
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
