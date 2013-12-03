package com.vidya.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Vidya Sagar
 * 
 */
public class MailUtils
{
	public static void sendMail(String from, String to, List<String> cc, List<String> bcc, String subject, String content, final String username, final String password,
			Properties mailProperties) throws Exception
	{
		Session session = Session.getDefaultInstance(mailProperties, new javax.mail.Authenticator()
		{
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(username, password);
			}
		});

		try
		{

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setRecipients(Message.RecipientType.CC, (InternetAddress[]) (getInternetAddressList(cc).toArray()));
			message.setRecipients(Message.RecipientType.BCC, (InternetAddress[]) (getInternetAddressList(bcc).toArray()));

			message.setSubject(subject);
			message.setText(content);

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static List<InternetAddress> getInternetAddressList(List<String> inputList) throws Exception
	{
		List<InternetAddress> list = new ArrayList<InternetAddress>();

		for (String str : inputList)
		{
			for (InternetAddress addr : InternetAddress.parse(str))
			{
				list.add(addr);
			}
		}
		return list;
	}
}
