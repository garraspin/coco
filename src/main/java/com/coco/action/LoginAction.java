package com.coco.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coco.form.LoginForm;
import com.coco.service.ICOCOService;
import com.coco.struts.ActionUtils;
import com.coco.struts.UserContainer;
import com.coco.vo.BaseVO;
import com.coco.vo.UserVO;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class LoginAction extends Action {

    private final Logger log = Logger.getLogger(LoginAction.class);
    private final ActionUtils actionUtils = new ActionUtils();

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response
    ) throws Exception {
		// Obtener email y contraseña
		String email = ((LoginForm) form).getEmailLogin();
		String password = ((LoginForm) form).getPasswordLogin();

		log.info("login user: = " + email);

		log.info("Without taking service...");
		// En CustomBaseAction se crea una instancia del servicio
		ICOCOService serviceImpl = actionUtils.getCOCOService(servlet);
		log.info("We have the service: " + serviceImpl.toString());

		UserVO userVO = serviceImpl.authenticate(email, password);
		if (userVO == null) {
            ActionErrors errors = new ActionErrors();
            errors.add("loginInvalid", new ActionMessage("login.invalid"));
            saveErrors(request, errors);
			return mapping. findForward("loginPage");
		} else {
			// Poner el login de usuario en UserContainer
			UserContainer existingContainer = actionUtils.getUserContainer(request);
			existingContainer.setUserVO(userVO);

			// Poner la lista de funciones y las reglas de clasificación en
			// UserContainer
			existingContainer.setFunctionsList(serviceImpl.getFunctions());
			existingContainer.setRankRulesList(serviceImpl.getRankRules());

			List<BaseVO> listCOCOProblems = serviceImpl.getListCOCOProblems(userVO.getId());
			// Poner la lista de problemas en UserContainer
			existingContainer.getListCOCO().setProblems(listCOCOProblems);

			log.info(existingContainer.getUserVO());

			return mapping.findForward("initPage");
		}
	}
}