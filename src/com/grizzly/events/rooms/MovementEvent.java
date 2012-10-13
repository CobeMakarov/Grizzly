package com.grizzly.events.rooms;

import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.habbohotel.pathfinding.LazyPathfinder;
import com.grizzly.habbohotel.pathfinding.Position;
import com.grizzly.habbohotel.sessions.Session;

public class MovementEvent implements Event 
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		/*
		 * My extremely bad pathfinding thingy!
		 */
		
		int X = Request.PopInt();
		int Y = Request.PopInt();
		
		Session.GrabRoomUser().GoalPosition = new Position(X, Y, Session.GrabRoomUser().CurrentPosition.Z);
		
		LazyPathfinder Pathfinder = new LazyPathfinder(Session);
		
		Pathfinder.Start();
		
	}
}
