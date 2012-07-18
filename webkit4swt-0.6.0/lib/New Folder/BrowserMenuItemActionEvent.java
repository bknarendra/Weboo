// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BrowserMenuItemActionEvent.java

package com.genuitec.blinki.webkit.swt;

import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Widget;

public class BrowserMenuItemActionEvent extends TypedEvent
{

    BrowserMenuItemActionEvent(Widget w)
    {
        super(w);
    }

    public int action;
    public static int ID_NAV_BACK = 10;
    public static int ID_NAV_FORWARD = 11;
    public static int ID_NAV_RELOAD = 12;
    public static int ID_NAV_STOP = 13;
    public static int ID_UNDO = 20;
    public static int ID_REDO = 21;
    public static int ID_CUT = 22;
    public static int ID_COPY = 23;
    public static int ID_PASTE = 24;
    public static int ID_DELETE = 25;
    public static int ID_SELECTALL = 26;
    public static int ID_PRINT = 30;
    public static int ID_VIEWSOURCE = 31;
    public static int ID_INSPECTELEMENT = 32;
    public boolean doit;
    private static final long serialVersionUID = 0x6870514e80ea7587L;

}
