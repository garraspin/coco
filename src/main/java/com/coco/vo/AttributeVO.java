package com.coco.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

public class AttributeVO extends BaseVO {
	private static final long serialVersionUID = 7937812194230225447L;
	// ID, name y description heredados de la clase BaseVO
	private double optima;
	private int rankRule;

	public AttributeVO() {
		this.optima = 0;
		this.rankRule = 0;
	}

	public AttributeVO(int id, String name, String desc, double optima, int rankRule) {
		super(id, name, desc);

		this.optima = optima;
		this.rankRule = rankRule;
	}

	public static List<AttributeVO> getNewAttributeList() {
		Factory factory = new Factory() {
			public Object create() {
				return new AttributeVO();
			}
		};
		return LazyList.decorate(new ArrayList<AttributeVO>(), factory);
	}

	public double getOptima() {
		return optima;
	}

	public void setOptima(double optima) {
		this.optima = optima;
	}

	public int getRankRule() {
		return rankRule;
	}

	public void setRankRule(int rankRule) {
		this.rankRule = rankRule;
	}
}