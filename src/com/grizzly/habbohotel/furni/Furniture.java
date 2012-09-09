package com.grizzly.habbohotel.furni;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Furniture
{
	public int ID;	
	public int Width;
	public int Length;
	public int Sprite;
    public int InteractionModesCount;
    
	public String PublicTitle;
	public String ItemTitle;
	public String Type;
    public String Interaction;
    
	public Float StackHeight;
	
	public Boolean Stackable;
	public Boolean Sitable;
	public Boolean Walkable;
	public Boolean Recyclable;
	public Boolean Tradeable;
	public Boolean Sellable;
    public Boolean Giftable;
    
    public List<Integer> Vendors;
    
    public Furniture(ResultSet Furni)
	{
		//Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_furni WHERE id = '" + ID + "'");
		
		//ResultSet Furni = Grizzly.GrabDatabase().GrabRow();
		
		FillClass(Furni);
	}
	
	private boolean FillClass(ResultSet Set)
	{
		try
		{
			this.ID = Set.getInt("id");
			this.Width = Set.getInt("width");
			this.Length = Set.getInt("length");
			this.Sprite = Set.getInt("sprite_id");
			this.InteractionModesCount = Set.getInt("id");
			
			this.PublicTitle = Set.getString("public_name");
			this.ItemTitle = Set.getString("item_name");
			this.Type = Set.getString("type");
			this.Interaction = Set.getString("interaction_type"); //TODO: Do this properly with an enum just like CatalogLayout
			
			this.StackHeight = Set.getFloat("stack_height");
			
			this.Stackable = (Set.getInt("can_stack") == 1);
			this.Sitable = (Set.getInt("can_sit") == 1);
			this.Walkable = (Set.getInt("is_walkable") == 1);
			this.Recyclable = (Set.getInt("allow_recycle") == 1);
			this.Tradeable = (Set.getInt("allow_trade") == 1);
			this.Sellable = (Set.getInt("allow_marketplace_sell") == 1);
			this.Giftable = (Set.getInt("allow_gift") == 1);
			
			return true;
		}
		catch(SQLException Ex)
		{
			return false;
		}
	}

}
