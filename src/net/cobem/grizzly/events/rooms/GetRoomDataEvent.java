package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.EventResponse;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.rooms.Room;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class GetRoomDataEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		if (!Session.GrabActor().InRoom())
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
