package backend;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;


@Suite
@IncludeEngines("cucumber")

//Specifies the location of the feature files. The feature files are stored in the "features/biblio" directory within the classpath.
@SelectClasspathResource("features/sophiaeats")

@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
//Specifies the package where the step definitions are located.
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "backend")

public class RunCucumberTest {
    // will run all features found on the classpath
    // in the same package as this class
}