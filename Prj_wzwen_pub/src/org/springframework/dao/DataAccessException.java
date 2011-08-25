// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DataAccessException.java

package org.springframework.dao;

import org.springframework.core.NestedRuntimeException;

public abstract class DataAccessException extends NestedRuntimeException
{

	public DataAccessException(String msg)
	{
		super(msg);
	}

	public DataAccessException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}