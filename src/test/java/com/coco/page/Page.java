package com.coco.page;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

import java.util.List;

import com.coco.widget.Button;
import com.coco.widget.CheckBox;
import com.coco.widget.Select;
import com.coco.widget.Text;
import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class Page<T> {
    private static final String COCO_BASE_URL = "http://localhost/coco/";
    private static final String MSG_WARN_CLASSNAME = "msgWarn";
    private static final String MSG_ERROR_CLASSNAME = "msgError";

    protected final WebDriver driver;

    public Page(WebDriver driver) {
        this.driver = driver;
    }

    public T load() {
        driver.get(COCO_BASE_URL);
        return (T) this;
    }

    public abstract boolean isAvailable();

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

    public List<String> errorMessages() {
        return getMessages(MSG_ERROR_CLASSNAME);
    }

    public List<String> warningMessages() {
        return getMessages(MSG_WARN_CLASSNAME);
    }

    private List<String> getMessages(String msgClassName) {
        List<WebElement> errorMsgs = driver.findElements(By.className(msgClassName));
        Function<WebElement, String> webElementToString = new Function<WebElement, String>() {
            @Override public String apply(WebElement webElement) { return webElement.getText(); }
        };

        return newArrayList(transform(errorMsgs, webElementToString));
    }
}
