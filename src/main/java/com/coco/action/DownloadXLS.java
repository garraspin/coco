package com.coco.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coco.struts.ActionUtils;
import com.coco.struts.UserContainer;
import com.coco.vo.AttributeVO;
import com.coco.vo.CellVO;
import com.coco.vo.ElementVO;
import com.coco.vo.InputVO;
import com.coco.vo.OutputVO;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

public class DownloadXLS extends Action {

    private final ActionUtils actionUtils = new ActionUtils();

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=sampleName.xls");

		UserContainer existingContainer = actionUtils.getUserContainer(request);
		MessageResources messageResources = getResources(request);

		InputVO inCOCO = existingContainer.getListCOCO().getActualCOCO().getInCOCO();
		OutputVO outCOCO = existingContainer.getListCOCO().getActualCOCO().getOutCOCO();

		WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
		try {
            addInputSheet(w, existingContainer, messageResources, inCOCO, outCOCO);
            addOutputSheet(w, messageResources, inCOCO, outCOCO);

			w.write();
			w.close();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
		return mapping.findForward("outputPage");
	}

    private void addInputSheet(WritableWorkbook w, UserContainer existingContainer, MessageResources messageResources,
                               InputVO inCOCO, OutputVO outCOCO
    ) throws WriteException {
        int numAttributes = inCOCO.getAttributes().size();
        int numElements = inCOCO.getElements().size();

        WritableSheet s = w.createSheet("Inputs", 0);
        // title
        s.addCell(new Label(0, 0, messageResources.getMessage("coco.title")));
        // coco problem name and description
        s.addCell(new Label(0, 1, messageResources.getMessage("coco.name")));
        s.addCell(new Label(1, 1, inCOCO.getName()));
        s.addCell(new Label(0, 2, messageResources.getMessage("coco.description")));
        s.addCell(new Label(1, 2, inCOCO.getDescription()));
        // id function, negative allowed and equilibrium
        s.addCell(new Label(0, 3, messageResources.getMessage("coco.functionTitle")));
        s.addCell(new Label(1, 3, existingContainer.getFunctionsList().get(inCOCO.getIdFunction() - 1).getName()));
        s.addCell(new Label(0, 4, messageResources.getMessage("coco.negativeAllowed")));
        s.addCell(new Label(1, 4, String.valueOf(inCOCO.getNegativeAllowed())));
        s.addCell(new Label(0, 5, messageResources.getMessage("coco.equilibrium")));
        s.addCell(new Label(1, 5, String.valueOf(inCOCO.getEquilibrium())));
        // matrix title
        s.addCell(new Label(0, 7, messageResources.getMessage("coco.matrix.initial")));
        // elementsAttributeTitle , attribute names , YAttribute name
        s.addCell(new Label(0, 8, messageResources.getMessage("coco.elementAttributeTitle")));
        for (int i = 0; i < numAttributes; i++) {
            AttributeVO attribute = inCOCO.getAttributes().get(i);
            s.addCell(new Label(i + 1, 8, attribute.getName()));
        }
        s.addCell(new Label(1 + numAttributes, 8, inCOCO.getAttributeY()));
        // Element names , elementAttributesValues and YValues
        for (int i = 0; i < numElements; i++) {
            ElementVO elto = inCOCO.getElements().get(i);
            s.addCell(new Label(0, 9 + i, elto.getName()));

            for (int j = 0; j < elto.getCells().size(); j++) {
                CellVO cell = elto.getCells().get(j);
                s.addCell(new Label(1 + j, 9 + i, String.valueOf(cell.getValue())));
            }
            s.addCell(new Label(1 + elto.getCells().size(), 9 + i, String.valueOf(outCOCO.getIdealYValues()[i])));
        }
        // rankRules and Optimal values
        s.addCell(new Label(0, 14, messageResources.getMessage("coco.optimalTitle")));
        s.addCell(new Label(0, 15, messageResources.getMessage("coco.rankRuleTitle")));
        for (int i = 0; i < numAttributes; i++) {
            AttributeVO attribute = inCOCO.getAttributes().get(i);
            s.addCell(new Label(i + 1, 14, String.valueOf(attribute.getOptima())));
            String rankRule = existingContainer.getRankRulesList().get(attribute.getRankRule() - 1).getName();
            s.addCell(new Label(i + 1, 15, rankRule));
        }
    }

    private void addOutputSheet(WritableWorkbook w, MessageResources messageResources, InputVO inCOCO, OutputVO outCOCO
    ) throws WriteException {
        int numAttributes = inCOCO.getAttributes().size();
        int numElements = inCOCO.getElements().size();

        WritableSheet s = w.createSheet("Outputs", 1);
        // Attributes / elements ranking table
        s.addCell(new Label(0, 0, messageResources.getMessage("coco.ranking.title")));
        for (int i = 0; i < numAttributes; i++) {
            AttributeVO attribute = inCOCO.getAttributes().get(i);
            s.addCell(new Label(i + 1, 0, attribute.getName()));
        }
        s.addCell(new Label(numAttributes + 1, 0, messageResources.getMessage("coco.ranking.total")));
        for (int i = 0; i < numElements; i++) {
            ElementVO elto = inCOCO.getElements().get(i);
            s.addCell(new Label(0, 1 + i, elto.getName()));

            for (int j = 0; j < elto.getCells().size(); j++) {
                CellVO cell = elto.getCells().get(j);
                s.addCell(new Label(1 + j, 1 + i, String.valueOf(cell.getRanking())));
            }
            s.addCell(new Label(1 + elto.getCells().size(), 1 + i, String.valueOf(elto.getTotalRanking())));
        }
        // Attributes / elements ideal values table
        s.addCell(new Label(0, numElements + 2, messageResources.getMessage("coco.idealvalue.title")));
        for (int i = 0; i < numAttributes; i++) {
            AttributeVO attribute = inCOCO.getAttributes().get(i);
            s.addCell(new Label(i + 1, numElements + 2, attribute.getName()));
        }
        s.addCell(new Label(numAttributes + 1, numElements + 2, messageResources.getMessage("coco.idealvalue.total")));
        for (int i = 0; i < numElements; i++) {
            ElementVO elto = inCOCO.getElements().get(i);
            s.addCell(new Label(0, i + numElements + 3, elto.getName()));

            for (int j = 0; j < elto.getCells().size(); j++) {
                CellVO cell = elto.getCells().get(j);
                s.addCell(new Label(1 + j, i + numElements + 3, String.valueOf(cell.getIdealValue())));
            }
            s.addCell(new Label(1 + elto.getCells().size(), i + numElements + 3, String.valueOf(elto.getTotalIdealValue())));
        }
        // Importance list
        s.addCell(new Label(0, 4 + (2 * numElements), messageResources.getMessage("coco.importantlist.title")));
        for (int i = 0; i < outCOCO.getImportanceObjects().length; i++) {
            s.addCell(new Label(i + 1, 4 + (2 * numElements), String.valueOf(outCOCO.getImportanceObjects()[i])));
        }
        // Sensivity list
        s.addCell(new Label(0, 5 + (2 * numElements), messageResources.getMessage("coco.sensitivitylist.title")));
        for (int i = 0; i < outCOCO.getSensitivityObjects().length; i++) {
            s.addCell(new Label(i + 1, 5 + (2 * numElements), String.valueOf(outCOCO.getSensitivityObjects()[i])));
        }
        // Best objects
        s.addCell(new Label(0, 6 + (2 * numElements), messageResources.getMessage("coco.bestobjectslist.title")));
        for (int i = 0; i < outCOCO.getBestObjects().length; i++) {
            String elementName = inCOCO.getElements().get(outCOCO.getBestObjects()[i]).getName();
            s.addCell(new Label(i + 1, 6 + (2 * numElements), elementName));
        }
        // Solution
        s.addCell(new Label(0, 7 + (2 * numElements), messageResources.getMessage("coco.solution")));
        s.addCell(new Label(1, 7 + (2 * numElements), String.valueOf(outCOCO.getSolution())));
    }

}
