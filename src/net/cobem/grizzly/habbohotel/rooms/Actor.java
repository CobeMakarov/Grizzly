package net.cobem.grizzly.habbohotel.rooms;

import java.util.Collection;

import net.cobem.grizzly.events.EventResponse;
import net.cobem.grizzly.events.composers.ComposerLibrary;
import net.cobem.grizzly.habbohotel.pathfinding.*;
import net.cobem.grizzly.habbohotel.rooms.items.FloorItem;
import net.cobem.grizzly.habbohotel.sessions.*;

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

		Message.AppendString("/" + Status + (Status.contains("sit") ? "sit " + CurrentPosition.Z + 1 : "")  + "/");

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
				if (NeedsPathChange)
				{
					break;
				}
				
				FloorItem NextItem = null;
				
				for(FloorItem Item : CurrentRoom.FloorItems.values())
				{
					if (Item.X == CurrentPosition.X && Item.Y == CurrentPosition.Y)
					{
						NextItem = Item;
					}
				}
				
				this.Rotation = this.CurrentPosition.Calculate(NextStep[0], NextStep[1]);
				
				if (NextItem != null)
				{
					this.CurrentPosition.Z += NextItem.Height.intValue();
				}
				
				this.UpdateStatus("mv " + NextStep[0] + "," + NextStep[1] + "," + Double.toString(this.CurrentPosition.Z));
				this.CurrentPosition.X = NextStep[0];
				this.CurrentPosition.Y = NextStep[1];
				
				
				try 
				{
					Thread.sleep(500);
				} catch (InterruptedException e) {}
			}
		//}
		
		for(FloorItem Item : CurrentRoom.FloorItems.values())
		{
			if (Item.X == CurrentPosition.X && Item.Y == CurrentPosition.Y && Item.GetBaseItem().Sitable)
			{
				Rotation = Item.Rotation;
				
				UpdateStatus("sit 1.3");
			}
		}
		
		this.UpdateStatus("");
		this.IsMoving = false;
		this.GoalPosition = null;
	}
}
