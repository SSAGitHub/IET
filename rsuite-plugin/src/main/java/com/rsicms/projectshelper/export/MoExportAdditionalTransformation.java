package com.rsicms.projectshelper.export;

import java.util.Map;

import javax.xml.transform.Templates;

public class MoExportAdditionalTransformation {

	private Templates template;

	private Map<String, Object> parameters;

	public MoExportAdditionalTransformation(Templates template){
		this.template = template;
	}
	
	public MoExportAdditionalTransformation(Templates template,
			Map<String, Object> parameters) {
		this(template);
		this.parameters = parameters;
	}

	public Templates getTemplate() {
		return template;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

}
