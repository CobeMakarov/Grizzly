package net.cobem.grizzly.habbohotel.misc;

import java.util.HashMap;
import java.util.Map;

import net.cobem.grizzly.habbohotel.misc.commands.*;
import net.cobem.grizzly.habbohotel.sessions.Session;


public class ChatCommandParser 
{
	private Map<String, ChatCommand> ChatCommands;
	
	public ChatCommandParser()
	{
		ChatCommands = new HashMap<String, ChatCommand>();
		
		//Default in Grizzly
		AddTo("help", new Help());
		AddTo("coords", new Coords());
		AddTo("summon", new Summon());
		AddTo("destructplugin", new DestructPlugin());
		AddTo("destroyplugin", new DestructPlugin());
		AddTo("deloadplugin", new DestructPlugin());
		AddTo("actiontest", new ActionTest());
	}
	
	private void AddTo(String Name, ChatCommand Command) //2Lazy4Lyfe
	{
		ChatCommands.put(Name, Command);
	}
	
	public boolean ParseChatCommand(Session mSession, String Command)
	{
		String CommandName = SplitCommand(Command, true)[0];
		
		if (!ChatCommands.containsKey((CommandName).toLowerCase()))
		{
			mSession.SendAlert(CommandName + " isn't a valid chat command!", null);
			return false;
		}
		
		if (mSession.GrabHabbo().Rank < ChatCommands.get(CommandName).MinimumRank())
		{
			mSession.SendAlert("You don't have permission to execute " + CommandName, null);
			return false;
		}
		
		ChatCommands.get(CommandName).Execute(mSession, SplitCommand(Command, false));
		return true;
	}
	
	public Map<String, ChatCommand> GrabCommands()
	{
		return ChatCommands;
	}
	
	private String[] SplitCommand(String Command, boolean Start)
	{
		String[] Split = Command.split(" ");
		
		if (Start)
		{
			return Split;
		}
		else
		{
			String Arguments = Command.replace(Split[0], "").substring(1);
			
			return Arguments.split(System.getProperty("line.separator"));
		}
	}
}
