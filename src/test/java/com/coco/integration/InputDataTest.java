package com.coco.integration;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import com.coco.data.UserMother;
import com.coco.page.InputDataPage;
import com.coco.service.CustomDatabase;
import com.coco.vo.BaseVO;
import com.coco.vo.UserVO;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InputDataTest {
    private static final UserVO TOM = UserMother.aUser();

    private WebDriver driver;
    private CustomDatabase db;

    @BeforeMethod
    public void setUp() throws Exception {
        db = new CustomDatabase(CustomDatabase.getConnectionFromDriver());
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
        db.destroy();
    }

    @Test
    public void testCreateProblem() throws Exception {
        InputDataPage inputDataPage = new InputDataPage(driver, TOM).load();

        assertThat(inputDataPage.problemLinks().size(), is(0));

        inputDataPage.clickCreateProblemMenuLink();
        inputDataPage.setInputData("name", "description", "1", false, "1.1");
        inputDataPage.setMatrixAttributeNames("Attribute Y", "Attr 1", "Attr 2", "Attr 3");
        inputDataPage.setMatrixOptimalValues("1.1", "2.2", "3.3");
        inputDataPage.setMatrixRankRules("1", "2", "3");
        inputDataPage.setMatrixElement(0, "Element 1", "y value 1", "1.1", "2.2", "3.3");
        inputDataPage.setMatrixElement(1, "Element 2", "y value 2", "4.4", "5.5", "6.6");
        inputDataPage.setMatrixElement(2, "Element 3", "y value 3", "7.7", "8.8", "9.9");
        inputDataPage.clickCreateButton();

        assertThat(inputDataPage.problemLinks().size(), is(1));

        inputDataPage.problemLinks().get(0).click();

        assertThat(inputDataPage.getInputData(), is(Arrays.asList("name", "description", "1", "false", "1.1")));
        assertThat(inputDataPage.getMatrixAttributeNames(), is(Arrays.asList("Attribute Y", "Attr 1", "Attr 2", "Attr 3")));
        assertThat(inputDataPage.getMatrixOptimalValues(), is(Arrays.asList("1.1", "2.2", "3.3")));
        assertThat(inputDataPage.getMatrixRankRules(), is(Arrays.asList("1", "2", "3")));
        assertThat(inputDataPage.getMatrixElement(0), is(Arrays.asList("Element 1", "y value 1", "1.1", "2.2", "3.3")));
        assertThat(inputDataPage.getMatrixElement(1), is(Arrays.asList("Element 2", "y value 2", "4.4", "5.5", "6.6")));
        assertThat(inputDataPage.getMatrixElement(2), is(Arrays.asList("Element 3", "y value 3", "7.7", "8.8", "9.9")));
    }
}
