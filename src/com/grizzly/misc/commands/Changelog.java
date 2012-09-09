package com.grizzly.misc.commands;

import com.grizzly.misc.ConsoleCommand;

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
