// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Variant.java

package com.genuitec.cef;


// Referenced classes of package com.genuitec.cef:
//            JNIObject, CEFBrowser

public class Variant extends JNIObject
{

    public int getType()
    {
        if(!$assertionsDisabled && !isAlive())
            throw new AssertionError();
        else
            return doGetType();
    }

    public void setNull()
    {
        if(!$assertionsDisabled && !isAlive())
        {
            throw new AssertionError();
        } else
        {
            doSetNull();
            return;
        }
    }

    public void setBool(boolean val)
    {
        if(!$assertionsDisabled && !isAlive())
        {
            throw new AssertionError();
        } else
        {
            doSetBool(val);
            return;
        }
    }

    public void setInt(int val)
    {
        if(!$assertionsDisabled && !isAlive())
        {
            throw new AssertionError();
        } else
        {
            doSetInt(val);
            return;
        }
    }

    public void setDouble(double val)
    {
        if(!$assertionsDisabled && !isAlive())
        {
            throw new AssertionError();
        } else
        {
            doSetDouble(val);
            return;
        }
    }

    public void setString(String val)
    {
        if(!$assertionsDisabled && !isAlive())
        {
            throw new AssertionError();
        } else
        {
            doSetString(val);
            return;
        }
    }

    public Number getNumber()
    {
        if(getType() == 2)
            return Integer.valueOf(getInt());
        if(getType() == 3)
            return Double.valueOf(getDouble());
        else
            return null;
    }

    public boolean getBool()
    {
        if(!$assertionsDisabled && !isAlive())
            throw new AssertionError();
        if(!$assertionsDisabled && getType() != 1)
            throw new AssertionError();
        else
            return doGetBool();
    }

    public int getInt()
    {
        if(!$assertionsDisabled && !isAlive())
            throw new AssertionError();
        if(!$assertionsDisabled && getType() != 2)
            throw new AssertionError();
        else
            return doGetInt();
    }

    public double getDouble()
    {
        if(!$assertionsDisabled && !isAlive())
            throw new AssertionError();
        if(!$assertionsDisabled && getType() != 3)
            throw new AssertionError();
        else
            return doGetDouble();
    }

    public String getString()
    {
        if(!$assertionsDisabled && !isAlive())
            throw new AssertionError();
        if(!$assertionsDisabled && getType() != 4)
            throw new AssertionError();
        else
            return doGetString();
    }

    public static native Variant create(CEFBrowser cefbrowser);

    public static Variant create(CEFBrowser browser, String value)
    {
        Variant res = create(browser);
        res.setString(value);
        return res;
    }

    public static Variant create(CEFBrowser browser, int value)
    {
        Variant res = create(browser);
        res.setInt(value);
        return res;
    }

    public static Variant create(CEFBrowser browser, double value)
    {
        Variant res = create(browser);
        res.setDouble(value);
        return res;
    }

    public static Variant create(CEFBrowser browser, boolean value)
    {
        Variant res = create(browser);
        res.setBool(value);
        return res;
    }

    protected Variant()
    {
    }

    public boolean isObject()
    {
        return getType() == 6;
    }

    public boolean isBool()
    {
        return getType() == 1;
    }

    public boolean isInt()
    {
        return getType() == 2;
    }

    public boolean isDouble()
    {
        return getType() == 3;
    }

    public boolean isNumber()
    {
        return getType() == 3 || getType() == 2;
    }

    public boolean isString()
    {
        return getType() == 4;
    }

    private native int doGetType();

    private native boolean doGetBool();

    private native int doGetInt();

    private native double doGetDouble();

    private native String doGetString();

    private native void doSetNull();

    private native void doSetBool(boolean flag);

    private native void doSetInt(int i);

    private native void doSetDouble(double d);

    private native void doSetString(String s);

    public static final int TYPE_NULL = 0;
    public static final int TYPE_BOOL = 1;
    public static final int TYPE_INT = 2;
    public static final int TYPE_DOUBLE = 3;
    public static final int TYPE_STRING = 4;
    public static final int TYPE_VOID = 5;
    public static final int TYPE_OBJECT = 6;
    static final boolean $assertionsDisabled = !com/genuitec/cef/Variant.desiredAssertionStatus();

}
