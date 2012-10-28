package net.cobem.grizzly.events.user;

import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.furni.Furniture;
import net.cobem.grizzly.habbohotel.sessions.Session;

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
		
		for(Furniture Item : Session.GrabHabbo().GrabItems().GrabFloors().values())
		{
			Session.GrabResponse().AppendInt32(Item.ID);
			Session.GrabResponse().AppendString(Item.Type.toUpperCase());
			Session.GrabResponse().AppendInt32(Item.ID);
			Session.GrabResponse().AppendInt32(Item.Sprite);
			Session.GrabResponse().AppendInt32(1);
			Session.GrabResponse().AppendString("");
			Session.GrabResponse().AppendInt32(0);
			Session.GrabResponse().AppendBoolean(Item.Recyclable);
			Session.GrabResponse().AppendBoolean(Item.Tradeable);
			Session.GrabResponse().AppendBoolean(Item.Stackable);
			Session.GrabResponse().AppendBoolean(Item.Sellable);
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
		
		for(Furniture Item : Session.GrabHabbo().GrabItems().GrabWalls().values())
		{
			Session.GrabResponse().AppendInt32(Item.ID);
			Session.GrabResponse().AppendString(Item.Type.toUpperCase());
			Session.GrabResponse().AppendInt32(Item.ID);
			Session.GrabResponse().AppendInt32(Item.Sprite);
			
			if (Item.ItemTitle.contains("floor") || Item.ItemTitle.contains("a2"))
			{
				 Session.GrabResponse().AppendInt32(3);
			}
			else if (Item.ItemTitle.contains("wall"))
			{
				Session.GrabResponse().AppendInt32(2);
			}
			else if (Item.ItemTitle.contains("land"))
			{
				Session.GrabResponse().AppendInt32(4); 
			}
			else
			{
				Session.GrabResponse().AppendInt32(1);
			}
			
			Session.GrabResponse().AppendInt32(0);
			
			if ((Item.ItemTitle.contains("floor") || Item.ItemTitle.contains("wall")) && Item.ItemTitle.split("_")[2] != null)
			{
				Session.GrabResponse().AppendString(Item.ItemTitle.split("_")[2]);
			}
			else
			{
				Session.GrabResponse().AppendString("");
			}
			
			Session.GrabResponse().AppendBoolean(Item.Recyclable);
			Session.GrabResponse().AppendBoolean(Item.Tradeable);
			Session.GrabResponse().AppendBoolean(Item.Stackable);
			Session.GrabResponse().AppendBoolean(Item.Sellable);
			Session.GrabResponse().AppendInt32(-1);
		}
		Session.SendResponse();
	}
}
