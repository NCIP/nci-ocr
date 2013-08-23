package gov.nih.nci.firebird.service.protocol;

import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AbstractSupplementalForm;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.test.RegistrationFactory;
import org.junit.Test;

import java.util.Set;

import static gov.nih.nci.firebird.data.FormStatus.NOT_APPLICABLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ProtocolUtilTest {

    @Test
    public void testMarkAllApproved() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        ProtocolUtil.markAllApproved(registration);
        assertAllFormsApproved(registration);
    }

    private void assertAllFormsApproved(InvestigatorRegistration registration) {
        assertEquals(RegistrationStatus.APPROVED, registration.getStatus());
        for (AbstractRegistrationForm form : registration.getForms()) {
            if (form instanceof AbstractSupplementalForm) {
                assertEquals(NOT_APPLICABLE, form.getFormStatus());
            } else {
                assertEquals(FormStatus.APPROVED, form.getFormStatus());
            }
        }
    }

    @Test
    public void testMarkApproved() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        ProtocolUtil.markApproved(registration);
        assertAllFormsApproved(registration);
    }

    @Test
    public void testSetFormsToInProgress() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        ProtocolUtil.setFormsToInProgress(registration);

        for (AbstractRegistrationForm form : registration.getFormsForSponsorReview()) {
            assertEquals(FormStatus.IN_PROGRESS, form.getFormStatus());
        }

    }

    @Test
    public void testGetAllEmailAddress() {
        InvestigatorRegistration registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        Set<String> emailAddresses = ProtocolUtil.getAllEmailAddress(registration);
        assertNotNull(emailAddresses);
        assertTrue(emailAddresses.contains(registration.getProfile().getPerson().getEmail()));
    }


}
