package com.grizzly.events.rooms;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.rooms.Room;
import com.grizzly.habbohotel.sessions.Session;

public class FinishRoomLoadEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		Room mRoom = Session.GrabRoomUser().CurrentRoom;
		
		Session.GrabResponse().Initialize(ComposerLibrary.FloorItems);
		Session.GrabResponse().AppendInt32(1);
		Session.GrabResponse().AppendInt32(mRoom.Owner);
		Session.GrabResponse().AppendString(mRoom.OwnerByName); // Owner Name
		Session.GrabResponse().AppendInt32(0); //Floor Item Count
		
		//Iterate through Floor Items
		Session.SendResponse();
		
		Session.GrabResponse().Initialize(ComposerLibrary.WallItems);
		Session.GrabResponse().AppendInt32(1);
		Session.GrabResponse().AppendInt32(mRoom.Owner);
		Session.GrabResponse().AppendString(mRoom.OwnerByName); // Owner Name
		Session.GrabResponse().AppendInt32(0); //Wall Item Count
		
		//Iterate through Wall Items
		Session.SendResponse();

		Session.GrabResponse().Initialize(ComposerLibrary.BeforeUsers);
		Session.GrabResponse().AppendInt32(1);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().ID);
		Session.GrabResponse().AppendString(Session.GrabHabbo().Username);
		Session.GrabResponse().AppendInt32(0);
		
		Session.SendResponse();
		
		mRoom.SendMessage(mRoom.GrabRoomUserStatus());
		mRoom.SendMessage(mRoom.GrabRoomStatus());
		
		Session.GrabResponse().Initialize(ComposerLibrary.RoomPanel);
		Session.GrabResponse().AppendBoolean(true);
		Session.GrabResponse().AppendInt32(Session.GrabRoomUser().CurrentRoom.ID);
		Session.GrabResponse().AppendBoolean(true);
		
		Session.SendResponse();

        Session.GrabResponse().Initialize(ComposerLibrary.RoomData);
        Session.GrabResponse().AppendBoolean(true);
		Session.GrabResponse().AppendInt32(mRoom.ID);
		Session.GrabResponse().AppendBoolean(false);
		Session.GrabResponse().AppendString(mRoom.Title);
		Session.GrabResponse().AppendBoolean(true);
		Session.GrabResponse().AppendInt32(mRoom.Owner);
		Session.GrabResponse().AppendString("Makarov"); // Owner name
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(mRoom.GrabRoomUsers().size());
		Session.GrabResponse().AppendInt32(25);
		Session.GrabResponse().AppendString(mRoom.Description);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(1);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendString("");
		Session.GrabResponse().AppendString("");
		Session.GrabResponse().AppendString("");
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendBoolean(true);
		Session.GrabResponse().AppendBoolean(true);
        Session.GrabResponse().AppendBoolean(false);
        
        Session.SendResponse();
        
        Grizzly.WriteOut(Session.GrabHabbo().Username + " entered room " + Session.GrabHabbo().ID);
		
	}
}
