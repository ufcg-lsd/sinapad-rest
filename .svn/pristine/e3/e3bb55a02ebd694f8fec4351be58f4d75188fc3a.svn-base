package br.lncc.sinapad.rest.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlRootElement(name = "list")
public class ListServiceResult extends Result {
	private List<ServiceResult> elements;

	public ListServiceResult() {
		this(RESTResultCodes.OK);
	}

	public ListServiceResult(int code) {
		super(code);
		elements = new ArrayList<ServiceResult>();
	}

	@XmlElementWrapper
	@XmlElement(name = "element")
	public List<ServiceResult> getElements() {
		return elements;
	}

	public void setElements(List<ServiceResult> elements) {
		this.elements = elements;
	}

	public void addElement(ServiceResult element) {
		this.elements.add(element);
	}

}
