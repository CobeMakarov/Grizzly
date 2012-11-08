package net.cobem.grizzly.habbohotel.users.items;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.habbohotel.furni.Furniture;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class InventoryItem
{
	public int ID;
	private Session Owner;
	private Furniture BaseItem;
	
	public InventoryItem(int mID, int Owner, int ItemID)
	{
		this.ID = mID;
		
		this.Owner = Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSessionByUserID(Owner);
		
		this.BaseItem = Grizzly.GrabHabboHotel().GrabFurnitureHandler().GrabFurniByID(ItemID);
	}
	
	public Session GrabOwner()
	{
		return Owner;
	}
	
	public Furniture GrabBaseItem()
	{
		return BaseItem;
	}
}
