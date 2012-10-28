package net.cobem.grizzly.habbohotel.pathfinding;

public class Position 
{
	public int X;
	public int Y;
	public int Z;
	
	public Position(int X, int Y, int Z)
	{
		this.X = X;
		this.Y = Y;
		this.Z = Z;
	}
	
	public Position GetPositionInFront(int Rotation)
	{
		Position Return = new Position(this.X, this.Y, this.Z);
		
		switch(Rotation)
		{
			case 0:
				Return.Y--;
			break;
			
			case 1:
				Return.X--;
				Return.Y--;
			break;
			
			case 2:
				Return.X++;
			break;
			
			case 3:
				Return.X--;
				Return.Y++;
			break;
			
			case 4:
				Return.Y++;
			break;
			
			case 5:
				Return.X++;
				Return.Y++;
			break;
			
			case 6:
				Return.X--;
			break;
			
			case 7:
				Return.X++;
				Return.Y--;
			break;
		}
		
		return Return;
	}
	
	public Position GetPositionBehind(int Rotation)
	{
		if (Rotation > 3)
		{
			Rotation -= 4;
		}
		else
		{
			Rotation += 4;
		}
		
		return this.GetPositionInFront(Rotation);
	}
	
	public double GetDistanceFromPosition(Position New)
    {
        return Math.sqrt(Math.pow(this.X - New.X, 2) + Math.pow(this.Y - New.Y, 2));
    }
	
	public int Calculate(int NewX, int NewY)
    {
        int Rotation = 0;

        if (X > NewX && Y > NewY)
            Rotation = 7;
        else if (X < NewX && Y < NewY)
            Rotation = 3;
        else if (X > NewX && Y < NewY)
            Rotation = 5;
        else if (X < NewX && Y > NewY)
            Rotation = 1;
        else if (X > NewX)
            Rotation = 6;
        else if (X < NewX)
            Rotation = 2;
        else if (Y < NewY)
            Rotation = 4;
        else if (Y > NewY)
            Rotation = 0;
  
        return Rotation;
    }
	
	public String GrabPair()
	{
		return "{" + this.X + "," + this.Y + "}";
	}
}
