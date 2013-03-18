package com.coco.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coco.service.ICOCOService;
import com.coco.struts.ActionUtils;
import com.coco.struts.UserContainer;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DeleteUserAction extends Action {

    private final ActionUtils actionUtils = new ActionUtils();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response
    ) throws Exception {
        if (actionUtils.isNotLoggedIn(request)) {
            return mapping.findForward("loginPage");
        }

        ICOCOService serviceImpl = actionUtils.getCOCOService(servlet);
        UserContainer existingContainer = actionUtils.getUserContainer(request);
        serviceImpl.deleteUser(Integer.valueOf(existingContainer.getUserVO().getId()));
        request.getSession().invalidate();

        return mapping.findForward("loginPage");
    }
}
