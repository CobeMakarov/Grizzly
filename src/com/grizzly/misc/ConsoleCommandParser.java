package com.grizzly.misc;

import java.util.*;

import com.grizzly.Grizzly;
import com.grizzly.misc.commands.*;

public class ConsoleCommandParser 
{
	private Map<String, ConsoleCommand> Commands;
	
	public ConsoleCommandParser()
	{
		Commands = new HashMap<String, ConsoleCommand>();
		
		RegisterCommands();
	}
	
	private void RegisterCommands()
	{
		Commands.put("help", new Help());
		Commands.put("commands", new Commands());
		Commands.put("stats", new Stats());
	}
	
	public void InvokeCommand(String Command)
	{
		if (!Commands.containsKey(SplitCommand(Command, true)[0]))
		{
			Grizzly.WriteOut(Command + " is not a valid Console Command!");
			return;
		}
		
		Commands.get(SplitCommand(Command, true)[0]).Parse(SplitCommand(Command, false));
	}
	
	public Map<String, ConsoleCommand> GrabCommands()
	{
		return Commands;
	}
	
	private String[] SplitCommand(String Command, boolean Start)
	{
		String[] Split = Command.split(System.getProperty("line.separator"));
		
		if (Start)
		{
			return Split;
		}
		else
		{
			String Arguments = Command.replace(Split[0], "");
			
			return Arguments.split(System.getProperty("line.separator"));
		}
	}
}
