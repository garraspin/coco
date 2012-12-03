package com.coco.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Page<T> {
    private static final String COCO_BASE_URL = "http://localhost/coco/";

    protected final WebDriver driver;

    public Page(WebDriver driver) {
        this.driver = driver;
    }

    public T load() {
        driver.get(COCO_BASE_URL);
        return (T) this;
    }

    protected WebElement input(final String name) {
        return driver.findElement(By.xpath("//input[@name='" + name + "']"));
    }

}
