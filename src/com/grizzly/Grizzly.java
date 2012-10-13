package com.grizzly;

import com.grizzly.misc.*;
import com.grizzly.net.*;
import com.grizzly.storage.*;
import com.grizzly.utils.ChangelogHandler;
import com.grizzly.habbohotel.*;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Grizzly 
{
	public static Version Version = new Version(1, 0, 2, 0);
	public static String HabboBuild = "RELEASE63-201207231112-16380487";
	public static Date EmulatorStart;
	
	private static Configuration Config;
	private static DateHandler DateHandler;
	private static Connection Connection;
	private static DatabaseHandler DatabaseHandler;
	private static HabboHotel HabboHotel;
	private static ConsoleCommandParser ConsoleCommandParser;
	
	public static void main(String[] args) 
	{
		EmulatorStart = new Date();
		
		System.out.println("Grizzly - " + Version.String());
		System.out.println("Built for " + HabboBuild);
		System.out.println("Writted by Cobe 'Makarov' Johnson");
		
		System.out.println();

		DateHandler = new DateHandler();
		
		try 
		{
			ChangelogHandler LogHandler = new ChangelogHandler();
			
			System.out.println("Latest Changes: ");
			
			for(String LogLine : LogHandler.GrabAll())
			{
				System.out.println(LogLine);
			}
		} 
		catch (IOException e2) { }
		
		System.out.println();
		
		Config = new Configuration();
		
		if (Config.Load("server.properties"))
		{
			WriteOut("Config parsed in " + DateHandler.GetDateFrom(EmulatorStart, DateFormat.Seconds)  + "s");
		}
		else
		{
			WriteOut("Config could not be found!! It should be '/server.properties'");
			System.exit(1);
		}
		Connection = new Connection(Integer.parseInt(Config.GrabValue("net.grizzly.port")));
		
		if (Connection.Listen())
		{
			WriteOut("Netty started listening on port (" 
					+ Config.GrabValue("net.grizzly.port") + 
					") in " +  DateHandler.GetDateFrom(EmulatorStart, DateFormat.Seconds) + "s");
		}
		else
		{
			WriteOut("Either net.grizzly.port cannot be found in your properties or the port is not opened!");
			System.exit(1);
		}
		
		DatabaseHandler = new DatabaseHandler();
		
		if (DatabaseHandler.TryConnect(Config))
		{
			WriteOut("JDBC with BoneCP started in " + DateHandler.GetDateFrom(EmulatorStart, DateFormat.Seconds) + "s");
		}
		else
		{
			WriteOut("Connection to your database has failed! Check your details in server.properties!");
			System.exit(1);
		}

		try 
		{
			HabboHotel = new HabboHotel();
		} 
		catch (SQLException e1) 
		{
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		
		WriteOut("HabboHotel Environment started in " + DateHandler.GetDateFrom(EmulatorStart, DateFormat.Seconds) + "s");
		
		GrabDatabase().RunFastQuery("TRUNCATE server_clients");
		
		WriteOut("Grizzly - Initialized in " + DateHandler.GetDateFrom(EmulatorStart, DateFormat.Seconds) + "s");
		
		System.out.println();
		
		ConsoleCommandParser = new ConsoleCommandParser();
		
		WriteOut("Loaded " + ConsoleCommandParser.GrabCommands().size() + " console commands. Feel free to try some!");

		//What the ****
		Toolkit.getDefaultToolkit().beep();  
		
		System.out.println();
		
		while(true)
		{
			try 
			{
				ConsoleCommandParser.InvokeCommand(new BufferedReader(new InputStreamReader(System.in)).readLine().toLowerCase());
			} 
			catch (IOException e) { }
		}
	}
	
	public static HabboHotel GrabHabboHotel()
	{
		return HabboHotel;
	}
	
	public static Configuration GrabConfig()
	{
		return Config;
	}
	
	public static DateHandler GrabDateHandler()
	{
		return DateHandler;
	}
	
	public static Connection GrabConnection()
	{
		return Connection;
	}
	
	public static DatabaseHandler GrabDatabase()
	{
		return DatabaseHandler;
	}
	
	public static ConsoleCommandParser GrabConsoleCommandParser()
	{
		return ConsoleCommandParser;
	}
	
	public static void WriteOut(Object Out)
	{
		System.out.println("[" + new SimpleDateFormat("M/d/yyyy HH:mm:ss").format(new Date()) + "] >> " + Out);
	}
}
