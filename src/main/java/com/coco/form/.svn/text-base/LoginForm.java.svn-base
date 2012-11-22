package com.coco.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class LoginForm extends ActionForm {
	private static final long serialVersionUID = 2654813677491749498L;
	private String passwordLogin = null;
	private String emailLogin = null;

	public void setEmailLogin(String email) {
		this.emailLogin = email;
	}

	public String getEmailLogin() {
		return (this.emailLogin);
	}

	public String getPasswordLogin() {
		return (this.passwordLogin);
	}

	public void setPasswordLogin(String password) {
		this.passwordLogin = password;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (getEmailLogin() == null || getEmailLogin().length() < 1) {
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
					"global.required", "emailLogin"));
		}

		if (getPasswordLogin() == null || getPasswordLogin().length() < 1) {
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
					"global.required", "passwordLogin"));
		}

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.passwordLogin = null;
		this.emailLogin = null;
	}

}