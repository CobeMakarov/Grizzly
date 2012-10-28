package net.cobem.grizzly.habbohotel;

import java.sql.SQLException;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.habbohotel.catalog.*;
import net.cobem.grizzly.habbohotel.furni.*;
import net.cobem.grizzly.habbohotel.misc.*;
import net.cobem.grizzly.habbohotel.rooms.*;
import net.cobem.grizzly.habbohotel.rooms.models.*;
import net.cobem.grizzly.habbohotel.sessions.*;

public class HabboHotel 
{
	private SessionHandler SessionHandler;
	private CatalogHandler CatalogHandler;
	private FurnitureHandler FurnitureHandler;
	private RoomHandler RoomHandler;
	private ModelHandler ModelHandler;
	private ChatCommandParser ChatCommandParser;
	
	public void Load() throws SQLException
	{
		Grizzly.WriteOut("");
		
		SessionHandler = new SessionHandler();
		CatalogHandler = new CatalogHandler();
		FurnitureHandler = new FurnitureHandler();
		ModelHandler = new ModelHandler();
		RoomHandler = new RoomHandler();
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
