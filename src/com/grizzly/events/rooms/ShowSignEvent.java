package com.grizzly.events.rooms;

import com.grizzly.events.Event;
import com.grizzly.events.EventRequest;
import com.grizzly.habbohotel.sessions.Session;

public class ShowSignEvent implements Event
{

	@Override
	public void Parse(Session Session, EventRequest Request)
	{
		int Sign = Request.PopInt();
		
		while(true)
		{
			Session.GrabRoomUser().UpdateStatus("sign " + Sign);
			
			try 
			{
				Thread.sleep(1500);
			} catch (InterruptedException e){}
			
			Session.GrabRoomUser().UpdateStatus("");
		}
	}

}
