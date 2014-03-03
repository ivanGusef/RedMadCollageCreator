package com.ivangusev.rmcc.client;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import com.ivangusev.rmcc.R;
import com.ivangusev.rmcc.client.exception.HttpException;
import com.ivangusev.rmcc.helper.JsonHelper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 21.02.14.
 */
public class HttpManager {

    private static final String USER_AGENT = "Android";

    private static final String CONTENT_TYPE_JSON = "application/json";

    private static HttpManager sInstance;

    public static HttpManager getInstance(Context context) {
        if (sInstance == null) sInstance = new HttpManager(context);
        return sInstance;
    }

    private final Context mContext;
    private final AndroidHttpClient mClient;

    private HttpManager(Context context) {
        mContext = context;
        mClient = AndroidHttpClient.newInstance(USER_AGENT);
    }

    public JSONObject doGet(String url) throws IOException, HttpException {
        return doGet(url, new ArrayList<NameValuePair>());
    }

    public JSONObject doGet(String url, List<NameValuePair> params) throws IOException, HttpException {
        params.add(new BasicNameValuePair("client_id", mContext.getString(R.string.client_id)));
        final HttpGet httpGet = new HttpGet(url + "?" + URLEncodedUtils.format(params, "UTF-8"));

        final HttpResponse httpResponse = mClient.execute(httpGet);
        final StatusLine statusLine = httpResponse.getStatusLine();

        final int statusCode = statusLine.getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new HttpException(statusCode, statusLine.getReasonPhrase());
        }

        final String contentType = httpResponse.getEntity().getContentType().getValue();
        if (contentType.contains(CONTENT_TYPE_JSON)) {
            final String json = readAsString(httpResponse);
            try {
                final JSONObject jsonObject = new JSONObject(json);
                final int code = JsonHelper.getInt(jsonObject, "meta.code");
                if (code != HttpStatus.SC_OK) {
                    throw new HttpException(code, JsonHelper.getString(jsonObject, "meta.error_message"));
                }
                return jsonObject;
            } catch (JSONException e) {
                throw new HttpException(-1, "Unreadable response has received: " + json);
            }
        }

        throw new HttpException(-2, "Illegal Content-Type: " + contentType);
    }

    private static String readAsString(HttpResponse response) throws IOException {
        final StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                }
            }
        }
        return sb.toString();
    }
}
