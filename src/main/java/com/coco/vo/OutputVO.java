package com.coco.vo;

import java.util.List;

import com.coco.MathUtils;

public class OutputVO extends BaseVO {

	// ID, name y description heredados de la clase BaseVO

	private static final long serialVersionUID = 3233797274725014009L;
	// solución
	private double solution;
	// Lista de los valores ideales
	private Double[] idealYValues = null;
	// Objetos con ranking igual a 1
	private int[] bestObjects;
	// Lista de importancia (media aritmética)
	private Double[] importanceObjects = null;
	// Lista de sensibilidad (desviación estándar)
	private Double[] sensitivityObjects = null;

	public OutputVO() {
		super(-1, "", "");

		solution = 0.0;
		idealYValues = null;
		bestObjects = null;
		importanceObjects = null;
		sensitivityObjects = null;
	}

	public OutputVO(int id, String name, String desc) {
		super(id, name, desc);

		solution = 0.0;
		idealYValues = null;
		bestObjects = null;
		importanceObjects = null;
		sensitivityObjects = null;
	}

	public OutputVO(InputVO inCOCO) {
		super(inCOCO.getId(), inCOCO.getName(), inCOCO.getDescription());

		calculateSolution(inCOCO);
	}

	public int[] getBestObjects() {
		return bestObjects;
	}

	public void setBestObjects(int[] bestObjects) {
		this.bestObjects = bestObjects;
	}

	public Double[] getIdealYValues() {
		return idealYValues;
	}

	public void setIdealYValues(Double[] idealYValues) {
		this.idealYValues = idealYValues;
	}

	public Double[] getImportanceObjects() {
		return importanceObjects;
	}

	public void setImportanceObjects(Double[] importanceObjects) {
		this.importanceObjects = importanceObjects;
	}

	public Double[] getSensitivityObjects() {
		return sensitivityObjects;
	}

	public void setSensitivityObjects(Double[] sensitivityObjects) {
		this.sensitivityObjects = sensitivityObjects;
	}

	public double getSolution() {
		return solution;
	}

	public void setSolution(double solution) {
		this.solution = solution;
	}

	public int[] calculateBestObjects(InputVO inCOCO) {
		int[] bestObjects = new int[inCOCO.getAttributes().size()];

		for (int i = 0; i < inCOCO.getAttributes().size(); i++) {
			List<CellVO> attributeCells = inCOCO.getAttributeCells(i);

			for (int elementIndex = 0; elementIndex < attributeCells.size(); elementIndex++) {
				CellVO cell = attributeCells.get(elementIndex);

				if (cell.getRanking() == 1) {
					bestObjects[i] = elementIndex;
					break;
				}
			}
		}
		return bestObjects;
	}

	public Double[] calculateImportanceObjects(InputVO inCOCO) {
		Double[] importanceList = new Double[inCOCO.getAttributes().size()];

		for (int i = 0; i < inCOCO.getAttributes().size(); i++) {
			importanceList[i] = MathUtils.average(inCOCO.getAttributeCells(i));
		}
		return importanceList;
	}

	public Double[] calculateSensitivityObjects(InputVO inCOCO) {
		Double[] sensivityList = new Double[inCOCO.getAttributes().size()];

		for (int i = 0; i < inCOCO.getAttributes().size(); i++) {
			sensivityList[i] = MathUtils.deviation(inCOCO.getAttributeCells(i));
		}
		return sensivityList;
	}

	public Double[] calculateIdealYValues(List<ElementVO> elements) {
		// Devolver null si no hay elementos o celdas
		if (elements == null || elements.get(0).getCells().isEmpty()) {
			return null;
		}

		Double[] idealYValues = new Double[elements.size()];
		int i = 0;
		for (ElementVO elto : elements) {
			double idealYValue = 0.0;

			// Añadir todas las celdas para calcular el valor ideal
			for (CellVO cell : elto.getCells()) {
				double idealXValue = cell.getIdealValue();
				idealYValue += idealXValue;
			}
			// Añadir el valor ideal a los resultados
			idealYValues[i] = idealYValue;
			i++;
		}
		return idealYValues;
	}

	public void calculateSolution(InputVO inCOCO) {
		// Calcular valores ideales
		for (int i = 0; i < inCOCO.getAttributes().size(); i++) {
			List<CellVO> lAttributeCells = inCOCO.getAttributeCells(i);
			double average = MathUtils.average(lAttributeCells);

			for (CellVO cell : lAttributeCells) {
				double idealValue = cell.getValue()
						- ((cell.getValue() - average) / 2);
				cell.setIdealValue(idealValue);
			}
		}

		setBestObjects(calculateBestObjects(inCOCO));
		setIdealYValues(calculateIdealYValues(inCOCO.getElements()));
		setImportanceObjects(calculateImportanceObjects(inCOCO));
		setSensitivityObjects(calculateSensitivityObjects(inCOCO));

		for (int i = 0; i < inCOCO.getAttributes().size(); i++) {
			double average = MathUtils.average(inCOCO.getAttributeCells(i));

			for (CellVO cell : inCOCO.getAttributeCells(i)) {
				double idealValue = cell.getValue() - ((cell.getValue() - average) / 2);
				cell.setIdealValue(idealValue);
			}
		}

		double solution = 0.0;
		for (ElementVO elto : inCOCO.getElements()) {
			double idealYValue = 0.0;
			for (CellVO cell : elto.getCells()) {
				idealYValue += cell.getIdealValue();
			}
			solution += Math.pow((elto.getYvalue() - idealYValue), 2);
		}

		this.solution = Math.sqrt(solution);
	}
}