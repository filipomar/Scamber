package controllers;

import models.EUser;

import org.apache.commons.lang3.StringUtils;

import play.mvc.Controller;
import controllers.security.Secured;

class BaseController extends Controller {

	protected static EUser getCurrentUser() {
		final String account = Secured.getUserNameFromSession(session());
		return StringUtils.isBlank(account) ? null : EUser.findByAccount(account);
	}

}
