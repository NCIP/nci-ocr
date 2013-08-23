package gov.nih.nci.firebird.web.action.login;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionSupport;

public class PrivacyDisclaimerSplashPageActionTest extends AbstractWebTest {

    private PrivacyDisclaimerSplashPageAction action = new PrivacyDisclaimerSplashPageAction();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        action.setServletRequest(getMockRequest());
    }

    @Test
    public void testShowDisclaimer() {
        assertEquals(ActionSupport.SUCCESS, action.showDisclaimer());
    }

    @Test
    public void testAcceptDisclaimer() {
        assertEquals(ActionSupport.SUCCESS, action.acceptDisclaimer());
        assertEquals(Boolean.TRUE, getMockSession().getAttribute(FirebirdUIConstants.DISCLAIMER_ACCEPTED));
    }

}
