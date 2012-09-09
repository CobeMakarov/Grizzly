package com.grizzly.misc.commands;

import com.grizzly.Grizzly;
import com.grizzly.misc.ConsoleCommand;

public class Help implements ConsoleCommand
{
	@Override
	public void Parse(String[] Parameters)
	{
		Grizzly.WriteOut("You are running Grizzly Alpha Post-Shuffle!");
	}
}
