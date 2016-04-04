package controllers;

import java.util.ArrayList;
import java.util.List;

import models.EItem;
import models.EUser;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import controllers.security.Secured;
import controllers.vo.SimpleItem;

public class ItemController extends BaseController {

	@Authenticated(Secured.class)
	public static Result newItems() {
		// Find all products that the user hasn't seen
		final EUser user = getCurrentUser();

		final List<SimpleItem> items = new ArrayList<SimpleItem>();
		final List<EItem> nonViewed = EItem.findNewItems(user.getAccount());
		for (final EItem item : nonViewed) {
			items.add(new SimpleItem(item));
		}

		return ok(Json.toJson(items).toString());
	}
}
