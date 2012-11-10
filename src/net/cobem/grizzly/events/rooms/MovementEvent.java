package net.cobem.grizzly.events.rooms;

import net.cobem.grizzly.events.Event;
import net.cobem.grizzly.events.EventRequest;
import net.cobem.grizzly.habbohotel.pathfinding.Position;
import net.cobem.grizzly.habbohotel.sessions.Session;

public class MovementEvent implements Event 
{
	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		if (Session.GrabActor().Frozen)
		{
			return;
		}
		
		int X = Request.PopInt();
		int Y = Request.PopInt();
			
		Session.GrabActor().GoalPosition = new Position(X, Y, Session.GrabActor().CurrentPosition.Z);
		
		if (Session.GrabActor().IsMoving)
		{
			Session.GrabActor().NeedsPathChange = true;
		}
	}
}
