package se.jacob;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ FilehandlerTest.class })
public class TestSuite {
	
	@BeforeClass
	public static void beforeTests() {
		
	}
	
	@AfterClass
	public static void afterTests() {
		
	}
}
