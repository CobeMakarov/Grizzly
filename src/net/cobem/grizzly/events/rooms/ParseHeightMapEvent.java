package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.rooms.Room;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class ParseHeightMapEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		Room mRoom = Session.GrabActor().CurrentRoom;
		
		if (mRoom != null)
		{
			Session.GrabResponse().Initialize(ComposerLibrary.Heightmap1);
			Session.GrabResponse().AppendString(mRoom.GrabModel().Heightmap);
			Session.SendResponse();
			
			Session.GrabResponse().Initialize(ComposerLibrary.Heightmap2);
			Session.GrabResponse().AppendString(mRoom.GrabModel().RelativeHeightmap());
			Session.SendResponse();
		}
		
		(new FinishRoomLoadEvent()).Parse(Session, Request);
	}
	
}
