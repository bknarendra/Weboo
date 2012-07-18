package a;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Freebase extends JSONTransport {

    private static final String FREEBASE_API_URL         = "http://api.freebase.com";
    private static final String FREEBASE_SANDBOX_API_URL = "http://www.sandbox-freebase.com";

    private static final String LOGIN_API     = "/api/account/login";
    private static final String MQLREAD_API   = "/api/service/mqlread";
    private static final String SEARCH_API    = "/api/service/search";
    private static final String GEOSEARCH_API = "/api/service/geosearch";
    private static final String BLOB_API_PREFIX  = "/api/trans/";
    public static Freebase getFreebase() {
        return new Freebase(FREEBASE_API_URL);
    }
    
    public static Freebase getFreebaseSandbox() {
        return new Freebase(FREEBASE_SANDBOX_API_URL);
    }
    
    private final String host;
    private String credential_cookie = null;
    
    private Freebase(String host) {
        this.host = host;
    }
    protected String getURL(String path) {
        return this.host + path;
    }
    
    public JSON mqlread(JSON query) {
        return mqlread(query, null, null);
    }

    public JSON mqlread(JSON query, JSON envelope, JSON params) {
        if (query == null) throw new FreebaseException("Query can't be null");
        if (envelope == null) envelope = JSON.o();
        envelope.put("query", jsonize(query));
        envelope.put("escape",false);

        List<NameValuePair> qparams = transform_params(params);
        qparams.add(new BasicNameValuePair("query",envelope.toString()));

        return invoke(MQLREAD_API,qparams);
    }

    public JSON mqlread_multiple(JSON queries) {
        return mqlread_multiple(queries, null, null);
    }
    
    @SuppressWarnings("unchecked")
    public JSON mqlread_multiple(JSON queries, JSON envelopes, JSON params) {
        if (queries == null || queries.object().keySet().size() == 0) throw new FreebaseException("Query can't be null or empty");
        if (envelopes == null) envelopes = JSON.o();
        
        JSON q = JSON.o();
        
        for (Object entry : queries.object().entrySet()) {
            Map.Entry<String,JSON> e = (Map.Entry<String,JSON>) entry;
            JSON envelope = envelopes.get(e.getKey());
            if (envelope == null) envelope = JSON.o();
            envelope.put("query", jsonize(e.getValue()));
            envelope.put("escape",false);
            q.put(e.getKey(), envelope);
        }

        List<NameValuePair> qparams = transform_params(params);
        qparams.add(new BasicNameValuePair("queries",q.toString()));

        return invoke(MQLREAD_API,qparams);
    }
    
    public JSON search(String query) {
        return search(query, null);
    }

    public JSON search(String query, JSON options) {
        if (query == null || query.trim().length() == 0) throw new FreebaseException("You must provide a string to search");
        
        List<NameValuePair> qparams = transform_params(options);
        qparams.add(new BasicNameValuePair("query",query));
        qparams.add(new BasicNameValuePair("format","json"));
        
        return invoke(SEARCH_API,qparams);
    }
 
    public JSON geosearch(String query) {
        return geosearch(query, null);
    }
    
    public JSON geosearch(String location, JSON options) {
        if (location == null || location.trim().length() == 0) throw new FreebaseException("You must provide a location to geoearch");
        
        List<NameValuePair> qparams = transform_params(options);
        qparams.add(new BasicNameValuePair("location",location));
        qparams.add(new BasicNameValuePair("format","json"));
        
        return invoke(GEOSEARCH_API,qparams);
    }
    
    public String get_blob(String id) {
        return get_blob(id,null);
    }
    
    public String get_blob(String id, JSON options) {
        if (id == null || id.trim().length() == 0) throw new FreebaseException("You must provide the id of the blob you want");
        String path = BLOB_API_PREFIX;
        String mode = (options != null && options.has("mode")) ? options.get("mode").string() : "raw";
        if ("raw".equals(mode) || "unsafe".equals(mode) || "blurb".equals(mode)) {
            path += mode;
        } else {
            throw new FreebaseException("Invalid mode; must be 'raw' or 'blurb' or 'unsafe'");
        }
        path += id;
            
        List<NameValuePair> qparams = transform_params(options);
        
        String url = host + path + "?" + URLEncodedUtils.format(qparams, "UTF-8");
                
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpRequestBase method = new HttpGet(url);
            method.setHeader("X-Requested-With", "1");
            HttpResponse response = httpclient.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return stringify(new InputStreamReader(entity.getContent(),"UTF-8"));
            } else {
                throw new FreebaseException("Response was empty");
            }
        } catch (ClientProtocolException e) {
            throw new FreebaseException(e);
        } catch (IOException e) {
            throw new FreebaseException(e);
        } catch (IllegalStateException e) {
            throw new FreebaseException(e);
        } catch (ClassCastException e) {
            throw new FreebaseException(e);
        }
    }

	@Override
	protected void sign(HttpRequestBase method) {
		// TODO Auto-generated method stub
		
	}    
}
