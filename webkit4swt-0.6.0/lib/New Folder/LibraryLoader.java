// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LibraryLoader.java

package com.genuitec.blinki.webkit.swt;

import com.genuitec.cef.CEFBrowser;
import org.eclipse.swt.SWTException;

// Referenced classes of package com.genuitec.blinki.webkit.swt:
//            Util

public class LibraryLoader
{

    public LibraryLoader()
    {
    }

    public static void setNativeLibraries(String libraries[])
    {
        nativeLibraries = libraries;
    }

    public static void loadLibraries()
    {
        try
        {
            String arr$[] = nativeLibraries;
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                String libname = arr$[i$];
                System.loadLibrary(libname);
            }

            CEFBrowser.initialize();
        }
        catch(Throwable t)
        {
            try
            {
                String arr$[] = nativeLibraries;
                int len$ = arr$.length;
                for(int i$ = 0; i$ < len$; i$++)
                {
                    String libname = arr$[i$];
                    Util.loadLibrary(libname, false);
                }

                CEFBrowser.initialize();
            }
            catch(Throwable t1)
            {
                SWTException ex = new SWTException(47, "Unable to load webkit native libraries.");
                ex.throwable = t1;
                throw ex;
            }
        }
    }

    static String nativeLibraries[] = {
        "icudt38", "webkit4swt-0.6.0"
    };

}
