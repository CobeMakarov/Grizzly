package net.cobem.grizzly.habbohotel.users.items;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.cobem.grizzly.Grizzly;


public class ItemHandler 
{
	private int ID;
	
	private Map<Integer, InventoryItem> Inventory;
	
	public ItemHandler(int ID) throws SQLException
	{
		this.ID = ID;
		
		Inventory = new HashMap<Integer, InventoryItem>();
		
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_users_items WHERE user = '" + ID + "'");
		
		if (Grizzly.GrabDatabase().RowCount() > 0)
		{
			ResultSet Items = Grizzly.GrabDatabase().GrabTable();
			
			while(Items.next())
			{	
				Inventory.put(new Integer(Items.getInt("id")), new InventoryItem(Items.getInt("id"), Items.getInt("user"), Items.getInt("item")));
			}
			
		}
	}
	
	public boolean RemoveItem(int ID, boolean Perm)
	{
		if (Perm)
		{
			Grizzly.GrabDatabase().RunFastQuery("DELETE FROM server_users_items WHERE id = '" + ID + "' AND user = '" + this.ID + "'");
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
		
		Grizzly.GrabDatabase().SetQuery("SELECT id FROM server_users_items WHERE user = '" + this.ID + "' AND item = '" + FurniID + "' ORDER BY id DESC LIMIT 1");
		
		
		Inventory.put(new Integer(Grizzly.GrabDatabase().GrabInt()), new InventoryItem(Grizzly.GrabDatabase().GrabInt(), this.ID, FurniID));
		return true;
	}
	
	public Map<Integer, InventoryItem> GrabInventory()
	{
		return Inventory;
	}
	
	public Map<Integer, InventoryItem> GrabFloors()
	{
		Map<Integer, InventoryItem> Items = new HashMap<Integer, InventoryItem>();
		
		for(InventoryItem Item : this.Inventory.values())
		{
			if (Item.GrabBaseItem().Type.contains("s"))
			{
				Items.put(Item.ID, Item);
			}
		}
		return Items;
	}
	
	public Map<Integer, InventoryItem> GrabWalls()
	{
		Map<Integer, InventoryItem> Items = new HashMap<Integer, InventoryItem>();
		
		for(InventoryItem Item : this.Inventory.values())
		{
			if (Item.GrabBaseItem().Type.contains("i"))
			{
				Items.put(Item.ID, Item);
			}
		}
		
		return Items;
	}
}
