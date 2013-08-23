package gov.nih.nci.firebird.service.registration;

import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.CtepFinancialDisclosure;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.pdf.PdfFormField;
import gov.nih.nci.firebird.service.pdf.PdfFormValues;
import gov.nih.nci.firebird.service.pdf.PdfService;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

import static gov.nih.nci.firebird.service.registration.FinancialDisclosureField.MONETARY_GAIN_RADIO_BUTTONS;
import static gov.nih.nci.firebird.service.registration.FinancialDisclosureField.FINANCIAL_INTEREST_RADIO_BUTTONS;
import static gov.nih.nci.firebird.service.registration.FinancialDisclosureField.EQUITY_IN_SPONSOR_RADIO_BUTTONS;
import static gov.nih.nci.firebird.service.registration.FinancialDisclosureField.OTHER_SPONSOR_PAYMENTS_RADIO_BUTTONS;
import static gov.nih.nci.firebird.service.registration.FinancialDisclosureField.DATE_SIGNED_TEXT;
import static gov.nih.nci.firebird.service.registration.FinancialDisclosureField.NAME_TEXT;
import static gov.nih.nci.firebird.service.registration.FinancialDisclosureField.PHARMACEUTICAL_COMPANIES;

/**
 * Enters financial disclosure data into the fields in an existing CTEP Financial Disclosure Form PDF
 * and returns the resulting PDF.
 */
@SuppressWarnings("PMD.TooManyStaticImports")   // imports are enum values
class CtepFinancialDisclosurePdfGenerator extends AbstractPdfFormGenerator {

    /**
     * Constructor.
     * @param registration Registration
     * @param pdfService PDF Service
     * @param fileService File Service
     */
    CtepFinancialDisclosurePdfGenerator(AbstractRegistration registration, PdfService pdfService,
                                        FileService fileService) {
        super(registration, pdfService, fileService);
    }

    @Override
    PdfFormValues getValues() {
        PdfFormValues values = new PdfFormValues();
        setDispositionValues(values);
        values.setValue(NAME_TEXT, getInvestigator().getDisplayName());
        values.setValue(PHARMACEUTICAL_COMPANIES, getPharmaceuticalCompanies());
        return values;

    }

    private void setDispositionValues(PdfFormValues values) {
        setRadioButtonValue(values,
                MONETARY_GAIN_RADIO_BUTTONS,
                getForm().getMonetaryGain(),
                NO_RADIO_BUTTON_SELECTED,
                FIELD_OPTION_YES,
                FIELD_OPTION_NO);
        setRadioButtonValue(values,
                OTHER_SPONSOR_PAYMENTS_RADIO_BUTTONS,
                getForm().getOtherSponsorPayments(),
                NO_RADIO_BUTTON_SELECTED,
                FIELD_OPTION_YES,
                FIELD_OPTION_NO);
        setRadioButtonValue(values,
                FINANCIAL_INTEREST_RADIO_BUTTONS,
                getForm().getFinancialInterest(),
                NO_RADIO_BUTTON_SELECTED,
                FIELD_OPTION_YES,
                FIELD_OPTION_NO);
        setRadioButtonValue(values,
                EQUITY_IN_SPONSOR_RADIO_BUTTONS,
                getForm().getEquityInSponsor(),
                NO_RADIO_BUTTON_SELECTED,
                FIELD_OPTION_YES,
                FIELD_OPTION_NO);
    }

    /**
     * String representation of the Pharmaceutical Company.
     * Uses the toString of Organization, which uses the Name value
     * @return String Organization names separated by a ", " delimiter
     */
    private String getPharmaceuticalCompanies() {
        return StringUtils.join(getForm().getPharmaceuticalCompanies(), SINGLE_LINE_STRING_DELIMITER);
    }

    @Override
    Collection<FirebirdFile> getAttachments() {
        return getForm().getSupportingDocumentation();
    }

    @Override
    CtepFinancialDisclosure getForm() {
        return getRegistration().getFinancialDisclosure();
    }

    @Override
    AnnualRegistration getRegistration() {
        return (AnnualRegistration) super.getRegistration();
    }


    @Override
    PdfFormField getDateSignedField() {
        return DATE_SIGNED_TEXT;
    }
}
