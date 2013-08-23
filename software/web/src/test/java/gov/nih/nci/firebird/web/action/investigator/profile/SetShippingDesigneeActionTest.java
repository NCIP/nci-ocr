package gov.nih.nci.firebird.web.action.investigator.profile;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.ShippingDesignee;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.organization.OrganizationService;
import gov.nih.nci.firebird.service.person.PersonService;
import gov.nih.nci.firebird.test.PersonAssociationFactory;
import gov.nih.nci.firebird.web.action.FirebirdWebTestUtility;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

@RunWith(MockitoJUnitRunner.class)
public class SetShippingDesigneeActionTest extends AbstractWebTest {

    @Inject
    private PersonService mockPersonService;

    @Inject
    private OrganizationService mockOrganizationService;

    @Inject
    private InvestigatorProfileService mockProfileService;

    @Inject
    private SetShippingDesigneeAction action;

    private InvestigatorProfile profile = new InvestigatorProfile();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        FirebirdWebTestUtility.setupMockRequest(action);
        action.setProfile(profile);
    }

    @Test
    public void testPrepare_NoOrganizationExternalIds() {
        action.prepare();
        verifyZeroInteractions(mockPersonService);
        verifyZeroInteractions(mockOrganizationService);
    }

    @Test
    public void testPrepare_PersonOrganizationExternalIds() throws Exception {
        String organizationExternalId = "12345";
        action.setSelectedPersonExternalId(organizationExternalId);
        action.prepare();
        verify(mockPersonService).getByExternalId(organizationExternalId);
        verifyZeroInteractions(mockOrganizationService);
    }

    @Test
    public void testPrepare_OrganizationOrganizationExternalIds() throws Exception {
        String organizationExternalId = "12345";
        action.setSelectedOrganizationExternalId(organizationExternalId);
        action.prepare();
        verify(mockOrganizationService).getByExternalId(organizationExternalId);
        verifyZeroInteractions(mockPersonService);
    }

    @Test
    public void testEnterSetShippingDesignee() {
        assertEquals(Action.INPUT, action.enterSetShippingDesignee());
    }

    @Test
    public void testSetShippingDesigneeAction() throws ValidationException {
        ShippingDesignee designee = PersonAssociationFactory.getInstance().createShippingDesignee();
        action.setShippingDesignee(designee);
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.setShippingDesigneeAction());
        verify(mockProfileService).setShippingDesignee(profile, designee);
    }

    @Test
    public void testSetShippingDesigneeAction_ValidationException() throws ValidationException {
        ShippingDesignee designee = PersonAssociationFactory.getInstance().createShippingDesignee();
        action.setShippingDesignee(designee);
        doThrow(new ValidationException(new ValidationFailure(""))).when(mockProfileService).setShippingDesignee(
                profile, designee);
        assertEquals(ActionSupport.INPUT, action.setShippingDesigneeAction());
        verify(mockProfileService).setShippingDesignee(profile, designee);
    }

}
