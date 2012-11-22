package com.coco.service;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

public class COCOServiceFactoryImpl implements ICOCOServiceFactory, PlugIn {
	private ActionServlet servlet = null;

	private static final String serviceClassName = "com.coco.service.COCOServiceImpl";
	public static final String cocoFactoryName = "com.coco.service.ICOCOServiceFactory";

	public ICOCOService createService() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		ICOCOService instance = (ICOCOService) Class.forName(serviceClassName).newInstance();

		instance.setServletContext(servlet.getServletContext());

		return instance;
	}

	public void init(ActionServlet servlet, ModuleConfig config)
			throws ServletException {
		// Guardar copia del servlet
		this.servlet = servlet;

		// Guardar copia de la factoria en el contexto
		servlet.getServletContext().setAttribute(cocoFactoryName, this);
	}

	public void destroy() {
		// nada
	}
}
