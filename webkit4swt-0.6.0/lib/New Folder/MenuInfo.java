// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MenuInfo.java

package com.genuitec.cef;


// Referenced classes of package com.genuitec.cef:
//            JNIObject

public class MenuInfo extends JNIObject
{

    public MenuInfo()
    {
    }

    public native int getTypeFlags();

    public native int getX();

    public native int getY();

    public native String getLinkUrl();

    public native String getImageUrl();

    public native String getPageUrl();

    public native String getFrameUrl();

    public native String getSelectionText();

    public native String getMisspelledWord();

    public native int getEditFlags();

    public native String getSecurityInfo();
}
