package com.grizzly.events.navigator;

import java.util.Map;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.rooms.Room;
import com.grizzly.habbohotel.sessions.Session;

public class ViewMyRoomsEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		Map<Integer, Room> UserRooms = Grizzly.GrabHabboHotel().GrabRoomHandler().GrabUsersRooms(Session.GrabHabbo().ID);
		
		Session.GrabResponse().Initialize(ComposerLibrary.OwnRooms);
		Session.GrabResponse().AppendInt32(2);
		Session.GrabResponse().AppendString("");
		Session.GrabResponse().AppendInt32(UserRooms.size());
		
		for (Room mRoom : UserRooms.values())
		{
			Session.GrabResponse().AppendInt32(mRoom.ID);
			Session.GrabResponse().AppendBoolean(false);
			Session.GrabResponse().AppendString(mRoom.Title);
			Session.GrabResponse().AppendBoolean(true);
			Session.GrabResponse().AppendInt32(mRoom.Owner);
			Session.GrabResponse().AppendString(Session.GrabHabbo().Username);
			Session.GrabResponse().AppendInt32(mRoom.State); // mRoom state
			Session.GrabResponse().AppendInt32(mRoom.GrabRoomUsers().size());
			Session.GrabResponse().AppendInt32(50);// Max users
			Session.GrabResponse().AppendString(mRoom.Description);
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendInt32(0); // can trade?
			Session.GrabResponse().AppendInt32(1);
			Session.GrabResponse().AppendInt32(3);
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendString("");

			Session.GrabResponse().AppendInt32(1);
			Session.GrabResponse().AppendString(""); // Notsure

			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendBoolean(true);
			Session.GrabResponse().AppendBoolean(true);
		}
		
		Session.GrabResponse().AppendBoolean(false);
		Session.SendResponse();
	}
}
