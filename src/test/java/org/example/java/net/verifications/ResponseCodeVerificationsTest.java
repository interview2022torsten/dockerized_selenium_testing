package org.example.java.net.verifications;


import org.example.java.net.tools.HttpURLConnectionTools;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.net.HttpURLConnection;

@ExtendWith(MockitoExtension.class)
public class ResponseCodeVerificationsTest {

    private static final String DEFAULT_URL_STRING = "http://www.example.org/";

    private ResponseCodeVerifications classUnderTest;

    @Mock
    private HttpURLConnectionTools mockHttpURLConnectionTools;

    @Test
    public void verifyPageResponseCode_happyPath_verificationPass() {
        Mockito.doReturn(HttpURLConnection.HTTP_OK).when(mockHttpURLConnectionTools).retrieveResponseCodeByString(Mockito.anyString());
        final SoftAssert softAssert = new SoftAssert();

        classUnderTest.verifyPageResponseCode(softAssert, DEFAULT_URL_STRING, HttpURLConnection.HTTP_OK);

        try {
            softAssert.assertAll();
        } catch (final AssertionError assertionError) {
            Assert.fail("SoftAssertion recorded on happy path!");
        }
    }

    @Test
    public void verifyPageResponseCode_happyPath_verificationFail() {
        Mockito.doReturn(HttpURLConnection.HTTP_BAD_REQUEST).when(mockHttpURLConnectionTools).retrieveResponseCodeByString(Mockito.anyString());
        final SoftAssert softAssert = new SoftAssert();

        classUnderTest.verifyPageResponseCode(softAssert, DEFAULT_URL_STRING, HttpURLConnection.HTTP_OK);

        Assertions.assertThrows(AssertionError.class, softAssert::assertAll);
    }

    @BeforeEach
    public void initializeClassUnderTest() {
        classUnderTest = new ResponseCodeVerifications(mockHttpURLConnectionTools);
    }
}