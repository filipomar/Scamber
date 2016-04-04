package utils;

import interfaces.FormInput;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;

public class FormUtils {

	public static <T extends FormInput> T get(final Class<T> clazz) {
		final DynamicForm requestData = Form.form().bindFromRequest();

		try {
			final T instance = clazz.newInstance();
			instance.fill(requestData);
			return instance;
		} catch (final Exception e) {
			Logger.error("Could not get form for " + clazz, e);
		}

		return null;
	}

}
