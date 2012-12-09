package com.coco.page;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

import org.openqa.selenium.WebElement;

public class CheckBox {
    private final WebElement checkbox;

    public CheckBox(WebElement checkbox) {
        this.checkbox = checkbox;
    }

    public void check() {
        if (isEmpty(getCheckAttribute())) {
            checkbox.click();
        }
    }

    public void uncheck() {
        if (isNotEmpty(getCheckAttribute())) {
            checkbox.click();
        }
    }

    public boolean isChecked() {
        return isNotEmpty(getCheckAttribute());
    }

    private String getCheckAttribute() {
        return checkbox.getAttribute("checked");
    }
}
