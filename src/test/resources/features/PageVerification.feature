Feature: Page Verification
  As a user,
  I want open a web page with my browser
  to make sure all hyperlinks on it are "live".

  @CompleteSoftAssert
  Scenario Outline: Verification of a web page and its hyperlinks
    Given The browser is open
    When  I open to the <page>
    Then  the <page> is loaded with <responseCode>
    And   the <page> contains no errors in the console log
    And   the <page>'s hyperlinks are "live"

    Examples:
      | page                                                 | responseCode |
      | https://www.w3.org/standards/badpage                 | 404          |
      | https://www.w3.org/standards/webofdevices/multimodal | 200          |
      | https://www.w3.org/standards/webdesign/htmlcss       | 200          |
