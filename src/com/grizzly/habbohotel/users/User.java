package com.grizzly.habbohotel.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.grizzly.Grizzly;
import com.grizzly.habbohotel.users.items.ItemHandler;
import com.grizzly.habbohotel.users.messenger.MessengerHandler;

public class User 
{
	public int ID;
	public int Credits;
	public int Rank;
	
	public String Username;
	public String Mail;
	public String Look;
	public String Motto;	
	public String Ticket;
	
	public GenderType Gender;
	
	private MessengerHandler Messenger;
	private ItemHandler Inventory;
		
	public User(String IP)
	{
		Grizzly.GrabDatabase().SetQuery("SELECT ticket FROM server_clients WHERE ip_address = '" + IP + "'  LIMIT 1");
		
		String Ticket = Grizzly.GrabDatabase().GrabString();
		
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_users WHERE client_key = '" + Ticket + "' LIMIT 1");
		
		if (Grizzly.GrabDatabase().RowCount() == 0)
		{
			Grizzly.WriteOut("Something went wrong when creating the user using ticket: " + Ticket);
			return;
		}

		ResultSet Habbo = Grizzly.GrabDatabase().GrabRow();
		
		FillClass(Habbo);
		
		this.Ticket = Ticket;
		
		
	}
	
	public User(int ID)
	{
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_users WHERE id = '" + ID + "'");
		
		ResultSet Habbo = Grizzly.GrabDatabase().GrabRow();
		
		FillClass(Habbo);
	}
	
	private boolean FillClass(ResultSet Set)
	{
		try
		{
			this.ID = Set.getInt("id");
			this.Credits = Set.getInt("credits");
			this.Rank = Set.getInt("rank");
			
			this.Username = Set.getString("username");
			this.Mail = Set.getString("email");
			this.Look = Set.getString("look");
			this.Motto = Set.getString("motto");
			this.Gender = GenderType.valueOf(Set.getString("gender"));
			
			this.Inventory = new ItemHandler(this.ID);
		}
		catch(SQLException E)
		{
			Grizzly.WriteOut(E.getMessage());
			return false;
		}
		return true;
	}
	
	public void RefreshMessenger(boolean Online) throws SQLException
	{
		this.InitMessenger();
		
		this.GrabMessenger().UpdateStatus(Online);
	}
	
	public ItemHandler GrabItems()
	{
		return Inventory;
	}
	
	/*public void RefreshMessenger() throws SQLException
	{
		//this.InitMessenger();
		
		EventResponse Message = new EventResponse();

		Message.Initialize(ComposerLibrary.UpdateFriendState);
		Message.AppendInt32(0);
		Message.AppendInt32(1);
		Message.AppendInt32(0);
		Message.AppendInt32(this.ID);
		Message.AppendString(this.Username);
		Message.AppendInt32(1);
		Message.AppendBoolean((Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSessionByUserID(this.ID) != null));
		Message.AppendBoolean(true);
		Message.AppendString(this.Look);
		Message.AppendInt32(0);
		Message.AppendString(this.Motto);
		Message.AppendInt32(0);
		Message.AppendInt32(0);
		Message.AppendInt32(0);
		
		for(User mUser : GrabMessenger().GrabFriends().values())
		{
			if (Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSessionByUserID(mUser.ID) != null)
			{
				Grizzly.GrabHabboHotel().GrabSessionHandler().GrabSessionByUserID(mUser.ID).SendResponse(Message);
			}
		}	
	}*/
	
	public void InitMessenger() throws SQLException
	{
		this.Messenger = new MessengerHandler(this.ID);
	}
	
	public MessengerHandler GrabMessenger()
	{
		return Messenger;
	}
	
	public boolean Append()
	{
		Grizzly.GrabDatabase().RunFastQuery("UPDATE server_users SET " +
				"credits = '" + this.Credits + "'," +
				"rank = '" + this.Rank + "'," +
				"username = '" + this.Username + "'," +
				"email = '" + this.Mail + "'," +
				"look = '" + this.Look + "'," +
				"motto = '" + this.Motto + "'," +
				"gender = '" + this.Gender.toString().toUpperCase() + "' WHERE id = '" + this.ID + "'");
		return true;
	}
}
