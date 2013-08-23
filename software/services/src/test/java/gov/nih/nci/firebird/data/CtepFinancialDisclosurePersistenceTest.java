package gov.nih.nci.firebird.data;

import static org.junit.Assert.*;

import gov.nih.nci.firebird.test.AbstractHibernateTestCase;
import gov.nih.nci.firebird.test.FormTypeFactory;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import org.junit.Test;

import java.util.Date;


public class CtepFinancialDisclosurePersistenceTest extends AbstractHibernateTestCase {

    @Test
    public void testSave() {
        AnnualRegistrationConfiguration configuration = new AnnualRegistrationConfiguration();
        configuration.setSponsor(OrganizationFactory.getInstance().create());
        configuration.setTimestamp(new Date());
        InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();
        save(configuration, profile);

        AnnualRegistration registration = createNewRegistration(configuration, profile);
        CtepFinancialDisclosure original = registration.getFinancialDisclosure();
        saveAndFlush(registration);

        CtepFinancialDisclosure reloaded = reloadObject(registration).getFinancialDisclosure();
        assertNotNull(reloaded);
        assertEquals(original.getEquityInSponsor(), reloaded.getEquityInSponsor());
        assertEquals(original.getFinancialInterest(), reloaded.getFinancialInterest());
        assertEquals(original.getMonetaryGain(), reloaded.getMonetaryGain());
        assertEquals(original.getOtherSponsorPayments(), reloaded.getOtherSponsorPayments());
        assertEquals(original.getPharmaceuticalCompanies().size(), original.getPharmaceuticalCompanies().size());

    }

    private AnnualRegistration createNewRegistration(AnnualRegistrationConfiguration configuration,
                                                     InvestigatorProfile profile) {
        AnnualRegistration registration = new AnnualRegistration();
        registration.setConfiguration(configuration);
        registration.setProfile(profile);
        registration.setStatus(RegistrationStatus.NOT_STARTED);
        registration.setAnnualRegistrationType(AnnualRegistrationType.INITIAL);
        registration.setDueDate(new Date());
        registration.configureForm(FormTypeFactory.getInstance().create(FormTypeEnum.CTEP_FINANCIAL_DISCLOSURE_FORM));

        registration.getFinancialDisclosure().setEquityInSponsor(false);
        registration.getFinancialDisclosure().setFinancialInterest(true);
        registration.getFinancialDisclosure().setMonetaryGain(false);
        registration.getFinancialDisclosure().setOtherSponsorPayments(true);
        registration.getFinancialDisclosure().getPharmaceuticalCompanies().
                add(OrganizationFactory.getInstance().create());
        return registration;
    }

}
