package com.grizzly.encryption;

import java.math.BigInteger;

public class HabboEncryption 
{
	private static BigInteger Big = new BigInteger("90e0d43db75b5b8ffc8a77e31cc9758fa43fe69f14184bef64e61574beb18fac32520566f6483b246ddc3c991cb366bae975a6f6b733fd9570e8e72efc1e511ff6e2bcac49bf9237222d7c2bf306300d4dfc37113bcc84fa4401c9e4f2b4c41ade9654ef00bd592944838fae21a05ea59fecc961766740c82d84f4299dfb33dd", 16);
	private static int E = 13;
	private static BigInteger Secret = new BigInteger("0");
	
	private RSA RSA;
	public Boolean Initialized;
	public RC4 RC4;
	private BigInteger Zero = new BigInteger("0");
	public DiffieHellman Hellman;
	
	public HabboEncryption()
	{
		this.RSA = new RSA(Big, E, Secret, Zero, Zero, Zero, Zero, Zero);
		this.Hellman = new DiffieHellman(200);
		this.RC4 = new RC4();
		this.Initialized = false;
	}
	
	public HabboEncryption(BigInteger _n, int _e, BigInteger _d)
	{
		Big = _n;
		E = _e;
		Secret = _d;
	
		this.RSA = new RSA(Big, E, Secret, Zero, Zero, Zero, Zero, Zero);
		this.RC4 = new RC4();
		this.Hellman = new DiffieHellman(200);
		this.Initialized = false;
	}
	
	public Boolean InitializeRC4(String ctext)
	{
		try
		{
			String publickey = this.RSA.Decrypt(ctext);
			//Environment.writeLine(publickey); // datkey
			String thatKey = publickey.replace((char)0 + "", "");
			Hellman.GenerateSharedKey(thatKey);
			this.RC4.init(Hellman.SharedKey.toByteArray());
		
			this.Initialized = true;
		
			return true;
		}
		catch (Exception e)
		{
			System.out.println(e);
			return false;
		}
	}
}
