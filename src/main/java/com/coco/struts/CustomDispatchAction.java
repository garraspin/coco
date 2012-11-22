package com.coco.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.actions.DispatchAction;

import com.coco.service.ICOCOService;
import com.coco.service.ICOCOServiceFactory;

public class CustomDispatchAction extends DispatchAction {

	private static final String cocoFactoryName = "com.coco.service.ICOCOServiceFactory";

	protected final Logger log = Logger.getLogger(this.getClass().getName());

	protected ICOCOService getCOCOService() {
		ICOCOServiceFactory factory = (ICOCOServiceFactory) getApplicationObject(cocoFactoryName);
		ICOCOService service = null;

		try {
			service = factory.createService();
		} catch (Exception ex) {
			log.info("Error trying to create the service ...");
			log.info(ex.getMessage());
		}

		return service;
	}

	// Obtener la instancia del objeto aplicación por el nombre
	protected Object getApplicationObject(String attrName) {
		return servlet.getServletContext().getAttribute(attrName);
	}

	// Obtener la instancia del objeto sesión por el nombre
    protected Object getSessionObject(HttpServletRequest req, String attrName) {
        Object sessionObj = null;
        HttpSession session = req.getSession(false);

        if (session != null) {
            sessionObj = session.getAttribute(attrName);
        }

        return sessionObj;
    }

    // Obtener userContainer de la sesión
    protected UserContainer getUserContainer(HttpServletRequest request) {
        UserContainer userContainer = (UserContainer) getSessionObject(request,
                "UserContainer");

        // Crear un nuevo userContainer si no existe todavía
        if (userContainer == null) {
            userContainer = new UserContainer();

            HttpSession session = request.getSession(true);
            session.setAttribute("UserContainer", userContainer);
        }

        return userContainer;
    }

	public boolean isNotLoggedIn(HttpServletRequest request) {
        return getUserContainer(request).getUserVO() == null;
	}
}
