package StepDefinitions.com;

import Pages.Localhost8080.BacklogPage;
import Pages.Localhost8080.IssuePage;
import Pages.Localhost8080.LoginPage;
import Pojo.PojoJira.ResponseBodyAuth;
import Pojo.ResponseBodyRestApi;
import Pojo.pojoJiraSession.SessionForUSer;
import Utils.ConfigReader;
import Utils.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;

public class CreateStory {

    WebDriver driver = Driver.getDriver();
    BacklogPage backlogPage = new BacklogPage();
    IssuePage issuePage = new IssuePage();
    LoginPage loginPage = new LoginPage();

    public String userName;
    public String value;

    public String key;

//    @Before
//    public void setUp2(){
//
//    }


    @Given("the user write restassured steps")
    public void the_user_write_restassured_steps() {

        SessionForUSer sessionForUSer = new SessionForUSer(ConfigReader.getProperty("userName"),ConfigReader.getProperty("password"));

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAccept(ContentType.JSON).setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200).expectContentType(ContentType.JSON).build();
        Response response = given().spec(requestSpecification)
                .body(sessionForUSer)
                .when().post("http://localhost:8080/rest/auth/1/session")
                .then().spec(responseSpecification)
                .extract().response();

        ResponseBodyAuth responseBodyAuth = response.as(ResponseBodyAuth.class);

        userName = responseBodyAuth.getSession().getName();
        value = responseBodyAuth.getSession().getValue();
        System.out.println(userName+value);

        File filePalyload = new File("target/issue.json");

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAccept(ContentType.JSON).setContentType(ContentType.JSON)
                .addHeader("Cookie",userName+"="+value).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(201).expectContentType(ContentType.JSON)
                .build();
        Response response1 = given().spec(requestSpecification)
                .body(filePalyload)
                .when().post("http://localhost:8080/rest/api/2/issue")
                .then().assertThat().spec(responseSpecification)
                .extract().response();

//        ResponseBodyRestApi responseBodyRestApi = given().spec(requestSpecification)
//                .body(filePalyload)
//                .when().post("http://localhost:8080/rest/api/2/issue")
//                .then().spec(responseSpecification)
//                .extract().as(ResponseBodyRestApi.class);
        ResponseBodyRestApi responseBodyRestApi = response1.getBody().as(ResponseBodyRestApi.class);

        System.out.println(responseBodyRestApi.getId());
        key =responseBodyRestApi.getKey();


    }

    @When("the user goes to the website")
    public void the_user_goes_to_the_website() throws InterruptedException {

        driver.get(ConfigReader.getProperty("urlJira"));
        loginPage.username.sendKeys(ConfigReader.getProperty("userName"));
        loginPage.password.sendKeys(ConfigReader.getProperty("password"));
        loginPage.submitButton.click();
        Thread.sleep(1000);



    }

    @When("the user click last created one")
    public void the_user_click_last_created_one() throws InterruptedException {

        Thread.sleep(500);
        int length=backlogPage.stories.size()-1;

        Actions actions=new Actions(driver);
        actions.moveToElement(backlogPage.stories.get(length)).click().perform();

    }

    @Then("click viewWorkflow")
    public void click_viewWorkflow() {

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        backlogPage.viewWorkflow.click();
    }

    @Then("validate information")
    public void validate_information() throws InterruptedException {

        Assert.assertEquals(key,issuePage.keyValue.getText());

        Thread.sleep(500);
        issuePage.owl.click();
        issuePage.logOutButton.click();


    }
}
