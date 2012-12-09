package com.coco;

import java.math.BigDecimal;
import java.util.List;

import com.coco.vo.CellVO;

public class MathUtils {
	public static BigDecimal average(List<CellVO> lCells) {
		BigDecimal sum = new BigDecimal(0);
		for (CellVO cell : lCells) {
			sum = sum.add(cell.getValue());
		}
        return lCells.isEmpty() ? new BigDecimal(0) : sum.divide(BigDecimal.valueOf(lCells.size()));
	}

	public static BigDecimal deviation(List<CellVO> lCells) {
		BigDecimal deviation = new BigDecimal(0);
		for (CellVO cell : lCells) {
            BigDecimal subtract = cell.getValue().subtract(average(lCells));
            deviation = deviation.add(subtract.multiply(subtract));
		}
        BigDecimal divide = deviation.divide(new BigDecimal(lCells.size()));
        return lCells.isEmpty() ? new BigDecimal(0) : new BigDecimal(Math.sqrt(divide.doubleValue()));
	}
}