package net.cobem.grizzly.misc;

import java.util.Date;

public class DateHandler 
{
	
	public int GetDateFrom(Date DateTime, DateFormat Format)
	{
		switch(Format)
		{
			case Seconds:
			{
				return (int) ((new Date().getTime() - DateTime.getTime()) / 1000);
			}
			
			case Minutes:
			{
				return 0;
			}
			
			case Hours:
			{
				return 0;
			}
		}
		
		return 0;
	}
}
