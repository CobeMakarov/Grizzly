package com.grizzly.events.rooms;

import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.rooms.Room;
import com.grizzly.habbohotel.sessions.Session;

public class ParseHeightMapEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		Room mRoom = Session.GrabRoomUser().CurrentRoom;
		
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
