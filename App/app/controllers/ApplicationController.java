package controllers;

import play.mvc.Result;
import play.mvc.Security.Authenticated;
import controllers.security.Secured;
import controllers.vo.PageContent;

public class ApplicationController extends BaseController {

	@Authenticated(Secured.class)
	public static Result index() {
		final PageContent content = new PageContent(null);
		return ok(views.html.index.render(content));
	}
}
