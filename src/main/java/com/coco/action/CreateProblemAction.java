package com.coco.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coco.struts.ActionUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import com.coco.struts.UserContainer;

import com.coco.vo.InputVO;

public class CreateProblemAction extends Action {
	private static final int NUM_INITIAL_ROWS = 3;
	private static final int NUM_INITIAL_COLS = 3;

    private final ActionUtils actionUtils = new ActionUtils();

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response
    ) throws ServletException, IOException {
        if (actionUtils.isNotLoggedIn(request)) {
            return mapping.findForward("loginPage");
        }

		// Crear un problema nuevo
		InputVO inCOCO = new InputVO();
		inCOCO.addEmptyElements(NUM_INITIAL_ROWS);
		inCOCO.addEmptyAttributes(NUM_INITIAL_COLS);
		
		// Guardar el problema nuevo en el formulario
		((DynaValidatorForm) form).set("inCOCO", inCOCO);

		// Guardar el problema nuevo en la sesión
		UserContainer usercontainer = actionUtils.getUserContainer(request);
		usercontainer.getListCOCO().getActualCOCO().setInCOCO(inCOCO);

		return mapping.findForward("inputPage");
	}

}
