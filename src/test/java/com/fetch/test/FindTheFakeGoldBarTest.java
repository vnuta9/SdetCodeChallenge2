package com.fetch.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
/**
 * SOLUTION:--
 * 1. Place one bar on left from the list of bars starting from 0
 * 2. and place one on the right from the remaining list of bars
 * 3. then weigh, check each time, to see which one is heavier
 * 4. continue until an inequality is found.
 * 5. Report which one is fake by identifying the heavier/lesser weighing pan and the last bar placed on it.
 */
public class FindTheFakeGoldBarTest {
    /**
     * List of bars represented as strings in an array form.
     */
    List<String> barsList = Arrays.asList("0","1","2","3","4","5","6","7","8");
    /**
     * Get the List in an iterator form for conveniently drawing from it during the test.
     */
    Iterator<String> barsItr = barsList.iterator();
    boolean FakeBarOnLeftBowl;
    boolean FakeBarOnRightBowl;
    WebDriver driver = null;

    /**
     * Main method for executing the test.
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        FindTheFakeGoldBarTest findFakeBar = new FindTheFakeGoldBarTest();
        findFakeBar.runTest();
    }

    /**
     * Method where the test runs to figure out the fake bar.
     * @throws InterruptedException
     */
    @Test
    private void runTest() throws InterruptedException {
        driver = new ChromeDriver();

//        driver.get("https://www.selenium.dev/selenium/web/web-form.html");//
        driver.get("http://sdetchallenge.fetch.com/");
        String title = driver.getTitle();
        System.out.println("title="+title);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        clickReset();
        /**
         * SOLUTION:--
         * 1. Place one bar on left from the list of bars starting from 0
         * 2. and place one on the right from the remaining list of bars
         * 3. then weigh, check each time, to see which one is heavier
         * 4. continue until an inequality is found.
         * 5. Report which one is fake by identifying the heavier/lesser weighing pan and the last bar placed on it.
         */
        int i_left_Bowl= -1;
        int i_right_Bowl= -1;
//        System.out.println("i_left_Bowl="+i_left_Bowl+", i_right_Bowl="+i_right_Bowl);
        for (int i=0; i<=4;i++){
          i_left_Bowl= i_left_Bowl+1;
          i_right_Bowl= i_right_Bowl+1;
          final String leftBar = barsItr.next();
          final String rightBar = (barsItr.hasNext() ? barsItr.next():"FakeBarOnRightBowl");
//          System.out.println("i_left_Bowl="+i_left_Bowl+", i_right_Bowl="+i_right_Bowl);
          putBar("left", i_left_Bowl,leftBar);
            /**
             * Even during the last round, if fake bar was not found indicates
             * that the last bar at index 8, ie., 9th bar should have been fake.
             */
          if(i == 4){
              System.out.println("Fake Bar found="+"FakeBarOnLeftBowl which is "+ rightBar);
              break;
          }
          Wait<WebDriver> waitLeft = new WebDriverWait(driver, Duration.ofSeconds(2));
          putBar("right", i_right_Bowl,rightBar);
          Wait<WebDriver> waitRight = new WebDriverWait(driver, Duration.ofSeconds(2));
          compareWeightToFindFakeBar();
          if(FakeBarOnRightBowl || FakeBarOnLeftBowl){
              System.out.println("Fake Bar found="+(FakeBarOnRightBowl ? "FakeBarOnRightBowl, which is bar #-->"+rightBar: "FakeBarOnLeftBowl, which is bar #-->"+leftBar));
              break;
          }
        }
        driver.quit();
    }

    /**
     * Reset the app, before commencing each test run
     */
    private void clickReset(){
        driver.findElement((By.cssSelector("#reset"))).click();
    }
    /**
     *  Put one bar each time, in the left or right bowls.
     *  We will use cssSelectors because they are conveniently named like
     *  #right_4, #left_5 etc.,
     * @param side
     * @param iBowl
     * @param iBar
     */
    private void putBar(String side,int iBowl, String iBar){
        String cssLctr = new StringBuffer().append("#").append(side).append("_").append(iBowl).toString();
//        System.out.println("cssLctr="+cssLctr);
        WebElement bowlPosition =  driver.findElement(By.cssSelector(cssLctr));
        bowlPosition.sendKeys(iBar);

    }

    /**
     * Click the 'weigh' button and check the result.
     * @throws InterruptedException
     */
    private void compareWeightToFindFakeBar() throws InterruptedException {
        driver.findElement(By.cssSelector("#weigh")).click();
        /**
         * Should NOT have used Thread.sleep(), which is NOT a good practice.
         * Should have used the wait mechanism, but for some reason, I can't make it working and taking a lot of implementation
         * time. Among few assessments, I could only find this much time to get this implemented.
         */
        Thread.sleep(5000);
        List<WebElement> results1 =  driver.findElements(By.xpath("//li[contains(text(), '>')]"));
        List<WebElement> results2 =  driver.findElements(By.xpath("//li[contains(text(), '<')]"));

        if(results1.size() > 0) {
            FakeBarOnRightBowl = true;
        }else if(results2.size() > 0) {
            FakeBarOnLeftBowl = true;
        }
    }
}
//    private boolean compareWeightFindFakeBar(){
//       boolean found_fake = false;
//       driver.findElement(By.cssSelector("#weigh")).click();
//
//       Wait<WebDriver> waitWeighResult = new WebDriverWait(driver, Duration.ofSeconds(5));
//       List<WebElement> results =  driver.findElements(By.xpath("//li[contains(text(), '>')]"));
//
//       if(results.size() > 0) {
//           int i = results.size();
//
//           waitWeighResult.until(d -> results.get(i - 1).isDisplayed());
//
//           for (WebElement ele : results) {
//               found_fake = ele.getText().contains(">");
//               break;
//           }
//       }
//       return found_fake;
//    }