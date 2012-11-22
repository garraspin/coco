package com.coco.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;

import com.coco.service.COCOServiceFactoryImpl;
import com.coco.service.ICOCOService;
import com.coco.service.ICOCOServiceFactory;

public class CustomBaseAction extends Action {

	protected final Logger log = Logger.getLogger(this.getClass().getName());

	protected ICOCOService getCOCOService() {
		ICOCOServiceFactory factory = (ICOCOServiceFactory) getApplicationObject(COCOServiceFactoryImpl.cocoFactoryName);
		ICOCOService service = null;

		try {
			service = factory.createService();
		} catch (Exception ex) {
			log.error("Error trying to create the service ...");
			log.error(ex.getStackTrace());
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
        UserContainer userContainer =
                (UserContainer) getSessionObject(request, "UserContainer");

        // Crear un nuevo userContainer si no existe todavía
        if (userContainer == null) {
            userContainer = new UserContainer();

            HttpSession session = request.getSession(true);
            session.setAttribute("UserContainer", userContainer);
        }

        return userContainer;
    }

	public boolean isLoggedIn(HttpServletRequest request) {
		UserContainer container = getUserContainer(request);
		return container.getUserVO() != null;
	}
}
