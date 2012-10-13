package com.grizzly.encryption;
/*
 * capostrike
 * joopie
 */

import java.math.BigInteger;

public class RSA {
	
	public int Exponent; // public exponent
	public BigInteger n; // modulus
	// private key
	public BigInteger Private;
	// extended private key
	public BigInteger p;
	public BigInteger q;
	public BigInteger dmp1;
	public BigInteger dmq1;
	public BigInteger coeff;
	// bools (can encrypt/decrypt?)
	public boolean Decryptable;
	public boolean Encryptable;

	private BigInteger Zero = new BigInteger("0");

	public RSA(BigInteger N, int E, BigInteger D, BigInteger P, BigInteger Q, BigInteger DP, BigInteger DQ, BigInteger C)
	{
		this.n = N;
		this.Exponent = E;
		this.Private = D;
		this.p = P;
		this.q = Q;
		this.dmp1 = DP;
		this.dmq1 = DQ;
		this.coeff = C;
		// adjust a few flags.
		Encryptable = (n != null && n != Zero && Exponent != 0);
		Decryptable = (Encryptable && Private != Zero && Private != null);
	}

	public int GetBlockSize() 
	{
		return (n.bitLength() + 7) / 8;
	}

	public BigInteger DoPublic(BigInteger x)
	{
		if (this.Encryptable)
		{
			return x.modPow( new BigInteger(this.Exponent + ""), this.n);
		}
	
		return Zero;
	}

	public String Encrypt(String text)
	{

		BigInteger m = new BigInteger(this.pkcs1pad2(text.getBytes(), this.GetBlockSize()));
		
		if (m.equals(Zero))
		{
			return null;
		}
	
		BigInteger c = this.DoPublic(m);
		
		if (c.equals(Zero))
		{
			return null;
		}
	
		String result = c.toString(16);
		
		if ((result.length() & 1) == 0)
		{
			return result;
		}

		return "0" + result;
	}

	private byte[] pkcs1pad2(byte[] data, int n)
	{
		byte[] bytes = new byte[n];
		
		int i = data.length - 1;
		
		while (i >= 0 && n > 11)
		{
			bytes[--n] = data[i--];
		}
		
		bytes[--n] = 0;
	
		while (n > 2)
		{
			bytes[--n] = 0x01;
		}
	
		bytes[--n] = 0x2;
		bytes[--n] = 0;
	
		return bytes;
	}

	public BigInteger DoPrivate(BigInteger x)
	{
		if (this.Decryptable)
		{
			return x.modPow(this.Private, this.n);
		}
	
		return Zero;
	}

	public String Decrypt(String ctext)
	{
		BigInteger c = new BigInteger(ctext, 16);
		BigInteger m = this.DoPrivate(c);
		
		if (m.equals(Zero))
		{
			return null;
		}
		
		byte[] bytes = this.pkcs1unpad2(m, this.GetBlockSize());
	
		if (bytes == null)
		{
			return null;
		}
	
		return new String(bytes);
	}
	/**
	* 
	* @param src
	* @param n
	* @param type Not used.
	* @return 
	* 
	*/
	private byte[] pkcs1unpad2(BigInteger src, int n) 
	{
		byte[] bytes = src.toByteArray();
		byte[] out;
		int i = 0;
		
		while(i < bytes.length && bytes[i] == 0) 
		{
			++i;
		}
		
		if(bytes.length-i != n-1 || bytes[i]>2) 
		{
			return null;
		}
		
		++i;
		
		while(bytes[i]!=0) 
		{
			if(++i>=bytes.length)
			{
				return null;
			}
		}
		
		out = new byte[(bytes.length-i)+1];
		int p = 0;
		
		while (++i < bytes.length) 
		{
			out[p++] = (bytes[i]);
		}
		
		return out;
	}
}