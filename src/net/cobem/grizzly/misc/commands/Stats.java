package net.cobem.grizzly.misc.commands;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.misc.ConsoleCommand;
import net.cobem.grizzly.misc.DateFormat;

public class Stats implements ConsoleCommand
{
	public void Parse(String[] Parameters)
	{
		Grizzly.WriteOut("Projected Statistics:");
		
		Grizzly.WriteOut("Seconds Running: " + Grizzly.GrabDateHandler().GetDateFrom(Grizzly.EmulatorStart, DateFormat.Seconds));
		
		Grizzly.WriteOut("Current Memory Usage: 0." + CalculateMemory() + " mb");
	}
	
	private long CalculateMemory()
	{
		System.gc(); 
		
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;
	}
}
