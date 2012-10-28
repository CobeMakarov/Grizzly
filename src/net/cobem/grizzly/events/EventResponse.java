package net.cobem.grizzly.events;

import java.io.IOException;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;

public class EventResponse
{
	private int Id;
	private ChannelBufferOutputStream bodystream;
	private ChannelBuffer body;
	
	public EventResponse Initialize(int id)
	{
		this.Id = id;
		this.body = ChannelBuffers.dynamicBuffer();
		this.bodystream = new ChannelBufferOutputStream(body);

		try 
		{
			this.bodystream.writeInt(0);
			this.bodystream.writeShort(id);

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return this;
	}
	
	public EventResponse AppendString(Object obj)
	{
		try 
		{
			bodystream.writeUTF(obj.toString());
		} 
		catch (IOException e) {}
		return this;
	}
	
	public void AppendInt32(Integer obj)
	{
		try {
			bodystream.writeInt(obj);
		} catch (IOException e) {
		}
	}
	
	public EventResponse AppendShort(int obj)
	{
		try 
		{
			bodystream.writeShort((short)obj);
		} 
		catch (IOException e) {}
		return this;
	}
	
	public EventResponse AppendBoolean(Boolean obj)
	{
		try 
		{
			bodystream.writeBoolean(obj);
		} 
		catch (IOException e) {}
		return this;
	}
	
	public void AppendBody(iSerializeEvent Obj)
	{
		try 
		{
			Obj.SerializePacket(this);
		} 
		catch (Exception e) {}
	}
	
	public String GetBodyString()
	{
		String str = new String(body.toString(Charset.defaultCharset()));

		String consoleText = str;

		for (int i = 0; i < 13; i++) 
		{ 
			consoleText = consoleText.replace(Character.toString((char)i), "{" + i + "}");
		}

		return consoleText;
	}
	
	public int GetHeader() 
	{
		return Id;
	}
	
	public ChannelBuffer Get()
	{
		body.setInt(0, body.writerIndex() - 4);
		
		return this.body;
	}
}
