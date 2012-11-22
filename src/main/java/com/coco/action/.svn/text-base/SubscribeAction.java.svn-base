package com.coco.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.coco.form.SubscribeForm;
import com.coco.service.ICOCOService;
import com.coco.struts.CustomBaseAction;
import com.coco.struts.UserContainer;

import com.coco.vo.UserVO;

public class SubscribeAction extends CustomBaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Obtenemos los datos del ActionForm.
		// Ya deberian haber sido validados por el ActionForm.
		String email = ((SubscribeForm) form).getEmailSubs();
		String password = ((SubscribeForm) form).getPasswordSubs();
		String name = ((SubscribeForm) form).getNameSubs();
		String surname = ((SubscribeForm) form).getSurnameSubs();
		String repassword = ((SubscribeForm) form).getRepasswordSubs();

		log.info("Without taking service...");

		// Desde la clase base CustomBaseAction creamos una instancia para el servicio.
		ICOCOService serviceImpl = getCOCOService();

		log.info("We have the service: " + serviceImpl.toString());

		if (password.compareTo(repassword) != 0) {
			return mapping.findForward("loginPage");
		} else {
			// Creamos el nuevo usuario
			UserVO userVO = new UserVO(-1, name, surname, email, password);
			// Lo guardamos en la bd
			serviceImpl.setUser(userVO);

			// Metemos los datos de usuario en la sesi�n.
			UserContainer existingContainer = getUserContainer(request);
			existingContainer.setUserVO(userVO);

			// Crear parametros rankValues y functions en la sesion
			existingContainer.setFunctionsList(serviceImpl.getFunctions());
			existingContainer.setRankRulesList(serviceImpl.getRankRules());

			// No es necesario buscar la lista de problemas porque no tiene.

			log.info(existingContainer.getUserVO());

			return mapping.findForward("initPage");
		}
	}
}