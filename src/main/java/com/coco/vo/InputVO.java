package com.coco.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.coco.MathUtils;

public class InputVO extends BaseVO {

	// ID, name y description heredados de la clase BaseVO

	private static final long serialVersionUID = -5907310327893063524L;
	// Función utilizada para obtener la solución
	private int idFunction;
	// Permitir valor negativo en la solución
	private boolean negativeAllowed;
	// Porcentaje para el intervalo del equilibro
	private int equilibrium;
	// Lista de elementos (element_name, y_value y CellVO[])
	private List<ElementVO> elements;
	// Lista de atributos (idAttribute, NameAttribute, idRankRule, optima, RankRuleName)
	private List<AttributeVO> attributes;
	// Nombre del Y Attribute
	private String attributeY;

	public InputVO() {
		super(-1, "", "");
		this.idFunction = 0;
		this.negativeAllowed = false;
		this.equilibrium = 0;
		this.attributeY = "";
		this.elements = ElementVO.getNewElementList();
		this.attributes = AttributeVO.getNewAttributeList();
	}

	public InputVO(int id, String name, String desc, int idFunction, boolean negativeAllowed, int equilibrium, String yAttribute) {
		super(id, name, desc);

		this.idFunction = idFunction;
		this.negativeAllowed = negativeAllowed;
		this.equilibrium = equilibrium;
		this.attributeY = yAttribute;
		this.elements = ElementVO.getNewElementList();
		this.attributes = AttributeVO.getNewAttributeList();
	}

	public int getEquilibrium() {
		return equilibrium;
	}

	public void setEquilibrium(int equilibrium) {
		this.equilibrium = equilibrium;
	}

	public int getIdFunction() {
		return idFunction;
	}

	public void setIdFunction(int idFunction) {
		this.idFunction = idFunction;
	}

	public boolean getNegativeAllowed() {
		return negativeAllowed;
	}

	public void setNegativeAllowed(boolean negativeAllowed) {
		this.negativeAllowed = negativeAllowed;
	}

	public String getAttributeY() {
		return attributeY;
	}

	public void setAttributeY(String attributeY) {
		this.attributeY = attributeY;
	}

	public List<AttributeVO> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeVO> attributes) {
		this.attributes = attributes;
	}

	public List<ElementVO> getElements() {
		return elements;
	}

	public void setElements(List<ElementVO> elements) {
		this.elements = elements;
	}

	public void addEmptyAttributes(int amount) {
		for (int i = 0; i < amount; i++) {
			addAttribute();
		}
	}
	private void addAttribute() {
		int nCols = attributes.size();

		// Añadir celdas
		for (ElementVO elto : elements) {
			List<CellVO> cells = elto.getCells();
			int i = elements.indexOf(elto);
			cells.add(new CellVO(-1, i + "_" + nCols, "", new BigDecimal(0), new BigDecimal(0), 0));
		}
		// Añadir atributos
		attributes.add(new AttributeVO(-1, "Attribute " + nCols, "", new BigDecimal(0), -1));
	}

	public void removeAttribute(int amount) {
		for (int i = 0; i < amount; i++) {
			removeAttribute();
		}
	}
	private void removeAttribute() {
		int nCols = attributes.size();

		// Borrar celdas
		for (ElementVO elto : elements) {
			List<CellVO> cells = elto.getCells();
			cells.remove(nCols - 1);
		}
		// Borrar atributos
		attributes.remove(nCols - 1);
	}

	public void addEmptyElements(int amount) {
		for (int i = 0; i < amount; i++) {
			addElement();
		}
	}
	
	private void addElement() {
		int nRows = elements.size();
		int nCols = attributes.size();

		// Añadir elemento
		ElementVO elto = new ElementVO(-1, "Element " + nRows, "", new BigDecimal(0));

		List<CellVO> cells = CellVO.getNewCellList();
		for (int j = 0; j < nCols; j++) {
			cells.add(new CellVO(-1, nRows + "_" + j, "", new BigDecimal(0), new BigDecimal(0), 0));
		}
		elto.setCells(cells);
		elements.add(elto);
	}

	public void removeElement(int amount) {
		for (int i = 0; i < amount; i++) {
			removeElement();
		}
	}
	
	private void removeElement() {
		if (!elements.isEmpty())
			elements.remove(elements.size() - 1);
	}

	public List<CellVO> getAttributeCells(int index) {
		// Devolver null si no hay elementos o celdas
		if ((elements == null) || 
				(elements.get(0).getCells() == null) ||
				(elements.get(0).getCells().size() < index)) {
			return null;
		}
		List<CellVO> lAttributes = new ArrayList<CellVO>();
		for (ElementVO elto : elements) {
			lAttributes.add(elto.getCells().get(index));
		}
		return lAttributes;
	}

	public List<CellVO> getElementCells(int index) {
		// Devolver null si no hay elementos o celdas
		if ((elements == null) || (elements.get(index) == null)) {
			return null;
		}
		return elements.get(index).getCells();
	}

	public void calculateRankings() {
		for (int i = 0; i < attributes.size(); i++) {
			List<CellVO> lCells = getAttributeCells(i);
			CellVO[] cells = lCells.toArray(new CellVO[lCells.size()]);
			Comparator<CellVO> comparator = null;

			switch (attributes.get(i).getRankRule()) {
			case 2:
				comparator = new CellVO.MinimumComparator();
				break;
			case 1:
				comparator = new CellVO.MaximumComparator();
				break;
			case 3:
				BigDecimal average = MathUtils.average(lCells);
				comparator = new CellVO.AverageComparator(average);
				break;
			}
			Arrays.sort(cells, comparator);

			for (CellVO cell : lCells) {
				cell.setRanking(Arrays.binarySearch(cells, cell, comparator));
			}
		}
	}
}