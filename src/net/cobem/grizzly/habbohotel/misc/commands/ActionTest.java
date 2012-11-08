package net.cobem.grizzly.habbohotel.misc.commands;

import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.misc.ChatCommand;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class ActionTest implements ChatCommand
{

	@Override
	public int MinimumRank()
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void Execute(Session Session, String[] Arguments)
	{
		if (Arguments.length == 0)
		{
			return;
		}
		
		Session.GrabResponse().Initialize(ComposerLibrary.Wave);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().ID);
		Session.GrabResponse().AppendInt32(new Integer(Arguments[0]));
		Session.GrabActor().CurrentRoom.SendMessage(Session.GrabResponse());
	}

}
