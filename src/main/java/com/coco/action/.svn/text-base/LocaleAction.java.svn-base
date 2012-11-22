package com.coco.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

// Cambiar locale y redireccionar a 'page' o 'success'
public final class LocaleAction extends Action {

	private final Log log = LogFactory.getFactory().getInstance(
			this.getClass().getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Extraer los atributos necesarios
		HttpSession session = request.getSession();
		Locale locale = getLocale(request);

		String language = null;
		String country = null;
		String page = null;

		try {
			language = ((DynaActionForm) form).getString("language");
			country = ((DynaActionForm) form).getString("country");
			page = ((DynaActionForm) form).getString("page");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		if ((language != null && language.length() > 0)
				&& (country != null && country.length() > 0)) {
			locale = new java.util.Locale(language, country);
		} else if (language != null && language.length() > 0) {
			locale = new java.util.Locale(language, "");
		}

		session.setAttribute(Globals.LOCALE_KEY, locale);

		if ((null == page) || ("".equals(page)))
			return mapping.findForward("loginPage");
		else
			return new ActionForward(page);
	}
}