package com.grizzly.habbohotel;

import java.sql.SQLException;

import com.grizzly.Grizzly;
import com.grizzly.habbohotel.catalog.*;
import com.grizzly.habbohotel.furni.*;
import com.grizzly.habbohotel.sessions.*;
import com.grizzly.habbohotel.rooms.*;

public class HabboHotel 
{
	private SessionHandler SessionHandler;
	private CatalogHandler CatalogHandler;
	private FurnitureHandler FurnitureHandler;
	private RoomHandler RoomHandler;
	
	public HabboHotel() throws SQLException
	{
		Grizzly.WriteOut("");
		
		SessionHandler = new SessionHandler();
		CatalogHandler = new CatalogHandler();
		FurnitureHandler = new FurnitureHandler();
		RoomHandler = new RoomHandler();
		
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
}
