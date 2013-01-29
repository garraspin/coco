package com.coco;

import java.util.List;

import com.coco.vo.CellVO;

public class MathUtils {
	public static double average(List<CellVO> lCells) {
		double sum = 0;
		for (CellVO cell : lCells) {
			sum += cell.getValue();
		}
        return lCells.isEmpty() ? 0 : sum / lCells.size();
	}

	public static double deviation(List<CellVO> lCells) {
		double deviation = 0;
		for (CellVO cell : lCells) {
            double subtract = cell.getValue() - average(lCells);
            deviation += subtract * subtract;
		}
        double divide = deviation / lCells.size();
        return lCells.isEmpty() ? 0 : Math.sqrt(divide);
	}
}