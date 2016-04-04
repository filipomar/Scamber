package controllers;

import static constants.GeneralConstants.AUTHENTICATION_ACCOUNT_FIELD;
import input.LoginForm;
import models.EUser;
import play.mvc.Result;
import utils.FormUtils;
import utils.MessagesUtils;
import views.html.loginPage;
import controllers.vo.PageContent;

public class AuthenticationController extends BaseController {

	public static Result loginPage() {
		final PageContent content = new PageContent(MessagesUtils.getLoginPageTitle());
		return ok(loginPage.render(content));
	}

	public static Result authenticate() {
		final LoginForm form = FormUtils.get(LoginForm.class);

		if (form == null || form.isInvalid()) {
			return forbidden();
		}

		if (EUser.exists(form.getAccount(), form.getPassword())) {
			// Marked user as logged in
			session(AUTHENTICATION_ACCOUNT_FIELD, form.getAccount());
			return redirect(routes.ApplicationController.index().toString());
		}

		final PageContent content = new PageContent(MessagesUtils.getLoginPageTitle());
		return notFound(loginPage.render(content));
	}

}
