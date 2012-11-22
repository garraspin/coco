package com.coco.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.coco.service.ICOCOService;
import com.coco.struts.CustomBaseAction;
import com.coco.struts.UserContainer;

public class DeleteProblemAction extends CustomBaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idProblem = request.getParameter("id");

		if (idProblem != null) {
			ICOCOService serviceImpl = getCOCOService();
			serviceImpl.deleteCOCOInput(idProblem);

			UserContainer existingContainer = getUserContainer(request);
			existingContainer.getListCOCO().setProblems(
					serviceImpl.getListCOCOProblems(existingContainer.getUserVO().getId()));
		}

		return mapping.findForward("initPage");
	}
}