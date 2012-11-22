package com.coco.service;

public interface ICOCOServiceFactory {
	public ICOCOService createService() throws ClassNotFoundException,
			IllegalAccessException, InstantiationException;

	public void destroy();
}
