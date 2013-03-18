package com.coco.vo;

import static java.lang.Math.sqrt;

import java.util.List;

import com.coco.MathUtils;

public class OutputVO extends BaseVO {

	// ID, name y description heredados de la clase BaseVO

	private static final long serialVersionUID = 3233797274725014009L;
	// solución
	private double solution;
	// Lista de los valores ideales
	private double[] idealYValues = null;
	// Objetos con ranking igual a 1
	private int[] bestObjects;
	// Lista de importancia (media aritmética)
	private double[] importanceObjects = null;
	// Lista de sensibilidad (desviación estándar)
	private double[] sensitivityObjects = null;

	public OutputVO() {
		super(-1, "", "");

		solution = 0;
		idealYValues = null;
		bestObjects = null;
		importanceObjects = null;
		sensitivityObjects = null;
	}

	public OutputVO(int id, String name, String desc) {
		super(id, name, desc);

		solution = 0;
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

	public double[] getIdealYValues() {
		return idealYValues;
	}

	public void setIdealYValues(double[] idealYValues) {
		this.idealYValues = idealYValues;
	}

	public double[] getImportanceObjects() {
		return importanceObjects;
	}

	public void setImportanceObjects(double[] importanceObjects) {
		this.importanceObjects = importanceObjects;
	}

	public double[] getSensitivityObjects() {
		return sensitivityObjects;
	}

	public void setSensitivityObjects(double[] sensitivityObjects) {
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

	public double[] calculateImportanceObjects(InputVO inCOCO) {
        double[] importanceList = new double[inCOCO.getAttributes().size()];

		for (int i = 0; i < inCOCO.getAttributes().size(); i++) {
			importanceList[i] = MathUtils.average(inCOCO.getAttributeCells(i));
		}
		return importanceList;
	}

	public double[] calculateSensitivityObjects(InputVO inCOCO) {
        double[] sensitivityList = new double[inCOCO.getAttributes().size()];

		for (int i = 0; i < inCOCO.getAttributes().size(); i++) {
			sensitivityList[i] = MathUtils.deviation(inCOCO.getAttributeCells(i));
		}
		return sensitivityList;
	}

	public double[] calculateIdealYValues(List<ElementVO> elements) {
		// Devolver null si no hay elementos o celdas
		if (elements == null || elements.get(0).getCells().isEmpty()) {
			return null;
		}

        double[] idealYValues = new double[elements.size()];
		int i = 0;
		for (ElementVO elto : elements) {
            double idealYValue = 0;

			// Añadir todas las celdas para calcular el valor ideal
			for (CellVO cell : elto.getCells()) {
                idealYValue += cell.getIdealValue();
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
                cell.setIdealValue(cell.getValue() - ((cell.getValue() - average) / 2));
			}
		}

		setBestObjects(calculateBestObjects(inCOCO));
//		setIdealYValues(calculateIdealYValues(inCOCO.getElements()));
		setImportanceObjects(calculateImportanceObjects(inCOCO));
		setSensitivityObjects(calculateSensitivityObjects(inCOCO));

		for (int i = 0; i < inCOCO.getAttributes().size(); i++) {
			double average = MathUtils.average(inCOCO.getAttributeCells(i));

			for (CellVO cell : inCOCO.getAttributeCells(i)) {
                cell.setIdealValue(cell.getValue() - ((cell.getValue() - average) / 2));
			}
		}

        double solution = 0;
		for (ElementVO elto : inCOCO.getElements()) {
            double idealYValue = 0;
			for (CellVO cell : elto.getCells()) {
                idealYValue +=cell.getIdealValue();
			}
            double diff = elto.getYvalue() - idealYValue;
            solution += diff * diff;
		}

		this.solution = sqrt(solution * solution);
	}
}