package com.coco.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coco.service.ICOCOService;
import com.coco.struts.ActionUtils;
import com.coco.struts.UserContainer;
import com.coco.vo.BaseVO;
import com.coco.vo.InputVO;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

public class SaveProblemAction extends DispatchAction {

    private final ActionUtils actionUtils = new ActionUtils();

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                              HttpServletResponse response) {
		// Obtener el problema del formulario
		InputVO inCOCO = (InputVO) ((DynaValidatorForm) form).get("inCOCO");
		UserContainer existingContainer = actionUtils.getUserContainer(request);
        ICOCOService serviceImpl = actionUtils.getCOCOService(servlet);

        // Guardar los input del problema en la base de datos
        int userId = existingContainer.getUserVO().getId();
        serviceImpl.setCOCOInput(inCOCO, userId);

		// Obtener la nueva lista de problemas
        List<BaseVO> listCOCOProblems = serviceImpl.getListCOCOProblems(userId);
        existingContainer.getListCOCO().setProblems(listCOCOProblems);

		// Determinar el actual problema
		existingContainer.getListCOCO().getActualCOCO().setInCOCO(inCOCO);

		return mapping.findForward("initPage");
	}

	public ActionForward alterRowCol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) {
		// Obtener los modificadores del formulario
		int nRowValue = Integer.valueOf(((DynaValidatorForm) form).getString("row"));
		int nColValue = Integer.valueOf(((DynaValidatorForm) form).getString("col"));

		// Obtener problema de la sesión y ponerlo en el formulario
		UserContainer usercontainer = actionUtils.getUserContainer(request);
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