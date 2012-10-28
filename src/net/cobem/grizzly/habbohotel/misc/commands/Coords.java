package net.cobem.grizzly.habbohotel.misc.commands;

import net.cobem.grizzly.habbohotel.misc.ChatCommand;
import net.cobem.grizzly.habbohotel.sessions.Session;

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
		
		Alert.append("X:" + mSession.GrabActor().CurrentPosition.X + "\r");
		Alert.append("Y:" + mSession.GrabActor().CurrentPosition.Y + "\r");
		Alert.append("Z:" + mSession.GrabActor().CurrentPosition.Z + "\r");
		Alert.append("Rotation:" + mSession.GrabActor().Rotation + "\r");
		
		mSession.SendAlert(Alert.toString(), null);
	}

}
