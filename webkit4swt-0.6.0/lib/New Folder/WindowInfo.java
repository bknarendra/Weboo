// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WindowInfo.java

package com.genuitec.cef;


public class WindowInfo
{

    private WindowInfo()
    {
    }

    public static WindowInfo createAsChild(long parentHandle, int x, int y, int width, int height)
    {
        WindowInfo windowInfo = new WindowInfo();
        windowInfo.parentHandle = parentHandle;
        windowInfo.x = x;
        windowInfo.y = y;
        windowInfo.width = width;
        windowInfo.height = height;
        return windowInfo;
    }

    public static WindowInfo createAsPopup(long parentHandle)
    {
        WindowInfo windowInfo = new WindowInfo();
        windowInfo.parentHandle = parentHandle;
        return windowInfo;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public long getWndHandle()
    {
        return wndHandle;
    }

    public void setWndHandle(long wndHandle)
    {
        this.wndHandle = wndHandle;
    }

    long parentHandle;
    long wndHandle;
    int x;
    int y;
    int width;
    int height;
}
