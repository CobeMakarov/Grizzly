package com.grizzly.utils;

public class UserInputFilter
{
	public static String filterString(String str, boolean allowLineBreaks) 
	{
		
		str = str.replace((char) 1, ' ');
		str = str.replace((char) 2, ' ');
		str = str.replace((char) 3, ' ');
		str = str.replace((char) 9, ' ');
		
		if (!allowLineBreaks) 
		{
			str = str.replace((char) 10, ' ');
			str = str.replace((char) 13, ' ');
		}
		
		return str;
	}
	
	public static Boolean isNullOrEmpty(String str)
	{
		if (str == null)
			return true;
		else if (str.length() == 0)
			return true;
		else
			return false;
	}
	
	public static Boolean isNullOrEmpty(Integer str)
	{
		if (str == null)
			return true;
		else if (str == -999999)
			return true;
		else
			return false;
	}
}

