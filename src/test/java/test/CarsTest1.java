package test;

import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CarsTest1 {

  private static final int WAIT_MAX = 4;
  static WebDriver driver;


  @BeforeClass
  public static void setup() {

    System.setProperty("webdriver.chrome.driver","C:\\Users\\Kim\\Documents\\drivers\\chromedriver_win32\\chromedriver.exe");

    //Reset Database
    com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
    driver = new ChromeDriver();
    driver.get("http://localhost:3000");
  }

  @AfterClass
  public static void tearDown() {
    driver.quit();
    //Reset Database
    com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
  }

  @Test
  //Verify that page is loaded and all expected data are visible
  public void test1() throws Exception {
    (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
      WebElement e = d.findElement(By.tagName("tbody"));
      List<WebElement> rows = e.findElements(By.tagName("tr"));
      Assert.assertThat(rows.size(), is(5));
      return true;
    });
  }

  @Test
  //Verify the filter functionality
  public void test2() throws Exception {
    //No need to WAIT, since we are running test in a fixed order, we know the DOM is ready (because of the wait in test1)
    WebElement element = driver.findElement(By.id("filter"));

    //The string that we enter in the filter-field
    element.sendKeys("2002");

    //Now we do the same as in test 1
    WebElement e = driver.findElement(By.tagName("tbody"));
    List<WebElement> rows = e.findElements(By.tagName("tr"));
    Assert.assertThat(rows.size(), is(2));
  }

  @Test
  public void test3() throws Exception {
    WebElement element = driver.findElement(By.id("filter"));

    //First we clear and then use a space (don't know why it dosen't work without)
    element.clear();
    element.sendKeys(Keys.SPACE);

    WebElement e = driver.findElement(By.tagName("tbody"));
    List<WebElement> rows = e.findElements(By.tagName("tr"));
    Assert.assertThat(rows.size(), is(5));
  }

  @Test
  public void test4() throws Exception {
    WebElement yearElement = driver.findElement(By.id("h_year"));

    //Here we click on the element
    yearElement.click();

    //Now we look at the first table row and first table cell
    WebElement topRow = driver.findElement(By.xpath("//tr[1]/td[1]"));
    Assert.assertThat(topRow.getText(), is("938"));

    WebElement bottomRow = driver.findElement(By.xpath("//tr[last()]/td[1]"));
    Assert.assertThat(bottomRow.getText(), is("940"));
  }

  @Test
  public void test5() throws Exception {
    //First we get the top row with the last cell
    WebElement topRow = driver.findElement(By.xpath("//tbody/tr[1]/td[8]"));

    //next we get the element with tagname 'a' in topRow
    WebElement edit = topRow.findElements(By.tagName("a")).get(0);

    //We click on that button
    edit.click();

    //We find the field
    WebElement description = driver.findElement(By.id("description"));

    //We clear the field and enter new description
    description.clear();
    description.sendKeys("cool car");

    //We find the save element and click it
    WebElement saveCar = driver.findElement(By.id("save"));
    saveCar.click();

    //We find the edited element and verify that it has the new description
    WebElement descriptionRow = driver.findElement(By.xpath("//tbody/tr[2]/td[6]"));
    Assert.assertThat(descriptionRow.getText(), is("cool car"));
  }

  @Test
  public void test6() throws Exception {
    //We find the new element and click it
    WebElement newCar = driver.findElement(By.id("new"));
    newCar.click();

    //We find the save element and click it
    WebElement saveCar = driver.findElement(By.id("save"));
    saveCar.click();

    //And the we see if we get the error message
    WebElement error = driver.findElement(By.id("submiterr"));
    Assert.assertThat(error.getText(), is("All fields are required"));
  }

  @Test
  public void test7() throws Exception {
    //We click on the 'new' element
    WebElement newCar = driver.findElement(By.id("new"));
    newCar.click();

    //Then we enter in all the informations on the new car
    WebElement year = driver.findElement(By.id("year"));
    year.sendKeys("2008");

    WebElement registered = driver.findElement(By.id("registered"));
    registered.sendKeys("2002-5-5");

    WebElement make = driver.findElement(By.id("make"));
    make.sendKeys("Kia");

    WebElement model = driver.findElement(By.id("model"));
    model.sendKeys("Rio");

    WebElement description = driver.findElement(By.id("description"));
    description.sendKeys("As new");

    WebElement price = driver.findElement(By.id("price"));
    price.sendKeys("31000");

    //Then we save the new car
    WebElement saveCar = driver.findElement(By.id("save"));
    saveCar.click();

    //At last, we test if the new car is added to the list.
    WebElement newRegistered = driver.findElement(By.xpath("//tr[last()]/td[3]"));
    Assert.assertThat(newRegistered.getText(), is("5/5/2002"));

    WebElement newMake = driver.findElement(By.xpath("//tr[last()]/td[4]"));
    Assert.assertThat(newMake.getText(), is("Kia"));
  }


}
