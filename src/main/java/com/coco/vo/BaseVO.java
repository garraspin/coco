package com.coco.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;

public class BaseVO implements java.io.Serializable {
	private static final long serialVersionUID = 8460331943851988615L;
	private int id;
	private Timestamp timeCreated = null;
	private String description;
	private String name;

	public BaseVO() {
		this.id = -1;
		this.name = "";
		this.description = "";
		setTimeCreated(new Timestamp(System.currentTimeMillis()));
	}

	public BaseVO(int id, String name, String desc) {
		this();

		this.id = id;
		this.name = name;
		this.description = desc;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTimeCreated(Timestamp now) {
		timeCreated = now;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public Timestamp getTimeCreated() {
		return timeCreated;
	}

	public String toString() {
		return getClass().toString() + ": " + id + ", " + name + ", " + description;
	}
	
	public static class NameComparator implements Comparator<BaseVO>, Serializable {
        public int compare(BaseVO o1, BaseVO o2) {
			return o1.getName().compareTo(o2.getName());
		}
	}
}