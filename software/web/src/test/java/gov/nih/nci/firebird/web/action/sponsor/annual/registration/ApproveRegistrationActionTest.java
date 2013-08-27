package gov.nih.nci.firebird.web.action.sponsor.annual.registration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.annual.registration.review.AnnualRegistrationReviewService;
import gov.nih.nci.firebird.test.AnnualRegistrationFactory;
import gov.nih.nci.firebird.test.FirebirdUserFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import org.junit.Test;

import com.google.inject.Inject;
import com.opensymphony.xwork2.Action;

public class ApproveRegistrationActionTest extends AbstractWebTest {

    @Inject
    private ApproveRegistrationAction action;
    @Inject
    private AnnualRegistrationReviewService mockAnnualRegistrationReviewService;
    private FirebirdUser sponsor = FirebirdUserFactory.getInstance().create();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        action.setServletRequest(getMockRequest());
        FirebirdWebTestUtility.setCurrentUser(action, sponsor);
    }

    @Test
    public void testEnterApproveRegistration() {
        assertEquals(Action.SUCCESS, action.enterApproveRegistration());
    }

    @Test
    public void testApproveRegistration() throws ValidationException {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstanceWithId().create();
        registration.setStatus(RegistrationStatus.ACCEPTED);
        action.setRegistration(registration);
        String approvalComments = null;
        assertEquals(Action.NONE, action.approveRegistration());
        verify(mockAnnualRegistrationReviewService).approveRegistration(registration, sponsor.getPerson().getCtepId(), approvalComments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApproveRegistration_NotApprovable() throws ValidationException {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstanceWithId().create();
        registration.setStatus(RegistrationStatus.IN_PROGRESS);
        action.setRegistration(registration);
        action.approveRegistration();
    }

}
