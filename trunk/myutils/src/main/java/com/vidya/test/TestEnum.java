/**
 * 
 * Mindtree © 2013. All rights reserved.
 *
 * Created Date : 28-Nov-2013 11:56:07 AM
 *
 */
package com.vidya.test;

/**
 * @author Vidya Sagar 
 *
 */
public enum TestEnum
{
    INSTANCE;

    static
    {
	System.out.println("Static Method invoked");
    }
    
    private TestEnum()
    {
	System.out.println("Invoked");
    }
    
    public void testMethod()
    {
	System.out.println("Method Invoked");
    }
    
    public static void main(String[] args)
    {
	TestEnum.INSTANCE.testMethod();
	TestEnum.INSTANCE.testMethod();
	TestEnum.INSTANCE.testMethod();
    }
}
