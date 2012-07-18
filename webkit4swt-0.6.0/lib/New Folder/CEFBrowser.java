// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CEFBrowser.java

package com.genuitec.cef;


// Referenced classes of package com.genuitec.cef:
//            JNIObject, WindowInfo, IHandler, Variant

public class CEFBrowser extends JNIObject
{

    protected CEFBrowser()
    {
    }

    public static CEFBrowser createBrowser(WindowInfo info, IHandler handler, String url)
    {
        return createSync(info, handler, url);
    }

    public void setUserAgent(String userAgent)
    {
        doSetUserAgent(userAgent);
    }

    public String getUserAgent(String forUrl)
    {
        return doGetUserAgent(forUrl);
    }

    public String getUserAgent()
    {
        return doGetUserAgent("http://www.google.com");
    }

    public Variant execute(String script, String url)
    {
        return executeJavascript(script, url, TF_MAIN);
    }

    public boolean back()
    {
        if(canGoBack())
        {
            goBack();
            return true;
        } else
        {
            return false;
        }
    }

    public boolean forward()
    {
        if(canGoForward())
        {
            goForward();
            return true;
        } else
        {
            return false;
        }
    }

    public boolean isBackEnabled()
    {
        return canGoBack();
    }

    public boolean isForwardEnabled()
    {
        return canGoForward();
    }

    public String getText()
    {
        return getSource(TF_MAIN);
    }

    public String getUrl()
    {
        return getURL();
    }

    public void refresh()
    {
        reload();
    }

    public boolean setText(String html)
    {
        loadString(html, "");
        return true;
    }

    public boolean setUrl(String url)
    {
        loadURL(url, "_self");
        return true;
    }

    public void stop()
    {
        stopLoad();
    }

    public boolean isPopupBrowser()
    {
        return isPopup();
    }

    public void setInputFocus(boolean enable)
    {
        setFocus(enable);
    }

    public void changeWindowPosition(WindowInfo info)
    {
        resize(info);
    }

    public long getHandle()
    {
        return getWindowHandle();
    }

    public static void setCacheFolder(String cacheFolder)
    {
        doSetCacheFolder(cacheFolder);
    }

    public static void setCookiesDbPath(String canonicalPath)
    {
        doSetCookiesDbPath(canonicalPath);
    }

    public void dispose()
    {
        freeNativeResources();
    }

    public static void initialize()
    {
        doInitialize();
    }

    static native CEFBrowser createSync(WindowInfo windowinfo, IHandler ihandler, String s);

    private native boolean canGoBack();

    private native void goBack();

    private native boolean canGoForward();

    private native void goForward();

    private native void reload();

    private native void stopLoad();

    private native long getWindowHandle();

    private native boolean isPopup();

    private native String getURL();

    private native void loadURL(String s, String s1);

    private native void loadString(String s, String s1);

    private native void resize(WindowInfo windowinfo);

    private native Variant executeJavascript(String s, String s1, int i);

    private native String getSource(int i);

    private native String getText(int i);

    private native void setFocus(boolean flag);

    private native void freeNativeResources();

    private native void doSetUserAgent(String s);

    private native String doGetUserAgent(String s);

    private static native void doInitialize();

    private static native void doSetCacheFolder(String s);

    private static native void doSetCookiesDbPath(String s);

    public static String getCookie(String name, String url)
    {
        String allCookies = doGetCookies(url);
        String cookieArr[] = allCookies.split("; ");
        for(int i = 0; i < cookieArr.length; i++)
        {
            String string = cookieArr[i];
            if(string.startsWith((new StringBuilder()).append(name.toUpperCase()).append("=").toString()))
                return string.substring(name.length() + 1);
        }

        return null;
    }

    public static String[] getCookiesNames(String url)
    {
        String allCookies = doGetCookies(url);
        String cookieArr[] = allCookies.split("; ");
        int resLen = cookieArr.length;
        for(int i = 0; i < cookieArr.length; i++)
        {
            String string = cookieArr[i];
            int indexOf = string.indexOf('=');
            if(indexOf != -1)
            {
                cookieArr[i] = string.substring(0, indexOf);
            } else
            {
                resLen--;
                cookieArr[i] = null;
            }
        }

        String resArray[] = new String[resLen];
        int j = 0;
        for(int i = 0; i < cookieArr.length; i++)
            if(cookieArr[i] != null)
            {
                resArray[j] = cookieArr[i];
                j++;
            }

        return resArray;
    }

    public static void deleteCookie(String name, String url)
    {
        doSetCookie(url, (new StringBuilder()).append(name).append("=; expires=Thu, 01-Jan-1970 00:00:01 GMT").toString());
    }

    public static boolean setCookie(String value, String url)
    {
        return doSetCookie(url, value);
    }

    private static native String doGetCookies(String s);

    private static native boolean doSetCookie(String s, String s1);

    public static int TF_FOCUSED = 0;
    public static int TF_MAIN = 1;

}
