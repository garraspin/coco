package com.coco.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

public class CellVO extends BaseVO {
	private static final long serialVersionUID = 8615998222221215386L;
	// ID, name y description heredados de la clase BaseVO
	private double value;
	private double idealValue;
	private int ranking;

	public CellVO() {
		this.value = 0;
		this.idealValue = 0;
		this.ranking = 0;
	}

	public CellVO(int id, String name, String desc, double value, double idealValue, int ranking) {
		super(id, name, desc);

		this.value = value;
		this.idealValue = idealValue;
		this.ranking = ranking;
	}

	public static List<CellVO> getNewCellList() {
		Factory factory = new Factory() {
			public Object create() {
				return new CellVO();
			}
		};
		return LazyList.decorate(new ArrayList<CellVO>(), factory);
	}

	public double getIdealValue() {
		return idealValue;
	}

	public void setIdealValue(double idealValue) {
		this.idealValue = idealValue;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public static class MinimumComparator implements Comparator<CellVO>, Serializable {
		@Override
        public int compare(CellVO o1, CellVO o2) {
			return new Double(o2.value - o1.value).intValue();
		}
	}

	public static class AverageComparator implements Comparator<CellVO>, Serializable {
		private final double average;
        @Override
		public int compare(CellVO o1, CellVO o2) {
			double diff1 = o1.value - average;
			double diff2 = o2.value - average;
			return new Double(diff1 - diff2).intValue();
		}

		public AverageComparator(double average) {
			this.average = average;
		}
	}

	public static class MaximumComparator implements Comparator<CellVO>, Serializable {
		public int compare(CellVO o1, CellVO o2) {
			return new Double(o1.value - o2.value).intValue();
		}
	}
}