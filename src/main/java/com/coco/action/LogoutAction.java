package com.coco.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class LogoutAction extends Action {

    @Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response
    ) throws Exception {
		// Invalidar sesión: Todos los atributos de UserContainer son liberados
		// cuando se libera la sesión (ver UserContainer.java)
		request.getSession().invalidate();

		return mapping.findForward("loginPage");
	}

}
