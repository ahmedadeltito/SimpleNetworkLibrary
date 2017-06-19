package com.ahmedadelsaid.simplenetworklibrary.networkrequest;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ahmedadelsaid.simplenetworklibrary.SimpleNetworkUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * NetworkRequestAsyncTask is an AsyncTask that handle all the process behind sending, receiving and
 * caching the response
 */

class NetworkRequestAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private final String LOG_TAG = NetworkRequestAsyncTask.class.getSimpleName();
    private String response, baseUrl, endpoint;
    private Map<String, String> params, headers;
    private JSONObject object;
    private RequestType requestType;
    private ContentType contentType;
    private Context context;
    private int code;
    private String message;
    private String error;
    private String charset = "UTF-8";
    private boolean decodedUrlInUTF = false;
    private final ResponseMemoryCache responseMemoryCache;
    private OnNetworkRequestResponseListener onNetworkRequestResponseListener;

    NetworkRequestAsyncTask(Context context, String baseUrl, String endpoint, RequestType requestType,
                            ContentType contentType, @Nullable Map<String, String> params,
                            @Nullable Map<String, String> headers, @Nullable JSONObject object) {
        this.context = context;
        this.baseUrl = baseUrl;
        this.endpoint = endpoint;
        this.requestType = requestType;
        this.contentType = contentType;
        if (params != null)
            this.params = params;
        if (headers != null)
            this.headers = headers;
        if (object != null)
            this.object = object;
        this.responseMemoryCache = ResponseMemoryCache.getInstance();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String cachedResponse = responseMemoryCache.getResponseFromCache(
                getCompletePath(buildPath(baseUrl, endpoint).build().toString()));
        if (SimpleNetworkUtils.stringIsNotEmpty(cachedResponse)) {
            if (onNetworkRequestResponseListener != null) {
                onNetworkRequestResponseListener.onSuccessResponse(cachedResponse, contentType, true);
            }
        }
    }

    @Override
    protected Boolean doInBackground(Void... par) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        response = null;
        try {
            urlConnection = getStructuredRequest(baseUrl, endpoint, requestType, contentType, params, headers, object);
            assert urlConnection != null;
            InputStream is = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (is == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0)
                return null;
            response = buffer.toString();
            Log.d(LOG_TAG, "Server Response: " + response);
            return true;
        } catch (FileNotFoundException e) {
            manageError(e, urlConnection);
            return false;
        } catch (IOException e) {
            manageError(e, urlConnection);
            return false;
        } catch (Exception e) {
            manageError(e, urlConnection);
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error Closing Stream", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            responseMemoryCache.addResponseToCache(getCompletePath(
                    buildPath(baseUrl, endpoint).build().toString()), response);
            if (onNetworkRequestResponseListener != null) {
                onNetworkRequestResponseListener.onSuccessResponse(response, contentType, false);
            }
        } else {
            if (onNetworkRequestResponseListener != null) {
                onNetworkRequestResponseListener.onErrorResponse(error, message, code);
            }
        }
    }

    private HttpURLConnection getStructuredRequest(String baseUrl, String endpoint, RequestType type,
                                                   ContentType contentType,
                                                   @Nullable Map<String, String> params,
                                                   @Nullable Map<String, String> headers,
                                                   @Nullable JSONObject object) throws Exception {
        HttpURLConnection urlConnection = null;
        URL url = null;
        Uri.Builder builderPath = buildPath(baseUrl, endpoint);
        if (type == RequestType.GET) {
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (decodedUrlInUTF) {
                        String decode = entry.getValue();
                        decode = decode.replace(" ", "%20");
                        builderPath.appendQueryParameter(entry.getKey(), decode);
                    } else {
                        builderPath.appendQueryParameter(entry.getKey(), entry.getValue());
                    }
                }
            }
            url = new URL(getCompletePath(builderPath.build().toString()));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(type.name());
            urlConnection = setHeaders(urlConnection, headers, contentType);
            urlConnection.connect();
        } else if (type == RequestType.POST) {
            url = new URL(getCompletePath(builderPath.build().toString()));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(type.name());
            urlConnection = setHeaders(urlConnection, headers, contentType);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            if (object != null) { // A JSON object will be send it.
                urlConnection.connect();
                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write(object.toString().getBytes());
                dataOutputStream.flush();
                dataOutputStream.close();
            } else { // if there is no JSON object will create the request with encoded url params
                Uri.Builder builder = new Uri.Builder();
                if (params != null) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        builder.appendQueryParameter(entry.getKey(), entry.getValue());
                    }
                    String query = builder.build().getEncodedQuery();
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, charset));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    urlConnection.connect();
                }
            }
        } else if (type == RequestType.PUT) {
            url = new URL(getCompletePath(builderPath.build().toString()));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(type.name());
            urlConnection = setHeaders(urlConnection, headers, contentType);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            if (object != null) { // A JSON object will be send it.
                urlConnection.connect();
                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write(object.toString().getBytes());
                dataOutputStream.flush();
                dataOutputStream.close();
            } else {
                Uri.Builder builder = new Uri.Builder();
                if (params != null) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        builder.appendQueryParameter(entry.getKey(), entry.getValue());
                    }
                    String query = builder.build().getEncodedQuery();
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, charset));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    urlConnection.connect();
                }
            }
        } else if (type == RequestType.PATCH) {
            url = new URL(getCompletePath(builderPath.build().toString()));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(type.name());
            urlConnection = setHeaders(urlConnection, headers, contentType);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            if (object != null) { // A JSON object will be send it.
                urlConnection.connect();
                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write(object.toString().getBytes());
                dataOutputStream.flush();
                dataOutputStream.close();
            } else {
                Uri.Builder builder = new Uri.Builder();
                if (params != null) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        builder.appendQueryParameter(entry.getKey(), entry.getValue());
                    }
                    String query = builder.build().getEncodedQuery();
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, charset));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    urlConnection.connect();
                }
            }
        } else if (type == RequestType.DELETE) {
            url = new URL(getCompletePath(builderPath.build().toString()));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(type.name());
            urlConnection = setHeaders(urlConnection, headers, contentType);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            if (object != null) { // A JSON object will be send it.
                urlConnection.connect();
                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write(object.toString().getBytes());
                dataOutputStream.flush();
                dataOutputStream.close();
            }
        }
        assert url != null;
        Log.d(LOG_TAG, url.toString());
        return urlConnection;
    }

    private String getCompletePath(String path) {
        String decodedUrlInUTF = "";
        try {
            decodedUrlInUTF = NetworkRequestAsyncTask.this.decodedUrlInUTF ?
                    java.net.URLDecoder.decode(path, charset) : path;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedUrlInUTF;
    }

    private Uri.Builder buildPath(String baseUrl, String endpoint) {
        Uri.Builder builderPath = Uri.parse("").buildUpon();
        if (baseUrl != null && !baseUrl.equalsIgnoreCase("")) {
            builderPath = Uri.parse(baseUrl).buildUpon();
        } else {
            Log.e(LOG_TAG, "No Base URL was set");
        }
        builderPath.appendPath(endpoint);
        return builderPath;
    }

    private HttpURLConnection setHeaders(HttpURLConnection urlConnection, @Nullable Map<String, String> headers,
                                         ContentType contentType) {
        if (contentType == ContentType.JSON) {
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
        } else if (contentType == ContentType.XML) {
            urlConnection.setRequestProperty("Content-Type", "application/xml");
            urlConnection.setRequestProperty("Accept", "application/xml");
        }
        if (headers != null && urlConnection != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return urlConnection;
    }

    private void manageError(Exception e, HttpURLConnection urlConnection) {
        if (SimpleNetworkUtils.isNetworkAvailable(context)) {
            if (urlConnection != null) {
                try {
                    code = urlConnection.getResponseCode();
                    if (urlConnection.getErrorStream() != null) {
                        InputStream is = urlConnection.getErrorStream();
                        StringBuilder buffer = new StringBuilder();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line).append("\n");
                        }
                        message = buffer.toString();
                    } else {
                        message = urlConnection.getResponseMessage();
                    }
                    error = urlConnection.getErrorStream().toString();
                    Log.e(LOG_TAG, "Error: " + message + ", code: " + code);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Log.e(LOG_TAG, "Error: " + e1.getMessage());
                }
            } else {
                code = 105;
                error = e.getMessage();
                message = "Error: No internet connection";
                Log.e(LOG_TAG, "code: " + code + ", " + message);
            }
        } else {
            code = 105;
            error = e.getMessage();
            message = "Error: No internet connection";
            Log.e(LOG_TAG, "code: " + code + ", " + message);
        }
    }

    public void setOnNetworkRequestResponseListener(OnNetworkRequestResponseListener onNetworkRequestResponseListener) {
        this.onNetworkRequestResponseListener = onNetworkRequestResponseListener;
    }

    public void setDecodedUrlInUTF(boolean decodedUrlInUTF) {
        this.decodedUrlInUTF = decodedUrlInUTF;
    }
}
