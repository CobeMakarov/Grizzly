package net.cobem.grizzly.habbohotel.catalog;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.habbohotel.furni.Furniture;


public class CatalogItem 
{
	public int ID;
	public int Page;
	public int Cost;
	public int Quantity;
	public int Item;
	
	public String Title;
	
	public Boolean ForVIP;
	
	public CatalogItem(ResultSet Page)
	{
		//Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_store_items WHERE id = '" + ID + "'");
		
		//ResultSet Page = Grizzly.GrabDatabase().GrabRow();
		
		FillClass(Page);
	}
	
	public CatalogItem(int ID)
	{
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_store_items WHERE id = '" + ID + "'");
		
		ResultSet Page = Grizzly.GrabDatabase().GrabRow();
		
		FillClass(Page);
	}
	
	private boolean FillClass(ResultSet Set)
	{
		try
		{
			this.ID = Set.getInt("id");
			this.Page = Set.getInt("page");
			this.Cost = Set.getInt("cost");
			this.Quantity = Set.getInt("quantity");
			this.Item = Set.getInt("unique_id");

			this.ForVIP = (Set.getInt("for_vip") == 1);
			
			this.Title = Set.getString("store_title");
			
			
			return true;
		}
		catch(SQLException Ex)
		{
			return false;
		}
	}
	
	public Furniture GrabBaseItem()
	{
		return Grizzly.GrabHabboHotel().GrabFurnitureHandler().GrabFurniByID(this.Item);
	}
}
