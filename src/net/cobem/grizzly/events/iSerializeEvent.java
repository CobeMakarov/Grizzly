package net.cobem.grizzly.events;

import net.cobem.grizzly.events.EventResponse;

public interface iSerializeEvent
{
	public void SerializePacket(EventResponse Message);
}
