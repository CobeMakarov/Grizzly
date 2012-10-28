package net.cobem.grizzly.habbohotel.users.items;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.habbohotel.furni.Furniture;


public class ItemHandler 
{
	private int ID;
	
	private Map<Integer, Furniture> Inventory;
	
	public ItemHandler(int ID) throws SQLException
	{
		this.ID = ID;
		
		Inventory = new HashMap<Integer, Furniture>();
		
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_users_items WHERE user = '" + ID + "'");
		
		if (Grizzly.GrabDatabase().RowCount() > 0)
		{
			ResultSet Items = Grizzly.GrabDatabase().GrabTable();
			
			while(Items.next())
			{	
				Inventory.put(new Integer(Items.getInt("item")), Grizzly.GrabHabboHotel().GrabFurnitureHandler().GrabFurniByID(Items.getInt("item")));
			}
			
		}
	}
	
	public boolean RemoveItem(int ID, boolean Perm)
	{
		if (Perm)
		{
			Grizzly.GrabDatabase().RunFastQuery("DELETE FROM server_users_items WHERE item = '" + ID + "' AND user = '" + this.ID + "'");
		}
		
		Inventory.remove(ID);
		return true;
	}
	
	public boolean AddItem(int FurniID, boolean Perm)
	{
		if (Perm)
		{
			Grizzly.GrabDatabase().RunFastQuery("INSERT INTO server_users_items (user, item) VALUES ('" + this.ID + "', '" + FurniID + "')");
		}
		
		Inventory.put(new Integer(FurniID), Grizzly.GrabHabboHotel().GrabFurnitureHandler().GrabFurniByID(FurniID));
		return true;
	}
	
	public Map<Integer, Furniture> GrabInventory()
	{
		return Inventory;
	}
	
	public Map<Integer, Furniture> GrabFloors()
	{
		Map<Integer, Furniture> Items = new HashMap<Integer, Furniture>();
		
		for(Furniture Item : this.Inventory.values())
		{
			if (Item.Type.contains("s"))
			{
				Items.put(Item.ID, Item);
			}
		}
		return Items;
	}
	
	public Map<Integer, Furniture> GrabWalls()
	{
		Map<Integer, Furniture> Items = new HashMap<Integer, Furniture>();
		
		for(Furniture Item : this.Inventory.values())
		{
			if (Item.Type.contains("i"))
			{
				Items.put(Item.ID, Item);
			}
		}
		
		return Items;
	}
}
