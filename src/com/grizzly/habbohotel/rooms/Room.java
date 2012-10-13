package com.grizzly.habbohotel.rooms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.grizzly.Grizzly;
import com.grizzly.events.EventResponse;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.sessions.Session;
import com.grizzly.habbohotel.pathfinding.Position;
import com.grizzly.habbohotel.rooms.models.*;

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
	public String OwnerByName;
	
	private Map<Integer, Session> Party = new HashMap<Integer, Session>();
	private Map<Integer, Session> RightsHolders = new HashMap<Integer, Session>();
	private Map<Integer, Position> UserPositions = new HashMap<Integer, Position>();
	
	public Model mModel;
	
	public Room(ResultSet Room)
	{
		//Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_rooms WHERE id = '" + ID + "'");
		
		//ResultSet Room = Grizzly.GrabDatabase().GrabRow();
		
		this.FillClass(Room);
		
		Grizzly.GrabDatabase().SetQuery("SELECT username FROM server_users WHERE id = '" + this.Owner + "'");
		
		this.OwnerByName = Grizzly.GrabDatabase().GrabString();
	}
	
	public Room(int ID)
	{
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_rooms WHERE id = '" + ID + "'");
		
		ResultSet Room = Grizzly.GrabDatabase().GrabRow();
		
		this.FillClass(Room);
		
		Grizzly.GrabDatabase().SetQuery("SELECT username FROM server_users WHERE id = '" + this.Owner + "'");
		
		this.OwnerByName = Grizzly.GrabDatabase().GrabString();
		
		Grizzly.WriteOut(this.OwnerByName);
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
			Grizzly.WriteOut(Ex.toString());
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
	
	public Model GrabModel()
	{
		return Grizzly.GrabHabboHotel().GrabModelHandler().GrabModel(this.Model);
	}
	
	public void SendMessage(EventResponse Message) 
	{
		for (Session mSession : GrabRoomUsers().values())
		{
			mSession.SendResponse(Message);
		}
	}
	
	public EventResponse GrabRoomUserStatus()
	{
		EventResponse Message = new EventResponse();
		
		Message.Initialize(ComposerLibrary.RoomUsers);
		Message.AppendInt32(this.GrabRoomUsers().size());
		
		for (Session Actor : this.GrabRoomUsers().values())
		{
			Message.AppendInt32(Actor.GrabHabbo().ID);
			Message.AppendString(Actor.GrabHabbo().Username);
			Message.AppendString(Actor.GrabHabbo().Motto);
			Message.AppendString(Actor.GrabHabbo().Look);
			Message.AppendInt32(Actor.GrabHabbo().ID);
			Message.AppendInt32(Actor.GrabRoomUser().CurrentPosition.X);
			Message.AppendInt32(Actor.GrabRoomUser().CurrentPosition.Y);
			Message.AppendString(Double.toString(Actor.GrabRoomUser().CurrentPosition.Z));
			Message.AppendInt32(Actor.GrabRoomUser().Rotation);
			Message.AppendInt32(1);
			Message.AppendString(Actor.GrabHabbo().Gender.toString().toLowerCase());
			Message.AppendInt32(-1);
			Message.AppendInt32(-1);
			Message.AppendInt32(0);
			Message.AppendInt32(1337);
		}
		
		return Message;
	}
	
	public EventResponse GrabRoomStatus()
	{
		EventResponse Message = new EventResponse();
		
		Message.Initialize(ComposerLibrary.RoomStatuses);
		Message.AppendInt32(this.GrabRoomUsers().size());
		
		for (Session Actor : this.GrabRoomUsers().values())
		{
			Message.AppendInt32(Actor.GrabHabbo().ID);
			Message.AppendInt32(Actor.GrabRoomUser().CurrentPosition.X);
			Message.AppendInt32(Actor.GrabRoomUser().CurrentPosition.Y);
			Message.AppendString(Double.toString(Actor.GrabRoomUser().CurrentPosition.Z));
			Message.AppendInt32(Actor.GrabRoomUser().Rotation);
			Message.AppendInt32(Actor.GrabRoomUser().Rotation);
			Message.AppendString("/flatctrl 4/");
		}
		
		return Message;
	}
	
	public Map<Integer, Position> GrabUserPositions()
	{
		return UserPositions;
	}
	
	public void RemoveUser(Session Session)
	{
		EventResponse Message = new EventResponse();
		
		Message.Initialize(ComposerLibrary.LeaveRoom);
		Message.AppendString(Session.GrabHabbo().ID);
		
		SendMessage(Message);
		
		this.GrabRoomUsers().remove(Session.GrabHabbo().ID);
		
		Grizzly.WriteOut(Session.GrabHabbo().Username + " left room " + this.ID);
		
		Session.GrabRoomUser().CurrentRoom = null;
	}
}
