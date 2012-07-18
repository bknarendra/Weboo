// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IHandler.java

package com.genuitec.cef;


// Referenced classes of package com.genuitec.cef:
//            CEFBrowser, WindowInfo, Request, LoadResourceAction, 
//            MenuInfo

public interface IHandler
{

    public abstract CEFBrowser handleBeforeCreated(CEFBrowser cefbrowser, WindowInfo windowinfo, boolean flag, String s);

    public abstract int handleAfterCreated(CEFBrowser cefbrowser);

    public abstract int handleBeforeBrowse(CEFBrowser cefbrowser, Request request, int i, boolean flag);

    public abstract int handleLoadStart(CEFBrowser cefbrowser);

    public abstract int handleLoadEnd(CEFBrowser cefbrowser);

    public abstract int handleBeforeWindowClosed(CEFBrowser cefbrowser);

    public abstract int handlePopupShow(CEFBrowser cefbrowser);

    public abstract int handleTakeFocus(CEFBrowser cefbrowser, boolean flag);

    public abstract int handleTitleChange(CEFBrowser cefbrowser, String s);

    public abstract LoadResourceAction handleBeforeResourceLoad(CEFBrowser cefbrowser, Request request, int i);

    public abstract int handleBeforeMenu(CEFBrowser cefbrowser, MenuInfo menuinfo);

    public abstract int handleJSAlert(CEFBrowser cefbrowser, String s);

    public static final int RV_HANDLED = 0;
    public static final int RV_CONTINUE = 1;
    public static final int NAVTYPE_LINKCLICKED = 0;
    public static final int NAVTYPE_FORMSUBMITTED = 1;
    public static final int NAVTYPE_BACKFORWARD = 2;
    public static final int NAVTYPE_RELOAD = 3;
    public static final int NAVTYPE_FORMRESUBMITTED = 4;
    public static final int NAVTYPE_OTHER = 5;
    public static final int ERR_FAILED = -2;
    public static final int ERR_ABORTED = -3;
    public static final int ERR_INVALID_ARGUMENT = -4;
    public static final int ERR_INVALID_HANDLE = -5;
    public static final int ERR_FILE_NOT_FOUND = -6;
    public static final int ERR_TIMED_OUT = -7;
    public static final int ERR_FILE_TOO_BIG = -8;
    public static final int ERR_UNEXPECTED = -9;
    public static final int ERR_ACCESS_DENIED = -10;
    public static final int ERR_NOT_IMPLEMENTED = -11;
    public static final int ERR_CONNECTION_CLOSED = -100;
    public static final int ERR_CONNECTION_RESET = -101;
    public static final int ERR_CONNECTION_REFUSED = -102;
    public static final int ERR_CONNECTION_ABORTED = -103;
    public static final int ERR_CONNECTION_FAILED = -104;
    public static final int ERR_NAME_NOT_RESOLVED = -105;
    public static final int ERR_INTERNET_DISCONNECTED = -106;
    public static final int ERR_SSL_PROTOCOL_ERROR = -107;
    public static final int ERR_ADDRESS_INVALID = -108;
    public static final int ERR_ADDRESS_UNREACHABLE = -109;
    public static final int ERR_SSL_CLIENT_AUTH_CERT_NEEDED = -110;
    public static final int ERR_TUNNEL_CONNECTION_FAILED = -111;
    public static final int ERR_NO_SSL_VERSIONS_ENABLED = -112;
    public static final int ERR_SSL_VERSION_OR_CIPHER_MISMATCH = -113;
    public static final int ERR_SSL_RENEGOTIATION_REQUESTED = -114;
    public static final int ERR_CERT_COMMON_NAME_INVALID = -200;
    public static final int ERR_CERT_DATE_INVALID = -201;
    public static final int ERR_CERT_AUTHORITY_INVALID = -202;
    public static final int ERR_CERT_CONTAINS_ERRORS = -203;
    public static final int ERR_CERT_NO_REVOCATION_MECHANISM = -204;
    public static final int ERR_CERT_UNABLE_TO_CHECK_REVOCATION = -205;
    public static final int ERR_CERT_REVOKED = -206;
    public static final int ERR_CERT_INVALID = -207;
    public static final int ERR_CERT_END = -208;
    public static final int ERR_INVALID_URL = -300;
    public static final int ERR_DISALLOWED_URL_SCHEME = -301;
    public static final int ERR_UNKNOWN_URL_SCHEME = -302;
    public static final int ERR_TOO_MANY_REDIRECTS = -310;
    public static final int ERR_UNSAFE_REDIRECT = -311;
    public static final int ERR_UNSAFE_PORT = -312;
    public static final int ERR_INVALID_RESPONSE = -320;
    public static final int ERR_INVALID_CHUNKED_ENCODING = -321;
    public static final int ERR_METHOD_NOT_SUPPORTED = -322;
    public static final int ERR_UNEXPECTED_PROXY_AUTH = -323;
    public static final int ERR_EMPTY_RESPONSE = -324;
    public static final int ERR_RESPONSE_HEADERS_TOO_BIG = -325;
    public static final int ERR_CACHE_MISS = -400;
    public static final int ERR_INSECURE_RESPONSE = -501;
    public static final int LOAD_NORMAL = 0;
    public static final int LOAD_VALIDATE_CACHE = 1;
    public static final int LOAD_BYPASS_CACHE = 2;
    public static final int LOAD_PREFERRING_CACHE = 4;
    public static final int LOAD_ONLY_FROM_CACHE = 8;
    public static final int LOAD_DISABLE_CACHE = 16;
    public static final int LOAD_DISABLE_INTERCEPT = 32;
    public static final int LOAD_ENABLE_UPLOAD_PROGRESS = 64;
    public static final int LOAD_IGNORE_CERT_COMMON_NAME_INVALID = 256;
    public static final int LOAD_IGNORE_CERT_DATE_INVALID = 512;
    public static final int LOAD_IGNORE_CERT_AUTHORITY_INVALID = 1024;
    public static final int LOAD_IGNORE_CERT_REVOCATION = 2048;
    public static final int LOAD_IGNORE_CERT_WRONG_USAGE = 4096;
    public static final int LOAD_DO_NOT_SAVE_COOKIES = 8192;
    public static final int LOAD_SDCH_DICTIONARY_ADVERTISED = 16384;
    public static final int LOAD_BYPASS_PROXY = 32768;
    public static final int ID_NAV_BACK = 10;
    public static final int ID_NAV_FORWARD = 11;
    public static final int ID_NAV_RELOAD = 12;
    public static final int ID_NAV_STOP = 13;
    public static final int ID_UNDO = 20;
    public static final int ID_REDO = 21;
    public static final int ID_CUT = 22;
    public static final int ID_COPY = 23;
    public static final int ID_PASTE = 24;
    public static final int ID_DELETE = 25;
    public static final int ID_SELECTALL = 26;
    public static final int ID_PRINT = 30;
    public static final int ID_VIEWSOURCE = 31;
    public static final int ID_INSPECTELEMENT = 32;
}
