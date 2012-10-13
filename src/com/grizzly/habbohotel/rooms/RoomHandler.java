package com.grizzly.habbohotel.rooms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.grizzly.Grizzly;

public class RoomHandler 
{
	private Map<Integer, Room> Rooms;
	
	public RoomHandler()
	{
		Rooms = new HashMap<Integer, Room>();
		
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_rooms");
		
		try 
		{
			ResultSet Results = Grizzly.GrabDatabase().GrabTable();
			
			while (Results.next())
			{
				Rooms.put(new Integer(Results.getInt("id")), new Room(Results));
			}
		} 
		catch (SQLException e) 
		{
			//
		}
		
		Grizzly.WriteOut("Loaded and stored " + Rooms.size() + " rooms!");
	}
	
	public Room GrabRoomById(int ID)
	{
		if (Rooms.containsKey(new Integer(ID)))
		{
			return Rooms.get(new Integer(ID));
		}
		else
		{
			return null;
		}		
	}
	
	public Map<Integer, Room> GrabUsersRooms(int User)
	{
		Map<Integer, Room> Result = new HashMap<Integer, Room>();
		
		for (Room UserRoom : Rooms.values())
		{
			if (UserRoom.Owner == User)
			{
				Result.put(new Integer(UserRoom.ID), UserRoom);
			}
		}
		
		return Result;
	}
	
	public Map<Integer, Room> GrabPopulatedRooms()
	{
		Map<Integer, Room> Result = new HashMap<Integer, Room>();
		
		for (Room UserRoom : Rooms.values())
		{
			if (UserRoom.GrabRoomUsers().size() >= 1)
			{
				Result.put(new Integer(UserRoom.ID), UserRoom);
			}
		}
		
		return Result;
	}
	
	public Map<Integer, Room> GrabRooms()
	{
		return Rooms;
	}
	
	public boolean RemoveRoom(int Room)
	{
		Grizzly.GrabDatabase().RunFastQuery("DELETE FROM server_rooms WHERE id = '" + Room + "'");
		
		//TODO: Remove items from room as well!
		Rooms.remove(Room);
		return true;
	}
	
	public int CreateRoom(String Name, int Owner, String Model)
	{
		Grizzly.GrabDatabase().RunFastQuery("INSERT INTO server_rooms " +
				"(name, owner, description, status, password, model, wallpaper, floor, outside) VALUES " +
				"('" + Name + "', '" + Owner + "', '" + Name + "', '0', ' ', '" + Model + "', '0', '0', '0.0')");
		
		Grizzly.GrabDatabase().SetQuery("SELECT id FROM server_rooms ORDER BY id DESC LIMIT 1");
		
		int LastID = Grizzly.GrabDatabase().GrabInt();
		
		Rooms.put(new Integer(LastID), new Room(LastID));
		return LastID;
	}
}
