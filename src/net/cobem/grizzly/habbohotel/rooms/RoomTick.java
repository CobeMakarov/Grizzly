package net.cobem.grizzly.habbohotel.rooms;

import net.cobem.grizzly.Grizzly;

public class RoomTick implements Runnable
{
	private boolean Running;
	private Thread RoomZ;
	
	public RoomTick()
	{
		RoomZ = new Thread(this);
		RoomZ.start();
		Running = true;
	}
	
	@Override
	public void run()
	{
		while(Running)
		{
			for(Room mRoom : Grizzly.GrabHabboHotel().GrabRoomHandler().GrabPopulatedRooms().values())
			{
				mRoom.Tick();
			}
			
			try 
			{
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
	}
	
	public void Stop()
	{
		Running = false;
	}
}
