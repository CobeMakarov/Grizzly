package net.cobem.grizzly.habbohotel.misc.commands;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.habbohotel.misc.ChatCommand;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class Summon implements ChatCommand
{

	@Override
	public int MinimumRank()
	{
		return 4;
	}

	@Override
	public void Execute(Session mSession, String[] Arguments)
	{
		if(Arguments.length != 1)
		{
			mSession.SendAlert("Invalid number of arguments!", null);
			return;
		}
		
		String TargetName = Arguments[0];
		
		Session Target = Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSessionByName(TargetName);
		
		if (Target == null)
		{
			mSession.SendAlert(TargetName + " is offline!", null);
			return;
		}
		
		Target.EnterRoom(mSession.GrabActor().CurrentRoom.ID);
	}

}
