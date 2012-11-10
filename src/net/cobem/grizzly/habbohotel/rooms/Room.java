package net.cobem.grizzly.habbohotel.rooms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.EventResponse;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.pathfinding.AffectedTile;
import net.cobem.grizzly.habbohotel.pathfinding.IPathfinder;
import net.cobem.grizzly.habbohotel.pathfinding.Position;
import net.cobem.grizzly.habbohotel.pathfinding.ihi.IHI_PathfinderLogic;
import net.cobem.grizzly.habbohotel.rooms.items.FloorItem;
import net.cobem.grizzly.habbohotel.rooms.models.*;
import net.cobem.grizzly.habbohotel.sessions.Session;

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
	private List<Integer> RightsHolders = new ArrayList<Integer>();
	private Map<Integer, Position> UserPositions = new HashMap<Integer, Position>();
	
	public Model mModel;
	
	private IPathfinder Pathfinder;
	
	public Map<Integer, FloorItem> FloorItems = new HashMap<Integer, FloorItem>();
	//private Map<Integer, FloorItem> WallItems = new HashMap<Integer, FloorItem>();
	
	private byte[][] FurniCollisionMap;
	
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
		
		//Grizzly.WriteOut(this.OwnerByName);
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
			
			this.Pathfinder = new IHI_PathfinderLogic();
			
			LoadRightHolders();
			
			Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_room_items WHERE room = '" + this.ID + "'");
			
			try 
			{
				ResultSet Results = Grizzly.GrabDatabase().GrabTable();
				
				while (Results.next())
				{
					FloorItems.put(new Integer(Results.getInt("id")), new FloorItem(Results));
				}
			} 
			catch (SQLException e) 
			{
				//
			}
			
			this.FurniCollisionMap = new byte[this.GrabModel().MapSizeX][this.GrabModel().MapSizeY]; 
			
			for(int x = 0; x < this.GrabModel().MapSizeX; x++) //Loopception courtesy of Cobe & Cecer
			{
				for(int y = 0; y < this.GrabModel().MapSizeY; y++)
				{
					FurniCollisionMap[x][y] = 1;
				}
			}
			
			for(FloorItem Item : FloorItems.values())
			{
				for(AffectedTile Tile : AffectedTile.GetAffectedTiles(Item.GetBaseItem().Length, Item.GetBaseItem().Width, Item.X, Item.Y, Item.Rotation))
				{
					if (!Item.GetBaseItem().Sitable && !Item.GetBaseItem().Walkable && !Item.GetBaseItem().Layable)
					{
						FurniCollisionMap[Tile.X][Tile.Y] = 0;
					}
					
					if (Item.GetBaseItem().Sitable || Item.GetBaseItem().Layable)
					{
						FurniCollisionMap[Tile.X][Tile.Y] = 2;
					}
				}
			}
			
			return true;
		}
		catch(SQLException Ex)
		{
			Grizzly.WriteOut(Ex.toString());
			return false;
		}
	}
	
	public Map<Integer, Session> GrabParty()
	{
		return Party;
	}
	
	public List<Integer> GrabRightHolders()
	{
		return RightsHolders;
	}
	
	public Model GrabModel()
	{
		return Grizzly.GrabHabboHotel().GrabModelHandler().GrabModel(this.Model);
	}
	
	public void SendMessage(EventResponse Message) 
	{
		for (Session mSession : GrabParty().values())
		{
			mSession.SendResponse(Message);
		}
	}
	
	public boolean LoadRightHolders()
	{
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_room_rights WHERE room = '" + this.ID + "'");
		
		ResultSet Whatever = Grizzly.GrabDatabase().GrabTable();
		try 
		{
			while(Whatever.next())
			{
					RightsHolders.add(Whatever.getInt("user"));
			}
		} catch (SQLException e){}
		
		return true;
	}
	public EventResponse GrabActorStatus()
	{
		EventResponse Message = new EventResponse();
		
		Message.Initialize(ComposerLibrary.RoomUsers);
		Message.AppendInt32(this.GrabParty().size());
		
		for (Session Actor : this.GrabParty().values())
		{
			Message.AppendInt32(Actor.GrabHabbo().ID);
			Message.AppendString(Actor.GrabHabbo().Username);
			Message.AppendString(Actor.GrabHabbo().Motto);
			Message.AppendString(Actor.GrabHabbo().Look);
			Message.AppendInt32(Actor.GrabHabbo().ID);
			Message.AppendInt32(Actor.GrabActor().CurrentPosition.X);
			Message.AppendInt32(Actor.GrabActor().CurrentPosition.Y);
			Message.AppendString(Double.toString(Actor.GrabActor().CurrentPosition.Z));
			Message.AppendInt32(Actor.GrabActor().Rotation);
			Message.AppendInt32(1);
			Message.AppendString(Actor.GrabHabbo().Gender.toString().toLowerCase());
			Message.AppendInt32(-1);
			Message.AppendInt32(-1);
			Message.AppendInt32(0);
			Message.AppendInt32(1);
		}
		
		return Message;
	}
	
	public EventResponse GrabRoomStatus()
	{
		EventResponse Message = new EventResponse();
		
		Message.Initialize(ComposerLibrary.RoomStatuses);
		Message.AppendInt32(this.GrabParty().size());
		
		for (Session Actor : this.GrabParty().values())
		{
			Message.AppendInt32(Actor.GrabHabbo().ID);
			Message.AppendInt32(Actor.GrabActor().CurrentPosition.X);
			Message.AppendInt32(Actor.GrabActor().CurrentPosition.Y);
			Message.AppendString(Double.toString(Actor.GrabActor().CurrentPosition.Z));
			Message.AppendInt32(Actor.GrabActor().Rotation);
			Message.AppendInt32(Actor.GrabActor().Rotation);
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
		
		this.GrabParty().remove(Session.GrabHabbo().ID);
		
		Grizzly.WriteOut(Session.GrabHabbo().Username + " left room " + this.ID);
		
		Session.GrabActor().CurrentRoom = null;
	}
	
	private byte[][] GenerateCollisionMap(boolean[][]Map)
	{
		byte[][] CollisionMap = new byte[this.GrabModel().MapSizeX][this.GrabModel().MapSizeY]; 
		
		for(int x = 0; x < this.GrabModel().MapSizeX; x++) //Loopception courtesy of Cobe & Cecer
		{
			for(int y = 0; y < this.GrabModel().MapSizeY; y++)
			{
				if (FurniCollisionMap[x][y] == 0) //Tile is closed becuase furni is there..
				{
					CollisionMap[x][y] = FurniCollisionMap[x][y];
				}
				else if (this.GrabModel().Squares[x][y] == SquareState.WALKABLE) //By model, it's walkable
				{
					CollisionMap[x][y] = 1;
				}
				else //It's not a valid tile
				{
					CollisionMap[x][y] = 0;
				}
			}
		}
		
		return CollisionMap;
	}
	
	public float[][] GenerateFurniHeightMap()
	{
		float[][] FurniHeightMap = new float[this.GrabModel().MapSizeX][this.GrabModel().MapSizeY]; 
		
		for(int x = 0; x < this.GrabModel().MapSizeX; x++) //Loopception courtesy of Cobe & Cecer
		{
			for(int y = 0; y < this.GrabModel().MapSizeY; y++)
			{
				if(FurniCollisionMap[x][y] == 0)
				{
					FurniHeightMap[x][y] = (float)this.GrabModel().SquareHeight[x][y];
				}
				else
				{
					for(FloorItem Item : FloorItems.values())
					{
						FurniHeightMap[x][y] = Item.Height;	
					}
				}
			}
		}
		
		return FurniHeightMap;
	}
	
	public void RegenerateCollisionMap()
	{
		this.Pathfinder.ApplyCollisionMap(
				GenerateCollisionMap(new boolean[this.GrabModel().MapSizeX][this.GrabModel().MapSizeY]), 
				GenerateFurniHeightMap());
	}
	
	private void RegenerateFurniCollisionMap()
	{
		for(int x = 0; x < this.GrabModel().MapSizeX; x++) //Loopception courtesy of Cobe & Cecer
		{
			for(int y = 0; y < this.GrabModel().MapSizeY; y++)
			{
				FurniCollisionMap[x][y] = 1;
			}
		}
		
		for(FloorItem Item : FloorItems.values())
		{
			for(AffectedTile Tile : AffectedTile.GetAffectedTiles(Item.GetBaseItem().Length, Item.GetBaseItem().Width, Item.X, Item.Y, Item.Rotation))
			{
				if (!Item.GetBaseItem().Sitable && !Item.GetBaseItem().Walkable && !Item.GetBaseItem().Layable)
				{
					FurniCollisionMap[Tile.X][Tile.Y] = 0;
				}
				
				if (Item.GetBaseItem().Sitable || Item.GetBaseItem().Layable)
				{
					FurniCollisionMap[Tile.X][Tile.Y] = 2;
				}
			}
		}
	}
	
	public boolean GenerateRoomItem()
	{
		Grizzly.GrabDatabase().SetQuery("SELECT * FROM server_room_items WHERE room = '" + this.ID + "' ORDER BY id DESC LIMIT 1");
			
		try 
		{
			ResultSet Results = Grizzly.GrabDatabase().GrabTable();
			
			while (Results.next())
			{
				this.FloorItems.put(Results.getInt("id"), new FloorItem(Results));
			}
		} catch (SQLException e) {}
		
		this.RegenerateFurniCollisionMap();
		
		return true;
	}
	
	public Map<Integer, FloorItem> GrabFloorItems()
	{
		return FloorItems;
	}
	
	public FloorItem FindItemByID(int ID)
	{
		FloorItem Return = null;
		
		for(FloorItem Item : FloorItems.values())
		{
			if(Item.ID == ID)
			{
				Return = Item;
			}
		}
		
		return Return;
	}
	public void Tick()
	{
		//MOVEMENT
		boolean[][]RoomUnitMap = new boolean[this.GrabModel().MapSizeX][this.GrabModel().MapSizeY]; 

		for(Session mSession : this.GrabParty().values())
		{
			if(mSession.GrabActor().NeedsPathChange) //Give them .5 of a second to get ready for their new path
			{
				Collection<byte[]> Path = this.Pathfinder.Path(
						(byte)mSession.GrabActor().CurrentPosition.X, 
						(byte)mSession.GrabActor().CurrentPosition.Y, 
						(byte)mSession.GrabActor().GoalPosition.X, 
						(byte)mSession.GrabActor().GoalPosition.Y, 
						Grizzly.GrabHabboHotel().GrabRoomHandler().MaxDrop, 
						Grizzly.GrabHabboHotel().GrabRoomHandler().MaxJump);
				
				mSession.GrabActor().CurrentPath = Path;
				
				mSession.GrabActor().Move(true);
				//mSession.GrabActor().NewPathReady = true;
			}
			else if(mSession.GrabActor().GoalPosition != null && !mSession.GrabActor().IsMoving && !mSession.GrabActor().NeedsPathChange) //Start moving them
			{
				Collection<byte[]> Path = this.Pathfinder.Path(
						(byte)mSession.GrabActor().CurrentPosition.X, 
						(byte)mSession.GrabActor().CurrentPosition.Y, 
						(byte)mSession.GrabActor().GoalPosition.X, 
						(byte)mSession.GrabActor().GoalPosition.Y, 
						Grizzly.GrabHabboHotel().GrabRoomHandler().MaxDrop, 
						Grizzly.GrabHabboHotel().GrabRoomHandler().MaxJump);
				
				mSession.GrabActor().CurrentPath = Path;
				
				mSession.GrabActor().Move(false);
			}	
			
			RoomUnitMap[mSession.GrabActor().CurrentPosition.X][mSession.GrabActor().CurrentPosition.Y] = true;
			
			this.Pathfinder.ApplyCollisionMap(GenerateCollisionMap(RoomUnitMap), GenerateFurniHeightMap());
			
			//SIGNS
			if (mSession.GrabActor().SignTimer > 0)
			{
				if (mSession.GrabActor().SignTimer == 1)
				{
					mSession.GrabActor().UpdateStatus("");
				}
				
				mSession.GrabActor().SignTimer--;
			}
		}
	}
}
