package com.coco.widget;

import org.openqa.selenium.WebElement;

public class Text {
    private final WebElement text;

    public Text(WebElement text) {
        this.text = text;
    }

    public void setValue(String value) {
        text.clear();
        text.sendKeys(value);
    }

    public String getValue() {
        return text.getAttribute("value");
    }
}
