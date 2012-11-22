package com.coco.struts;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import com.coco.vo.BaseVO;

public class ListCOCOProblems {
	// Lista con los problemas del usuario
	private List<BaseVO> problems;

	// Problema actual (en uso)
	private COCOProblem actualCOCO;

	public ListCOCOProblems() {
		this.problems = new ArrayList<BaseVO>();
		this.actualCOCO = new COCOProblem();
	}

	public List<BaseVO> getProblems() {
		return problems;
	}

	public void setProblems(List<BaseVO> problems) {
		Collections.sort(problems, new BaseVO.NameComparator());
		this.problems = problems;
	}

	public COCOProblem getActualCOCO() {
		return actualCOCO;
	}

	public void setActualCOCO(COCOProblem actualCOCO) {
		this.actualCOCO = actualCOCO;
	}

	public void empty() {
		problems.clear();
		actualCOCO = null;
	}

	public void removeProblem(String idCOCO) {
		BaseVO problem = findProblem(idCOCO);

		if (problem != null) {
			if (problem.getId() == actualCOCO.getInCOCO().getId()) {
				actualCOCO = null;
			}
			problems.remove(problem);
		}
	}

	public int getSize() {
		return problems.size();
	}

	public void setSize(int size) {
		// size se calcula con problems.size()
	}

	public BaseVO findProblem(String idCOCO) {
		for (BaseVO problem : problems) {
			if (problem.getId() == Integer.parseInt(idCOCO)) {
				return problem;
			}
		}

		return null;
	}

	public void removeProblems(List<String> idCOCOs) {
		for (String id : idCOCOs) {
			removeProblem(id);
		}
	}
}