package com.coco.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coco.service.ICOCOService;
import com.coco.struts.ActionUtils;
import com.coco.struts.COCOProblem;
import com.coco.struts.UserContainer;
import com.coco.vo.InputVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

public class ShowProblemAction extends DispatchAction {
    private final ActionUtils actionUtils = new ActionUtils();

	public ActionForward showInput(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                   HttpServletResponse response) {
		if (actionUtils.isNotLoggedIn(request)) {
			return mapping.findForward("loginPage");
		}

		String idProblem = request.getParameter("id");
		ICOCOService serviceImpl = actionUtils.getCOCOService(servlet);
		InputVO inCOCO = serviceImpl.getCOCOInput(idProblem);

		if (inCOCO.getId() == -1) {
			return mapping.findForward("initPage");
		}

        // Poner el problema seleccionado en sesión
        ((DynaValidatorForm) form).set("inCOCO", inCOCO);

        // Poner el problema seleccionado en sesión
        UserContainer usercontainer = actionUtils.getUserContainer(request);
        if (usercontainer.getListCOCO().getActualCOCO() == null) {
            usercontainer.getListCOCO().setActualCOCO(new COCOProblem(inCOCO));
        } else {
            usercontainer.getListCOCO().getActualCOCO().setInCOCO(inCOCO);
        }

		return mapping.findForward("inputPage");
	}

	public ActionForward showOutput(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                    HttpServletResponse response) {
        if (actionUtils.isNotLoggedIn(request)) {
            return mapping.findForward("loginPage");
        }

		UserContainer existingContainer = actionUtils.getUserContainer(request);
		ICOCOService serviceImpl = actionUtils.getCOCOService(servlet);

		String idProblem = request.getParameter("id");
		if ("-1".equals(idProblem)) {
			return mapping.findForward("initOutPage");
		}
		InputVO inCOCO = serviceImpl.getCOCOInput(idProblem);

        COCOProblem actualCOCO = new COCOProblem(inCOCO);
        actualCOCO.calculateOutput();

        existingContainer.getListCOCO().setActualCOCO(actualCOCO);

		((DynaValidatorForm) form).set("outCOCO", actualCOCO.getOutCOCO());
		((DynaValidatorForm) form).set("inCOCO", actualCOCO.getInCOCO());

		return mapping.findForward("outputPage");
	}
}