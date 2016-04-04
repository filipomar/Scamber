package controllers.vo;

import java.util.ArrayList;
import java.util.List;

import play.api.templates.Html;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PageContent {

	@JsonIgnore
	private Html body;

	@JsonIgnore
	private String cssClass;

	@JsonIgnore
	private final String pageTitle;

	@JsonProperty
	private final List<String> jsModules;

	public Html getBody() {
		return this.body;
	}

	public String getCssClass() {
		return this.cssClass;
	}

	public String getPageTitle() {
		return this.pageTitle;
	}

	public void setBody(final Html body) {
		this.body = body;
	}

	public void setCssClass(final String cssClass) {
		this.cssClass = cssClass;
	}

	public PageContent(final String pageTitle) {
		this.pageTitle = pageTitle;
		this.setCssClass("");
		this.jsModules = new ArrayList<String>();
	}

	public void addJSModule(final String module) {
		this.jsModules.add(module);
	}

}
