package org.example.java.net.verifications;

import org.example.java.net.tools.HttpURLConnectionTools;
import org.testng.asserts.SoftAssert;

public final class ResponseCodeVerifications {

    private final HttpURLConnectionTools httpURLConnectionTools;

    /**
     * Constructor.
     *
     * @param httpURLConnectionTools the {@link HttpURLConnectionTools}
     */
    public ResponseCodeVerifications(final HttpURLConnectionTools httpURLConnectionTools) {
        this.httpURLConnectionTools = httpURLConnectionTools;
    }

    public void verifyPageResponseCode(SoftAssert softAssert, final String url, final int expectedResponseCode) {
        final int responseCode = httpURLConnectionTools.retrieveResponseCodeByString(url);
        final String errorMessage = String.format("Invalid response code (<%d>) for URL <%s> detected", responseCode, url);
        softAssert.assertEquals(responseCode, expectedResponseCode, errorMessage);
    }
}
