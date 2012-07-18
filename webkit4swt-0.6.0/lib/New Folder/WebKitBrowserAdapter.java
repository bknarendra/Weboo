// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WebKitBrowserAdapter.java

package com.genuitec.blinki.webkit.swt;

import org.eclipse.swt.browser.*;

// Referenced classes of package com.genuitec.blinki.webkit.swt:
//            WebKitBrowser

public class WebKitBrowserAdapter
    implements OpenWindowListener, CloseWindowListener, LocationListener, ProgressListener, StatusTextListener, TitleListener, VisibilityWindowListener
{

    public WebKitBrowserAdapter(WebKitBrowser browser)
    {
        this.browser = browser;
        installListeners();
    }

    protected void installListeners()
    {
        getBrowser().addOpenWindowListener(this);
        getBrowser().addCloseWindowListener(this);
        getBrowser().addLocationListener(this);
        getBrowser().addProgressListener(this);
        getBrowser().addStatusTextListener(this);
        getBrowser().addTitleListener(this);
        getBrowser().addVisibilityWindowListener(this);
    }

    public void removeListeners()
    {
        getBrowser().removeOpenWindowListener(this);
        getBrowser().removeCloseWindowListener(this);
        getBrowser().removeLocationListener(this);
        getBrowser().removeProgressListener(this);
        getBrowser().removeStatusTextListener(this);
        getBrowser().removeTitleListener(this);
        getBrowser().removeVisibilityWindowListener(this);
    }

    public WebKitBrowser getBrowser()
    {
        return browser;
    }

    public void open(WindowEvent windowevent)
    {
    }

    public void close(WindowEvent windowevent)
    {
    }

    public void hide(WindowEvent windowevent)
    {
    }

    public void show(WindowEvent windowevent)
    {
    }

    public void changing(LocationEvent locationevent)
    {
    }

    public void changed(LocationEvent locationevent)
    {
    }

    public void changed(ProgressEvent progressevent)
    {
    }

    public void completed(ProgressEvent progressevent)
    {
    }

    public void changed(StatusTextEvent statustextevent)
    {
    }

    public void changed(TitleEvent titleevent)
    {
    }

    private WebKitBrowser browser;
}
