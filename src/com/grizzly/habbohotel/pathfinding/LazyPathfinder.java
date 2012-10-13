package com.grizzly.habbohotel.pathfinding;

import com.grizzly.habbohotel.sessions.*;

public class LazyPathfinder 
{
	private Position Current;
	private Position Goal;
	
	private Session Session;
	
	public LazyPathfinder(Session mSession)
	{
		Session = mSession;
		
		Current = Session.GrabRoomUser().CurrentPosition;
		Goal = Session.GrabRoomUser().GoalPosition;
	}
	
	public void Start()
	{
		if (Current.X == Goal.X && Current.Y == Goal.Y) // No Movement!
		{
			return;
		}
		
		if (!UsersTileCheck())
		{
			return;
		}
		
		Session.GrabRoomUser().Rotation = Session.GrabRoomUser().CurrentPosition.Calculate(Goal.X, Goal.Y);
		Session.GrabRoomUser().UpdateStatus("mv "+ Goal.X +","+ Goal.Y +"," + Double.toString(Current.Z));	
		
		Session.GrabRoomUser().CurrentPosition.X = Goal.X;
		Session.GrabRoomUser().CurrentPosition.Y = Goal.Y;
		
		try 
		{
			Thread.sleep(500);
		} 
		catch (InterruptedException e) {}
		
		Session.GrabRoomUser().UpdateStatus("");
		
		/*
		 * Clean things up
		 */
		Session.GrabRoomUser().CurrentRoom.GrabUserPositions().remove(Session.GrabHabbo().ID);
		
		Session.GrabRoomUser().CurrentRoom.GrabUserPositions().put(Session.GrabHabbo().ID, Session.GrabRoomUser().CurrentPosition);
		
	}
	
	public boolean UsersTileCheck()
	{
		for(Position mPosition : Session.GrabRoomUser().CurrentRoom.GrabUserPositions().values())
		{
			if (mPosition.X == Goal.X && mPosition.Y == Goal.Y)
			{
				return false;
			}
		}
		
		return true;
	}
}
