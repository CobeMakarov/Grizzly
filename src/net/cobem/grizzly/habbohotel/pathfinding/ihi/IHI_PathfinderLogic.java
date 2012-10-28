package net.cobem.grizzly.habbohotel.pathfinding.ihi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.cobem.grizzly.habbohotel.pathfinding.IPathfinder;

public class IHI_PathfinderLogic implements IPathfinder
{
	/**
	 * Stores the state of the tiles.
	 */
	private byte [][] collisionMap;
	
	/**
	 * Stores the height of the tiles.
	 */
	private float [][] height;
	
	
	/**
	 * Set the tile map.
	 * @param collisionMap The new state of the tiles.
	 * @param height The new height of the tiles.
	 */
    public void ApplyCollisionMap(byte[][] collisionMap, float[][] height)
    {
        // Is this replacing an existing tile map
        if (this.collisionMap != null)
            // Yes, ensure thread safety.
            synchronized (this.collisionMap)
            {
                // Set the tile states.
            	this.collisionMap = collisionMap;
                // Set the tile heights
            	this.height = height;
            }
        else
        {
            // No, don't worry about other threads.

            // Set the tile states.
        	this.collisionMap = collisionMap;
            // Set the tile heights
        	this.height = height;
        }
    }
    
    /**
	  Get the next step on a path.
    
	  @param startX PointA X
	  @param startY PointA Y
	  @param endX PointB X
	  @param endY PointB Y
	  @param maximumFall The maximum height a path allowed to decrease in a single step.
	  @param maximumJump The maximum height a path allowed to increase in a single step.
	 */
	 public Collection<byte[]> Path(byte startX, byte startY, byte endX, byte endY, float maximumFall, float maximumJump)
	 {
	     IHI_PathfinderValues values;
	     synchronized (collisionMap) // Thread Safety
	     {
	         values = new IHI_PathfinderValues(collisionMap, height, maximumFall, maximumJump);
	
	         if (endX >= collisionMap.length || // Is EndX outside the bounds of the collision map?
	             endY >= collisionMap[0].length || // Is EndY outside the bounds of the collision map?
	             startX > collisionMap.length-1 || // Is StartX outside the bounds of the collision map?
	             startY > collisionMap[0].length-1 || // Is StartY outside the bounds of the collision map?
	             collisionMap[endX][endY] == 0 || // Is the target blocked by the collision map?
	             (startX == endX && startY == endY)) // Is the start also the target?
	        	 
	        	 // If any of these are yes, no path can be made. Don't run the path finder.
	             return new ArrayList<byte[]>();
	
	         /**************
	          * Enter Init *
	          **************/
	
	         /*
	          * G = Cost so far.
	          * H = Estimated remaining cost.
	          * F = G + H.
	          */
	
	         values.count++;
	         values.binaryHeap[values.count] = values.lastID;
	         values.X[values.lastID] = startX;
	         values.Y[values.lastID] = startY;
	         values.H[values.lastID] = (short) GetH(startX, startY, endX, endY);
	         values.parent[values.lastID] = 0;
	         values.G[values.lastID] = 0;
	         values.F[values.lastID] = (short) (values.G[values.lastID] + values.H[values.lastID]);
	
	         /**************
	          * Leave Init *
	          **************/
	
	         while (values.count != 0)
	         {
	             values.location = values.binaryHeap[1];
	
	             if (values.X[values.location] == endX && values.Y[values.location] == endY)
	                 break;
	
	             Move(values);
	             
	             //TODO: I commented out first Add method!
	             // Add the surrounding tiles to the open list.
	             try
	             {
		             Add(-1, 0, endX, endY, values);
		             Add(0, -1, endX, endY, values);
		             Add(1, 0, endX, endY, values);
		             Add(0, 1, endX, endY, values);
		
		             Add(-1, -1, endX, endY, values);
		             Add(-1, 1, endX, endY, values);
		             Add(1, -1, endX, endY, values);
		             Add(1, 1, endX, endY, values);
	             }
	             catch(ArrayIndexOutOfBoundsException Ex)
	             {
	            	 //Grizzly.WriteOut("Movement Exception at " + endX + "X and " + endY + "Y");
	             }
	         }
	     }
	
	     // If no new tiles can be checked then the path must be impossible.
	     if (values.count == 0)
	         return new ArrayList<byte[]>();
	         
	
         ArrayList<byte[]> path = new ArrayList<byte[]>();
	
	     while (values.X[values.parent[values.location]] != startX ||
	            values.Y[values.parent[values.location]] != startY)
	     {
	         path.add(new byte[] {values.X[values.location], values.Y[values.location]});
	         values.location = values.parent[values.location];
	     }
	     path.add(new byte[] {values.X[values.location], values.Y[values.location]});
	     
	     Collections.reverse(path);
	
	     return path;
	 }
	 
	 /**
	  * Estimate the cost from X,Y to EndX,EndY.
	  */
     private static int GetH(int x, int y, int endX, int endY)
     {
         return (Math.abs(x + endX) + Math.abs(y + endY));
     }
     
     /**
      * Add a tile to the open list.
      * @param x The X offset from the current tile.  
      * @param y The Y offset from the current tile.
      * @param endX The X coordinate of the destination tile.
      * @param endY The Y coordinate of the destination tile.
      * @param values The values instance for this path calculation.
      */
     private void Add(int x, int y, byte endX, byte endY, IHI_PathfinderValues values)
     {
    	 byte x2 = (byte) (values.X[values.location] + x);
         byte y2 = (byte) (values.Y[values.location] + y);
         
         short parent = values.location;

         // Disallow paths outside the map.
         if (x2 >= collisionMap.length || y2 >= collisionMap[0].length)
             return;

         
         if (values.tiles[x2][y2] == 2)
             return;
         
         // If the tile blocked or a seat/bed that isn't on the last tile, disallow.
         if ((collisionMap[x2][y2] == 0 || (collisionMap[x2][y2] == 2 && (x2 != endX || y2 != endY))))
             return;

         float z = values.Z[x2][y2];
         float z2 = values.Z[values.X[parent]][values.Y[parent]];
         
         // Disallow excessive height changes.
         if (z > z2 + values.maximumJump || z < z2 - values.maximumFall)
             return;
         
         // If this is the starting tile skip diagonal checking.
         if (parent > 0)
         {
        	 // If the step is diagonal then check for corner cutting and disallow.
             if (values.X[parent] != x2 && values.Y[parent] != y2)
             {
                 if (collisionMap[x2][values.Y[parent]] == 0 || collisionMap[x2][values.Y[parent]] == 2)
                     return;
                 if (collisionMap[values.X[parent]][y2] == 0 || collisionMap[values.X[parent]][y2] == 2)
                     return;
             }
         }
         
         if (values.tiles[x2][y2] == 1)
         {
             short i = 1;
             for (; i <= values.count; i++)
             {
                 if (values.X[i] == x2 && values.Y[i] == y2)
                     break;
             }

             if (values.X[i] == endX || values.Y[i] == endY)
             {
                 if (10 + values.G[parent] < values.G[i])
                     values.parent[i] = parent;
             }
             else if (14 + values.G[parent] < values.G[i])
                 values.parent[i] = parent;
             return;
         }

         values.lastID++;
         values.count++;
         values.binaryHeap[values.count] = values.lastID;
         values.X[values.lastID] = x2;
         values.Y[values.lastID] = y2;
         values.H[values.lastID] = (short) GetH(x2, y2, endX, endY);
         values.parent[values.lastID] = parent;

         if (x2 == values.X[parent] || y2 == values.Y[parent])
             values.G[values.lastID] = (short) (10 + values.G[parent]);
         else
             values.G[values.lastID] = (short) (14 + values.G[parent]);
         values.F[values.lastID] = (short) (values.G[values.lastID] + values.H[values.lastID]);

         for (short c = values.count; c != 1; c /= 2)
         {
             if (values.F[values.binaryHeap[c]] > values.F[values.binaryHeap[c/2]])
                 break;
             short temp = values.binaryHeap[c / 2];
             values.binaryHeap[c/2] = values.binaryHeap[c];
             values.binaryHeap[c] = temp;
         }
         values.tiles[x2][ y2] = 1;
     }

     private static void Move(IHI_PathfinderValues values)
     {
         values.tiles[values.X[values.binaryHeap[1]]][values.Y[values.binaryHeap[1]]] = 2;


         values.binaryHeap[1] = values.binaryHeap[values.count];
         values.count--;

         short location = 1;
         while (true)
         {
             short high = location;
             if (2*high + 1 <= values.count)
             {
                 if (values.F[values.binaryHeap[high]] >= values.F[values.binaryHeap[2*high]])
                     location = (short) (2*high);
                 if (values.F[values.binaryHeap[location]] >= values.F[values.binaryHeap[2*high + 1]])
                     location = (short) (2*high + 1);
             }
             else if (2*high <= values.count)
             {
                 if (values.F[values.binaryHeap[high]] >= values.F[values.binaryHeap[2*high]])
                     location = (short) (2*high);
             }

             if (high == location)
                 break;
             
             short temp = values.binaryHeap[high];
             values.binaryHeap[high] = values.binaryHeap[location];
             values.binaryHeap[location] = temp;
         }
     }
}
