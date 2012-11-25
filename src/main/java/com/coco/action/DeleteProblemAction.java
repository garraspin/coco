package com.coco.action;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coco.service.ICOCOService;
import com.coco.struts.ActionUtils;
import com.coco.struts.UserContainer;
import com.coco.vo.BaseVO;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DeleteProblemAction extends Action {

    private final ActionUtils actionUtils = new ActionUtils();

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response
    ) throws ServletException, IOException {
		String idProblem = request.getParameter("id");

		if (idProblem != null) {
			ICOCOService serviceImpl = actionUtils.getCOCOService(servlet);
			serviceImpl.deleteCOCOInput(idProblem);

			UserContainer existingContainer = actionUtils.getUserContainer(request);
            int userId = existingContainer.getUserVO().getId();
            List<BaseVO> listCOCOProblems = serviceImpl.getListCOCOProblems(userId);
            existingContainer.getListCOCO().setProblems(listCOCOProblems);
        }

		return mapping.findForward("initPage");
	}
}