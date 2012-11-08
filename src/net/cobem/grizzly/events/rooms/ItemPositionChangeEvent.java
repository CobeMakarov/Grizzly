package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.EventResponse;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.rooms.Room;
import net.cobem.grizzly.habbohotel.rooms.items.FloorItem;
import net.cobem.grizzly.habbohotel.sessions.Session;
import net.cobem.grizzly.habbohotel.pathfinding.Position;

public class ItemPositionChangeEvent implements Event
{

	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		if(!Session.GrabActor().CurrentRoom.GrabRightHolders().contains(Session.GrabHabbo().ID))
		{
			return;
		}
		
		Room mRoom = Session.GrabActor().CurrentRoom;
		
		int ID = Request.PopInt();
		int X = Request.PopInt();
		int Y = Request.PopInt();
		int Rotation = Request.PopInt();
		
		FloorItem Item = mRoom.FloorItems.get(ID);
		
		if (Item == null)
		{
			return;
		}
		
		Item.X = X;
		Item.Y = Y;
		
		Float Z = (float)mRoom.GrabModel().GrabTileHeight(X, Y);
		
		for (FloorItem fItem : mRoom.GrabFloorItems().values())
		{
			if (fItem.X == Item.X && fItem.Y == Item.Y && fItem.ID != Item.ID)
			{
				if (!fItem.GetBaseItem().Stackable)
				{
					Grizzly.WriteOut("non-stackable");
					return;
				}
				
				Grizzly.WriteOut("Old Height: " + Item.Height);
				
				Z += fItem.GetBaseItem().StackHeight;
				
				Grizzly.WriteOut("New Height: " + Z);
			}
		}
		
		Item.Height = Z;
		
		Item.Rotation = Rotation;
		
		for (Position mPosition : mRoom.GrabUserPositions().values())
		{
			if (mPosition.X == X && mPosition.Y == Y)
			{
				Grizzly.WriteOut("return 2");
				return;
			}
		}
		
		Grizzly.GrabDatabase().RunFastQuery("UPDATE server_room_items SET x = '" + Item.X + "', y = '" + Item.Y + "', rotation = '" + Item.Rotation + "', height = '" + Item.Height + "' WHERE id = '" + ID + "'");
		
		EventResponse Message = new EventResponse();
		
		Message.Initialize(ComposerLibrary.UpdateFloorItem);
		Message.AppendInt32(ID);
		Message.AppendInt32(Item.GetBaseItem().Sprite);
		Message.AppendInt32(Item.X);
		Message.AppendInt32(Item.Y);
		Message.AppendInt32(Item.Rotation);
		Message.AppendString(Item.Height.toString()); 
		Message.AppendInt32(0);
		Message.AppendInt32(0);
		Message.AppendString("0"); // TODO: Triggers
		Message.AppendInt32(-1);
		Message.AppendInt32(Item.GetBaseItem().Interaction.equals("default") ? 1 : 0);
		Message.AppendInt32(Session.GrabActor().CurrentRoom.Owner);
	
		mRoom.GrabFloorItems().remove(ID);
		mRoom.GrabFloorItems().put(ID, new FloorItem(ID));
		
		mRoom.RegenerateCollisionMap();
		
		Session.GrabActor().CurrentRoom.SendMessage(Message);
	}

}
