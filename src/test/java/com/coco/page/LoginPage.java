package com.coco.page;

import com.coco.vo.UserVO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends Page<LoginPage> {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void setEmailLogin(String email) {
        input("emailLogin").sendKeys(email);
    }

    public void setPasswordLogin(String password) {
        input("passwordLogin").sendKeys(password);
    }

    public void submitLogin() {
        input("submitLogin").click();
    }

    public void setNameSubscribe(String name) {
        input("nameSubs").sendKeys(name);
    }

    public void setSurnameSubscribe(String surname) {
        input("surnameSubs").sendKeys(surname);
    }

    public void setEmailSubscribe(String email) {
        input("emailSubs").sendKeys(email);
    }

    public void setPasswordSubscribe(String password) {
        input("passwordSubs").sendKeys(password);
    }

    public void setRepasswordSubscribe(String repassword) {
        input("repasswordSubs").sendKeys(repassword);
    }

    public void submitSubscribe() {
        input("submitSubs").click();
    }

    public void loginUser(UserVO userVO) {
        setEmailLogin(userVO.getEmailAdress());
        setPasswordLogin(userVO.getPassword());
        submitLogin();
    }
}
