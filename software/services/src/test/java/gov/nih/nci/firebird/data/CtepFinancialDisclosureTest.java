package gov.nih.nci.firebird.data;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.FirebirdModule;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.test.AnnualRegistrationFactory;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.FormTypeFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class CtepFinancialDisclosureTest {

    @Inject
    private ResourceBundle resources;

    @Before
    public void setUp() {
        Guice.createInjector(new FirebirdModule()).injectMembers(this);
        System.setProperty("registration.validation.require.nes.status.active", "true");
    }

    private CtepFinancialDisclosure financialDisclosure;
    private ValidationResult result;

    @Before
    public void init() {
        AnnualRegistration registration = AnnualRegistrationFactory.getInstance().create();
        registration.configureForm(FormTypeFactory.getInstance().create(FormTypeEnum.CTEP_FINANCIAL_DISCLOSURE_FORM));

        AnnualRegistrationFactory.getInstance().setupFinancialDisclosure(registration);
        financialDisclosure = registration.getFinancialDisclosure();
        result = new ValidationResult();
    }

    /**
     * One answer is true. Has one Pharmaceutical company specified
     */
    @Test
    public void testPharmaceutical() {
        financialDisclosure.setEquityInSponsor(true);
        financialDisclosure.validate(result, resources);

        assertTrue(result.isValid());
    }

    /**
     * make all answers false. has one Pharmaceutical company specified
     */
    @Test
    public void testAllAnswersFalse() {
        financialDisclosure.validate(result, resources);

        assertTrue(result.isValid());
    }

    /**
     * clear all Pharmaceutical companies.
     */
    @Test
    public void testNoPharmaceuticalCompany() {
        financialDisclosure.getPharmaceuticalCompanies().clear();
        financialDisclosure.validate(result, resources);

        assertEquals(1, result.getFailures().size());
        assertTrue(hasError(result,
                getErrorMessage("validation.failure.ctep.financial.disclosure.questions.incomplete")));
    }

    /**
     * Make one answer null. No Pharmaceutical companies.
     */
    @Test
    public void testAnswerNull() {
        financialDisclosure.setFinancialInterest(null);
        financialDisclosure.validate(result, resources);

        assertEquals(1, result.getFailures().size());
        assertTrue(hasError(result,
                getErrorMessage("validation.failure.ctep.financial.disclosure.questions.incomplete")));
    }

    protected boolean hasError(ValidationResult result, String errorMessage) {
        for (ValidationFailure failure : result.getFailures()) {
            if (failure.getMessage().equals(errorMessage)) {
                return true;
            }
        }
        return false;
    }

    protected String getErrorMessage(String key, Object... args) {
        return MessageFormat.format(resources.getString(key), args);
    }

    @Test
    public void testIsAdditionalDocumentsUploaded_False() {
        assertFalse(financialDisclosure.isAdditionalDocumentsUploaded());
    }

    @Test
    public void testIsAdditionalDocumentsUploaded_True() {
        FirebirdFile file = new FirebirdFile();
        financialDisclosure.getSupportingDocumentation().add(file);
        assertTrue(financialDisclosure.isAdditionalDocumentsUploaded());
    }

    @Test
    public void testGetNumberOfAdditionalDocuments() {
        FirebirdFile file = new FirebirdFile();
        financialDisclosure.getSupportingDocumentation().add(file);
        assertEquals(1, financialDisclosure.getNumberOfAdditionalDocuments());
    }

    @Test
    public void testCopyForm() {
        CtepFinancialDisclosure oldForm = new CtepFinancialDisclosure();
        oldForm.setEquityInSponsor(true);
        oldForm.setFinancialInterest(false);
        oldForm.setMonetaryGain(true);
        oldForm.setOtherSponsorPayments(false);
        Organization pharmaceuticalCompany = OrganizationFactory.getInstance().create();
        oldForm.getPharmaceuticalCompanies().add(pharmaceuticalCompany);
        FirebirdFile supportingDocument = FirebirdFileFactory.getInstance().create();
        oldForm.getSupportingDocumentation().add(supportingDocument);

        financialDisclosure.copyForm(oldForm);
        assertTrue(financialDisclosure.getEquityInSponsor());
        assertFalse(financialDisclosure.getFinancialInterest());
        assertTrue(financialDisclosure.getMonetaryGain());
        assertFalse(financialDisclosure.getOtherSponsorPayments());
        assertTrue(financialDisclosure.getPharmaceuticalCompanies().contains(pharmaceuticalCompany));
        assertTrue(financialDisclosure.getSupportingDocumentation().contains(supportingDocument));
    }
}
