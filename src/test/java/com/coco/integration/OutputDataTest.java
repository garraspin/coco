package com.coco.integration;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import com.coco.data.DirectDataSource;
import com.coco.data.UserMother;
import com.coco.database.CustomDatabase;
import com.coco.page.InputDataPage;
import com.coco.page.OutputDataPage;
import com.coco.vo.BaseVO;
import com.coco.vo.UserVO;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class OutputDataTest {
    private static final UserVO TOM = UserMother.aUser();

    private WebDriver driver;
    private CustomDatabase db;

    @BeforeMethod
    public void setUp() throws Exception {
        db = new CustomDatabase(new DirectDataSource());
        driver = new FirefoxDriver();

        db.saveUser(TOM);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        driver.quit();

        for (BaseVO baseVO : db.getListCOCOProblems(TOM.getId())) {
            db.deleteCOCOInput(db.getCOCOInput(baseVO.getId()));
        }
        db.removeUser(TOM.getId());
    }

    @Test
    public void testSolutionProblem() throws Exception {
        InputDataPage inputDataPage = new InputDataPage(driver, TOM).load();

        inputDataPage.clickCreateProblemMenuLink();
        inputDataPage.setInputData("name", "description", "2", true, "1");
        inputDataPage.setMatrixAttributeNames("Attribute Y", "Attr 1", "Attr 2", "Attr 3");
        inputDataPage.setMatrixOptimalValues("1.1", "2.2", "3.3");
        inputDataPage.setMatrixRankRules("1", "2", "3");
        inputDataPage.setMatrixElement(0, "Element 1", "10", "1.1", "2.2", "3.3");
        inputDataPage.setMatrixElement(1, "Element 2", "20", "4.4", "5.5", "6.6");
        inputDataPage.setMatrixElement(2, "Element 3", "30", "7.7", "8.8", "9.9");
        inputDataPage.clickCreateButton();
        inputDataPage.problemLinks().get(0).click();
        OutputDataPage outputDataPage = inputDataPage.clickOutputPageLink();

        assertThat(outputDataPage.isAvailable(), is(true));

        assertThat(outputDataPage.getAttributeNamesInRankingMatrix(), is(Arrays.asList("Attr 1", "Attr 2", "Attr 3")));
        assertThat(outputDataPage.getElementInRankingMatrix(0), is(Arrays.asList("Element 1", "0", "2", "0", "2")));
        assertThat(outputDataPage.getElementInRankingMatrix(1), is(Arrays.asList("Element 2", "1", "1", "1", "3")));
        assertThat(outputDataPage.getElementInRankingMatrix(2), is(Arrays.asList("Element 3", "2", "0", "2", "4")));

        assertThat(outputDataPage.getAttributeNamesInIdealValueMatrix(), is(Arrays.asList("Attr 1", "Attr 2", "Attr 3")));
        assertThat(outputDataPage.getElementInIdealValueMatrix(0), is(Arrays.asList("Element 1", "2.75", "3.85", "4.95", "11.55")));
        assertThat(outputDataPage.getElementInIdealValueMatrix(1), is(Arrays.asList("Element 2", "4.4", "5.5", "6.6", "16.5")));
        assertThat(outputDataPage.getElementInIdealValueMatrix(2), is(Arrays.asList("Element 3", "6.05", "7.15", "8.25", "21.45")));

        assertThat(outputDataPage.getAttributeNamesInResultsMatrix(), is(Arrays.asList("Attr 1", "Attr 2", "Attr 3")));
        assertThat(outputDataPage.getImportantListInResultsMatrix(), is(Arrays.asList("4.4", "5.5", "6.6")));
        assertThat(outputDataPage.getSensitivityListInResultsMatrix(), is(Arrays.asList("2.6944", "2.6944", "2.6944")));
        assertThat(outputDataPage.getBestObjectListInResultsMatrix(), is(Arrays.asList("Element 2", "Element 2", "Element 2")));
        assertThat(outputDataPage.getSolution(), is("0.0"));

    }
}
