package com.coco.service;

import java.util.List;

import javax.servlet.ServletContext;

import com.coco.vo.InputVO;
import com.coco.vo.OutputVO;
import com.coco.vo.UserVO;
import com.coco.vo.BaseVO;

public interface ICOCOService {
	public List<BaseVO> getListCOCOProblems(int idUser);

	public InputVO getCOCOInput(String itemId);

	public OutputVO getCOCOOutput(String itemId);

	public void setCOCOInput(InputVO inCOCO, int idUser);

	public void setCOCOOutput(OutputVO outCOCO, InputVO inCOCO);

	public void deleteCOCOInput(String idCOCO);

	public List<BaseVO> getRankRules();

	public List<BaseVO> getFunctions();

	public void setServletContext(ServletContext ctx);

	public UserVO authenticate(String email, String password);

	public void setUser(UserVO user);

	public UserVO getUser(String email, String password);

	public void logout(String email);

	public void destroy();
}