package com.coco.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.coco.struts.CustomBaseAction;

public class LogoutAction extends CustomBaseAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Invalidar sesión: Todos los atributos de UserContainer son liberados
		// cuando se libera la sesión (ver UserContainer.java)
		request.getSession().invalidate();

		return mapping.findForward("loginPage");
	}

}
