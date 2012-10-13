package com.grizzly.habbohotel;

import java.sql.SQLException;

import com.grizzly.Grizzly;
import com.grizzly.habbohotel.catalog.*;
import com.grizzly.habbohotel.furni.*;
import com.grizzly.habbohotel.sessions.*;
import com.grizzly.habbohotel.rooms.*;
import com.grizzly.habbohotel.rooms.models.*;
import com.grizzly.habbohotel.misc.*;
public class HabboHotel 
{
	private SessionHandler SessionHandler;
	private CatalogHandler CatalogHandler;
	private FurnitureHandler FurnitureHandler;
	private RoomHandler RoomHandler;
	private ModelHandler ModelHandler;
	private ChatCommandParser ChatCommandParser;
	
	public HabboHotel() throws SQLException
	{
		Grizzly.WriteOut("");
		
		SessionHandler = new SessionHandler();
		CatalogHandler = new CatalogHandler();
		FurnitureHandler = new FurnitureHandler();
		RoomHandler = new RoomHandler();
		ModelHandler = new ModelHandler();
		ChatCommandParser = new ChatCommandParser();
		
		Grizzly.WriteOut("");
	}
	
	public SessionHandler GrabSessionHandler()
	{
		return SessionHandler;
	}
	
	public CatalogHandler GrabCatalogHandler()
	{
		return CatalogHandler;
	}
	
	public FurnitureHandler GrabFurnitureHandler()
	{
		return FurnitureHandler;
	}
	
	public RoomHandler GrabRoomHandler()
	{
		return RoomHandler;
	}
	
	public ModelHandler GrabModelHandler()
	{
		return ModelHandler;
	}
	
	public ChatCommandParser GrabChatCommandParser()
	{
		return ChatCommandParser;
	}
	
	/*
	 * Misc.
	 */
	public int ParseSmile(String Str)
	{
		if (Str.contains(":)") || Str.contains("=)") || Str.contains(":D") || Str.contains("=D"))
		{
			return 1;
		}
		
		if (Str.contains(":@") || Str.contains(">:(") || Str.contains(">:@"))
		{
			return 2;
		}
		
		if (Str.contains(":o") || Str.contains("D:"))
		{
			return 3; 
		}
		
		if (Str.contains(":(") || Str.contains(":'(") || Str.contains("=(") || Str.contains("='("))
		{
			return 4;
		}
		
		return 0;
	}
}
