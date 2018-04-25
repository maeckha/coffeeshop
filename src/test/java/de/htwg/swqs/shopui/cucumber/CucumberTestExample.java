package de.htwg.swqs.shopui.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(cucumber.api.junit.Cucumber.class)
@SpringBootTest
@CucumberOptions(
    features = "classpath:cucumber",
    plugin = "pretty",
    strict = true
)
public class CucumberTestExample {

}

