package com.coco.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

public class CellVO extends BaseVO {
	private static final long serialVersionUID = 8615998222221215386L;
	// ID, name y description heredados de la clase BaseVO
	private BigDecimal value;
	private BigDecimal idealValue;
	private int ranking;

	public CellVO() {
		this.value = new BigDecimal(0);
		this.idealValue = new BigDecimal(0);
		this.ranking = 0;
	}

	public CellVO(int id, String name, String desc, BigDecimal value, BigDecimal idealValue, int ranking) {
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

	public BigDecimal getIdealValue() {
		return idealValue;
	}

	public void setIdealValue(BigDecimal idealValue) {
		this.idealValue = idealValue;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public static class MinimumComparator implements Comparator<CellVO>, Serializable {
		public int compare(CellVO o1, CellVO o2) {
			return o2.value.subtract(o1.value).intValue();
		}
	}

	public static class AverageComparator implements Comparator<CellVO>, Serializable {
		private final BigDecimal average;

		public int compare(CellVO o1, CellVO o2) {
			BigDecimal diff1 = o1.value.subtract(average).abs();
			BigDecimal diff2 = o2.value.subtract(average).abs();
			return diff1.subtract(diff2).intValue();
		}

		public AverageComparator(BigDecimal average) {
			this.average = average;
		}
	}

	public static class MaximumComparator implements Comparator<CellVO>, Serializable {
		public int compare(CellVO o1, CellVO o2) {
			return o1.value.subtract(o2.value).intValue();
		}
	}
}