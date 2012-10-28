package net.cobem.grizzly.storage;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.cobem.grizzly.Grizzly;
import net.cobem.grizzly.misc.*;

public class DatabaseHandler 
{
	private Connection Connection;
	private Statement Statement;
	
	private String ClassQuery;
	
	public boolean TryConnect(Configuration Config)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
            Connection = (new DatabasePool(Config)).GrabConnection();
            
            Statement = Connection.createStatement();
            
            return true;
		}
		catch (ClassNotFoundException CNF) 
		{
	        return false;
	    } 
		catch (SQLException SQL) 
		{
	    	Grizzly.WriteOut(SQL.getMessage());
	    	return false;
	    }
	}
	
	public boolean RunFastQuery(String Query)
	{
		try
		{
			return Statement.execute(Query);
		}
		catch(SQLException E)
		{
			Grizzly.WriteOut(E.getMessage());
			return false;
		}
	}
	
	public void SetQuery(String Query)
	{
		if (ClassQuery != null)
		{
			try
			{
				Statement = this.Connection.createStatement();
			}
			catch(SQLException Ex)
			{
				Grizzly.WriteOut(Ex.getMessage());
			}
		}
		
		ClassQuery = Query;
	}
	
	public String GrabString()
	{
		try
		{
			ResultSet Result = Statement.executeQuery(ClassQuery);
			
			Result.first();
			
			return Result.getString(1);
		}
		catch(SQLException E)
		{
			Grizzly.WriteOut(E.getMessage());
			return null;
		}
	}
	
	public int GrabInt()
	{
		try
		{
			ResultSet Result = Statement.executeQuery(ClassQuery);
			
			Result.first();
			
			return Result.getInt(1);
		}
		catch(SQLException E)
		{
			Grizzly.WriteOut(E.getMessage());
			return 0;
		}
	}
	
	public ResultSet GrabRow()
	{
		try
		{
			ResultSet Result = Statement.executeQuery(ClassQuery);
			
			while(Result.next())
			{
				return Result;
			}
		}
		catch(SQLException E)
		{
			Grizzly.WriteOut(E.getMessage());
			return null;
		}
		
		return null;
	}
	
	public ResultSet GrabTable()
	{
		try
		{
			return Statement.executeQuery(ClassQuery);
		}
		catch(SQLException E)
		{
			Grizzly.WriteOut(E.getMessage());
			return null;
		}
	}
	
	public int RowCount()
	{
		try
		{
			ResultSet Result = Statement.executeQuery(ClassQuery);
			
			return (Result.last() ? Result.getRow() : 0);
		}
		catch(SQLException E)
		{
			Grizzly.WriteOut(E.getMessage());
			return 0;
		}
	}
}
