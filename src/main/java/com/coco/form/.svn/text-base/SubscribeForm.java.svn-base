package com.coco.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class SubscribeForm extends ActionForm {
	private static final long serialVersionUID = 7237938687988182944L;
	private String nameSubs = null;
	private String surnameSubs = null;
	private String passwordSubs = null;
	private String repasswordSubs = null;
	private String emailSubs = null;

	public String getEmailSubs() {
		return emailSubs;
	}

	public void setEmailSubs(String emailSubs) {
		this.emailSubs = emailSubs;
	}

	public String getNameSubs() {
		return nameSubs;
	}

	public void setNameSubs(String nameSubs) {
		this.nameSubs = nameSubs;
	}

	public String getPasswordSubs() {
		return passwordSubs;
	}

	public void setPasswordSubs(String passwordSubs) {
		this.passwordSubs = passwordSubs;
	}

	public String getRepasswordSubs() {
		return repasswordSubs;
	}

	public void setRepasswordSubs(String repasswordSubs) {
		this.repasswordSubs = repasswordSubs;
	}

	public String getSurnameSubs() {
		return surnameSubs;
	}

	public void setSurnameSubs(String surnameSubs) {
		this.surnameSubs = surnameSubs;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		if (getNameSubs() == null || getNameSubs().length() < 1) {
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
					"global.required", "name"));
		}

		if (getSurnameSubs() == null || getSurnameSubs().length() < 1) {
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
					"global.required", "surname"));
		}

		if (getPasswordSubs() == null || getPasswordSubs().length() < 1) {
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
					"global.required", "password"));
		}

		if (getRepasswordSubs() == null
				|| !getRepasswordSubs().matches(getPasswordSubs())) {
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
					"error.password", "repassword"));
		}

		if (getEmailSubs() == null || getEmailSubs().length() < 1) {
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
					"global.required", "email"));
		}

		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.nameSubs = null;
		this.surnameSubs = null;
		this.passwordSubs = null;
		this.repasswordSubs = null;
		this.emailSubs = null;
	}

}
