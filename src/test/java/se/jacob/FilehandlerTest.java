package se.jacob;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FilehandlerTest {
	
	@Before
	public void beforeTests() {
		
	}
	
	@After
	public void afterTests() {
		try{
			 
    		File file = new File(Constants.RESOURCES + "testfile.xml");
 
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
 
    	} catch(Exception e){
 
    		e.printStackTrace();
 
    	}
 
	}
	
	@Test
	public void createNewFile() {
		File file = new File(Constants.RESOURCES + "testfile.xml");
		assertNotNull(file);
	}
	
	
}
