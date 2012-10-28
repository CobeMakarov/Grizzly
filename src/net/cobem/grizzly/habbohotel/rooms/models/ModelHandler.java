package net.cobem.grizzly.habbohotel.rooms.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.cobem.grizzly.Grizzly;


public class ModelHandler 
{
	private Map<String, Model> Models;
	
	public ModelHandler()
	{
		Models = new HashMap<String, Model>();
		
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_room_models");
		
		try 
		{
			ResultSet Results = Grizzly.GrabDatabase().GrabTable();
			
			while (Results.next())
			{
				Models.put(Results.getString("id"), new Model(Results));
			}
		} 
		catch (SQLException e) 
		{
			//
		}
		
		Grizzly.WriteOut("Loaded and stored " + Models.size() + " models!");
	}
	
	public Model GrabModel(String Name)
	{
		return Models.get(Name);
	}
}
