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

    protected Text text(final String name) {
        return new Text(driver.findElement(By.xpath("//input[@name='" + name + "']")));
    }

    protected CheckBox checkBox(String name) {
        return new CheckBox(driver.findElement(By.xpath("//input[@type='checkbox' and @name='" + name + "']")));
    }

    protected Select select(String name) {
        return new Select(driver.findElement(By.xpath("//select[@name='" + name + "']")));
    }

    protected Button button(String name) {
        return new Button(driver.findElement(By.xpath("//input[@name='" + name + "']")));
    }

}
