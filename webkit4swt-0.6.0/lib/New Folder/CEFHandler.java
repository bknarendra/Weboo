// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CEFHandler.java

package com.genuitec.blinki.webkit.swt;

import com.genuitec.cef.*;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

// Referenced classes of package com.genuitec.blinki.webkit.swt:
//            WebkitWindowEvent, BrowserMenuEvent, BrowserMenuEventListener, BrowserMenuItemNameEvent, 
//            BrowserMenuItemActionEvent, WebKitBrowser, Util, JSAlertHandler

public class CEFHandler
    implements IHandler
{

    protected CEFHandler()
    {
        loadingProgress = 0;
    }

    public CEFBrowser handleBeforeCreated(CEFBrowser parentBrowser, WindowInfo wndInfo, boolean isPopup, String url)
    {
        CEFBrowser result = null;
        if(isPopup)
        {
            WindowEvent newEvent = createWindowEvent(webKitBrowser);
            if(wndInfo.getX() != 0x80000000 && wndInfo.getY() != 0x80000000)
                newEvent.location = new Point(wndInfo.getX(), wndInfo.getY());
            if(wndInfo.getWidth() != 0x80000000 && wndInfo.getHeight() != 0x80000000)
                newEvent.size = new Point(wndInfo.getWidth(), wndInfo.getHeight());
            newEvent.required = false;
            for(Iterator i$ = webKitBrowser.getOpenWindowListeners().iterator(); i$.hasNext();)
            {
                OpenWindowListener openWindowListener = (OpenWindowListener)i$.next();
                try
                {
                    openWindowListener.open(newEvent);
                }
                catch(Exception e)
                {
                    Util.logError(e);
                }
            }

            if(newEvent instanceof WebkitWindowEvent)
            {
                WebkitWindowEvent webkitEvent = (WebkitWindowEvent)newEvent;
                WebKitBrowser userWebKitBrowser = webkitEvent.webkitBrowser;
                userWebKitBrowser.setPopupParent(getWebKitBrowser());
                result = userWebKitBrowser.privGetCEFBrowsr();
            } else
            if(newEvent.required)
                result = parentBrowser;
        }
        return result;
    }

    public WebKitBrowser getWebKitBrowser()
    {
        return webKitBrowser;
    }

    public void setWebKitBrowser(WebKitBrowser webKitBrowser)
    {
        this.webKitBrowser = webKitBrowser;
    }

    public int handleAfterCreated(CEFBrowser browser)
    {
        if(!browser.isPopupBrowser())
        {
            getWebKitBrowser().privSetCEFBrowser(browser);
            webKitBrowser.resizeChromiumToFit();
        }
        return 1;
    }

    public int handleBeforeBrowse(CEFBrowser browser, Request request, int navType, boolean isRedirect)
    {
        loadingProgress = 0;
        LocationEvent newEvent = createLocationEvent(browser);
        newEvent.location = request.getURL();
        LocationListener locationListener;
        for(Iterator i$ = webKitBrowser.getLocationListeners().iterator(); i$.hasNext(); locationListener.changing(newEvent))
            locationListener = (LocationListener)i$.next();

        return newEvent.doit ? 1 : 0;
    }

    public int handleLoadStart(CEFBrowser browser)
    {
        if(loadingProgress <= 0)
            loadingProgress = 1;
        else
            loadingProgress++;
        if(loadingProgress > 1)
        {
            ProgressEvent progressEvent = new ProgressEvent(webKitBrowser);
            progressEvent.display = getWebKitBrowser().getDisplay();
            progressEvent.widget = getWebKitBrowser();
            progressEvent.current = 100;
            progressEvent.total = 100;
            for(Iterator i$ = getWebKitBrowser().getProgressListners().iterator(); i$.hasNext();)
            {
                ProgressListener listener = (ProgressListener)i$.next();
                try
                {
                    listener.changed(progressEvent);
                }
                catch(Exception e)
                {
                    Util.logError(e);
                }
            }

        }
        return 0;
    }

    public int handleLoadEnd(CEFBrowser browser)
    {
        if(loadingProgress <= 0)
            loadingProgress = 0;
        else
            loadingProgress--;
        if(loadingProgress == 0)
        {
            ProgressEvent progressEvent = new ProgressEvent(webKitBrowser);
            progressEvent.display = getWebKitBrowser().getDisplay();
            progressEvent.widget = getWebKitBrowser();
            progressEvent.current = 100;
            progressEvent.total = 100;
            for(Iterator i$ = getWebKitBrowser().getProgressListners().iterator(); i$.hasNext();)
            {
                ProgressListener listener = (ProgressListener)i$.next();
                try
                {
                    listener.completed(progressEvent);
                }
                catch(Exception e)
                {
                    Util.logError(e);
                }
            }

            LocationEvent newEvent = createLocationEvent(browser);
            newEvent.location = browser.getUrl();
            for(Iterator i$ = getWebKitBrowser().getLocationListeners().iterator(); i$.hasNext();)
            {
                LocationListener locationListener = (LocationListener)i$.next();
                try
                {
                    locationListener.changed(newEvent);
                }
                catch(Exception e)
                {
                    Util.logError(e);
                }
            }

        }
        return 0;
    }

    public int handleLoadError(CEFBrowser browser, int errorCode, String failedURL, StringBuilder stringbuilder)
    {
        return 1;
    }

    public int handleAddressChange(CEFBrowser browser, String url)
    {
        return 0;
    }

    public int handleTitleChange(CEFBrowser CEFBrowser, String title)
    {
        TitleEvent newEvent = new TitleEvent(getWebKitBrowser());
        newEvent.display = getWebKitBrowser().getDisplay();
        newEvent.widget = getWebKitBrowser();
        newEvent.title = title;
        for(Iterator i$ = getWebKitBrowser().getTitleListeners().iterator(); i$.hasNext();)
        {
            TitleListener titleListener = (TitleListener)i$.next();
            try
            {
                titleListener.changed(newEvent);
            }
            catch(Exception e)
            {
                Util.logError(e);
            }
        }

        return 0;
    }

    public LoadResourceAction handleBeforeResourceLoad(CEFBrowser browser, Request request, int loadFlags)
    {
        getWebKitBrowser().getDisplay().asyncExec(new Runnable() {

            public void run()
            {
                if(!getWebKitBrowser().isDisposed() && loadingProgress > 0)
                {
                    ProgressEvent progressEvent = new ProgressEvent(getWebKitBrowser());
                    progressEvent.display = getWebKitBrowser().getDisplay();
                    progressEvent.widget = getWebKitBrowser();
                    progressEvent.current = 0;
                    progressEvent.total = 100;
                    for(Iterator i$ = getWebKitBrowser().getProgressListners().iterator(); i$.hasNext();)
                    {
                        ProgressListener progressListener = (ProgressListener)i$.next();
                        try
                        {
                            progressListener.changed(progressEvent);
                        }
                        catch(Exception e)
                        {
                            Util.logError(e);
                        }
                    }

                }
            }

            final CEFHandler this$0;

            
            {
                this$0 = CEFHandler.this;
                super();
            }
        });
        return null;
    }

    public int handleBeforeWindowClosed(CEFBrowser browser)
    {
        if(webKitBrowser.getPopupParent() != null && !getWebKitBrowser().isDisposed())
        {
            WindowEvent newEvent = createWindowEvent(webKitBrowser.getPopupParent());
            for(Iterator i$ = webKitBrowser.getPopupParent().getCloseWindowListeners().iterator(); i$.hasNext();)
            {
                CloseWindowListener closeWindowListener = (CloseWindowListener)i$.next();
                try
                {
                    closeWindowListener.close(newEvent);
                }
                catch(Exception e)
                {
                    Util.logError(e);
                }
            }

        }
        return 0;
    }

    public int handlePopupShow(CEFBrowser CEFBrowser)
    {
        if(getWebKitBrowser().getPopupParent() != null && !getWebKitBrowser().isDisposed())
        {
            WindowEvent newEvent = createWindowEvent(getWebKitBrowser());
            for(Iterator i$ = getWebKitBrowser().getVisibilityWindowListeners().iterator(); i$.hasNext();)
            {
                VisibilityWindowListener visibilityWindowListener = (VisibilityWindowListener)i$.next();
                try
                {
                    visibilityWindowListener.show(newEvent);
                }
                catch(Exception e)
                {
                    Util.logError(e);
                }
            }

        }
        return 0;
    }

    public int handleTakeFocus(CEFBrowser browser, boolean reverse)
    {
        getWebKitBrowser().setFocusOnStartEnd(true);
        getWebKitBrowser().traverse(reverse ? 8 : 16);
        getWebKitBrowser().setFocusOnStartEnd(false);
        return 0;
    }

    public void onJavaExceptionThrown(Throwable ex)
    {
        ex.printStackTrace();
    }

    LocationEvent createLocationEvent(CEFBrowser CEFBrowser)
    {
        LocationEvent newEvent = new LocationEvent(getWebKitBrowser());
        newEvent.display = getWebKitBrowser().getDisplay();
        newEvent.widget = getWebKitBrowser();
        newEvent.location = CEFBrowser.getUrl();
        newEvent.doit = true;
        return newEvent;
    }

    WindowEvent createWindowEvent(WebKitBrowser webKit)
    {
        WebkitWindowEvent newEvent = new WebkitWindowEvent(webKit);
        newEvent.display = webKit.getDisplay();
        newEvent.widget = webKit;
        return newEvent;
    }

    public int handleBeforeMenu(CEFBrowser browser, MenuInfo menuInfo)
    {
        BrowserMenuEvent browserMenuEvent = new BrowserMenuEvent(getWebKitBrowser());
        browserMenuEvent.x = menuInfo.getX();
        browserMenuEvent.y = menuInfo.getY();
        browserMenuEvent.typeFlags = menuInfo.getTypeFlags();
        browserMenuEvent.editFlags = menuInfo.getEditFlags();
        browserMenuEvent.linkUrl = menuInfo.getLinkUrl();
        browserMenuEvent.imageUrl = menuInfo.getImageUrl();
        browserMenuEvent.pageUrl = menuInfo.getPageUrl();
        browserMenuEvent.frameUrl = menuInfo.getFrameUrl();
        browserMenuEvent.selectionText = menuInfo.getSelectionText();
        browserMenuEvent.misspelledWord = menuInfo.getMisspelledWord();
        browserMenuEvent.doit = true;
        for(Iterator i$ = getWebKitBrowser().getBrowserMenuEventListeners().iterator(); i$.hasNext();)
        {
            BrowserMenuEventListener listener = (BrowserMenuEventListener)i$.next();
            try
            {
                listener.beforeMenuShow(browserMenuEvent);
            }
            catch(Exception e)
            {
                Util.logError(e);
            }
        }

        return browserMenuEvent.doit ? 1 : 0;
    }

    public int handleJSAlert(CEFBrowser browser, String message)
    {
        JSAlertHandler jsAlertHandler = getWebKitBrowser().getJSAlertHandler();
        if(jsAlertHandler == null)
            break MISSING_BLOCK_LABEL_25;
        jsAlertHandler.alert(message);
        return 0;
        Throwable ex;
        ex;
        return 1;
    }

    public void handleGetMenuLabel(CEFBrowser browser, int menuId, StringBuilder textLabel)
    {
        BrowserMenuItemNameEvent menuItemNameEvent = new BrowserMenuItemNameEvent(getWebKitBrowser());
        menuItemNameEvent.action = menuId;
        menuItemNameEvent.actionString = textLabel;
        for(Iterator i$ = getWebKitBrowser().getBrowserMenuEventListeners().iterator(); i$.hasNext();)
        {
            BrowserMenuEventListener listener = (BrowserMenuEventListener)i$.next();
            try
            {
                listener.menuItemName(menuItemNameEvent);
            }
            catch(Exception e)
            {
                Util.logError(e);
            }
        }

    }

    public int handleMenuAction(CEFBrowser browser, int menuId)
    {
        BrowserMenuItemActionEvent menuItemNameEvent = new BrowserMenuItemActionEvent(getWebKitBrowser());
        menuItemNameEvent.action = menuId;
        menuItemNameEvent.doit = true;
        for(Iterator i$ = getWebKitBrowser().getBrowserMenuEventListeners().iterator(); i$.hasNext();)
        {
            BrowserMenuEventListener listener = (BrowserMenuEventListener)i$.next();
            try
            {
                listener.menuItemAction(menuItemNameEvent);
            }
            catch(Exception e)
            {
                Util.logError(e);
            }
        }

        return menuItemNameEvent.doit ? 1 : 0;
    }

    private int loadingProgress;
    private WebKitBrowser webKitBrowser;
    private static final int TOTAL_PROGRESS_VALUE = 100;

}
