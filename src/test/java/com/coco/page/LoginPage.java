package com.coco.page;

import com.coco.vo.UserVO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends Page<LoginPage> {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAvailable() {
        return !driver.findElements(By.name("loginForm")).isEmpty();
    }

    public void setEmailLogin(String email) {
        text("emailLogin").setValue(email);
    }

    public void setPasswordLogin(String password) {
        text("passwordLogin").setValue(password);
    }

    public void submitLogin() {
        button("submitLogin").click();
    }

    public void setNameSubscribe(String name) {
        text("nameSubs").setValue(name);
    }

    public void setSurnameSubscribe(String surname) {
        text("surnameSubs").setValue(surname);
    }

    public void setEmailSubscribe(String email) {
        text("emailSubs").setValue(email);
    }

    public void setPasswordSubscribe(String password) {
        text("passwordSubs").setValue(password);
    }

    public void setRepasswordSubscribe(String repassword) {
        text("repasswordSubs").setValue(repassword);
    }

    public void submitSubscribe() {
        button("submitSubs").click();
    }

    public void loginUser(UserVO userVO) {
        setEmailLogin(userVO.getEmailAdress());
        setPasswordLogin(userVO.getPassword());
        submitLogin();
    }
}
