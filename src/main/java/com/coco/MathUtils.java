package com.coco;

import java.util.List;

import com.coco.vo.CellVO;

public class MathUtils {
	public static double average(List<CellVO> lCells) {
		double sum = 0.0;
		for (CellVO cell : lCells) {
			sum += cell.getValue();
		}
		return lCells.isEmpty() ? 0 : sum / lCells.size();
	}

	public static double deviation(List<CellVO> lCells) {
		double deviation = 0.0;
		int index = 0;
		for (CellVO cell : lCells) {
			deviation += java.lang.Math.pow(cell.getValue() - average(lCells), 2);
			index++;
		}
		return lCells.isEmpty() ? 0 : java.lang.Math.sqrt(deviation / lCells.size());
	}
}