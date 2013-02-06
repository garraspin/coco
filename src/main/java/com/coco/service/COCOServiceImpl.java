package com.coco.service;

import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletContext;

import com.coco.database.CustomDatabase;
import com.coco.vo.BaseVO;
import com.coco.vo.InputVO;
import com.coco.vo.OutputVO;
import com.coco.vo.UserVO;

public class COCOServiceImpl implements ICOCOService {

	ServletContext servletContext = null;

	// Objeto para la conexión a la base de datos
	CustomDatabase database = null;

	public COCOServiceImpl() throws NamingException {
		// Inicializar la base de datos
		database = new CustomDatabase();
	}

	public void setServletContext(ServletContext ctx) {
		this.servletContext = ctx;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public List<BaseVO> getListCOCOProblems(int idUser) {
		return database.getListCOCOProblems(idUser);
	}

	public InputVO getCOCOInput(String idCOCO) {
		return database.getCOCOInput(Integer.valueOf(idCOCO));
	}

	public OutputVO getCOCOOutput(String idCOCO) {
		return new OutputVO(getCOCOInput(idCOCO));
	}

	public void setCOCOInput(InputVO inCOCO, int idUser) {
		if (inCOCO.getId() == -1) {
			database.createCOCOInput(inCOCO, idUser);
		} else {
			database.updateCOCOInput(inCOCO, idUser);
		}
	}

	public void setCOCOOutput(OutputVO outCOCO, InputVO inCOCO) {
		database.setCOCOOutput(outCOCO, inCOCO);
	}

	public void deleteCOCOInput(String idCOCO) {
		database.deleteCOCOInput(getCOCOInput(idCOCO));
	}

	public List<BaseVO> getFunctions() {
		return database.getFunctions();
	}

	public List<BaseVO> getRankRules() {
		return database.getRankRules();
	}

	public UserVO authenticate(String email, String password) {
		return database.authenticate(email, password);
	}

	public void logout(String email) {
		// nada
	}

	public void destroy() {
        // nada
	}

	public void saveUser(UserVO user) {
		database.saveUser(user);
	}

	public UserVO getUser(String email, String password) {
		return database.getUser(email, password);
	}
}