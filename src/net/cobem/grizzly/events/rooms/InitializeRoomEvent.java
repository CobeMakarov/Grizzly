package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.pathfinding.Position;
import net.cobem.grizzly.habbohotel.rooms.Room;
import net.cobem.grizzly.habbohotel.sessions.Session;

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
		
		if (mRoom.GrabParty().size() == 50)
		{
			Session.GrabResponse().Initialize(ComposerLibrary.RoomFull);
			Session.GrabResponse().AppendInt32(1);
			Session.GrabResponse().AppendBoolean(false);
			Session.GrabResponse().AppendBoolean(false);
			Session.SendResponse();
		}
		
		Session.GrabActor().CurrentRoom = mRoom;
		
		mRoom.GrabParty().put(new Integer(Session.GrabHabbo().ID), Session);
		
		Session.GrabActor().CurrentPosition = new Position(
				mRoom.GrabModel().DoorX,
				mRoom.GrabModel().DoorY,
				mRoom.GrabModel().DoorZ); 
		
		Session.GrabActor().Rotation = mRoom.GrabModel().DoorRot;
		
		Session.GrabResponse().Initialize(ComposerLibrary.InitRoomProcess);
		Session.SendResponse();

		Session.GrabResponse().Initialize(ComposerLibrary.ModelAndId);
		Session.GrabResponse().AppendString(mRoom.Model); 
		Session.GrabResponse().AppendInt32(mRoom.ID);
		Session.SendResponse();
		
		if (!mRoom.Wallpaper.equals("0.0"))
		{
			Session.GrabResponse().Initialize(ComposerLibrary.Papers);
			Session.GrabResponse().AppendString("wallpaper");
			Session.GrabResponse().AppendString(mRoom.Wallpaper);
			Session.SendResponse();
		}

		if (!mRoom.Floor.equals("0.0"))
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

		if(Session.GrabActor().CurrentRoom.GrabRightHolders().contains(Session.GrabHabbo().ID))
		{
			Session.GrabResponse().Initialize(ComposerLibrary.LoadRightsOnRoom);
			Session.GrabResponse().AppendInt32(4);
			Session.SendResponse();
		}
		
		if (mRoom.Owner == Session.GrabHabbo().ID)
		{
			Session.GrabResponse().Initialize(ComposerLibrary.RoomOwnerPower);
			Session.SendResponse();
		}
		
		Session.GrabResponse().Initialize(ComposerLibrary.RoomEvents);
		Session.GrabResponse().AppendString("-1");
		Session.SendResponse();
	}
}
