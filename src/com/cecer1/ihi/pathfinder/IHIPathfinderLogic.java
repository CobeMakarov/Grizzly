package com.cecer1.ihi.pathfinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.cobem.grizzly.habbohotel.pathfinding.IPathfinder;


public class IHIPathfinderLogic implements IPathfinder
{
	/// <summary>
    ///   Stores the state of the tiles.
    /// </summary>
    private byte[][] _collisionMap;

    /// <summary>
    ///   Stores the height of the tiles.
    /// </summary>
    private float[][] _height;

    // IPathfinder Members

    /// <summary>
    ///   Set the tile map.
    /// </summary>
    /// <param name = "map">The state of the tiles.</param>
    /// <param name = "height">The height of the tiles.</param>
    public void ApplyCollisionMap(byte[][] map, float[][] height)
    {
        // Is this replacing an existing tile map
        if (_collisionMap != null)
            // Yes, ensure thread safety.
            synchronized (_collisionMap)
            {
                // Set the tile states.
                _collisionMap = map;
                // Set the tile heights
                _height = height;
            }
        else
        {
            // No, don't worry about other threads.

            // Set the tile states.
            _collisionMap = map;
            // Set the tile heights
            _height = height;
        }
    }

    /// <summary>
    ///   Get the next step on a path.
    /// </summary>
    /// <param name = "startX">PointA X</param>
    /// <param name = "startY">PointA Y</param>
    /// <param name = "endX">PointB X</param>
    /// <param name = "endY">PointB Y</param>
    /// <param name = "maxDrop">The Maximum height to drop in a single step.</param>
    /// <param name = "maxJump">The Maximum height to rise in a single step.</param>
    /// <returns></returns>
    public Collection<byte[]> Path(byte startX, byte startY, byte endX, byte endY, float maxDrop, float maxJump)
    {
        IHIPathfinderValues values;
        synchronized (_collisionMap) // Thread Safety
        {
            values = new IHIPathfinderValues(_collisionMap, _height, maxDrop, maxJump);

            if (endX >= _collisionMap.length || // Is EndX outside the bounds of the collision map?
                endY >= _collisionMap[0].length || // Is EndY outside the bounds of the collision map?
                startX >= _collisionMap.length || // Is StartX outside the bounds of the collision map?
                startY >= _collisionMap[0].length || // Is StartY outside the bounds of the collision map?
                _collisionMap[endX][endY] == 0 || // Is the target blocked by the collision map?
                (startX == endX && startY == endY)) // Is the start also the target?
                
                // If any of these are yes, no path can be made. Don't run the path finder.
                return new ArrayList<byte[]>();

            // Init
            values.Count++;
            values.BinaryHeap[values.Count] = values.LastID;
            values.X[values.LastID] = startX;
            values.Y[values.LastID] = startY;
            values.H[values.LastID] = (short) GetH(startX, startY, endX, endY);
            values.Parent[values.LastID] = 0;
            values.G[values.LastID] = 0;
            values.F[values.LastID] = (short) (values.G[values.LastID] + values.H[values.LastID]);

            //endregion-here

            while (values.Count != 0)
            {
            	values.Location = values.BinaryHeap[1];

            	if (values.X[values.Location] == endX && values.Y[values.Location] == endY)
            		break;

            	Move(values);

            	// Add the surrounding tiles.

                Add(-1, 0, endX, endY, values);
                Add(0, -1, endX, endY, values);
                Add(1, 0, endX, endY, values);
                Add(0, 1, endX, endY, values);

                Add(-1, -1, endX, endY, values);
                Add(-1, 1, endX, endY, values);
                Add(1, -1, endX, endY, values);
                Add(1, 1, endX, endY, values);
                //endregion-here
            }
        }

        // If no new tiles can be checked then the path must be impossible.
        if (values.Count == 0)
        	return new ArrayList<byte[]>();

        ArrayList<byte[]> path = new ArrayList<byte[]>();

        while (values.X[values.Parent[values.Location]] != startX ||
               values.Y[values.Parent[values.Location]] != startY)
        {
            path.add(new byte[] {values.X[values.Location], values.Y[values.Location]});
            values.Location = values.Parent[values.Location];
        }
        path.add(new byte[] {values.X[values.Location], values.Y[values.Location]});
        
        Collections.reverse(path);

        return path;
    }

    //endregion-here

    /// <summary>
    ///   Estimate the cost from X,Y to EndX,EndY.
    /// </summary>
    /// <returns></returns>
    private static int GetH(int x, int y, int endX, int endY)
    {
        return (Math.abs(x + endX) + Math.abs(y + endY));
    }

    /// <summary>
    /// </summary>
    private void Add(int j, int k, byte endX, byte endY, IHIPathfinderValues values)
    {
        byte x2 = (byte) (values.X[values.Location] + j);
        byte y2 = (byte) (values.Y[values.Location] + k);
        short parent = values.Location;
        
        // PATHFINDER RULE: Disallow (non-)tiles beyond the map
        if (x2 >= _collisionMap.length || y2 >= _collisionMap[0].length)
            return;
        //endregion-here

        // PATHFINDER RULE: Disallow closed tiles
        if (_collisionMap[x2][y2] == 0)
            return;
        //endregion-here
        // PATHFINDER RULE: Disallow interactive tiles EXCEPT for destination tile.
        if (_collisionMap[x2][y2] == 2 && (x2 != endX || y2 != endY))
            return;
        //endregion-here

        float z = values.Z[x2][y2];
        float z2 = values.Z[values.X[parent]][values.Y[parent]];

        // PATHFINDER RULE: Disallow height changes beyond the limit
        if (z > z2 + values.MaxJump || z < z2 - values.MaxDrop)
            return;
        //endregion-here

        if (parent > 0)
        {
            // PATHFINDER RULE: Disallow parernt tile (backtracking)
            if (values.X[parent] == x2 && values.Y[parent] == y2)
                return;
            //endregion-here
            // PATHFINDER RULE: Disallow diagonals when walking though solid/interactive corners
            if (_collisionMap[x2][values.Y[parent]] == 0 || _collisionMap[x2][values.Y[parent]] == 2)
                return;
            if (_collisionMap[values.X[parent]][y2] == 0 || _collisionMap[values.X[parent]][y2] == 2)
                return;
            //endregion-here
        }


        if (values.Tiles[x2][y2] == 1)
        {
            short i = 1;
            for (; i <= values.Count; i++)
            {
                if (values.X[i] == x2 && values.Y[i] == y2)
                    break;
            }

            if (values.X[i] == endX || values.Y[i] == endY)
            {
                if (10 + values.G[parent] < values.G[i])
                    values.Parent[i] = parent;
            }
            else if (14 + values.G[parent] < values.G[i])
                values.Parent[i] = parent;
            return;
        }

        values.LastID++;
        values.Count++;
        values.BinaryHeap[values.Count] = values.LastID;
        values.Y[values.LastID] = y2;
        values.H[values.LastID] = (short) GetH(x2, y2, endX, endY);
        values.Parent[values.LastID] = parent;

        if (x2 == values.X[parent] || y2 == values.Y[parent])
            values.G[values.LastID] = (short) (10 + values.G[parent]);
        else
            values.G[values.LastID] = (short) (14 + values.G[parent]);
        values.F[values.LastID] = (short) (values.G[values.LastID] + values.H[values.LastID]);

        for (short c = values.Count; c != 1; c /= 2)
        {
            if (values.F[values.BinaryHeap[c]] > values.F[values.BinaryHeap[c/2]])
                break;
            short temp = values.BinaryHeap[c/2];
            values.BinaryHeap[c/2] = values.BinaryHeap[c];
            values.BinaryHeap[c] = temp;
        }
        values.Tiles[x2][y2] = 1;
    }

    private static void Move(IHIPathfinderValues values)
    {
        values.Tiles[values.X[values.BinaryHeap[1]]][values.Y[values.BinaryHeap[1]]] = 2;


        values.BinaryHeap[1] = values.BinaryHeap[values.Count];
        values.Count--;

        short location = 1;
        while (true)
        {
            short high = location;
            if (2*high + 1 <= values.Count)
            {
                if (values.F[values.BinaryHeap[high]] >= values.F[values.BinaryHeap[2*high]])
                    location = (short) (2*high);
                if (values.F[values.BinaryHeap[location]] >= values.F[values.BinaryHeap[2*high + 1]])
                    location = (short) (2*high + 1);
            }
            else if (2*high <= values.Count)
            {
                if (values.F[values.BinaryHeap[high]] >= values.F[values.BinaryHeap[2*high]])
                    location = (short) (2*high);
            }

            if (high == location)
                break;
            short temp = values.BinaryHeap[high];
            values.BinaryHeap[high] = values.BinaryHeap[location];
            values.BinaryHeap[location] = temp;
        }
    }
}
