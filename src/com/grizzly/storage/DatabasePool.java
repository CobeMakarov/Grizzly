package com.grizzly.storage;

import com.grizzly.Grizzly;
import com.grizzly.misc.*;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabasePool 
{
	private BoneCP Pool;
	
	public DatabasePool(Configuration Config)
	{
		BoneCPConfig CPConfig = new BoneCPConfig();
		CPConfig.setJdbcUrl("jdbc:mysql://" + Config.GrabValue("jdbc.grizzly.host") + "/" + Config.GrabValue("jdbc.grizzly.database"));
		CPConfig.setMinConnectionsPerPartition(2);
		CPConfig.setMaxConnectionsPerPartition(10);
		CPConfig.setPartitionCount(1);
		CPConfig.setUsername(Config.GrabValue("jdbc.grizzly.username"));
		CPConfig.setPassword(Config.GrabValue("jdbc.grizzly.password"));
		
		try 
		{
			Pool = new BoneCP(CPConfig);
		} 
		catch (SQLException e) 
		{
			Grizzly.WriteOut(e.getMessage());
		}
	}
	
	public Connection GrabConnection()
	{
		try 
		{
			return Pool.getConnection();
		} 
		catch (SQLException e) 
		{
			return null;
		}
	}
}
