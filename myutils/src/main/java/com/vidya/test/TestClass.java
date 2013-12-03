/**
 * 
 * Mindtree © 2013. All rights reserved.
 *
 * Created Date : 28-Nov-2013 12:13:30 PM
 *
 */
package com.vidya.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.vidya.utils.FileUtils;

/**
 * @author Vidya Sagar 
 *
 */
public class TestClass
{
    
    public static void main(String[] args)
    {
	try
	{
	    //System.out.println(RESTUtils.INSTANCE.sendGetRequest("https://172.19.3.82:7443/password-vault-2.1/pv/ui/gettargetusers?ciId=153820"));
	    
	    Properties mailProperties = FileUtils.INSTANCE.readPropertiesFile("/home/mindtree/test_workspace/myutils/src/main/resources/mail.properties");
	    
	    List<String> toList = new ArrayList<String>();
	    toList.add("vidya_karnati@mindtree.com");
	    
	    MailWrapper wrapper = new MailWrapper.Builder("vidya_karnati@mindtree.com", toList, "Basic Mail Subject", "Basic Mail Content", "M1014501@mindtree.com", "mindtree@0987", mailProperties).build();
	    wrapper.sendMail();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
}
