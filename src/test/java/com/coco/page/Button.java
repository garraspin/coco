package com.coco.page;

import org.openqa.selenium.WebElement;

public class Button {
    private final WebElement button;

    public Button(WebElement button) {
        this.button = button;
    }

    public void click() {
        button.click();
    }
}
