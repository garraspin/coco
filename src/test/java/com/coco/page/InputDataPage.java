package com.coco.page;

import static com.google.common.collect.Lists.transform;

import java.util.Arrays;
import java.util.List;

import com.coco.vo.UserVO;
import com.coco.widget.Select;
import com.coco.widget.Text;
import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class InputDataPage extends Page<InputDataPage> {

    private final UserVO user;

    public InputDataPage(WebDriver driver, UserVO user) {
        super(driver);
        this.user = user;
    }

    @Override
    public InputDataPage load() {
        new LoginPage(driver).load().loginUser(user);
        return this;
    }

    @Override
    public boolean isAvailable() {
        List<WebElement> headers = driver.findElements(By.xpath("id('header')/h2"));
        return !headers.isEmpty() && headers.get(0).getText().equals("Input data page");
    }

    public void clickCreateProblemMenuLink() {
        getMenuLinks().get(0).click();
    }

    public void setInputData(String name, String description, String functionId, boolean negativesAllowed, String balanceInterval) {
        text("inCOCO.name").setValue(name);
        text("inCOCO.description").setValue(description);
        text("inCOCO.equilibrium").setValue(balanceInterval);
        select("inCOCO.idFunction").select(functionId);
        if (negativesAllowed) {
            checkBox("inCOCO.negativeAllowed").check();
        } else {
            checkBox("inCOCO.negativeAllowed").uncheck();
        }
    }

    public void clickCreateButton() {
        button("submitCreate").click();
    }

    public void setMatrixAttributeNames(String attributeYName, String ... attributeNames) {
        int numAttributes = attributeNameInputs().size();
        if (numAttributes > attributeNames.length) {
            clickRemoveAttributeButton();
            setMatrixAttributeNames(attributeYName, attributeNames);
        } else if (numAttributes < attributeNames.length) {
            clickAddAttributeButton();
            setMatrixAttributeNames(attributeYName, attributeNames);
        } else {
            text("inCOCO.attributeY").setValue(attributeYName);
            setAttributeNames(attributeNames);
        }
    }

    public void setMatrixOptimalValues(String... optimalValues) {
        List<WebElement> optimalInputs = optimalInputs();
        for (int i = 0; i < optimalValues.length; i++) {
            new Text(optimalInputs.get(i)).setValue(optimalValues[i]);
        }
    }

    public void setMatrixRankRules(String ... rankRuleIds) {
        List<WebElement> rankRuleInputs = rankRuleSelects();
        for (int i = 0; i < rankRuleIds.length; i++) {
            new Select(rankRuleInputs.get(i)).select(rankRuleIds[i]);
        }
    }

    private List<WebElement> rankRuleSelects() {
        return tagsWithNameAttributeLike("select", "inCOCO.attributes[", "].rankRule");
    }

    public void setMatrixElement(int index, String name, String yValue, String ... cellValues) {
        if (index > tagsWithNameAttributeLike("input", "inCOCO.elements[", "].name").size() - 1) {
            clickAddElementButton();
            setMatrixElement(index, name, yValue, cellValues);
        } else {
            text("inCOCO.elements[" + index + "].name").setValue(name);
            text("inCOCO.elements[" + index + "].yvalue").setValue(yValue);

            for (int i = 0; i < cellValues.length; i++) {
                text("inCOCO.elements[" + index + "].cells[" + i + "].value").setValue(cellValues[i]);
            }
        }
    }

    public List<WebElement> problemLinks() {
        return driver.findElements(By.xpath("//div[@id='columnB']//a[contains(@href, 'showProblem')]"));
    }

    public List<String> getInputData() {
        return Arrays.asList(
                text("inCOCO.name").getValue(),
                text("inCOCO.description").getValue(),
                select("inCOCO.idFunction").getSelected(),
                String.valueOf(checkBox("inCOCO.negativeAllowed").isChecked()),
                text("inCOCO.equilibrium").getValue()
        );
    }

    public List<String> getMatrixAttributeNames() {
        List<String> result = Arrays.asList(text("inCOCO.attributeY").getValue());
        result.addAll(webElementsToTextValues(attributeNameInputs()));
        return result;
    }

    public List<String> getMatrixOptimalValues() {
        return webElementsToTextValues(optimalInputs());
    }

    public List<String> getMatrixRankRules() {
        return webElementsToSelectedValues(rankRuleSelects());
    }

    public List<String> getMatrixElement(int index) {
        List<String> result = Arrays.asList(
                text("inCOCO.elements[" + index + "].name").getValue(),
                text("inCOCO.elements[" + index + "].yvalue").getValue()
        );
        result.addAll(webElementsToTextValues(tagsWithNameAttributeLike("input", "inCOCO.elements[" + index + "].cells[", "].value")));
        return result;
    }

    private List<String> webElementsToTextValues(List<WebElement> webElements) {
        return transform(webElements, new Function<WebElement, String>() {
            @Override
            public String apply(WebElement webElement) {
                return new Text(webElement).getValue();
            }
        });
    }

    private List<String> webElementsToSelectedValues(List<WebElement> webElements) {
        return transform(webElements, new Function<WebElement, String>() {
            @Override
            public String apply(WebElement webElement) {
                return new Select(webElement).getSelected();
            }
        });
    }

    private List<WebElement> optimalInputs() {
        return tagsWithNameAttributeLike("input", "inCOCO.attributes[", "].optima");
    }

    private List<WebElement> attributeNameInputs() {
        return tagsWithNameAttributeLike("input", "inCOCO.attributes[", "].name");
    }

    private List<WebElement> getMenuLinks() {
        return driver.findElement(By.id("menu")).findElements(By.tagName("a"));
    }

    private void clickAddAttributeButton() {
        driver.findElement(By.xpath("//a[contains(@href, 'col=1&row=0')]")).click();
    }

    private void clickRemoveAttributeButton() {
        driver.findElement(By.xpath("//a[contains(@href, 'col=-1&row=0')]")).click();
    }

    private void clickAddElementButton() {
        driver.findElement(By.xpath("//a[contains(@href, 'col=0&row=1')]")).click();
    }

    private void clickRemoveElementButton() {
        driver.findElement(By.xpath("//a[contains(@href, 'col=0&row=-1')]")).click();
    }

    private List<WebElement> tagsWithNameAttributeLike(final String tagName, final String prefix, final String suffix) {
        int i = suffix.length() - 1;
        return driver.findElements(By.xpath("//" + tagName + "[starts-with(@name, '" + prefix + "') and '" + suffix + "' = substring(@name, string-length(@name) - " + i + ")]"));
    }

    private void setAttributeNames(String... attributeNames) {
        List<WebElement> attributeInputs = attributeNameInputs();
        for (int i = 0; i < attributeNames.length; i++) {
            new Text(attributeInputs.get(i)).setValue(attributeNames[i]);
        }
    }
}
