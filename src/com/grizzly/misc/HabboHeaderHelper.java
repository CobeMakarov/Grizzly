package com.grizzly.misc;

import java.nio.ByteBuffer;

public class HabboHeaderHelper 
{	
	public Short TryCalculateHeader(byte FirstNumber, byte CharValue)
	{
		byte[] ByteArray = new byte[] { FirstNumber, CharValue };
		
		return ByteBuffer.wrap(ByteArray).asShortBuffer().get();
	}
}
