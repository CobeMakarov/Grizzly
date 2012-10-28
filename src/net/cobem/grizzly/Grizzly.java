package net.cobem.grizzly;


import java.awt.Toolkit;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.cobem.grizzly.habbohotel.*;
import net.cobem.grizzly.misc.*;
import net.cobem.grizzly.net.*;
import net.cobem.grizzly.storage.*;
import net.cobem.grizzly.utils.GrizzlyHelper;

public class Grizzly 
{
	public static Version Version = new Version(1, 0, 2, 4);
	public static String HabboBuild = "RELEASE63-201207231112-16380487";
	//public static String HabboBuild = "RELEASE63-201210152303-146968328";
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
		System.out.println("Writted by Cobe 'Makarov'");
		
		System.out.println();

		DateHandler = new DateHandler();
		
		GrizzlyHelper Helper = new GrizzlyHelper();
		
		System.out.println(Helper.GRUD());
		
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
			HabboHotel.Load();
		} 
		catch (SQLException e1) 
		{
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		
		WriteOut("HabboHotel Environment started in " + DateHandler.GetDateFrom(EmulatorStart, DateFormat.Seconds) + "s");
		
		GrabDatabase().RunFastQuery("TRUNCATE server_clients");
		GrabDatabase().RunFastQuery("UPDATE server_users SET online = '0'");
		
		WriteOut("Grizzly - Initialized in " + DateHandler.GetDateFrom(EmulatorStart, DateFormat.Seconds) + "s");
		
/*		System.out.println();
		
		ConsoleCommandParser = new ConsoleCommandParser();
		
		WriteOut("Loaded " + ConsoleCommandParser.GrabCommands().size() + " console commands. Feel free to try some!");
*/
		//What the ****
		Toolkit.getDefaultToolkit().beep();  
		
		System.out.println();
		
		/*while(true)
		{
			try 
			{
				ConsoleCommandParser.InvokeCommand(new BufferedReader(new InputStreamReader(System.in)).readLine().toLowerCase());
			} 
			catch (IOException e) { }
		}*/
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
		System.out.println("[" + new SimpleDateFormat(Config.GrabValue("console.grizzly.date")).format(new Date()) + "] >> " + Out);
	}
}
