package com.coco.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.coco.service.COCOServiceFactoryImpl;
import com.coco.service.ICOCOService;
import com.coco.service.ICOCOServiceFactory;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;

public class ActionUtils {

    private final Logger log = Logger.getLogger(ActionUtils.class);

    // Obtener la instancia del objeto aplicación por el nombre
    protected <T> T getApplicationObject(ActionServlet servlet, String attrName) {
        return (T) servlet.getServletContext().getAttribute(attrName);
    }

    // Obtener la instancia del objeto sesión por el nombre
    protected <T> T getSessionObject(HttpServletRequest req, String attrName) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (T) session.getAttribute(attrName);
    }

    public boolean isNotLoggedIn(HttpServletRequest request) {
        return getUserContainer(request).getUserVO() == null;
    }

    // Obtener userContainer de la sesión
    public UserContainer getUserContainer(HttpServletRequest request) {
        UserContainer userContainer = getSessionObject(request, "UserContainer");

        // Crear un nuevo userContainer si no existe todavía
        if (userContainer == null) {
            userContainer = new UserContainer();

            HttpSession session = request.getSession(true);
            session.setAttribute("UserContainer", userContainer);
        }

        return userContainer;
    }

    public ICOCOService getCOCOService(ActionServlet servlet) {
        try {
            ICOCOServiceFactory factory = getApplicationObject(servlet, COCOServiceFactoryImpl.COCO_FACTORY_NAME);
            return factory.createService();
        } catch (Exception ex) {
            log.error("Error trying to create the service ...", ex);
        }

        return null;
    }
}
