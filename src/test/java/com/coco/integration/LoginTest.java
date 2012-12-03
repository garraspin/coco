package com.coco.integration;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.coco.data.UserMother;
import com.coco.page.LoginPage;
import com.coco.service.CustomDatabase;
import com.coco.vo.UserVO;
import org.openqa.selenium.By;
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
        db = new CustomDatabase(CustomDatabase.getConnectionFromDriver());
        driver = new FirefoxDriver();
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
    public void testLogin() throws Exception {
        db.saveUser(TOM);
        new LoginPage(driver).load().loginUser(TOM);

        assertThat(driver.findElement(By.xpath("//.[@id='header']/h2")).getText(), is("Input data page"));
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

        assertThat(driver.findElement(By.xpath("//.[@id='header']/h2")).getText(), is("Input data page"));
    }
}
