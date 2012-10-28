package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.rooms.Room;
import net.cobem.grizzly.habbohotel.rooms.items.FloorItem;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class FinishRoomLoadEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		Room mRoom = Session.GrabActor().CurrentRoom;
		
		Session.GrabResponse().Initialize(ComposerLibrary.FloorItems);
		Session.GrabResponse().AppendInt32(1);
		Session.GrabResponse().AppendInt32(mRoom.Owner);
		Session.GrabResponse().AppendString(mRoom.OwnerByName); // Owner Name
		Session.GrabResponse().AppendInt32(mRoom.FloorItems.size()); //Floor Item Count
		
		for(FloorItem Item : mRoom.FloorItems.values())
		{
			Session.GrabResponse().AppendBody(Item);
			Session.GrabResponse().AppendInt32(mRoom.Owner);
		}
		
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
		
		mRoom.SendMessage(mRoom.GrabActorStatus());
		mRoom.SendMessage(mRoom.GrabRoomStatus());
		
		Session.GrabResponse().Initialize(ComposerLibrary.RoomPanel);
		Session.GrabResponse().AppendBoolean(true);
		Session.GrabResponse().AppendInt32(Session.GrabActor().CurrentRoom.ID);
		Session.GrabResponse().AppendBoolean(true);
		
		Session.SendResponse();

        Session.GrabResponse().Initialize(ComposerLibrary.RoomData);
        Session.GrabResponse().AppendBoolean(true);
		Session.GrabResponse().AppendInt32(mRoom.ID);
		Session.GrabResponse().AppendBoolean(false);
		Session.GrabResponse().AppendString(mRoom.Title);
		Session.GrabResponse().AppendBoolean(true);
		Session.GrabResponse().AppendInt32(mRoom.Owner);
		Session.GrabResponse().AppendString(mRoom.OwnerByName); // Owner name
		Session.GrabResponse().AppendInt32(0);
		Session.GrabResponse().AppendInt32(mRoom.GrabParty().size());
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
        
        Grizzly.WriteOut(Session.GrabHabbo().Username + " entered room " + mRoom.ID);
		
        Session.GrabActor().GoalPosition = null;
        
        mRoom.RegenerateCollisionMap();
	}
}
