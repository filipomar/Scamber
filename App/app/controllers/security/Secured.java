package controllers.security;

import models.EUser;

import org.apache.commons.lang3.StringUtils;

import play.mvc.Http.Context;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Security.Authenticator;
import constants.GeneralConstants;
import controllers.routes;

public class Secured extends Authenticator {
	@Override
	public String getUsername(final Context ctx) {
		final String account = getUserNameFromSession(ctx.session());
		if (StringUtils.isBlank(account)) {
			return null;
		}

		if (EUser.exists(account)) {
			return account;
		} else {
			return null;
		}
	}

	public static String getUserNameFromSession(final Session session) {
		return session.get(GeneralConstants.AUTHENTICATION_ACCOUNT_FIELD);
	}

	@Override
	public Result onUnauthorized(final Context ctx) {
		return redirect(routes.AuthenticationController.loginPage().toString());
	}
}
