package com.coco.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.coco.form.LoginForm;
import com.coco.service.ICOCOService;
import com.coco.struts.CustomBaseAction;
import com.coco.struts.UserContainer;

import com.coco.vo.BaseVO;
import com.coco.vo.UserVO;

public class LoginAction extends CustomBaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Obtener email y contraseña
		String email = ((LoginForm) form).getEmailLogin();
		String password = ((LoginForm) form).getPasswordLogin();

		log.info("EMAIL = " + email);
		log.info("PASSWORD = " + password);
		log.info("Without taking service...");
		// En la clase CustomBaseAction se crea una instancia del servicio
		ICOCOService serviceImpl = getCOCOService();
		log.info("We have the service: " + serviceImpl.toString());

		UserVO userVO = serviceImpl.authenticate(email, password);
		if (userVO == null) {
			return mapping.findForward("loginPage");
		} else {
			// Poner el login de usuario en UserContainer
			UserContainer existingContainer = getUserContainer(request);
			existingContainer.setUserVO(userVO);

			// Poner la lista de funciones y las reglas de clasificación en
			// UserContainer
			existingContainer.setFunctionsList(serviceImpl.getFunctions());
			existingContainer.setRankRulesList(serviceImpl.getRankRules());

			List<BaseVO> listCOCOProblems = serviceImpl
					.getListCOCOProblems(userVO.getId());
			// Poner la lista de problemas en UserContainer
			existingContainer.getListCOCO().setProblems(listCOCOProblems);

			log.info(existingContainer.getUserVO());

			return mapping.findForward("initPage");
		}
	}
}