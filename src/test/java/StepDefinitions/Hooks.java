package StepDefinitions;

import Utils.BrowserUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void setUp(Scenario scenario){
        System.out.println("Run Before every class");
        System.out.println(scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario){
        System.out.println("Run after every class");

        if(scenario.isFailed()){
            System.out.println("failed");
            BrowserUtils.takeScreenShot();
        }

    }
}
