package com.coco.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

public class LoginForm extends ValidatorForm {
	private static final long serialVersionUID = 2654813677491749498L;
	private String passwordLogin = null;
	private String emailLogin = null;

	public void setEmailLogin(String email) {
		this.emailLogin = email;
	}

	public String getEmailLogin() {
		return this.emailLogin;
	}

	public String getPasswordLogin() {
		return this.passwordLogin;
	}

	public void setPasswordLogin(String password) {
		this.passwordLogin = password;
	}

    @Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.passwordLogin = null;
		this.emailLogin = null;
	}

}