package org.example.org.openqa.selenium.webelement.verifications;

import org.example.java.net.tools.HttpURLConnectionTools;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WebElementVerifications {

    private static final Logger LOGGER = Logger.getLogger(WebElementVerifications.class.getName());

    private static final int RESPONSE_CODE_NON_LIVE_RANGE_START = 400;
    private static final int RESPONSE_CODE_NON_LIVE_RANGE_END = 499;

    private static final String ATTRIBUTE_HYPERTEXT_REFERENCE = "href";
    private static final String TAG_NAME_ANCHOR = "a";

    private final HttpURLConnectionTools httpURLConnectionTools;

    /**
     * Constructor.
     *
     * @param httpURLConnectionTools the {@link HttpURLConnectionTools}
     */
    public WebElementVerifications(final HttpURLConnectionTools httpURLConnectionTools) {
        this.httpURLConnectionTools = httpURLConnectionTools;
    }

    public void verifyAllHyperlinksAreLive(SoftAssert softAssert, final Collection<WebElement> hyperlinks) {
        hyperlinks.forEach(hyperlink -> {
            // Fail fast
            final String tagName = hyperlink.getTagName();
            if (!TAG_NAME_ANCHOR.equalsIgnoreCase(tagName)) {
                final String errorMessage = String.format("Not an anchor WebElement <%s>", hyperlink);
                softAssert.fail(errorMessage);

                // Skip to next iteration
                return;
            }

            final String href = hyperlink.getAttribute(ATTRIBUTE_HYPERTEXT_REFERENCE);
            final String linkText = hyperlink.getText();

            final String logMessage = String.format("Verifying Hyperlink <%s> referenced as <%s>", href, linkText);
            LOGGER.info(logMessage);

            final URL hrefUrl;
            try {
                hrefUrl = new URL(href);
            } catch (final MalformedURLException malformedURLException) {
                final String errorMessage = String.format("Can not create URL from <%s> referenced as <%s>", href, linkText);
                softAssert.fail(errorMessage);

                // Skip to next iteration
                return;
            }

            final int responseCode = httpURLConnectionTools.retrieveResponseCodeByUrl(hrefUrl);
            final boolean isOutOfRange = isOutOfRange(RESPONSE_CODE_NON_LIVE_RANGE_START, responseCode, RESPONSE_CODE_NON_LIVE_RANGE_END);

            final String errorMessage = String.format("Non live (<%d>) hyperlink <%s> referenced as <%s> detected", responseCode, hrefUrl, linkText);
            softAssert.assertTrue(isOutOfRange, errorMessage);
        });
    }

    private static boolean isOutOfRange(final int startInclusive, final int value, final int endInclusive) {
        return !isInRange(startInclusive, value, endInclusive);
    }

    private static boolean isInRange(final int startInclusive, final int value, final int endInclusive) {
        return IntStream.rangeClosed(startInclusive, endInclusive)
                .boxed()
                .collect(Collectors.toList())
                .contains(value);
    }
}
