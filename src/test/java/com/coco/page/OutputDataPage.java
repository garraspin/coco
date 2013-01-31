package com.coco.page;

import static com.google.common.collect.Lists.transform;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class OutputDataPage extends Page<OutputDataPage> {

    public OutputDataPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAvailable() {
        List<WebElement> headers = driver.findElements(By.xpath("id('header')/h2"));
        return !headers.isEmpty() && headers.get(0).getText().equals("Output data page");
    }

    public List<String> getAttributeNamesInRankingMatrix() {
        return getAttributeNamesInMatrix("ranking-matrix");
    }

    public List<String> getElementInRankingMatrix(int index) {
        return getElementValuesInMatrix(index, "ranking-matrix");
    }

    public List<String> getAttributeNamesInIdealValueMatrix() {
        return getAttributeNamesInMatrix("ideal-values-matrix");
    }

    public List<String> getElementInIdealValueMatrix(int index) {
        return getElementValuesInMatrix(index, "ideal-values-matrix");
    }

    public List<String> getAttributeNamesInResultsMatrix() {
        return getAttributeNamesInMatrix("results-matrix");
    }

    public List<String> getImportantListInResultsMatrix() {
        return getResultValues(0);
    }

    public List<String> getSensitivityListInResultsMatrix() {
        return getResultValues(1);
    }

    public List<String> getBestObjectListInResultsMatrix() {
        return getResultValues(2);
    }

    public String getSolution() {
        return driver.findElement(By.id("solution-cell")).getText();
    }

    private List<String> getResultValues(int index) {
        List<String> values = getElementValuesInMatrix(index, "results-matrix");
        values.remove(0);
        values.remove(values.size() - 1);
        return values;
    }

    private List<String> getAttributeNamesInMatrix(String matrixId) {
        List<WebElement> trs = getTrsFromTable(matrixId);
        List<WebElement> tds = trs.get(0).findElements(By.tagName("td"));

        return webElementToText(Arrays.asList(tds.get(1), tds.get(2), tds.get(3)));
    }

    private List<WebElement> getTrsFromTable(String id) {
        return driver.findElements(By.xpath("id('" + id + "')//tr"));
    }

    private List<String> webElementToText(List<WebElement> webElements) {
        return transform(webElements, new Function<WebElement, String>() {
            @Override
            public String apply(WebElement webElement) {
                return webElement.getText();
            }
        });
    }

    private List<String> getElementValuesInMatrix(int index, String matrixId) {
        List<WebElement> trs = getTrsFromTable(matrixId);
        List<WebElement> tds = trs.get(index + 1).findElements(By.tagName("td"));
        return webElementToText(tds);
    }
}
