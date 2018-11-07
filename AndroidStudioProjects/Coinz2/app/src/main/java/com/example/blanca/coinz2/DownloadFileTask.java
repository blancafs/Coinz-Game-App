package com.example.blanca.coinz2;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import javax.net.ssl.HttpsURLConnection;

public class DownloadFileTask extends AsyncTask<String, Void, String> {
    private static final String tag = "DownloadFileTask";

    @Override
    protected String doInBackground(String... urls) { // ... can take in multiple arguments
        try {
            return loadFileFromNetwork(urls[0]);
        } catch (IOException e) {
            return "Unable to load content. Check network connection.";
        }
    }

    private String loadFileFromNetwork(String urlString) throws IOException {
        Log.d(tag, "[loadFileFromNetwork] urlString = " +urlString + "==========================");
        return readStream(downloadUrl(new URL(urlString)));
    }

    // given string representation of url, sets up connection and gets input stream.
    private InputStream downloadUrl(URL url) throws IOException {
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setReadTimeout(10000); // milliseconds!
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        Log.d(tag, "[downloadUrl] have set conn, read timeout, request method,input, and connected  ================");
        return conn.getInputStream();
    }

    @NonNull
    private String readStream(InputStream stream) throws IOException {
        String result = IOUtils.toString(stream, StandardCharsets.UTF_8);
        Log.d(tag, "[readStream] result = " + result + " =================================");
        return result;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        DownloadCompleteRunner.downloadComplete(result);
        Log.d(tag, "[downloadCompleteRunner] download Complete result, onPost execute finished ==================");
    }
}
