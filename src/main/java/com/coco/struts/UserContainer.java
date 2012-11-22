package com.coco.struts;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.List;
import java.util.ArrayList;

import com.coco.vo.BaseVO;
import com.coco.vo.UserVO;

// Guarda los datos de usuario en la sesión
// Implementa el interfaz HttpSessionBindingListener para liberar memoria cuando termine la sesión
public class UserContainer implements HttpSessionBindingListener {
	// Lista de problemas
	private ListCOCOProblems listCOCO = null;
	// Datos del usuario
	private UserVO userVO = null;
	// Lista de rankRules
	private List<BaseVO> rankRulesList = null;
	// Lista de funciones
	private List<BaseVO> functionsList = null;

	public UserContainer() {
		super();
		initialize();
	}

	public ListCOCOProblems getListCOCO() {
		return listCOCO;
	}

	public void setListCOCO(ListCOCOProblems listCOCO) {
		this.listCOCO = listCOCO;
	}

	public List<BaseVO> getFunctionsList() {
		return functionsList;
	}

	public void setFunctionsList(List<BaseVO> functionsList) {
		this.functionsList = functionsList;
	}

	public List<BaseVO> getRankRulesList() {
		return rankRulesList;
	}

	public void setRankRulesList(List<BaseVO> rankRulesList) {
		this.rankRulesList = rankRulesList;
	}

	// El container llama a esta función cuando se termina la sesión
	public void valueUnbound(HttpSessionBindingEvent event) {
		System.out.println("Freeing the session...");
		cleanUp();
	}

	// El container llama a esta función cuando se genera la sesión
	public void valueBound(HttpSessionBindingEvent event) {
		// nada
	}

	public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO newVO) {
		this.userVO = newVO;
	}

	// Inicialización de los datos del usuario en la sesión
	private void initialize() {
		listCOCO = new ListCOCOProblems();
		functionsList = new ArrayList<BaseVO>();
		rankRulesList = new ArrayList<BaseVO>();
	}

	// Liberar la sesión
	public void cleanUp() {
		setUserVO(null);
		listCOCO = new ListCOCOProblems();
		functionsList = new ArrayList<BaseVO>();
		rankRulesList = new ArrayList<BaseVO>();
	}
}