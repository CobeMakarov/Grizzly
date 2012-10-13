package com.grizzly.events.rooms;

import com.grizzly.Grizzly;
import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.pathfinding.Position;
import com.grizzly.habbohotel.rooms.Room;
import com.grizzly.habbohotel.sessions.Session;

public class InitializeRoomEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		(new LeaveRoomEvent()).Parse(Session, Request);
		
		int ID;
		
		if (Session.OverideID == 0)
		{
			ID = Request.PopInt();
		}
		else
		{
			ID = Session.OverideID;
			Session.OverideID = 0;
		}
		
		if (Grizzly.GrabHabboHotel().GrabRoomHandler().GrabRoomById(ID) == null)
		{
			try
			{
				Grizzly.GrabHabboHotel().GrabRoomHandler().GrabRooms().put(new Integer(ID), new Room(ID));
			}
			catch(Exception Ex)
			{
				//Room doesn't exist.
				return;
			}
		}
		
//		Grizzly.WriteOut(Session.GrabHabbo().Username + " is joining room " + ID);
		
		Room mRoom = Grizzly.GrabHabboHotel().GrabRoomHandler().GrabRoomById(ID);
		
		if (mRoom.GrabRoomUsers().size() == 50)
		{
			Session.GrabResponse().Initialize(ComposerLibrary.RoomFull);
			Session.GrabResponse().AppendInt32(1);
			Session.GrabResponse().AppendBoolean(false);
			Session.GrabResponse().AppendBoolean(false);
			Session.SendResponse();
		}
		
		Session.GrabRoomUser().CurrentRoom = mRoom;
		
		mRoom.GrabRoomUsers().put(new Integer(Session.GrabHabbo().ID), Session);
		
		Session.GrabRoomUser().CurrentPosition = new Position(
				mRoom.GrabModel().DoorX + 1,
				mRoom.GrabModel().DoorY,
				mRoom.GrabModel().DoorZ); 
		
		Session.GrabRoomUser().Rotation = mRoom.GrabModel().DoorRot;
		
		Session.GrabResponse().Initialize(ComposerLibrary.InitRoomProcess);
		Session.SendResponse();

		Session.GrabResponse().Initialize(ComposerLibrary.ModelAndId);
		Session.GrabResponse().AppendString(mRoom.Model); 
		Session.GrabResponse().AppendInt32(mRoom.ID);
		Session.SendResponse();
		
		if (mRoom.Wallpaper != "0")
		{
			Session.GrabResponse().Initialize(ComposerLibrary.Papers);
			Session.GrabResponse().AppendString("wallpaper");
			Session.GrabResponse().AppendString(mRoom.Wallpaper);
			Session.SendResponse();
		}

		if (mRoom.Floor != "0")
		{
			Session.GrabResponse().Initialize(ComposerLibrary.Papers);
			Session.GrabResponse().AppendString("floor");
			Session.GrabResponse().AppendString(mRoom.Floor);
			Session.SendResponse();
		}

		Session.GrabResponse().Initialize(ComposerLibrary.Papers);
		Session.GrabResponse().AppendString("landscape");
		Session.GrabResponse().AppendString(mRoom.Landscape);
		Session.SendResponse();

		if (mRoom.GrabRightHolders().containsKey(Session.GrabHabbo().ID))
		{
			Session.GrabResponse().Initialize(ComposerLibrary.LoadRightsOnRoom);
			Session.GrabResponse().AppendInt32(4);
			Session.SendResponse();

			Session.GrabResponse().Initialize(ComposerLibrary.RoomOwnerPower);
			Session.SendResponse();
		}

		Session.GrabResponse().Initialize(ComposerLibrary.RoomEvents);
		Session.GrabResponse().AppendString("-1");
		Session.SendResponse();
	}
}
