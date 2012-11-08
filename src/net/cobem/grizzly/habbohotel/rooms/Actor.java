package net.cobem.grizzly.habbohotel.rooms;

import java.util.Collection;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.EventResponse;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.events.rooms.SayEvent;
import net.cobem.grizzly.events.rooms.ShoutEvent;
import net.cobem.grizzly.habbohotel.pathfinding.*;
import net.cobem.grizzly.habbohotel.rooms.items.FloorItem;
import net.cobem.grizzly.habbohotel.sessions.*;
import net.cobem.grizzly.plugins.HabboEvent;

public class Actor 
{
	private Session Session;
	
	public Room CurrentRoom = null;
	
	public Position CurrentPosition = null;
	public Position GoalPosition = null;
	
	public int Rotation = 0;
	public int SignTimer = 0;
	
	public boolean IsMoving;
	public boolean NeedsPathChange;
	public boolean StopMoving;
	
	public String OverrideSpeech;
	
	public Actor(Session mSession)
	{
		this.Session = mSession;
	}
	
	public void UpdateStatus(String Status)
	{
		EventResponse Message = new EventResponse();

		Message.Initialize(ComposerLibrary.RoomStatuses);
		Message.AppendInt32(1);
		Message.AppendInt32(Session.GrabHabbo().ID);
		Message.AppendInt32(CurrentPosition.X);
		Message.AppendInt32(CurrentPosition.Y);
		Message.AppendString(Integer.toString(CurrentPosition.Z));
		Message.AppendInt32(Rotation);
		Message.AppendInt32(Rotation);

		Message.AppendString("/flatctrl 4/" + Status + "//");

		if (CurrentRoom != null)
		{
			CurrentRoom.SendMessage(Message);
		}
	}
	
	public Boolean InRoom()
	{
		if (this.CurrentRoom == null)
		{
			return false;
		}
		
		if (this.CurrentRoom.ID == 0)
		{
			return false;
		}
		
		return true;
	}
	
	public void Speak(String STR, boolean Shout)
	{
		this.OverrideSpeech = STR;
		
		if (!Shout)
		{
			new SayEvent().Parse(Session, null);
		}
		else
		{
			new ShoutEvent().Parse(Session, null);
		}
	}
	public void Move(Collection<byte[]> Path)
	{
		if (Path.isEmpty())
		{
			return;
		}
		
		this.IsMoving = true;
		
		//while(this.CurrentPosition.GrabPair() != this.GoalPosition.GrabPair())
		//{
			for(byte[]NextStep : Path)
			{
				if (StopMoving)
				{
					break;
				}
				
				FloorItem NextItem = null;
				
				for(FloorItem Item : CurrentRoom.FloorItems.values())
				{
					for(AffectedTile Tile : AffectedTile.GetAffectedTiles(Item.GetBaseItem().Length, Item.GetBaseItem().Width, Item.X, Item.Y, Item.Rotation))
					{
						if (Tile.X == NextStep[0] && Tile.Y == NextStep[1])
						{
							NextItem = Item;
						}
					}
				}
				
				this.Rotation = this.CurrentPosition.Calculate(NextStep[0], NextStep[1]);
				
				if (NextItem != null)
				{
					this.CurrentPosition.Z = NextItem.Height.intValue();
				}
				else
				{
					this.CurrentPosition.Z = 0;
				}
				
				this.UpdateStatus("mv " + NextStep[0] + "," + NextStep[1] + "," + Double.toString(this.CurrentPosition.Z));
				this.CurrentPosition.X = NextStep[0];
				this.CurrentPosition.Y = NextStep[1];
				
				Grizzly.GrabPluginHandler().RunEvent(HabboEvent.OnWalk, Session);
				
				try 
				{
					Thread.sleep(500);
				} catch (InterruptedException e) {}
			}
		//}
		
		this.UpdateStatus("");
		
		for(FloorItem Item : CurrentRoom.FloorItems.values())
		{
			for(AffectedTile Tile : AffectedTile.GetAffectedTiles(Item.GetBaseItem().Length, Item.GetBaseItem().Width, Item.X, Item.Y, Item.Rotation))
			{
				if (Tile.X == CurrentPosition.X && Tile.Y == CurrentPosition.Y)
				{
					if (Item.GetBaseItem().Sitable)
					{
						Rotation = Item.Rotation;
					
						UpdateStatus("sit " + Item.GetBaseItem().StackHeight);
					}
					
					if (Item.GetBaseItem().Layable)
					{
						Rotation = Item.Rotation;
					
						UpdateStatus("lay " + Item.GetBaseItem().StackHeight);
					}
				}
			}
		}
		
		this.IsMoving = false;
		this.GoalPosition = null;
	}
}
