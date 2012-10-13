package com.grizzly.habbohotel.misc.commands;

import com.grizzly.habbohotel.misc.ChatCommand;
import com.grizzly.habbohotel.sessions.Session;

public class Coords implements ChatCommand
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
		
		Alert.append("X:" + mSession.GrabRoomUser().CurrentPosition.X + "\r");
		Alert.append("Y:" + mSession.GrabRoomUser().CurrentPosition.Y + "\r");
		Alert.append("Z:" + mSession.GrabRoomUser().CurrentPosition.Z + "\r");
		Alert.append("Rotation:" + mSession.GrabRoomUser().Rotation + "\r");
		
		mSession.SendAlert(Alert.toString(), null);
	}

}
