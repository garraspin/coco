package com.coco.widget;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Select {
    private final WebElement select;

    public Select(WebElement select) {
        this.select = select;
    }

    public void select(String value) {
        select.findElement(By.xpath("//option[@value='" + value + "']")).click();
    }

    public String getSelected() {
        return select.findElement(By.xpath("//option[@selected]")).getAttribute("value");
    }
}
