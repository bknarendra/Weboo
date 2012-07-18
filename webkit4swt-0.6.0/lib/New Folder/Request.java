// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Request.java

package com.genuitec.cef;


// Referenced classes of package com.genuitec.cef:
//            JNIObject

public class Request extends JNIObject
{

    public Request()
    {
    }

    public native String getURL();

    public native void setURL(String s);

    public native String getFrame();

    public native void setFrame(String s);

    public native String getMethod();

    public native void setMethod(String s);
}
