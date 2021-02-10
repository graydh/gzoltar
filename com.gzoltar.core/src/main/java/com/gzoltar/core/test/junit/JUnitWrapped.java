package com.gzoltar.core.test.junit;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import static org.junit.Assert.*;

public class JUnitWrapped {
	Class<?> testClass;
	String testClassName;
	String testMethodName;
	public JUnitWrapped() {
		JUnitTestClassMethodReader obj = new JUnitTestClassMethodReader(); //initialize JUnitTestClassMethodReader
		String[] arr = obj.readClassMethod();
		this.testClassName = arr[0]; //read from JUnitTestClassMethodReader
		this.testMethodName = arr[1]; //read from JUnitTestClassMethodReader
	}
	
	@Test
	public void runTest() throws Exception { //executes a junit test inside this junit test
		Class<?> clazz = Class.forName(this.testClassName);
		Request request = Request.method(clazz, this.testMethodName);
	    JUnitCore runner = new JUnitCore();
	    Result result = runner.run(request);
	    if(!result.wasSuccessful()) {
	    	assertTrue(this.testClassName + "." + this.testMethodName + " has failed!", false); //fail test
	    }  
	}
}