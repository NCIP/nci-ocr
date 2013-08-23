package gov.nih.nci.firebird.web.action.investigator.profile;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.data.AbstractPersonAssociation;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.OrderingDesignee;
import gov.nih.nci.firebird.data.ShippingDesignee;
import gov.nih.nci.firebird.test.PersonAssociationFactory;
import gov.nih.nci.firebird.test.PersonFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import org.junit.Test;

import com.google.inject.Inject;
import com.opensymphony.xwork2.Action;

public class DesigneeAssociationTabActionTest extends AbstractWebTest {

    private InvestigatorProfile profile = new InvestigatorProfile();

    @Inject
    private DesigneeAssociationTabAction action;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        action.setProfile(profile);
    }

    @Test
    public void testEnterDesigneeAssociations() {
        assertEquals(Action.SUCCESS, action.enterDesigneeAssociations());
    }

    @Test
    public void testGetOrderingDesigneesJson() throws Exception {
        OrderingDesignee designee1 = addOrderingDesignee();
        designee1.setId(1L);
        OrderingDesignee designee2 = addOrderingDesignee();
        designee2.setId(2L);

        String json = action.getOrderingDesigneesJson();
        checkAssociationInJson(json, designee1);
        checkAssociationInJson(json, designee2);
    }

    private OrderingDesignee addOrderingDesignee() throws Exception {
        return profile.addOrderingDesignee(PersonFactory.getInstanceWithId().create());
    }

    private void checkAssociationInJson(String json, AbstractPersonAssociation association) {
        assertTrue(json, json.contains("\"id\":" + association.getId().toString() + ""));
        assertTrue(json, json.contains("\"displayName\":\"" + association.getPerson().getDisplayName() + "\""));
        assertTrue(json, json.contains("\"email\":\"" + association.getPerson().getEmail() + "\""));
        assertTrue(json, json.contains("\"phoneNumber\":\"" + association.getPerson().getPhoneNumber() + "\""));
        assertTrue(
                json,
                json.contains("\"streetAddress\":\"" + association.getPerson().getPostalAddress().getStreetAddress()
                        + "\""));
    }

    @Test
    public void testGetShippingDesigneeJson() throws Exception {
        ShippingDesignee shippingDesignee = PersonAssociationFactory.getInstance().createShippingDesignee(profile);
        profile.setShippingDesignee(shippingDesignee);
        shippingDesignee.setId(1L);
        shippingDesignee.setShippingAddress(ValueGenerator.getUniqueDomesticAddress());

        String json = action.getShippingDesigneeJson();
        checkAssociationInJson(json, shippingDesignee);
        assertTrue(json,
                json.contains("\"streetAddress\":\"" + shippingDesignee.getShippingAddress().getStreetAddress() + "\""));
        assertTrue(json, json.contains("\"name\":\"" + shippingDesignee.getOrganization().getName() + "\""));
    }

}
