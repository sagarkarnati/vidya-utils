package com.vidya.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @author Vidya Sagar
 * 
 */
public enum FileUtils
{
	INSTANCE;

	public String readFile(String absPath) throws Exception
	{
		StringBuilder strBuilder = new StringBuilder();
		RandomAccessFile aFile = null;
		FileChannel inChannel = null;
		MappedByteBuffer buffer = null;

		try
		{
			aFile = new RandomAccessFile(absPath, "r");
			inChannel = aFile.getChannel();
			buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
			buffer.load();

			for (int i = 0; i < buffer.limit(); i++)
			{
				strBuilder.append((char) buffer.get());
			}
		} catch (Exception e)
		{
			throw new Exception(e);
		} finally
		{
			try
			{
				if (buffer != null)
					buffer.clear();
				if (inChannel != null)
					inChannel.close();
				if (aFile != null)
					aFile.close();
			} catch (Exception e)
			{
			}
		}
		return strBuilder.toString();
	}

	public Properties readPropertiesFile(String filePath) throws Exception
	{
		Properties props = new Properties();
		props.load(new FileInputStream(filePath));

		return props;
	}

	public void saveProperty(String filePath, String key, String value) throws Exception
	{
		// read properties from the file
		Properties props = readPropertiesFile(filePath);

		// add the date.
		props.setProperty(key, value);

		// save properties to project root folder
		props.store(new FileOutputStream(filePath), "Updated on " + new Date());
	}

	public void saveProperties(String filePath, Map<String, String> values) throws Exception
	{
		// read properties from the file
		Properties props = readPropertiesFile(filePath);

		for (Entry<String, String> entry : values.entrySet())
		{
			// add the date.
			props.setProperty(entry.getKey(), entry.getKey());
		}

		// save properties to project root folder
		props.store(new FileOutputStream(filePath), "Updated on " + new Date());
	}

	public List<File> getAllFilesRecursively(String dirPath) throws Exception
	{
		List<File> fileList = new ArrayList<File>();

		getFiles(new File(dirPath), fileList);

		return fileList;
	}

	private void getFiles(File path, List<File> fileList) throws Exception
	{
		if (path.isFile())
		{
			fileList.add(path);
		} else
		{
			for (File file : path.listFiles())
			{
				getFiles(file, fileList);
			}
		}
	}

	public static void main(String[] args)
	{
		try
		{
			System.out.println(FileUtils.INSTANCE.getAllFilesRecursively("/home/mindtree/Desktop/Documents4Print"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
