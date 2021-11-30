package org.example.java.net.tools;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public final class  HttpURLConnectionTools {

    /**
     * Using Bad Request (400) --- based on RFC 2616 - 14.23
     *
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc2616/#section-10.4.1">RFC 2616 - 14.23</a>
     */
    private static final int DEFAULT_RESPONSE_CODE_ERROR = HttpURLConnection.HTTP_BAD_REQUEST;
    private static final int DEFAULT_TIMEOUT = (int) TimeUnit.MILLISECONDS.toMillis(500);
    private static final String REQUEST_METHOD_HEAD = "HEAD";

    public int retrieveResponseCodeByString(final String url) {
        try {
            final URL urlContainer = new URL(url);
            return retrieveResponseCodeByUrl(urlContainer);
        } catch (final MalformedURLException malformedURLException) {
            return DEFAULT_RESPONSE_CODE_ERROR;
        }
    }

    public int retrieveResponseCodeByUrl(final URL url) {
        final HttpURLConnection httpURLConnection;
        try {
            httpURLConnection = createHttpURLConnection(url);
            httpURLConnection.setRequestMethod(REQUEST_METHOD_HEAD);
            httpURLConnection.setConnectTimeout(DEFAULT_TIMEOUT);

            httpURLConnection.connect();
            final int responseCode = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();

            return responseCode;
        } catch (final ClassCastException | IOException exception) {
            // ClassCastException covers Mail-to URLs!
            return DEFAULT_RESPONSE_CODE_ERROR;
        }
    }

    /**
     * Package protected for testability!
     */
    HttpURLConnection createHttpURLConnection(final URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }
}
