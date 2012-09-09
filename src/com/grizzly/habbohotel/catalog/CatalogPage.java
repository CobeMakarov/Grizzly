package com.grizzly.habbohotel.catalog;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CatalogPage 
{
	public int ID;
	public int Parent;
	public int Order;
	public int IconColor;
	public int IconImage;
	public int Rank;
	
	public CatalogLayout Layout;
	
	public String Title;
	public String Header;
	public String Description;
	
	public CatalogPage(ResultSet Page)
	{
		//Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_store_pages WHERE id = '" + ID + "'");
		
		//ResultSet Page = Grizzly.GrabDatabase().GrabRow();
		
		FillClass(Page);
	}
	
	private boolean FillClass(ResultSet Set)
	{
		try
		{
			this.ID = Set.getInt("id");
			this.Parent = Set.getInt("parent");
			this.Order = Set.getInt("order");
			this.IconColor = Set.getInt("icon_color");
			this.IconImage = Set.getInt("icon_image");
			this.Rank = Set.getInt("minimum_rank");
			
			this.Layout = CatalogLayout.valueOf(Set.getString("layout"));
			
			this.Title = Set.getString("title");
			this.Header = Set.getString("header");
			this.Description = Set.getString("description");
			return true;
		}
		catch(SQLException Ex)
		{
			return false;
		}
	}
}
