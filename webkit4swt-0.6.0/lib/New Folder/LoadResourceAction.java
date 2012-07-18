// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LoadResourceAction.java

package com.genuitec.cef;

import java.io.InputStream;

public final class LoadResourceAction
{

    public String getRedirectURL()
    {
        return redirectURL;
    }

    public InputStream getReader()
    {
        return reader;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public LoadResourceAction(String redirectURL)
    {
        this.redirectURL = redirectURL;
    }

    public LoadResourceAction(InputStream reader, String mimeType)
    {
        this.reader = reader;
        this.mimeType = mimeType;
    }

    public static LoadResourceAction redirectTo(String redirectURL)
    {
        return new LoadResourceAction(redirectURL);
    }

    public static LoadResourceAction loadFrom(InputStream reader, String mimeType)
    {
        return new LoadResourceAction(reader, mimeType);
    }

    String redirectURL;
    InputStream reader;
    String mimeType;
}
