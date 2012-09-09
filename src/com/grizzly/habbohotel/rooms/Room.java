package com.grizzly.habbohotel.rooms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.grizzly.Grizzly;
import com.grizzly.events.EventResponse;
import com.grizzly.habbohotel.sessions.Session;

public class Room 
{
	
	public int ID;
	public int Owner;
	public int State;

	public String Floor;
	public String Description;
	public String Model;
	public String Title;
	public String Wallpaper;
	public String Landscape;

	private Map<Integer, Session> Party = new HashMap<Integer, Session>();
	private Map<Integer, Session> RightsHolders = new HashMap<Integer, Session>();
	
	public Room(ResultSet Room)
	{
		//Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_rooms WHERE id = '" + ID + "'");
		
		//ResultSet Room = Grizzly.GrabDatabase().GrabRow();
		
		this.FillClass(Room);
	}
	
	public Room(int ID)
	{
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_rooms WHERE id = '" + ID + "'");
		
		ResultSet Room = Grizzly.GrabDatabase().GrabRow();
		
		this.FillClass(Room);
	}
	
	private boolean FillClass(ResultSet Set)
	{
		try
		{
			this.ID = Set.getInt("id");
			this.Owner = Set.getInt("owner");
			this.State = Set.getInt("status");
			
			this.Floor = Set.getString("floor");
			this.Description = Set.getString("description");
			this.Model = Set.getString("model");
			this.Title = Set.getString("name");
			this.Wallpaper = Set.getString("wallpaper");
			this.Landscape = Set.getString("outside");
			
			return true;
		}
		catch(SQLException Ex)
		{
			return false;
		}
	}
	
	public Map<Integer, Session> GrabRoomUsers()
	{
		return Party;
	}
	
	public Map<Integer, Session> GrabRightHolders()
	{
		return RightsHolders;
	}

	public void SendMessage(EventResponse Message) 
	{
		for (Session mSession : GrabRoomUsers().values())
		{
			mSession.SendResponse(Message);
		}
	}
}
