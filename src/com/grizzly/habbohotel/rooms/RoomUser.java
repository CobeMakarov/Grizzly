package com.grizzly.habbohotel.rooms;

import com.grizzly.events.EventResponse;
import com.grizzly.events.composers.ComposerLibrary;
import com.grizzly.habbohotel.pathfinding.*;
import com.grizzly.habbohotel.sessions.*;

public class RoomUser 
{
	private Session Session;
	
	public Room CurrentRoom = null;
	
	public Position CurrentPosition = null;
	public Position GoalPosition = null;
	
	public int Rotation = 0;
	
	public RoomUser(Session mSession)
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
}
