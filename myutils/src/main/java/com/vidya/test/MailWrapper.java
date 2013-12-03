package com.vidya.test;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.vidya.utils.CollectionUtils;

public class MailWrapper
{
	private final String from;
	private final List<String> to;
	private final String subject;
	private final String content;
	private final String username;
	private final String password;
	private final Properties mailProperties;

	private List<String> cc;
	private List<String> bcc;
	private List<String> replyTo;
	private String[] attachments;
	private boolean htmlMsg;

	//  TODO Implement Compression
	//  private boolean zipFiles;

	public static class Builder
	{
		// Required Params
		private final String from;
		private final List<String> to;
		private final String subject;
		private final String content;
		private final String username;
		private final String password;
		private final Properties mailProperties;

		// optional params
		private List<String> cc = Collections.emptyList();
		private List<String> bcc = Collections.emptyList();
		private List<String> replyTo = Collections.emptyList();
		private String[] attachments = new String[0];
		private boolean htmlMsg = true;

		//	TODO Implement Compression
		//	private boolean zipFiles = true;

		public Builder(String from, List<String> to, String subject, String content, String username, String password, Properties mailProperties)
		{
			this.from = from;
			this.to = to;
			this.subject = subject;
			this.content = content;
			this.username = username;
			this.password = password;
			this.mailProperties = mailProperties;
		}

		public Builder cc(List<String> cc)
		{
			this.cc = cc;
			return this;
		}

		public Builder bcc(List<String> bcc)
		{
			this.bcc = bcc;
			return this;
		}

		public Builder replyTo(List<String> replyto)
		{
			this.replyTo = replyto;
			return this;
		}

		public Builder asHTML()
		{
			this.htmlMsg = true;
			return this;
		}

		public Builder addFiles(String... attachments)
		{
			this.attachments = attachments;
			return this;
		}

		//TODO Implement Compression
		/*	public Builder zipFiles()
	{
	    this.zipFiles = true;
	    return this;
	}
		 */
		public MailWrapper build()
		{
			return new MailWrapper(this);
		}
	}

	private MailWrapper(Builder builder)
	{
		//mandatory
		from = builder.from;
		to = builder.to;
		subject = builder.subject;
		content = builder.content;
		username = builder.username;
		password = builder.password;
		mailProperties = builder.mailProperties;

		//optional
		cc = builder.cc;
		bcc = builder.bcc;
		replyTo = builder.replyTo;
		htmlMsg = builder.htmlMsg;
		attachments = builder.attachments;

		//TODO implement compression
		//zipFiles = builder.zipFiles;
	}

	public void sendMail() throws Exception
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
			message.setSubject(subject);

			message.setRecipients(Message.RecipientType.TO, getInternetAddressList(to));
			if (CollectionUtils.INSTANCE.hasElements(cc))
				message.setRecipients(Message.RecipientType.CC, getInternetAddressList(cc));

			if (CollectionUtils.INSTANCE.hasElements(bcc))
				message.setRecipients(Message.RecipientType.BCC, (InternetAddress[]) getInternetAddressList(cc));

			if (CollectionUtils.INSTANCE.hasElements(replyTo))
				message.setReplyTo((InternetAddress[]) getInternetAddressList(replyTo));

			if(CollectionUtils.INSTANCE.hasElements(attachments))
			{
				Multipart multipart = getMultiPart(content,attachments);
				message.setContent(multipart);
			}else if (htmlMsg)
			{
				message.setContent(content, "text/html");
			}
			else
			{
				message.setText(content);
			}

			Transport.send(message);
		}
		catch (MessagingException e)
		{
			throw new RuntimeException(e);
		}
	}

	// add support for multiple files
	private Multipart getMultiPart(String content,String... filepaths) throws Exception
	{
		BodyPart messageBodyPart = new MimeBodyPart();
		if (htmlMsg)
		{
			messageBodyPart.setContent(content, "text/html");
		}
		else
		{
			messageBodyPart.setText(content);
		}

		// Create a multipart message
		Multipart multipart = new MimeMultipart();

		// Set text message part
		multipart.addBodyPart(messageBodyPart);

		for (String filepath : filepaths)
		{
			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(filepath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filepath);
			multipart.addBodyPart(messageBodyPart);
		}
		return multipart;
	}

	private static InternetAddress[] getInternetAddressList(List<String> inputList) throws Exception
	{
		InternetAddress[] array = new InternetAddress[inputList.size()];
		int i = 0;
		for (String str : inputList)
		{
			for (InternetAddress addr : InternetAddress.parse(str))
			{
				array[i++] = addr;
			}
		}
		return array;
	}   
}