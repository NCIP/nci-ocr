package gov.nih.nci.firebird.web.action.investigator.profile;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.ShippingDesignee;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.organization.OrganizationSearchService;
import gov.nih.nci.firebird.service.person.PersonSearchService;
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
    private PersonSearchService mockPersonSearchService;

    @Inject
    private OrganizationSearchService mockOrganizationSearchService;

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
    public void testPrepare_NoSearchKeys() {
        action.prepare();
        verifyZeroInteractions(mockPersonSearchService);
        verifyZeroInteractions(mockOrganizationSearchService);
    }

    @Test
    public void testPrepare_PersonSearchKeys() {
        String searchKey = "12345";
        action.setSelectedPersonKey(searchKey);
        action.prepare();
        verify(mockPersonSearchService).getPerson(searchKey);
        verifyZeroInteractions(mockOrganizationSearchService);
    }

    @Test
    public void testPrepare_OrganizationSearchKeys() throws Exception {
        String searchKey = "12345";
        action.setSelectedOrganizationKey(searchKey);
        action.prepare();
        verify(mockOrganizationSearchService).getOrganization(searchKey);
        verifyZeroInteractions(mockPersonSearchService);
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
