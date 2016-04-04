package controllers;

import java.io.File;

import models.EUser;

import org.apache.commons.lang3.StringUtils;

import play.Play;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import utils.ConfigurationUtils;
import controllers.security.Secured;

public class UserController extends BaseController {

	@Authenticated(Secured.class)
	public static Result userPicture(final String account) {
		final EUser user = EUser.findByAccount(account);
		if (user == null) {
			return notFound();
		}

		if (StringUtils.isBlank(user.getPicture())) {
			return internalServerError();
		}

		final File file = Play.application().getFile(ConfigurationUtils.getUserPicturesPath(user.getPicture()));
		if (!file.isFile()) {
			return internalServerError();
		}

		return ok(file, true);
	}
}
