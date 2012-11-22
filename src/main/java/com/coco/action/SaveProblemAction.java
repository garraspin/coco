package com.coco.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import com.coco.service.ICOCOService;
import com.coco.struts.CustomDispatchAction;
import com.coco.struts.UserContainer;
import com.coco.vo.InputVO;

public class SaveProblemAction extends CustomDispatchAction {
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// Obtener el problema del formulario
		InputVO inCOCO = (InputVO) ((DynaValidatorForm) form).get("inCOCO");

		UserContainer existingContainer = getUserContainer(request);

		// Guardar los input del problema en la base de datos
		ICOCOService serviceImpl = getCOCOService();
		serviceImpl.setCOCOInput(inCOCO, existingContainer.getUserVO().getId());

		// Obtener la nueva lista de problemas
		existingContainer.getListCOCO().setProblems(serviceImpl.getListCOCOProblems(existingContainer.getUserVO().getId()));

		// Determinar el actual problema
		existingContainer.getListCOCO().getActualCOCO().setInCOCO(inCOCO);

		return mapping.findForward("initPage");
	}

	public ActionForward alterRowCol(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// Obtener los modificadores del formulario
		int nRowValue = Integer.valueOf(((DynaValidatorForm) form).getString("row"));
		int nColValue = Integer.valueOf(((DynaValidatorForm) form).getString("col"));

		// Obtener problema de la sesión y ponerlo en el formulario
		UserContainer usercontainer = getUserContainer(request);
		InputVO inCOCO = usercontainer.getListCOCO().getActualCOCO().getInCOCO();

		if (nRowValue > 0) {
			inCOCO.addEmptyElements(nRowValue);
		} else {
			inCOCO.removeElement(Math.abs(nRowValue));
		}
		if (nColValue > 0) {
			inCOCO.addEmptyAttributes(nColValue);
		} else {
			inCOCO.removeAttribute(Math.abs(nColValue));
		}

		// Guardar el problema en el formulario
		((DynaValidatorForm) form).set("inCOCO", inCOCO);

		return mapping.findForward("inputPage");
	}
}