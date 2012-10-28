package net.cobem.grizzly.misc.commands;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.misc.ConsoleCommand;

public class Help implements ConsoleCommand
{
	@Override
	public void Parse(String[] Parameters)
	{
		Grizzly.WriteOut("You are running Grizzly Alpha Post-Shuffle!");
	}
}
