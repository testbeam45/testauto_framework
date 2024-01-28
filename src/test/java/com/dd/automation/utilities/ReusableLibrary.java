package com.dd.automation.utilities;

import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class ReusableLibrary implements WebDriver {
    private WebDriver _driver;


    public ReusableLibrary(WebDriver driver){
        this._driver=driver;
    }


    /**
     * check whether page is fully loaded
     *
     * @param driver*/
    public boolean pageSync(WebDriver driver){
        boolean actionStatus=false;
        int iCount=50;
        try{
            for(int i=0;i<iCount;i++){
                boolean iResult =webpageState();
                Thread.sleep(1000);
                if(iResult){
                    actionStatus=true;
                    break;
                }
            }
        }catch (Exception e){
            System.out.println("Page Sync Exception: "+e);
        }
        return actionStatus;
    }

    public boolean webpageState(){
        boolean actionStatus=false;
        try{
            String state = ((JavascriptExecutor) this._driver).executeScript("return document.readyState").toString();
            if(state.equals("complete")){
                actionStatus=true;
            }
        }catch (Exception e){
            System.out.println("Webpage State Exception: "+e);
        }
        return actionStatus;
    }

    public void assertIntFields(String fieldName,Integer exp, Integer act){
        System.out.println(fieldName+" expected : "+exp+" and actual: "+act);
        Assert.assertEquals(act,exp);
    }

    @Override
    public void get(String s) {
    }

    @Override
    public String getCurrentUrl() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return null;
    }

    @Override
    public WebElement findElement(By by) {
        return null;
    }

    @Override
    public String getPageSource() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void quit() {

    }

    @Override
    public Set<String> getWindowHandles() {
        return null;
    }

    @Override
    public String getWindowHandle() {
        return null;
    }

    @Override
    public TargetLocator switchTo() {
        return null;
    }

    @Override
    public Navigation navigate() {
        return null;
    }

    @Override
    public Options manage() {
        return null;
    }
}
