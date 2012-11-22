package com.coco.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import com.coco.service.ICOCOService;
import com.coco.struts.COCOProblem;
import com.coco.struts.UserContainer;
import com.coco.vo.InputVO;

public class ShowProblemAction extends com.coco.struts.CustomDispatchAction {
	public ActionForward showInput(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		if (isNotLoggedIn(request)) {
			return mapping.findForward("loginPage");
		}

		String idProblem = request.getParameter("id");

		ICOCOService serviceImpl = getCOCOService();
		InputVO inCOCO = serviceImpl.getCOCOInput(idProblem);

		if (inCOCO.getId() == -1) {
			return mapping.findForward("initPage");
		}

		// Poner el problema seleccionado en sessión
		UserContainer usercontainer = getUserContainer(request);
		if (usercontainer.getListCOCO().getActualCOCO() == null) {
			usercontainer.getListCOCO().setActualCOCO(new COCOProblem(inCOCO));
		} else {
			usercontainer.getListCOCO().getActualCOCO().setInCOCO(inCOCO);
		}

		((DynaValidatorForm) form).set("inCOCO", inCOCO);
		return mapping.findForward("inputPage");
	}

	public ActionForward showOutput(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UserContainer existingContainer = getUserContainer(request);
		ICOCOService serviceImpl = getCOCOService();

		String idProblem = request.getParameter("id");
		if ("-1".equals(idProblem)) {
			return mapping.findForward("initOutPage");
		}
		InputVO inCOCO = serviceImpl.getCOCOInput(idProblem);

		if (existingContainer.getListCOCO().getActualCOCO() == null) {
			existingContainer.getListCOCO().setActualCOCO(new COCOProblem(inCOCO));
		} else {
			existingContainer.getListCOCO().getActualCOCO().setInCOCO(inCOCO);
		}
		existingContainer.getListCOCO().getActualCOCO().calculateOutput();

		((DynaValidatorForm) form).set("outCOCO", existingContainer.getListCOCO().getActualCOCO().getOutCOCO());
		((DynaValidatorForm) form).set("inCOCO", existingContainer.getListCOCO().getActualCOCO().getInCOCO());

		return mapping.findForward("outputPage");
	}
}