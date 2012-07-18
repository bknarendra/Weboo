// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Util.java

package com.genuitec.blinki.webkit.swt;

import java.io.*;
import java.lang.reflect.Method;

public class Util
{

    public Util()
    {
    }

    public static void logError(Exception ex)
    {
        String pluginClassName = "com.genuitec.blinki.webkit.swt.WebkitBrowserPlugin";
        try
        {
            Class pluginClass = Class.forName(pluginClassName);
            Method logErrorMethod = pluginClass.getMethod("logError", new Class[] {
                java/lang/Exception
            });
            logErrorMethod.invoke(null, new Object[] {
                ex
            });
        }
        catch(Exception e)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    static boolean extract(String fileName, String mappedName)
    {
        FileOutputStream os;
        InputStream is;
        File file;
        os = null;
        is = null;
        file = new File(fileName);
        if(file.exists())
            break MISSING_BLOCK_LABEL_184;
        is = com/genuitec/blinki/webkit/swt/Util.getResourceAsStream((new StringBuilder()).append("/").append(mappedName).toString());
        if(is == null)
            break MISSING_BLOCK_LABEL_184;
        byte buffer[] = new byte[4096];
        os = new FileOutputStream(fileName);
        int read;
        while((read = is.read(buffer)) != -1) 
            os.write(buffer, 0, read);
        os.close();
        is.close();
        if(!"win32".equals("win32"))
            try
            {
                Runtime.getRuntime().exec(new String[] {
                    "chmod", "755", fileName
                }).waitFor();
            }
            catch(Throwable e) { }
        if(load(fileName))
            return true;
        break MISSING_BLOCK_LABEL_184;
        Throwable e;
        e;
        try
        {
            if(os != null)
                os.close();
        }
        catch(IOException e1) { }
        try
        {
            if(is != null)
                is.close();
        }
        catch(IOException e1) { }
        if(file.exists())
            file.delete();
        return false;
    }

    static boolean load(String libName)
    {
        if(libName.indexOf(SEPARATOR) != -1)
            System.load(libName);
        else
            System.loadLibrary(libName);
        return true;
        UnsatisfiedLinkError e;
        e;
        e.printStackTrace();
        return false;
    }

    public static void loadLibrary(String name, boolean shouldMapName)
    {
        String libName = name;
        String mappedName = System.mapLibraryName(libName);
        String path = System.getProperty("swt.library.path");
        if(path != null)
        {
            path = (new File(path)).getAbsolutePath();
            if(load((new StringBuilder()).append(path).append(SEPARATOR).append(libName).toString()))
                return;
            if(shouldMapName && load((new StringBuilder()).append(path).append(SEPARATOR).append(mappedName).toString()))
                return;
        }
        if(load(libName))
            return;
        if(shouldMapName && load(libName))
            return;
        if(path == null)
        {
            path = System.getProperty("java.io.tmpdir");
            path = (new File(path)).getAbsolutePath();
            if(load((new StringBuilder()).append(path).append(SEPARATOR).append(mappedName).toString()))
                return;
        }
        if(path != null && extract((new StringBuilder()).append(path).append(SEPARATOR).append(mappedName).toString(), mappedName))
            return;
        else
            throw new UnsatisfiedLinkError((new StringBuilder()).append("no ").append(libName).append(" in swt.library.path, java.library.path or the jar file").toString());
    }

    static final String SEPARATOR = System.getProperty("file.separator");

}
