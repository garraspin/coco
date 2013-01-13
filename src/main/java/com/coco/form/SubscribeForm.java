package com.coco.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

public class SubscribeForm extends ValidatorForm {
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

    @Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.nameSubs = null;
		this.surnameSubs = null;
		this.passwordSubs = null;
		this.repasswordSubs = null;
		this.emailSubs = null;
	}

}
