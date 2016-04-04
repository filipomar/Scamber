package input;

import static constants.GeneralConstants.LOGIN_ACCOUNT_FIELD;
import static constants.GeneralConstants.PASSWORD_ACCOUNT_FIELD;
import interfaces.FormInput;

import org.apache.commons.lang3.StringUtils;

import play.data.DynamicForm;

public class LoginForm implements FormInput {

	private String account;
	private String password;

	@Override
	public boolean isInvalid() {
		return StringUtils.isBlank(this.account) || StringUtils.isBlank(this.password);
	}

	@Override
	public void fill(final DynamicForm form) {
		this.account = form.get(LOGIN_ACCOUNT_FIELD);
		this.password = form.get(PASSWORD_ACCOUNT_FIELD);
	}

	public String getAccount() {
		return this.account;
	}

	public String getPassword() {
		return this.password;
	}

}
