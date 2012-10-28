package net.cobem.grizzly.habbohotel.rooms.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model 
{
	public String Name;
	public String Heightmap;
	public String SerializeMap = "";
	public String SerializeRelativeMap = "";
	public int DoorX;
	public int DoorY;
	public int DoorZ;
	public int DoorRot;
	public int MapSizeX;
	public int MapSizeY;
	public int MapSize;
	public int OPEN = 0;
	public int CLOSED = 1;
	public List<String> Lines;
	public SquareState[][] Squares;
	public double[][] SquareHeight;
	public int[][] SqState;

	public Model(ResultSet Set)
	{
		try
		{
			this.Name = Set.getString("id");
			this.Heightmap = Set.getString("heightmap");
			this.DoorX = Set.getInt("door_x");
			this.DoorY = Set.getInt("door_y");
			this.DoorZ = Set.getInt("door_z");
			this.DoorRot = Set.getInt("door_dir");
		}
		catch(SQLException Ex)
		{
			
		}
		
		String[] Temporary = Heightmap.split(Character.toString((char)13));

		this.MapSizeX = Temporary[0].length();
		this.MapSizeY = Temporary.length;
		this.Lines = new ArrayList<String>();
		this.Lines = new ArrayList<String>(Arrays.asList(Heightmap.split(Character.toString((char)13))));

		this.SqState = new int[MapSizeX][MapSizeY];
		this.SquareHeight = new double[MapSizeX][MapSizeY];
		this.Squares = new SquareState[MapSizeX][MapSizeY];


		for (int y = 0; y < MapSizeY; y++)
		{
			if (y > 0)
			{
				Temporary[y] = Temporary[y].substring(1);
			}

			for (int x = 0; x < MapSizeX; x++)
			{
				String Square = Temporary[y].substring(x,x + 1).trim().toLowerCase();

				if (Square.equals("x"))
				{
					Squares[x][y] = SquareState.CLOSED;
					SqState[x][y] = CLOSED;
				}
				else if(isNumeric(Square))
				{
					Squares[x][y] = SquareState.WALKABLE;
					SqState[x][y] = OPEN;
					SquareHeight[x][y] = Double.parseDouble(Square);
					MapSize++;
				}

				if (this.DoorX == x && this.DoorY == y)
				{
					Squares[x][y] = SquareState.WALKABLE;
					SerializeRelativeMap += (int)DoorZ + "";
				}
				else
				{
					if(Square.isEmpty() || Square == null)
					{
						continue;
					}
					SerializeRelativeMap += Square;
				}
			}
			SerializeRelativeMap += (char)13;
		}

		for(String MapLine: Heightmap.split("\r\n"))
		{
			if(MapLine.isEmpty() || MapLine == null)
			{
				continue;
			}
			SerializeMap += MapLine + (char)13;
		}
	}
	public String Heightmap()
	{
		return SerializeMap;
	}
	public String RelativeHeightmap()
	{
		return SerializeRelativeMap;
	}
	
	private boolean isNumeric(String Input)
	{
		try
		{
			Integer.parseInt(Input);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public double GrabTileHeight(int X, int Y) 
	{
		return SquareHeight[X][Y];
	}
}
