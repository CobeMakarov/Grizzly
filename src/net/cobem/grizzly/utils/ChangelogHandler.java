package net.cobem.grizzly.utils;

import java.io.*;

import net.cobem.grizzly.Grizzly;


public class ChangelogHandler 
{
	StringBuilder Log;
	
	public ChangelogHandler() throws IOException
	{
		try 
		{
			BufferedReader Reader = new BufferedReader(new FileReader("changelog.txt"));
			
			Log = new StringBuilder();
			
			String Line = "";
			String NewLine = System.getProperty("line.separator");
			
			while((Line = Reader.readLine()) != null)
			{
				if (Line.contains("[" + Grizzly.Version.String() + "]"))
				{
					Log.append(Line.replace("[" + Grizzly.Version.String() + "] ", ""));
					Log.append(NewLine);
				}
			}
		} 
		catch (FileNotFoundException e) 
		{
			Grizzly.WriteOut("Cannot find the changelog!");
		}
	}
	
	public String[] GrabAll()
	{
		return Log.toString().split(System.getProperty("line.separator"));
	}
	
	public String GrabLatest()
	{
		String Return = null;
		
		int i = 0;
		
		for(String Line : Log.toString().split(System.getProperty("line.separator")))
		{
			if (i > Log.toString().split(System.getProperty("line.separator")).length)
			{
				i++;
				continue;
			}
			
			Return = Line;
		}
		
		return Return;
	}
	
	public void WriteTo()
	{
		
	}
}
