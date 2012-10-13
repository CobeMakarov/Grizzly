package com.grizzly.events.rooms;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.EventResponse;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.rooms.Room;
import com.grizzly.habbohotel.sessions.Session;

public class GetRoomDataEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		if (!Session.GrabRoomUser().InRoom())
		{
			return;
		}
		
		Room mRoom = Grizzly.GrabHabboHotel().GrabRoomHandler().GrabRoomById(Request.PopInt());
		
		EventResponse Message = new EventResponse();
        
        Message.Initialize(ComposerLibrary.SerializeRoomData);
        Message.AppendInt32(mRoom.ID);
        Message.AppendString(mRoom.Title);
        Message.AppendString(mRoom.Description);
        Message.AppendInt32(mRoom.State);
        Message.AppendInt32(1);
        Message.AppendInt32(25);
        Message.AppendInt32(50);
        Message.AppendInt32(0); // tags count
        Message.AppendInt32(0); // Room Blocking Disabled
        Message.AppendInt32(0); // allowed pets
        Message.AppendInt32(0); // pets can eat
        Message.AppendInt32(0); // users walkable
        Message.AppendInt32(0); // hide walls
        Message.AppendInt32(1); // Walls Type
        Message.AppendInt32(1); // Floors Type
        Session.SendResponse(Message);
	}
}
