package net.cobem.grizzly.misc.commands;

import net.cobem.grizzly.misc.ConsoleCommand;

public class Changelog implements ConsoleCommand
{
	@Override
	public void Parse(String[] Parameters)
	{
		switch(Parameters[0])
		{
			case "add":
			break;
			
			case "clear":
			break;
		}
	}

}
