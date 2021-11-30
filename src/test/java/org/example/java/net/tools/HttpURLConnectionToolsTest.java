package org.example.java.net.tools;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@ExtendWith(MockitoExtension.class)
public class HttpURLConnectionToolsTest {

    private static final int DEFAULT_RESPONSE_CODE_ERROR = HttpURLConnection.HTTP_BAD_REQUEST;
    private static final String DEFAULT_URL_STRING = "http://www.example.org/";

    @Spy
    HttpURLConnectionTools classUnderTest;

    @Mock
    HttpURLConnection mockHttpURLConnection;

    @Test
    public void retrieveResponseCodeByString_happyPath() throws Exception {
        Mockito.doReturn(mockHttpURLConnection).when(classUnderTest).createHttpURLConnection(Mockito.any(URL.class));
        Mockito.doReturn(HttpURLConnection.HTTP_OK).when(mockHttpURLConnection).getResponseCode();

        final int responseCode = classUnderTest.retrieveResponseCodeByString(DEFAULT_URL_STRING);

        Assertions.assertEquals(HttpURLConnection.HTTP_OK, responseCode);
    }

    @Test
    public void retrieveResponseCodeByString_badPath_withNull() {
        final int responseCode = classUnderTest.retrieveResponseCodeByString(null);

        Assertions.assertEquals(DEFAULT_RESPONSE_CODE_ERROR, responseCode);
    }

    @Test
    public void retrieveResponseCodeByString_badPath_emptyString() {
        final int responseCode = classUnderTest.retrieveResponseCodeByString("");

        Assertions.assertEquals(DEFAULT_RESPONSE_CODE_ERROR, responseCode);
    }

    @Test
    public void retrieveResponseCodeByUrl_happyPath() throws Exception {
        Mockito.doReturn(mockHttpURLConnection).when(classUnderTest).createHttpURLConnection(Mockito.any(URL.class));
        Mockito.doReturn(HttpURLConnection.HTTP_OK).when(mockHttpURLConnection).getResponseCode();

        final int responseCode = classUnderTest.retrieveResponseCodeByUrl(new URL(DEFAULT_URL_STRING));

        Assertions.assertEquals(HttpURLConnection.HTTP_OK, responseCode);
    }

    @Test
    public void retrieveResponseCodeByUrl_badPath_UrlWithEmptyString() {
        Assertions.assertThrows(MalformedURLException.class, () -> {
            classUnderTest.retrieveResponseCodeByUrl(new URL(""));
        });
    }

    @Test
    public void testRetrieveResponseCodeByUrl_badPath_UrlIsMailTo() throws Exception {
        final int responseCode = classUnderTest.retrieveResponseCodeByUrl(new URL("mailto:email@example.com"));

        Assertions.assertEquals(DEFAULT_RESPONSE_CODE_ERROR, responseCode);
    }

    @Test
    public void testRetrieveResponseCodeByUrl_badPath_throwsIoException() throws Exception {
        Mockito.doReturn(mockHttpURLConnection).when(classUnderTest).createHttpURLConnection(Mockito.any(URL.class));
        Mockito.doThrow(IOException.class).when(mockHttpURLConnection).getResponseCode();

        final int responseCode = classUnderTest.retrieveResponseCodeByUrl(new URL(DEFAULT_URL_STRING));

        Assertions.assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, responseCode);
    }
}