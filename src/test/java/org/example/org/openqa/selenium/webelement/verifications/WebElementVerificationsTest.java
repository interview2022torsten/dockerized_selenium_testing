package org.example.org.openqa.selenium.webelement.verifications;

import org.example.java.net.tools.HttpURLConnectionTools;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class WebElementVerificationsTest {

    private static final int DEFAULT_RESPONSE_CODE_ERROR = HttpURLConnection.HTTP_BAD_REQUEST;

    private static final String DEFAULT_URL_STRING = "http://www.example.org/";
    private static final String DEFAULT_HYPERLINK_LABEL = "DEFAULT HYPERLINK LABEL";
    private static final String TAG_NAME_ANCHOR = "a";

    private WebElementVerifications classUnderTest;

    @Mock
    private WebElement mockWebElement;

    @Mock
    private HttpURLConnectionTools mockHttpURLConnectionTools;

    @Test
    public void verifyAllHyperlinksAreLive_happyPath() {
        Mockito.doReturn(TAG_NAME_ANCHOR).when(mockWebElement).getTagName();
        Mockito.doReturn(DEFAULT_HYPERLINK_LABEL).when(mockWebElement).getText();
        Mockito.doReturn(DEFAULT_URL_STRING).when(mockWebElement).getAttribute(Mockito.anyString());
        Mockito.doReturn(HttpURLConnection.HTTP_OK).when(mockHttpURLConnectionTools).retrieveResponseCodeByUrl(Mockito.any(URL.class));
        Collection<WebElement> hyperlinks = Collections.singleton(mockWebElement);
        final SoftAssert softAssert = new SoftAssert();

        classUnderTest.verifyAllHyperlinksAreLive(softAssert, hyperlinks);

        try {
            softAssert.assertAll();
        } catch (final AssertionError assertionError) {
            Assert.fail("SoftAssertion recorded on happy path!");
        }
    }

    @Test
    public void verifyAllHyperlinksAreLive_badPath_notAnAnchorElement() {
        Mockito.doReturn("").when(mockWebElement).getTagName();
        Collection<WebElement> hyperlinks = Collections.singleton(mockWebElement);
        final SoftAssert softAssert = new SoftAssert();

        classUnderTest.verifyAllHyperlinksAreLive(softAssert, hyperlinks);

        Assertions.assertThrows(AssertionError.class, softAssert::assertAll);
        Mockito.verifyNoInteractions(mockHttpURLConnectionTools);
    }

    @Test
    public void verifyAllHyperlinksAreLive_badPath_hyperlinkUrlInvalid() {
        Mockito.doReturn(TAG_NAME_ANCHOR).when(mockWebElement).getTagName();
        Mockito.doReturn(DEFAULT_HYPERLINK_LABEL).when(mockWebElement).getText();
        Mockito.doReturn("").when(mockWebElement).getAttribute(Mockito.anyString());

        Collection<WebElement> hyperlinks = Collections.singleton(mockWebElement);
        final SoftAssert softAssert = new SoftAssert();

        classUnderTest.verifyAllHyperlinksAreLive(softAssert, hyperlinks);

        Assertions.assertThrows(AssertionError.class, softAssert::assertAll);
        Mockito.verifyNoInteractions(mockHttpURLConnectionTools);
    }

    @Test
    public void verifyAllHyperlinksAreLive_badPath_hyperlinkNotLive() {
        Mockito.doReturn(TAG_NAME_ANCHOR).when(mockWebElement).getTagName();
        Mockito.doReturn(DEFAULT_HYPERLINK_LABEL).when(mockWebElement).getText();
        Mockito.doReturn(DEFAULT_URL_STRING).when(mockWebElement).getAttribute(Mockito.anyString());
        Mockito.doReturn(DEFAULT_RESPONSE_CODE_ERROR).when(mockHttpURLConnectionTools).retrieveResponseCodeByUrl(Mockito.any(URL.class));

        Collection<WebElement> hyperlinks = Collections.singleton(mockWebElement);
        final SoftAssert softAssert = new SoftAssert();

        classUnderTest.verifyAllHyperlinksAreLive(softAssert, hyperlinks);

        Assertions.assertThrows(AssertionError.class, softAssert::assertAll);
    }

    @BeforeEach
    public void initializeClassUnderTest() {
        classUnderTest = new WebElementVerifications(mockHttpURLConnectionTools);
    }
}