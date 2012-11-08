package net.cobem.grizzly.habbohotel.misc.commands;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.habbohotel.misc.ChatCommand;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class DestructPlugin implements ChatCommand
{

	@Override
	public int MinimumRank()
	{
		return 6;
	}

	@Override
	public void Execute(Session mSession, String[] Arguments)
	{
		if (Arguments.length == 0)
		{
			mSession.SendAlert("You didn't specify a plugin to destroy!", null);
			return;
		}
		
		if (Grizzly.GrabPluginHandler().DestructPlugin(mSession, Arguments[0]))
		{
			mSession.GrabActor().Speak("*Disables the plugin " + Arguments[0] + "*", true);
		}
	}

}
