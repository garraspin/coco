package com.coco.action;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coco.service.ICOCOService;
import com.coco.struts.ActionUtils;
import com.coco.struts.UserContainer;
import com.coco.vo.AttributeVO;
import com.coco.vo.BaseVO;
import com.coco.vo.CellVO;
import com.coco.vo.ElementVO;
import com.coco.vo.InputVO;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

public class LoadXLS extends Action {
    private final ActionUtils actionUtils = new ActionUtils();

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response
    ) throws ServletException, IOException {
		FormFile file = (FormFile) ((DynaValidatorForm) form).get("file");
        ICOCOService serviceImpl = actionUtils.getCOCOService(servlet);
        InputVO inCOCO = readInCOCO(file, serviceImpl);

		// Salvar el nuevo problema
		UserContainer existingContainer = actionUtils.getUserContainer(request);
        int userId = existingContainer.getUserVO().getId();
        serviceImpl.setCOCOInput(inCOCO, userId);

		// Obtener la nueva lista de problemas
        List<BaseVO> listCOCOProblems = serviceImpl.getListCOCOProblems(userId);
        existingContainer.getListCOCO().setProblems(listCOCOProblems);

		// Determinar el actual problema
		existingContainer.getListCOCO().getActualCOCO().setInCOCO(inCOCO);

		return mapping.findForward("initPage");
	}

    private InputVO readInCOCO(FormFile file, ICOCOService cocoService) throws IOException {
        InputVO inCOCO = new InputVO();
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(file.getInputStream());
        } catch (BiffException e) {
            e.printStackTrace();
        }

        Sheet sheet = workbook.getSheet(0);
        // Obtener los datos de entrada del problema
        Cell cell = sheet.getCell(1, 1);
        inCOCO.setName(cell.getContents());
        cell = sheet.getCell(1, 2);
        inCOCO.setDescription(cell.getContents());
        cell = sheet.getCell(1, 3);
        inCOCO.setIdFunction(getIdFromName(cell.getContents(), cocoService.getFunctions()));
        cell = sheet.getCell(1, 4);
        inCOCO.setNegativeAllowed(Boolean.parseBoolean(cell.getContents()));
        cell = sheet.getCell(1, 5);
        inCOCO.setEquilibrium(Integer.parseInt(cell.getContents()));

        // Atributos
        int col = 1;
        List<AttributeVO> attributes = inCOCO.getAttributes();
        while (true) {
            cell = sheet.getCell(col, 8);
            AttributeVO attribute = attributes.get(col - 1);
            attribute.setName(cell.getContents());
            try {
                sheet.getCell(col++ + 2, 8).getContents();
            } catch (Exception e) {
                break;
            }
        }
        cell = sheet.getCell(col, 8);
        inCOCO.setAttributeY(cell.getContents());

        // Elements
        int row = 9;
        List<ElementVO> elements = inCOCO.getElements();
        while (true) {
            col = 0;
            cell = sheet.getCell(col, row);
            ElementVO element = elements.get(row - 9);
            element.setName(cell.getContents());

            // Celdas del elemento
            col = 1;
            while (true) {
                cell = sheet.getCell(col, row);
                CellVO cell1 = element.getCells().get(col - 1);
                cell1.setValue(Double.parseDouble(cell.getContents()));

                try {
                    sheet.getCell(col++ + 2, row).getContents();
                } catch (Exception e) {
                    break;
                }
            }
            cell = sheet.getCell(col, row);
            element.setYvalue(Double.parseDouble(cell.getContents()));

            try {
                if (sheet.getCell(col, ++row).getContents().equals("")) {
                    break;
                }
            } catch (Exception e) {
                break;
            }
        }

        for (int i = 0; i < attributes.size(); i++) {
            AttributeVO attribute = attributes.get(i);
            cell = sheet.getCell(i + 1, 11 + elements.size());
            attribute.setOptima(Double.parseDouble(cell.getContents()));
            cell = sheet.getCell(i + 1, 12 + elements.size());
            attribute.setRankRule(getIdFromName(cell.getContents(), cocoService.getRankRules()));
        }
        workbook.close();
        return inCOCO;
    }

    private int getIdFromName(String name, List<BaseVO> list) {
		for (BaseVO base : list) {
			if (name.equals(base.getName())) {
				return base.getId();
			}
		}
		return -1;
	}
}