/*
 * UberEmulator ~ Meth0d
 */
package net.cobem.grizzly.habbohotel.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class AffectedTile
{

	public int X;
	public int Y;
	public int I;
	
	public AffectedTile()
	{
		X = 0;
		Y = 0;
		I = 0;
	}
	
	public AffectedTile(int x, int y, int i)
	{
		X = x;
		Y = y;
		I = i;
	}
	
	public static List<AffectedTile> GetAffectedTiles(int Length, int Width, int PosX, int PosY, int Rotation)
	{
		List<AffectedTile> PointList = new ArrayList<AffectedTile>();

		if (Length > 1)
		{
			if (Rotation == 0 || Rotation == 4)
			{
				for (int i = 0; i < Length; i++)
				{
					PointList.add(new AffectedTile(PosX, PosY + i, i));

					for (int j = 0; j < Width; j++)
					{
						PointList.add(new AffectedTile(PosX + j, PosY + i, (i < j) ? j : i));
					}
				}
			}
			else if (Rotation == 2 || Rotation == 6)
			{
				for (int i = 0; i < Length; i++)
				{
					PointList.add(new AffectedTile(PosX + i, PosY, i));

					for (int j = 0; j < Width; j++)
					{
						PointList.add(new AffectedTile(PosX + i, PosY + j, (i < j) ? j : i));
					}
				}
			}
		}

		if (Width > 1)
		{
			if (Rotation == 0 || Rotation == 4)
			{
				for (int i = 0; i < Width; i++)
				{
					PointList.add(new AffectedTile(PosX + i, PosY, i));

					for (int j = 0; j < Length; j++)
					{
						PointList.add(new AffectedTile(PosX + i, PosY + j, (i < j) ? j : i));
					}
				}
			}
			else if (Rotation == 2 || Rotation == 6)
			{
				for (int i = 0; i < Width; i++)
				{
					PointList.add(new AffectedTile(PosX, PosY + i, i));

					for (int j = 0; j < Length; j++)
					{
						PointList.add(new AffectedTile(PosX + j, PosY + i, (i < j) ? j : i));
					}
				}
			}
		}
		return PointList;
	}
}
