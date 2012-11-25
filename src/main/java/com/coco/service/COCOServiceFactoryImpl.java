package com.coco.service;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

public class COCOServiceFactoryImpl implements ICOCOServiceFactory, PlugIn {
	private ActionServlet servlet = null;

	private static final String SERVICE_CLASS_NAME = "com.coco.service.COCOServiceImpl";
	public static final String COCO_FACTORY_NAME = "com.coco.service.ICOCOServiceFactory";

    @Override
	public ICOCOService createService() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		ICOCOService instance = (ICOCOService) Class.forName(SERVICE_CLASS_NAME).newInstance();

		instance.setServletContext(servlet.getServletContext());

		return instance;
	}

    @Override
	public void init(ActionServlet servlet, ModuleConfig config) throws ServletException {
		// Guardar copia del servlet
		this.servlet = servlet;

		// Guardar copia de la factoria en el contexto
		servlet.getServletContext().setAttribute(COCO_FACTORY_NAME, this);
	}

    @Override
	public void destroy() {
		// nada
	}
}
