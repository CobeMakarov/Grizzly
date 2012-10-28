package net.cobem.grizzly.habbohotel.rooms.items;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.events.EventResponse;
import net.cobem.grizzly.events.iSerializeEvent;
import net.cobem.grizzly.habbohotel.furni.Furniture;

public class FloorItem implements iSerializeEvent
{
	public int ID;
	public int Base;
	public int X;
	public int Y;
	public int Rotation;
	public Float Height;
	public String StringHeight;
	public String ExtraData;
	
	public FloorItem(ResultSet Set)
	{
		this.FillClass(Set);
	}
	
	private boolean FillClass(ResultSet Set)
	{
		try
		{
			this.ID = Set.getInt("id");
			this.Base = Set.getInt("base");
			this.X = Set.getInt("x");
			this.Y = Set.getInt("y");
			this.Rotation = Set.getInt("rotation");
			this.Height = Set.getFloat("height");
			this.StringHeight = "" + this.Height;
			this.ExtraData = Set.getString("extra");
			
			return true;
		}
		catch(SQLException Ex)
		{
			Grizzly.WriteOut(Ex.toString());
			return false;
		}
	}
	public Furniture GetBaseItem()
	{
		return Grizzly.GrabHabboHotel().GrabFurnitureHandler().GrabFurniByID(Base);
	}
	
	public void SerializePacket(EventResponse Message)
	{
		Message.AppendInt32(ID);
        Message.AppendInt32(GetBaseItem().Sprite);
        Message.AppendInt32(X);
        Message.AppendInt32(Y);
        Message.AppendInt32(Rotation);
        Message.AppendString(Float.toString(Height));
        Message.AppendInt32(0);
        Message.AppendInt32(0);
        Message.AppendString(ExtraData);
        Message.AppendInt32(-1);
        Message.AppendInt32(GetBaseItem().Interaction.equals("default") ? 1 : 0);
	}
	
	/**
	 * @source: Shynoshy + Alex
	 */
	public boolean ChangeState()
	{
		if ((this.GetBaseItem().Interaction.equals("default") || this.GetBaseItem().Interaction.equals("gate")) && (this.GetBaseItem().InteractionModesCount > 1))
		{
			if (ExtraData.isEmpty())
			{
                ExtraData = "0";
			}
			
			Integer Temp = Integer.parseInt(ExtraData) + 1;
			
			if (Temp >= this.GetBaseItem().InteractionModesCount)
			{
				ExtraData = "0";
			}
			else
			{
				ExtraData = "1";
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}


	/**
	 * @source: Shynoshy + Alex
	 */
	public void SaveState()
	{
		Grizzly.GrabDatabase().RunFastQuery("UPDATE server_room_items SET extra = '" + ExtraData + "' WHERE id = '" + ID + "'");
	}
}
