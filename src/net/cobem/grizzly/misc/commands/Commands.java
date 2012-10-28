package net.cobem.grizzly.misc.commands;

import java.util.Map;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.misc.ConsoleCommand;


public class Commands implements ConsoleCommand
{
	@Override
	public void Parse(String[] Parameters)
	{
		Grizzly.WriteOut("Available Console Commands");
		
		String CommandList = "";
		
		for (Map.Entry<String, ConsoleCommand> Commands : Grizzly.GrabConsoleCommandParser().GrabCommands().entrySet())
		{
			CommandList += (Commands.getKey() + ", ");
		}
		
		Grizzly.WriteOut(CommandList);
	}

}
