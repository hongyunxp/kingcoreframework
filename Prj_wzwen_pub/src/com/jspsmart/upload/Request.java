// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   Request.java

package com.jspsmart.upload;

import java.util.Enumeration;
import java.util.Hashtable;

public class Request
{

    private Hashtable m_parameters;
    private int m_counter;

    Request()
    {
        m_parameters = new Hashtable();
        m_counter = 0;
    }

    protected void putParameter(String name, String value)
    {
        if(name == null)
            throw new IllegalArgumentException("The name of an element cannot be null.");
        if(m_parameters.containsKey(name))
        {
            Hashtable values = (Hashtable)m_parameters.get(name);
            values.put(new Integer(values.size()), value);
        } else
        {
            Hashtable values = new Hashtable();
            values.put(new Integer(0), value);
            m_parameters.put(name, values);
            m_counter++;
        }
    }

    public String getParameter(String name)
    {
        if(name == null)
            throw new IllegalArgumentException("Form's name is invalid or does not exist (1305).");
        Hashtable values = (Hashtable)m_parameters.get(name);
        if(values == null)
            return null;
        else
            return (String)values.get(new Integer(0));
    }

    public Enumeration getParameterNames()
    {
        return m_parameters.keys();
    }

    public String[] getParameterValues(String name)
    {
        if(name == null)
            throw new IllegalArgumentException("Form's name is invalid or does not exist (1305).");
        Hashtable values = (Hashtable)m_parameters.get(name);
        if(values == null)
            return null;
        String strValues[] = new String[values.size()];
        for(int i = 0; i < values.size(); i++)
            strValues[i] = (String)values.get(new Integer(i));

        return strValues;
    }
}
