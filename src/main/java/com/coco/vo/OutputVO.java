package com.coco.vo;

import java.math.BigDecimal;
import java.util.List;

import com.coco.MathUtils;

public class OutputVO extends BaseVO {

	// ID, name y description heredados de la clase BaseVO

	private static final long serialVersionUID = 3233797274725014009L;
	// solución
	private BigDecimal solution;
	// Lista de los valores ideales
	private BigDecimal[] idealYValues = null;
	// Objetos con ranking igual a 1
	private int[] bestObjects;
	// Lista de importancia (media aritmética)
	private BigDecimal[] importanceObjects = null;
	// Lista de sensibilidad (desviación estándar)
	private BigDecimal[] sensitivityObjects = null;

	public OutputVO() {
		super(-1, "", "");

		solution = new BigDecimal(0.0);
		idealYValues = null;
		bestObjects = null;
		importanceObjects = null;
		sensitivityObjects = null;
	}

	public OutputVO(int id, String name, String desc) {
		super(id, name, desc);

		solution = new BigDecimal(0.0);
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

	public BigDecimal[] getIdealYValues() {
		return idealYValues;
	}

	public void setIdealYValues(BigDecimal[] idealYValues) {
		this.idealYValues = idealYValues;
	}

	public BigDecimal[] getImportanceObjects() {
		return importanceObjects;
	}

	public void setImportanceObjects(BigDecimal[] importanceObjects) {
		this.importanceObjects = importanceObjects;
	}

	public BigDecimal[] getSensitivityObjects() {
		return sensitivityObjects;
	}

	public void setSensitivityObjects(BigDecimal[] sensitivityObjects) {
		this.sensitivityObjects = sensitivityObjects;
	}

	public BigDecimal getSolution() {
		return solution;
	}

	public void setSolution(BigDecimal solution) {
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

	public BigDecimal[] calculateImportanceObjects(InputVO inCOCO) {
		BigDecimal[] importanceList = new BigDecimal[inCOCO.getAttributes().size()];

		for (int i = 0; i < inCOCO.getAttributes().size(); i++) {
			importanceList[i] = MathUtils.average(inCOCO.getAttributeCells(i));
		}
		return importanceList;
	}

	public BigDecimal[] calculateSensitivityObjects(InputVO inCOCO) {
		BigDecimal[] sensitivityList = new BigDecimal[inCOCO.getAttributes().size()];

		for (int i = 0; i < inCOCO.getAttributes().size(); i++) {
			sensitivityList[i] = MathUtils.deviation(inCOCO.getAttributeCells(i));
		}
		return sensitivityList;
	}

	public BigDecimal[] calculateIdealYValues(List<ElementVO> elements) {
		// Devolver null si no hay elementos o celdas
		if (elements == null || elements.get(0).getCells().isEmpty()) {
			return null;
		}

		BigDecimal[] idealYValues = new BigDecimal[elements.size()];
		int i = 0;
		for (ElementVO elto : elements) {
			BigDecimal idealYValue = new BigDecimal(0.0);

			// Añadir todas las celdas para calcular el valor ideal
			for (CellVO cell : elto.getCells()) {
                idealYValue.add(cell.getIdealValue());
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
			BigDecimal average = MathUtils.average(lAttributeCells);

			for (CellVO cell : lAttributeCells) {
                cell.setIdealValue(
                        cell.getValue().subtract((cell.getValue().subtract(average)).divide(new BigDecimal(2))));
			}
		}

		setBestObjects(calculateBestObjects(inCOCO));
		setIdealYValues(calculateIdealYValues(inCOCO.getElements()));
		setImportanceObjects(calculateImportanceObjects(inCOCO));
		setSensitivityObjects(calculateSensitivityObjects(inCOCO));

		for (int i = 0; i < inCOCO.getAttributes().size(); i++) {
			BigDecimal average = MathUtils.average(inCOCO.getAttributeCells(i));

			for (CellVO cell : inCOCO.getAttributeCells(i)) {
                cell.setIdealValue(
                        cell.getValue().subtract((cell.getValue().subtract(average)).divide(new BigDecimal(2))));
			}
		}

		BigDecimal solution = new BigDecimal(0);
		for (ElementVO elto : inCOCO.getElements()) {
			BigDecimal idealYValue = new BigDecimal(0);
			for (CellVO cell : elto.getCells()) {
                idealYValue = idealYValue.add(cell.getIdealValue());
			}
			solution = solution.add(elto.getYvalue().subtract(idealYValue).pow(2));
		}

		this.solution = solution.multiply(solution);
	}
}