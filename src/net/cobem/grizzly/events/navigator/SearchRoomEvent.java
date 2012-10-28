package net.cobem.grizzly.events.navigator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.events.EventResponse;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.rooms.Room;
import net.cobem.grizzly.habbohotel.sessions.Session;
import net.cobem.grizzly.utils.UserInputFilter;

public class SearchRoomEvent implements Event
{

	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		Map<Integer, Room> Return = new HashMap<Integer, Room>();
		
		String Search = Request.PopFixedString();
		
		EventResponse Message = new EventResponse();
		
		String Query = Search.split(":")[0];
		String Value = UserInputFilter.filterString(Search.split(":")[1], false);
		
		if (Query != null && Value != null)
		{
			switch (Query)
			{
				case "owner":
					Grizzly.GrabDatabase().SetQuery("SELECT id FROM server_users WHERE username = '" + Value + "'");
					
					int Owner = Grizzly.GrabDatabase().GrabInt();
					
					Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_rooms WHERE owner = '" + Owner + "'");
					
					try 
					{
						ResultSet Results = Grizzly.GrabDatabase().GrabTable();
						
						while (Results.next())
						{
							Return.put(new Integer(Results.getInt("id")), new Room(Results));
						}
					} 
					catch (SQLException e) 
					{
						//
					}
				break;
			}
		}
		else
		{
			Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_rooms WHERE name LIKE '" + Search + "'");
			
			try 
			{
				ResultSet Results = Grizzly.GrabDatabase().GrabTable();
				
				while (Results.next())
				{
					Return.put(new Integer(Results.getInt("id")), new Room(Results));
				}
			} 
			catch (SQLException e) 
			{
				//
			}
		}
		
		
		Message.Initialize(ComposerLibrary.OwnRooms);
		Message.AppendInt32(5);
		Message.AppendString("");
		Message.AppendInt32(Return.size());
		
		for(Room mRoom : Return.values())
		{
			Message.AppendInt32(mRoom.ID);
			Message.AppendBoolean(false);
			Message.AppendString(mRoom.Title);
			Message.AppendInt32(mRoom.Owner);
			Message.AppendString(mRoom.OwnerByName);
			Message.AppendInt32(0);
			Message.AppendInt32(0); // User's in room
			Message.AppendInt32(25);
			Message.AppendString(mRoom.Description);
			Message.AppendInt32(0);
			Message.AppendInt32(0);
			Message.AppendInt32(0);
			Message.AppendInt32(2);
			Message.AppendInt32(0);
			Message.AppendInt32(0);
			Message.AppendInt32(0);
			Message.AppendInt32(0);
			Message.AppendInt32(0);
			Message.AppendInt32(0);
			Message.AppendString("");
			Message.AppendBoolean(true);
			Message.AppendBoolean(true);
		}
		
		Session.SendResponse(Message);
	}

}
