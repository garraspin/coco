package com.coco.integration;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.coco.data.UserMother;
import com.coco.page.InputDataPage;
import com.coco.page.LoginPage;
import com.coco.service.CustomDatabase;
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
        new LoginPage(driver).load().loginUser(TOM);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        driver.quit();

        if (!db.removeUser(TOM.getId())) {
            db.removeUser(TOM.getName());
        }
        db.destroy();
    }

    @Test
    public void testCreateProblem() throws Exception {
        InputDataPage inputDataPage = new InputDataPage(driver);
        inputDataPage.clickCreateProblemMenuLink();
        inputDataPage.setInputData("name", "description", 1, false, 1.1);
        inputDataPage.setMatrixElementNames();
        inputDataPage.setMatrixAttributeNames();
        inputDataPage.setMatrixYAttributeName();
        inputDataPage.setMatrixElementValues();
        inputDataPage.setMatrixElementsOptionalValues();
        inputDataPage.setMatrixRankRules();
        inputDataPage.clickCreateButton();

        assertThat(inputDataPage.problemLink("name").isAvailable(), is(true));
    }
}
