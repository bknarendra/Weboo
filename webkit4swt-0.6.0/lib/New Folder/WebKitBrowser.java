// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WebKitBrowser.java

package com.genuitec.blinki.webkit.swt;

import com.genuitec.cef.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.*;

// Referenced classes of package com.genuitec.blinki.webkit.swt:
//            CEFHandler, LibraryLoader, JSAlertHandler, BrowserMenuEventListener

public class WebKitBrowser extends Composite
{

    public WebKitBrowser(Composite parent, int style)
    {
        super(parent, style);
        openWindowListeners = new CopyOnWriteArrayList();
        closeWindowListeners = new CopyOnWriteArrayList();
        locationListners = new CopyOnWriteArrayList();
        progressListeners = new CopyOnWriteArrayList();
        statusTextListeners = new CopyOnWriteArrayList();
        titleListeners = new CopyOnWriteArrayList();
        visibilityWindowListeners = new CopyOnWriteArrayList();
        browserMenuEventListeners = new CopyOnWriteArrayList();
        focusOnStartEnd = false;
        popupParent = null;
        final Display display = getDisplay();
        cefHandler = createCEFHandler();
        cefHandler.setWebKitBrowser(this);
        WindowInfo wndInfo = WindowInfo.createAsChild(handle, 0, 0, 0, 0);
        cefBrowser = CEFBrowser.createBrowser(wndInfo, cefHandler, initialUrl);
        checkCefBrowserCreationResult();
        addListener(11, new Listener() {

            public void handleEvent(Event event)
            {
                resizeChromiumToFit();
            }

            final WebKitBrowser this$0;

            
            {
                this$0 = WebKitBrowser.this;
                super();
            }
        });
        addListener(15, new Listener() {

            public void handleEvent(Event event)
            {
                if(WebKitBrowser.this == display.getFocusControl())
                    cefBrowser.setInputFocus(true);
            }

            final Display val$display;
            final WebKitBrowser this$0;

            
            {
                this$0 = WebKitBrowser.this;
                display = display1;
                super();
            }
        });
        addListener(16, new Listener() {

            public void handleEvent(Event event1)
            {
            }

            final WebKitBrowser this$0;

            
            {
                this$0 = WebKitBrowser.this;
                super();
            }
        });
        addListener(31, new Listener() {

            public void handleEvent(Event event)
            {
                if(!focusOnStartEnd)
                    event.doit = false;
            }

            final WebKitBrowser this$0;

            
            {
                this$0 = WebKitBrowser.this;
                super();
            }
        });
        addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e)
            {
                if(popupParent != null)
                {
                    WindowEvent newEvent = cefHandler.createWindowEvent(cefHandler.getWebKitBrowser());
                    CloseWindowListener closeWindowListener;
                    for(Iterator i$ = closeWindowListeners.iterator(); i$.hasNext(); closeWindowListener.close(newEvent))
                        closeWindowListener = (CloseWindowListener)i$.next();

                }
                cefBrowser.dispose();
            }

            final WebKitBrowser this$0;

            
            {
                this$0 = WebKitBrowser.this;
                super();
            }
        });
        addListener(1, new Listener() {

            public void handleEvent(Event event1)
            {
            }

            final WebKitBrowser this$0;

            
            {
                this$0 = WebKitBrowser.this;
                super();
            }
        });
    }

    protected void initializeLibraries()
    {
    }

    protected void checkCefBrowserCreationResult()
    {
    }

    protected CEFHandler createCEFHandler()
    {
        return new CEFHandler();
    }

    protected CEFBrowser privGetCEFBrowsr()
    {
        return cefBrowser;
    }

    protected void privSetCEFBrowser(CEFBrowser cefBrowser)
    {
        this.cefBrowser = cefBrowser;
    }

    public String getUrl()
    {
        return cefBrowser.getUrl();
    }

    public boolean setUrl(String url)
    {
        cefBrowser.setUrl(url);
        return true;
    }

    public WebKitBrowser getPopupParent()
    {
        return popupParent;
    }

    public void setPopupParent(WebKitBrowser popupParent)
    {
        this.popupParent = popupParent;
    }

    public boolean isPopup()
    {
        return getPopupParent() != null;
    }

    public boolean execute(String script)
    {
        if(cefBrowser != null)
        {
            cefBrowser.execute(script, null);
            return true;
        } else
        {
            return false;
        }
    }

    public Object evaluate(String script)
    {
        Object res = null;
        if(cefBrowser != null)
        {
            Variant result = cefBrowser.execute(script, null);
            res = getJavaValueFromVariant(result);
        }
        return res;
    }

    protected Object getJavaValueFromVariant(Variant result)
    {
        Object res = null;
        switch(result.getType())
        {
        case 1: // '\001'
            res = Boolean.valueOf(result.getBool());
            break;

        case 2: // '\002'
            res = Integer.valueOf(result.getInt());
            break;

        case 3: // '\003'
            res = Double.valueOf(result.getDouble());
            break;

        case 4: // '\004'
            res = result.getString();
            break;
        }
        return res;
    }

    protected void setJavaValueToVariant(Object value, Variant result)
    {
        if(value instanceof Boolean)
            result.setBool(((Boolean)value).booleanValue());
        else
        if(value instanceof Integer)
            result.setInt(((Integer)value).intValue());
        else
        if(value instanceof Double)
            result.setDouble(((Double)value).doubleValue());
        else
        if(value instanceof String)
            result.setString((String)value);
    }

    public boolean forward()
    {
        if(!isForwardEnabled())
            return false;
        else
            return cefBrowser != null && cefBrowser.forward();
    }

    public boolean back()
    {
        if(!isBackEnabled())
            return false;
        else
            return cefBrowser != null && cefBrowser.back();
    }

    public boolean isBackEnabled()
    {
        return cefBrowser != null && cefBrowser.isBackEnabled();
    }

    public boolean isForwardEnabled()
    {
        return cefBrowser != null && cefBrowser.isForwardEnabled();
    }

    public void refresh()
    {
        if(cefBrowser != null)
            cefBrowser.refresh();
    }

    public void stop()
    {
        if(cefBrowser != null)
            cefBrowser.stop();
    }

    public String getText()
    {
        return cefBrowser != null ? cefBrowser.getText() : null;
    }

    public boolean setText(String html)
    {
        return cefBrowser != null && cefBrowser.setText(html);
    }

    public String getUserAgent()
    {
        return cefBrowser.getUserAgent();
    }

    public void setUserAgent(String agent)
    {
        cefBrowser.setUserAgent(agent);
    }

    void resizeChromiumToFit()
    {
        if(cefBrowser != null)
        {
            Rectangle clientArea = getClientArea();
            WindowInfo wndInfo = WindowInfo.createAsChild(0L, clientArea.x, clientArea.y, clientArea.width, clientArea.height);
            cefBrowser.changeWindowPosition(wndInfo);
        }
    }

    public boolean isFocusControl()
    {
        return cefBrowser.getHandle() == (long)OS.GetFocus();
    }

    boolean getFocusOnStartEnd()
    {
        return focusOnStartEnd;
    }

    void setFocusOnStartEnd(boolean val)
    {
        focusOnStartEnd = val;
    }

    protected List getOpenWindowListeners()
    {
        return openWindowListeners;
    }

    public void addOpenWindowListener(OpenWindowListener listener)
    {
        getOpenWindowListeners().add(listener);
    }

    public void removeOpenWindowListener(OpenWindowListener listener)
    {
        getOpenWindowListeners().remove(listener);
    }

    protected List getCloseWindowListeners()
    {
        return closeWindowListeners;
    }

    public void addCloseWindowListener(CloseWindowListener listener)
    {
        getCloseWindowListeners().add(listener);
    }

    public void removeCloseWindowListener(CloseWindowListener listener)
    {
        getCloseWindowListeners().remove(listener);
    }

    protected List getLocationListeners()
    {
        return locationListners;
    }

    public void addLocationListener(LocationListener listener)
    {
        getLocationListeners().add(listener);
    }

    public void removeLocationListener(LocationListener listener)
    {
        getLocationListeners().remove(listener);
    }

    protected List getProgressListners()
    {
        return progressListeners;
    }

    public void addProgressListener(ProgressListener listener)
    {
        getProgressListners().add(listener);
    }

    public void removeProgressListener(ProgressListener listener)
    {
        getProgressListners().remove(listener);
    }

    protected List getStatusTextListners()
    {
        return statusTextListeners;
    }

    public void addStatusTextListener(StatusTextListener listener)
    {
        getStatusTextListners().add(listener);
    }

    public void removeStatusTextListener(StatusTextListener listener)
    {
        getStatusTextListners().remove(listener);
    }

    protected List getTitleListeners()
    {
        return titleListeners;
    }

    public void addTitleListener(TitleListener listener)
    {
        getTitleListeners().add(listener);
    }

    public void removeTitleListener(TitleListener listener)
    {
        getTitleListeners().remove(listener);
    }

    protected List getVisibilityWindowListeners()
    {
        return visibilityWindowListeners;
    }

    public void addVisibilityWindowListener(VisibilityWindowListener listener)
    {
        getVisibilityWindowListeners().add(listener);
    }

    public void removeVisibilityWindowListener(VisibilityWindowListener listener)
    {
        getVisibilityWindowListeners().remove(listener);
    }

    protected List getBrowserMenuEventListeners()
    {
        return browserMenuEventListeners;
    }

    public void addBrowserMenuEventListener(BrowserMenuEventListener listener)
    {
        getBrowserMenuEventListeners().add(listener);
    }

    public void removeBrowserMenuEventListener(BrowserMenuEventListener listener)
    {
        getBrowserMenuEventListeners().remove(listener);
    }

    public static void setProfileFolder(File profileFolder)
    {
        String profilePath;
        try
        {
            profilePath = (new StringBuilder()).append(profileFolder.getCanonicalPath()).append(File.separator).toString();
        }
        catch(IOException e)
        {
            throw new RuntimeException("Invalid folder passed", e);
        }
        CEFBrowser.setCacheFolder((new StringBuilder()).append(profilePath).append("Cache").toString());
        CEFBrowser.setCookiesDbPath((new StringBuilder()).append(profilePath).append("Cookies.db").toString());
    }

    public static String getCookie(String name, String url)
    {
        if(name == null)
            SWT.error(4);
        if(url == null)
            SWT.error(4);
        return CEFBrowser.getCookie(name, url);
    }

    public static String[] getCookiesNames(String url)
    {
        if(url == null)
            SWT.error(4);
        return CEFBrowser.getCookiesNames(url);
    }

    public static boolean setCookie(String value, String url)
    {
        if(value == null)
            SWT.error(4);
        if(url == null)
            SWT.error(4);
        return CEFBrowser.setCookie(value, url);
    }

    public static void deleteCookie(String name, String url)
    {
        if(name == null)
            SWT.error(4);
        if(url == null)
            SWT.error(4);
        CEFBrowser.deleteCookie(name, url);
    }

    public void setJSAlertHandler(JSAlertHandler handler)
    {
        alertHandler = handler;
    }

    public JSAlertHandler getJSAlertHandler()
    {
        return alertHandler;
    }

    private List openWindowListeners;
    private List closeWindowListeners;
    private List locationListners;
    private List progressListeners;
    private List statusTextListeners;
    private List titleListeners;
    private List visibilityWindowListeners;
    private List browserMenuEventListeners;
    protected CEFBrowser cefBrowser;
    protected CEFHandler cefHandler;
    private JSAlertHandler alertHandler;
    private boolean focusOnStartEnd;
    private WebKitBrowser popupParent;
    private static String initialUrl = "";

    static 
    {
        LibraryLoader.loadLibraries();
    }



}
