package com.coco.struts;

import com.coco.vo.InputVO;
import com.coco.vo.OutputVO;

public class COCOProblem {
	private InputVO inCOCO;
	private OutputVO outCOCO;

	public COCOProblem() {
		inCOCO = new InputVO();
		outCOCO = new OutputVO();
	}

	public COCOProblem(InputVO in) {
		inCOCO = in;
		outCOCO = new OutputVO();
	}

	public InputVO getInCOCO() {
		return inCOCO;
	}

	public void setInCOCO(InputVO inCOCO) {
		this.inCOCO = inCOCO;
	}

	public OutputVO getOutCOCO() {
		return outCOCO;
	}

	public void setOutCOCO(OutputVO outCOCO) {
		this.outCOCO = outCOCO;
	}

	public void calculateOutput() {
		this.inCOCO.calculateRankings();
		setOutCOCO(new OutputVO(this.inCOCO));
	}
}