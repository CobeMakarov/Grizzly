package net.cobem.grizzly.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import net.cobem.grizzly.Grizzly;

public class GrizzlyHelper 
{
	public GrizzlyHelper()
	{
	}
	
	public String GRUD()
	{
		try
		{
			String Data = URLEncoder.encode("version", "UTF-8") + "=" + URLEncoder.encode(Grizzly.Version.String(), "UTF-8");
			Data += "&" + URLEncoder.encode("operation", "UTF-8") + "=" + URLEncoder.encode("grab_changelog", "UTF-8");
			
			URL Url = new URL("http://cobem.net/forest/grizzly/grud/");
			
			URLConnection Connection = Url.openConnection();
			
			Connection.setDoOutput(true);

		    OutputStreamWriter Writer = new OutputStreamWriter(Connection.getOutputStream());
		    
		    Writer.write(Data);
		    
		    Writer.flush();
		    
		    
		    // Get the response
		    BufferedReader Reader = new BufferedReader(new InputStreamReader(Connection.getInputStream()));
		    
		    String Line = "";
		    
		    StringBuilder Return = new StringBuilder();
		    String NewLine = System.getProperty("line.separator");
		    
		    while ((Line = Reader.readLine()) != null)
		    {
		    	Return.append(Line.replace("<br>", NewLine));
		    }
		    
		    Writer.close();
		    Reader.close();
		    
		    return Return.toString();
		} catch (Exception e) { }
		
		return "";
	}
}
