package net.cobem.grizzly.events.user;

import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.sessions.Session;
import net.cobem.grizzly.habbohotel.users.items.InventoryItem;

public class InitializeInventoryEvent implements Event
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		Session.GrabResponse().Initialize(ComposerLibrary.SendInventory);
		Session.GrabResponse().AppendString("S");
		Session.GrabResponse().AppendInt32(1);
		Session.GrabResponse().AppendInt32(1);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().GrabItems().GrabFloors().size());
		
		for(InventoryItem Item : Session.GrabHabbo().GrabItems().GrabFloors().values())
		{
			Session.GrabResponse().AppendInt32(Item.ID);
			Session.GrabResponse().AppendString(Item.GrabBaseItem().Type.toUpperCase());
			Session.GrabResponse().AppendInt32(Item.ID);
			Session.GrabResponse().AppendInt32(Item.GrabBaseItem().Sprite);
			Session.GrabResponse().AppendInt32(1);
			Session.GrabResponse().AppendString("");
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendBoolean(Item.GrabBaseItem().Recyclable);
			Session.GrabResponse().AppendBoolean(Item.GrabBaseItem().Tradeable);
			Session.GrabResponse().AppendBoolean(Item.GrabBaseItem().Stackable);
			Session.GrabResponse().AppendBoolean(Item.GrabBaseItem().Sellable);
			Session.GrabResponse().AppendInt32(-1);
			Session.GrabResponse().AppendString("");
			Session.GrabResponse().AppendInt32(0);
		}
		Session.SendResponse();
		
		Session.GrabResponse().Initialize(ComposerLibrary.SendInventory);
		Session.GrabResponse().AppendString("I");
		Session.GrabResponse().AppendInt32(1);
		Session.GrabResponse().AppendInt32(1);
		Session.GrabResponse().AppendInt32(Session.GrabHabbo().GrabItems().GrabWalls().size());
		
		for(InventoryItem Item : Session.GrabHabbo().GrabItems().GrabWalls().values())
		{
			Session.GrabResponse().AppendInt32(Item.ID);
			Session.GrabResponse().AppendString(Item.GrabBaseItem().Type.toUpperCase());
			Session.GrabResponse().AppendInt32(Item.ID);
			Session.GrabResponse().AppendInt32(Item.GrabBaseItem().Sprite);
			
			if (Item.GrabBaseItem().ItemTitle.contains("floor") || Item.GrabBaseItem().ItemTitle.contains("a2"))
			{
				 Session.GrabResponse().AppendInt32(3);
			}
			else if (Item.GrabBaseItem().ItemTitle.contains("wall"))
			{
				Session.GrabResponse().AppendInt32(2);
			}
			else if (Item.GrabBaseItem().ItemTitle.contains("land"))
			{
				Session.GrabResponse().AppendInt32(4); 
			}
			else
			{
				Session.GrabResponse().AppendInt32(1);
			}
			
			Session.GrabResponse().AppendInt32(0);
			
			if ((Item.GrabBaseItem().ItemTitle.contains("floor") || Item.GrabBaseItem().ItemTitle.contains("wall")) && Item.GrabBaseItem().ItemTitle.split("_")[2] != null)
			{
				Session.GrabResponse().AppendString(Item.GrabBaseItem().ItemTitle.split("_")[2]);
			}
			else
			{
				Session.GrabResponse().AppendString("");
			}
			
			Session.GrabResponse().AppendBoolean(Item.GrabBaseItem().Recyclable);
			Session.GrabResponse().AppendBoolean(Item.GrabBaseItem().Tradeable);
			Session.GrabResponse().AppendBoolean(Item.GrabBaseItem().Stackable);
			Session.GrabResponse().AppendBoolean(Item.GrabBaseItem().Sellable);
			Session.GrabResponse().AppendInt32(-1);
		}
		Session.SendResponse();
	}
}
