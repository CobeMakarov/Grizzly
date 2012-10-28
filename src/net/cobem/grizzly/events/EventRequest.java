package net.cobem.grizzly.events;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class EventRequest implements Cloneable
{
	private Short Header;
	private ChannelBuffer Buffer;

	public EventRequest(Short id, ChannelBuffer buffer)
	{
		this.Header = id;
		this.Buffer = (buffer == null || buffer.readableBytes() == 0) ? ChannelBuffers.EMPTY_BUFFER : buffer;
	}

	
	public byte[] ReadBytes(int Amount)
	{
		return Buffer.readBytes(Amount).array();
	}
	
	public int ReadShort()
	{
		return ByteBuffer.wrap(ReadBytes(2)).asShortBuffer().get();	
	}
	
	public ChannelBuffer ReadFixedValue() 
	{
		
		return Buffer.readBytes(ReadShort());
	}

	public Integer PopInt()
	{
		try
		{
			return ByteBuffer.wrap(ReadBytes(4)).asIntBuffer().get();
		}
		catch (Exception e)
		{
		}
		return 0;
	}

	public int PopFixedInt()
	{
		int i = 0;
		String s = PopFixedString();

		i = Integer.parseInt(s);

		return i;
	}


	public String PopFixedString()
	{
		return new String(ReadFixedValue().toString(Charset.defaultCharset()));
	}

	public Short GetHeader()
	{
		return this.Header;
	}

	public String GetBodyString()
	{
		String str = new String(Buffer.toString(Charset.defaultCharset()));
		
		String consoleText = str;
		
		for (int i = 0; i < 13; i++) 
		{ 
			consoleText = consoleText.replace(Character.toString((char)i), "{" + i + "}");
		}
		
		return consoleText;
	}

	public int GetCurrentLength()
	{
		return Buffer.readableBytes();
	}
}
