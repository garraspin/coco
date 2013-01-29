package com.coco.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

public class ElementVO extends BaseVO {
	private static final long serialVersionUID = 2777959842275364599L;
	// ID, name and description heredados de la clase BaseVO
	private double yvalue;
	// lista de celdas (value, rank pos, ideal_value)
	private List<CellVO> cells;

	public ElementVO() {
		this.yvalue = 0;
		this.cells = CellVO.getNewCellList();
	}

	public ElementVO(Integer id, String name, String desc, double yvalue) {
		super(id, name, desc);
		this.yvalue = yvalue;
		this.cells = CellVO.getNewCellList();
	}

	public static List<ElementVO> getNewElementList() {
		Factory factory = new Factory() {
			public Object create() {
				return new ElementVO();
			}
		};
		return LazyList.decorate(new ArrayList<ElementVO>(), factory);
	}

	public double getYvalue() {
		return yvalue;
	}

	public void setYvalue(double value) {
		yvalue = value;
	}

	public List<CellVO> getCells() {
		return cells;
	}

	public void setCells(List<CellVO> cells) {
		this.cells = cells;
	}

	public int getTotalRanking() {
		int total = 0;
		for (CellVO cell : cells) {
			total += cell.getRanking();
		}
		return total;
	}

	public double getTotalIdealValue() {
		double total = 0;
		for (CellVO cell : cells) {
			total += cell.getIdealValue();
		}
		return total;
	}
}
