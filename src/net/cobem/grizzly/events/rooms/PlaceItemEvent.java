package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.EventResponse;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.events.user.InitializeInventoryEvent;
import net.cobem.grizzly.habbohotel.furni.Furniture;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class PlaceItemEvent implements Event
{

	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		if (Session.GrabActor().CurrentRoom.Owner != Session.GrabHabbo().ID)
		{
			return;
		}
		
		EventResponse Message = new EventResponse();
		
		String PlaceData = Request.PopFixedString();
		String[] Bits = PlaceData.split(" ");
		Integer ID = Integer.parseInt(Bits[0]);
		
		Integer X = Integer.parseInt(Bits[1]);
		Integer Y = Integer.parseInt(Bits[2]);
		Integer Rotation = Integer.parseInt(Bits[3]);
		
		//TODO: Wall Items
		Furniture Item = Session.GrabHabbo().GrabItems().GrabFloors().get(ID);
		
		Grizzly.GrabDatabase().RunFastQuery("INSERT INTO server_room_items (room, x, y, base, rotation, height, extra) VALUES ('" + Session.GrabActor().CurrentRoom.ID + "', '" + X + "', '" + Y + "', '" + Item.ID + "', '" + Rotation + "', '" + (float)Session.GrabActor().CurrentRoom.GrabModel().GrabTileHeight(X, Y) + "',' ')");
		
		Message.Initialize(ComposerLibrary.SendFloorItem);
		Message.AppendInt32(ID);
		Message.AppendInt32(Item.Sprite);
		Message.AppendInt32(X);
		Message.AppendInt32(Y);
		Message.AppendInt32(Rotation);
		Message.AppendString(Float.toString((float)Session.GrabActor().CurrentRoom.GrabModel().GrabTileHeight(X, Y))); 
		Message.AppendInt32(0);
		Message.AppendInt32(0);
		Message.AppendString("0"); // TODO: Triggers
		Message.AppendInt32(-1);
		Message.AppendInt32(Item.Interaction.equals("default") ? 1 : 0);
		Message.AppendInt32(Session.GrabActor().CurrentRoom.Owner);
		Message.AppendString(Session.GrabActor().CurrentRoom.OwnerByName);
		
		Session.GrabActor().CurrentRoom.SendMessage(Message);
		
		Session.GrabHabbo().GrabItems().RemoveItem(ID, true);
		
		Message.Initialize(ComposerLibrary.DisposeItemFromInventory);
		Message.AppendInt32(ID);
		Session.SendResponse(Message);
		
		(new InitializeInventoryEvent()).Parse(Session, Request);
		
		Session.GrabActor().CurrentRoom.DropItem(); //Not sure what the hell is going on..
		
		Session.GrabActor().CurrentRoom.RegenerateCollisionMap();
	}

}
