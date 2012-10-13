package com.grizzly.habbohotel.misc.commands;

import com.grizzly.Grizzly;
import com.grizzly.habbohotel.misc.ChatCommand;
import com.grizzly.habbohotel.sessions.Session;

public class Help implements ChatCommand
{

	@Override
	public int MinimumRank() 
	{
		return 1;
	}

	@Override
	public void Execute(Session mSession, String[] Arguments) 
	{
		StringBuilder Alert = new StringBuilder();
		
		Alert.append("Grizzly " + Grizzly.Version.String() + "\r");
		Alert.append(Grizzly.HabboBuild + "\r");
		Alert.append("Emulator created by Cobe Makarov\r");
		mSession.SendAlert(Alert.toString(), null);
	}

}
