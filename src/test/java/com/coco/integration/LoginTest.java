package com.coco.integration;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.coco.data.DirectDataSource;
import com.coco.data.UserMother;
import com.coco.database.CustomDatabase;
import com.coco.page.InputDataPage;
import com.coco.page.LoginPage;
import com.coco.vo.UserVO;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {
    private static final UserVO TOM = UserMother.aUser();

    private WebDriver driver;
    private CustomDatabase db;

    @BeforeMethod
    public void setUp() throws Exception {
        db = new CustomDatabase(new DirectDataSource());
        driver = new FirefoxDriver();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        driver.quit();

        if (!db.removeUser(TOM.getId())) {
            db.removeUser(TOM.getName());
        }
    }

    @Test
    public void testLogin() throws Exception {
        db.saveUser(TOM);
        new LoginPage(driver).load().loginUser(TOM);

        assertThat(new InputDataPage(driver, TOM).isAvailable(), is(true));
    }

    @Test
    public void testUnsuccessfulLogin() throws Exception {
        LoginPage loginPage = new LoginPage(driver).load();

        loginPage.submitLogin();
        assertThat(loginPage.errorMessages(), contains("E-mail is required.", "Password is required."));

        loginPage.loginUser(TOM);
        assertThat(loginPage.isAvailable(), is(true));
        assertThat(loginPage.warningMessages(), contains("The e-mail and password combination is not valid"));
    }

    @Test
    public void testSubscribe() throws Exception {
        LoginPage loginPage = new LoginPage(driver).load();

        loginPage.setNameSubscribe(TOM.getName());
        loginPage.setSurnameSubscribe(TOM.getLastName());
        loginPage.setEmailSubscribe(TOM.getEmailAdress());
        loginPage.setPasswordSubscribe(TOM.getPassword());
        loginPage.setRepasswordSubscribe(TOM.getPassword());
        loginPage.submitSubscribe();

        assertThat(new InputDataPage(driver, TOM).isAvailable(), is(true));
    }

    @Test
    public void testUnsuccessfulSubscribe() throws Exception {
        LoginPage loginPage = new LoginPage(driver).load();
        loginPage.submitSubscribe();

        assertThat(loginPage.errorMessages(), contains(
            "Name is required.", "Surname is required.", "E-mail is required.", "Password is required."
        ));
    }
}
