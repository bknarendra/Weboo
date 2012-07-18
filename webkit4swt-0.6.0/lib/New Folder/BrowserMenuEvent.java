// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BrowserMenuEvent.java

package com.genuitec.blinki.webkit.swt;

import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Widget;

public final class BrowserMenuEvent extends TypedEvent
{

    BrowserMenuEvent(Widget w)
    {
        super(w);
    }

    public int x;
    public int y;
    public int typeFlags;
    public int editFlags;
    public String linkUrl;
    public String imageUrl;
    public String pageUrl;
    public String frameUrl;
    public String selectionText;
    public String misspelledWord;
    public boolean doit;
    private static final long serialVersionUID = 0x6c6e75cbcbde3536L;
}
