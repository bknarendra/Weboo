// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JNIObject.java

package com.genuitec.cef;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.*;

public class JNIObject
{
    private static final class RunnableImplementation
        implements Runnable
    {

        public void run()
        {
            do
                try
                {
                    InternalPhantomReference ref;
                    do
                        ref = (InternalPhantomReference)JNIObject.holderQueue.remove();
                    while(ref == null);
                    ref.clear();
                }
                catch(InterruptedException e) { }
            while(true);
        }

        private RunnableImplementation()
        {
        }

    }

    private static final class InternalPhantomReference extends PhantomReference
    {

        public void destroy()
        {
            JNIObject.allResources.remove(this);
            if(nativePtr != 0L)
            {
                JNIObject.nativeReleasePtr(nativePtr, nativeClassPtr);
                nativePtr = 0L;
                nativeClassPtr = 0L;
            }
        }

        private long nativeClassPtr;
        private long nativePtr;

        public InternalPhantomReference(JNIObject obj, long nativePtr, long nativeClassPtr)
        {
            super(obj, JNIObject.holderQueue);
            this.nativePtr = nativePtr;
            this.nativeClassPtr = nativeClassPtr;
            JNIObject.allResources.add(this);
        }
    }


    public JNIObject()
    {
    }

    private void setNativeClassPtr(long nativeClassPtr)
    {
        nativeResource = new InternalPhantomReference(this, nativePtr, nativeClassPtr);
    }

    public void destroy()
    {
        nativePtr = 0L;
        if(nativeResource != null)
        {
            nativeResource.destroy();
            nativeResource = null;
        }
    }

    public boolean isAlive()
    {
        return nativePtr != 0L;
    }

    private static native void nativeReleasePtr(long l, long l1);

    public static void destroy(JNIObject arr[])
    {
        JNIObject arr$[] = arr;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            JNIObject object = arr$[i$];
            if(object != null)
                object.destroy();
        }

    }

    public static void destroy(JNIObject object)
    {
        if(object != null)
            object.destroy();
    }

    private static ReferenceQueue holderQueue = new ReferenceQueue();
    private static final Set allResources = Collections.synchronizedSet(new HashSet(5000));
    private long nativePtr;
    private InternalPhantomReference nativeResource;

    static 
    {
        Runnable runnable = new RunnableImplementation();
        Thread thread = new Thread(runnable, "Native resource collecting loop");
        thread.setDaemon(true);
        thread.start();
    }



}
