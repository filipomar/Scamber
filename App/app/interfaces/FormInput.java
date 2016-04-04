package interfaces;

import play.data.DynamicForm;

public interface FormInput extends Validatable {

	void fill(DynamicForm requestData);
}
