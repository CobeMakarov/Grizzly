package com.grizzly.habbohotel.furni;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.grizzly.Grizzly;

public class FurnitureHandler 
{
	private Map<Integer, Furniture> Furniture;
	
	public FurnitureHandler() throws SQLException
	{
		Furniture = new HashMap<Integer, Furniture>();
		
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_furni");
		
		ResultSet Furni = Grizzly.GrabDatabase().GrabTable();
		
		while(Furni.next())
		{
			Furniture.put(new Integer(Furni.getInt("id")), new Furniture(Furni));
		}
		
		Grizzly.WriteOut("Loaded " + Furniture.size() + " furniture items!");
	}
	
	public Furniture GrabFurniByID(int ID)
	{
		if (Furniture.containsKey(ID))
		{
			return Furniture.get(ID);
		}
		
		return null;
	}
}
