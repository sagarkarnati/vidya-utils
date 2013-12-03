package com.vidya.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Vidya Sagar
 * 
 */
public enum CollectionUtils
{
	INSTANCE;

	public <T extends Collection<?>> boolean hasElements(T collection)
	{
		return (collection != null && collection.size() > 0);
	}

	public <T extends Map<?, ?>> boolean hasElements(T collection)
	{
		return (collection != null && collection.size() > 0);
	}

	public <T> boolean hasElements(T[] collection)
	{
		return (collection != null && collection.length > 0);
	}

	public static void main(String[] args)
	{
		List<String> arrayList = null;

		System.out.println(CollectionUtils.INSTANCE.hasElements(arrayList));
	}
}
