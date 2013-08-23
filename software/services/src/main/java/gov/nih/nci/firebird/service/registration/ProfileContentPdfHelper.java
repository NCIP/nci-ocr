/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a person or an entity, and all other entities that control,
 * are controlled by, or are under common control with the entity. Control for
 * purposes of this definition means (i) the direct or indirect power to cause
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares,
 * or (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up,
 * no-charge, irrevocable, transferable and royalty-free right and license in
 * its rights in the NCI OCR Software to (i) use, install, access, operate,
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the NCI OCR Software; (ii) distribute and
 * have distributed to and by third parties the NCI OCR Software and any
 * modifications and derivative works thereof; and (iii) sublicense the
 * foregoing rights set out in (i) and (ii) to third parties, including the
 * right to license such rights to further third parties. For sake of clarity,
 * and not by way of limitation, NCI shall have no right of accounting or right
 * of payment from You or Your sub-licensees for the rights granted under this
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the
 * above copyright notice, this list of conditions and the disclaimer and
 * limitation of liability of Article 6, below. Your redistributions in object
 * code form must reproduce the above copyright notice, this list of conditions
 * and the disclaimer of Article 6 in the documentation and/or other materials
 * provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: This product includes software
 * developed by 5AM and the National Cancer Institute. If You do not include
 * such end-user documentation, You shall include this acknowledgment in the
 * Software itself, wherever such third-party acknowledgments normally appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", or "5AM"
 * to endorse or promote products derived from this Software. This License does
 * not authorize You to use any trademarks, service marks, trade names, logos or
 * product names of either NCI or 5AM, except as required to comply with the
 * terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this
 * Software into Your proprietary programs and into any third party proprietary
 * programs. However, if You incorporate the Software into third party
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software
 * into such third party proprietary programs and for informing Your
 * sub-licensees, including without limitation Your end-users, of their
 * obligation to secure any required permissions from such third parties before
 * incorporating the Software into such third party proprietary software
 * programs. In the event that You fail to obtain such permissions, You agree
 * to indemnify NCI for any claims against NCI by such third parties, except to
 * the extent prohibited by law, resulting from Your failure to obtain such
 * permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own
 * copyright statement to Your modifications and to the derivative works, and
 * You may provide additional or different license terms and conditions in Your
 * sublicenses of modifications of the Software, or any derivative works of the
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC. OR THEIR
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.firebird.service.registration;

import static gov.nih.nci.firebird.service.registration.AbstractPdfWriterGenerator.THREE_COLUMNS;
import static gov.nih.nci.firebird.service.registration.AbstractPdfWriterGenerator.FOUR_COLUMNS;
import static gov.nih.nci.firebird.service.registration.AbstractPdfWriterGenerator.FIVE_COLUMNS;
import static gov.nih.nci.firebird.service.registration.AbstractPdfWriterGenerator.SPACE_CHARACTER;
import static gov.nih.nci.firebird.service.registration.AbstractPdfWriterGenerator.VALUE_CELL_INDENTATION;
import static gov.nih.nci.firebird.service.registration.AbstractPdfWriterGenerator.PADDING_RIGHT;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.Address;
import gov.nih.nci.firebird.data.BoardCertifiedSpecialty;
import gov.nih.nci.firebird.data.CredentialType;
import gov.nih.nci.firebird.data.Degree;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.MedicalLicense;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.SpecialtyCredential;
import gov.nih.nci.firebird.data.WorkHistory;

import java.util.SortedSet;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * Provides common methods for adding profile-based content to a PDF.
 */
@SuppressWarnings({ "PMD.TooManyMethods", "PMD.TooManyStaticImports" })
// more readable with broken out helper methods
// importing static constants, not methods
class ProfileContentPdfHelper {

    private static final String CONTACT_INFORMATION_HEADER_KEY = "pdf.contact.information.header";
    private static final String CREDENTIALS_HEADER_KEY = "pdf.credentials.header";
    private static final String PRIMARY_ORGANIZATION_HEADER_KEY = "pdf.primary.organization.header";
    private static final String WORK_HISTORY_TITLE_KEY = "pdf.work.history.heading";
    private static final String DEGREES_TITLE_KEY = "pdf.degrees.heading";
    private static final String INTERNSHIPS_TITLE_KEY = "pdf.internships.heading";
    private static final String RESIDENCIES_TITLE_KEY = "pdf.residencies.heading";
    private static final String FELLOWSHIPS_TITLE_KEY = "pdf.fellowships.heading";
    private static final String LICENSES_TITLE_KEY = "pdf.licenses.heading";
    private static final String SPECIALTIES_TITLE_KEY = "pdf.specialties.heading";
    private static final String INVESTIGATOR_EXTERNAL_ID_TITLE_KEY = "pdf.investigator.external.id.heading";
    private static final String INVESTIGATOR_PROVIDER_NUMBER_TITLE_KEY = "pdf.investigator.provider.number.heading";
    private static final String INVESTIGATOR_PROVIDER_CTEP_ID_TITLE_KEY = "pdf.investigator.number.heading";
    private static final String NAME_TITLE_KEY = "pdf.name.heading";
    private static final String ADDRESS_TITLE_KEY = "pdf.address.heading";
    private static final String EMAIL_ADDRESS_TITLE_KEY = "pdf.email.heading";
    private static final String PHONE_NUMBER_TITLE_KEY = "pdf.phone.heading";
    private static final String CTEP_ID_TITLE_KEY = "pdf.ctep.id.heading";
    private final AbstractPdfWriterGenerator pdfGenerator;

    ProfileContentPdfHelper(AbstractPdfWriterGenerator pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
    }

    void addInvestigatorName(PdfPTable table, int index) {
        table.addCell(pdfGenerator.createHeaderAndValueTableCell(index, NAME_TITLE_KEY, getInvestigator()
                .getDisplayName(), true));
    }

    private Person getInvestigator() {
        return pdfGenerator.getInvestigator();
    }

    void addInvestigatorExternalId(PdfPTable table, int index) {
        table.addCell(pdfGenerator.createHeaderAndValueTableCell(index, INVESTIGATOR_EXTERNAL_ID_TITLE_KEY,
                getInvestigator().getExternalId(), true));
    }

    void addInvestigatorEmail(PdfPTable table, int index) {
        table.addCell(pdfGenerator.createHeaderAndValueTableCell(index, EMAIL_ADDRESS_TITLE_KEY, getInvestigator()
                .getEmail(), false));
    }

    void addInvestigatorPhoneNumber(PdfPTable table, int index) {
        table.addCell(pdfGenerator.createHeaderAndValueTableCell(index, PHONE_NUMBER_TITLE_KEY, getInvestigator()
                .getPhoneNumber(), false));
    }

    void addInvestigatorProviderNumber(PdfPTable table, int index) {
        table.addCell(pdfGenerator.createHeaderAndValueTableCell(index, INVESTIGATOR_PROVIDER_NUMBER_TITLE_KEY,
                getInvestigator().getProviderNumber(), false));
    }

    void addInvestigatorCtepId(PdfPTable table, int index) {
        table.addCell(pdfGenerator.createHeaderAndValueTableCell(index, INVESTIGATOR_PROVIDER_CTEP_ID_TITLE_KEY,
                getInvestigator().getCtepId(), false));
    }

    void addInvestigatorAddress(PdfPTable table, int index) {
        addAddress(getInvestigator().getPostalAddress(), table, index);
    }

    private void addAddress(Address address, PdfPTable table, int index) {
        PdfPTable addressTable = pdfGenerator.createTable(AbstractPdfWriterGenerator.ONE_COLUMN);
        addressTable.addCell(pdfGenerator.createHeaderCell(index + ". "
                + pdfGenerator.getFromResources(ADDRESS_TITLE_KEY)));

        Paragraph contents = new Paragraph();
        if (getOrganization() != null) {
            contents.add(pdfGenerator.getValueChunk(getOrganization().getName()));
            contents.add(Chunk.NEWLINE);
        }
        if (address != null) {
            addAddressContent(contents, address);
        }
        PdfPCell addressCell = pdfGenerator.createTableCell(contents);
        addressCell.setPaddingLeft(VALUE_CELL_INDENTATION);
        addressTable.addCell(addressCell);
        PdfPCell addressTableCell = pdfGenerator.createCell(addressTable);
        addressTableCell.setPaddingRight(PADDING_RIGHT);
        table.addCell(addressTableCell);
    }

    private Organization getOrganization() {
        if (getProfile().getPrimaryOrganization() != null) {
            return getProfile().getPrimaryOrganization().getOrganization();
        } else {
            return null;
        }
    }

    void addOrganizationName(PdfPTable table, int index) {
        String name = getOrganization() != null ? getOrganization().getName() : StringUtils.EMPTY;
        table.addCell(pdfGenerator.createHeaderAndValueTableCell(index, NAME_TITLE_KEY, name,
                true));
    }

    void addOrganizationAddress(PdfPTable table, int index) {
        Address organizationAddress = getOrganization() != null ? getOrganization().getPostalAddress() : null;
        addAddress(organizationAddress, table, index);
    }

    void addOrganizationCtepId(PdfPTable table, int index) {
        String ctepId = getOrganization() != null ? getOrganization().getCtepId() : StringUtils.EMPTY;
        table.addCell(pdfGenerator.createHeaderAndValueTableCell(index, CTEP_ID_TITLE_KEY, ctepId, false));
    }

    void addOrganizationEmail(PdfPTable table, int index) {
        String email = getOrganization() != null ? getOrganization().getEmail() : StringUtils.EMPTY;
        table.addCell(pdfGenerator.createHeaderAndValueTableCell(index, EMAIL_ADDRESS_TITLE_KEY, email, false));
    }

    void addOrganizationPhoneNumber(PdfPTable table, int index) {
        String phoneNumber = getOrganization() != null ? getOrganization().getPhoneNumber() : StringUtils.EMPTY;
        table.addCell(pdfGenerator.createHeaderAndValueTableCell(index, PHONE_NUMBER_TITLE_KEY, phoneNumber, false));
    }

    void addWorkHistory(int index) throws DocumentException {
        SortedSet<AbstractCredential<?>> credentials = getProfile().getCurrentCredentials(CredentialType.WORK_HISTORY);
        PdfPTable table = pdfGenerator.createTable(FOUR_COLUMNS);

        addWorkHistoryHeading(table, index, WORK_HISTORY_TITLE_KEY);
        if (credentials.isEmpty()) {
            pdfGenerator.addCellAndCompleteRow(table, pdfGenerator.getValueNone());
        } else {
            addWorkHistory(table, credentials);
        }
        pdfGenerator.getDocument().add(table);
    }

    private InvestigatorProfile getProfile() {
        return pdfGenerator.getProfile();
    }

    private void addWorkHistoryHeading(PdfPTable table, int index, String positionHeadingKey) {
        pdfGenerator.addHeadings(table, index, positionHeadingKey, "pdf.work.history.position.heading",
                "pdf.specialty.credential.institution.heading", "pdf.specialty.credential.start_date.heading",
                "pdf.specialty.credential.end_date.heading");
    }

    private void addWorkHistory(PdfPTable table, SortedSet<AbstractCredential<?>> credentials) {
        for (AbstractCredential<?> credential : credentials) {
            addWorkHistory(table, (WorkHistory) credential);
        }
    }

    private void addWorkHistory(PdfPTable table, WorkHistory credential) {
        pdfGenerator.addValues(table, credential.getPosition(), credential.getIssuer().getName(),
                pdfGenerator.formatMonthAndYearDate(credential.getEffectiveDate()),
                pdfGenerator.formatMonthAndYearDate(credential.getExpirationDate()));
    }

    void addFellowships(int index) throws DocumentException {
        SortedSet<AbstractCredential<?>> credentials = getProfile().getCurrentCredentials(CredentialType.FELLOWSHIP);
        addSpecialtyCredentials(FELLOWSHIPS_TITLE_KEY, index, credentials);
    }

    void addResidencies(int index) throws DocumentException {
        SortedSet<AbstractCredential<?>> credentials = getProfile().getCurrentCredentials(CredentialType.RESIDENCY);
        addSpecialtyCredentials(RESIDENCIES_TITLE_KEY, index, credentials);
    }

    void addInternships(int index) throws DocumentException {
        SortedSet<AbstractCredential<?>> credentials = getProfile().getCurrentCredentials(CredentialType.INTERNSHIP);
        addSpecialtyCredentials(INTERNSHIPS_TITLE_KEY, index, credentials);
    }

    private void addSpecialtyCredentials(String headingKey, int index, SortedSet<AbstractCredential<?>> credentials)
            throws DocumentException {
        PdfPTable table = pdfGenerator.createTable(FOUR_COLUMNS);
        addSpecialtyCredentialsHeading(table, index, headingKey);
        if (credentials.isEmpty()) {
            pdfGenerator.addCellAndCompleteRow(table, pdfGenerator.getValueNone());
        } else {
            addSpecialtyCredentials(table, credentials);
        }
        pdfGenerator.getDocument().add(table);
    }

    void addDegrees(int index) throws DocumentException {
        PdfPTable table = pdfGenerator.createTable(THREE_COLUMNS);
        addDegreeHeadings(table, index, DEGREES_TITLE_KEY);
        SortedSet<AbstractCredential<?>> credentials = getProfile().getCurrentCredentials(CredentialType.DEGREE);
        if (credentials.isEmpty()) {
            pdfGenerator.addCellAndCompleteRow(table, pdfGenerator.getValueNone());
        } else {
            addDegrees(table, credentials);
        }
        pdfGenerator.getDocument().add(table);
    }

    private void addSpecialtyCredentials(PdfPTable table, SortedSet<AbstractCredential<?>> credentials) {
        for (AbstractCredential<?> credential : credentials) {
            addSpecialtyCredential(table, (SpecialtyCredential) credential);
        }
    }

    private void addSpecialtyCredentialsHeading(PdfPTable table, int index, String degreeHeadingKey) {
        pdfGenerator.addHeadings(table, index, degreeHeadingKey, "pdf.specialty.credential.type.heading",
                "pdf.specialty.credential.institution.heading", "pdf.specialty.credential.start_date.heading",
                "pdf.specialty.credential.end_date.heading");
    }

    private void addSpecialtyCredential(PdfPTable table, SpecialtyCredential credential) {
        pdfGenerator.addValues(table, credential.getSpecialty().getName(), credential.getIssuer().getName(),
                pdfGenerator.formatMonthAndYearDate(credential.getEffectiveDate()),
                pdfGenerator.formatMonthAndYearDate(credential.getExpirationDate()));
    }

    private void addDegrees(PdfPTable table, SortedSet<AbstractCredential<?>> credentials) {
        for (AbstractCredential<?> credential : credentials) {
            addDegree(table, (Degree) credential);
        }
    }

    private void addDegreeHeadings(PdfPTable table, int index, String degreeHeadingKey) {
        pdfGenerator.addHeadings(table, index, degreeHeadingKey, "pdf.degree.type.heading", "pdf.degree.year.heading",
                "pdf.degree.institution.heading");
    }

    private void addDegree(PdfPTable table, Degree degree) {
        pdfGenerator.addValues(table, degree.getDegreeType().getName(),
                pdfGenerator.formatYearDate(degree.getEffectiveDate()), degree.getIssuer().getName());
    }

    void addLicenses(int index) throws DocumentException {
        PdfPTable table = pdfGenerator.createTable(FOUR_COLUMNS);
        addLicenseHeadings(table, index, LICENSES_TITLE_KEY);
        SortedSet<AbstractCredential<?>> credentials = getProfile().getCurrentCredentials(
                CredentialType.MEDICAL_LICENSE);
        if (credentials.isEmpty()) {
            pdfGenerator.addCellAndCompleteRow(table, pdfGenerator.getValueNone());
        } else {
            addLicenses(table, credentials);
        }
        pdfGenerator.getDocument().add(table);
    }

    private void addLicenses(PdfPTable table, SortedSet<AbstractCredential<?>> credentials) {
        for (AbstractCredential<?> credential : credentials) {
            addLicense(table, (MedicalLicense) credential);
        }
    }

    private void addLicenseHeadings(PdfPTable table, int index, String licensesHeadingKey) {
        pdfGenerator.addHeadings(table, index, licensesHeadingKey, "pdf.license.type.heading",
                "pdf.license.number.heading", "pdf.license.state.heading", "pdf.license.expiration_date.heading");
    }

    private void addLicense(PdfPTable table, MedicalLicense license) {
        pdfGenerator.addValues(table, license.getLicenseType().getName(), license.getLicenseId(),
                license.getStateAndCountryString(), pdfGenerator.formatMonthAndYearDate(license.getExpirationDate()));
    }

    void addSpecialties(int index) throws DocumentException {
        PdfPTable table = pdfGenerator.createTable(FIVE_COLUMNS);
        addSpecialtyHeadings(table, index, SPECIALTIES_TITLE_KEY);
        SortedSet<AbstractCredential<?>> credentials = getProfile().getCurrentCredentials(CredentialType.SPECIALTY);
        if (credentials.isEmpty()) {
            pdfGenerator.addCellAndCompleteRow(table, pdfGenerator.getValueNone());
        } else {
            addSpecialties(table, credentials);
        }
        pdfGenerator.getDocument().add(table);
    }

    private void addSpecialtyHeadings(PdfPTable table, int index, String specialtyHeadingKey) {
        pdfGenerator.addHeadings(table, index, specialtyHeadingKey, "pdf.specialty.type.heading",
                "pdf.specialty.board.heading", "pdf.specialty.eligibility.heading",
                "pdf.specialty.effective_date.heading", "pdf.specialty.expiration_date.heading");
    }

    private void addSpecialties(PdfPTable table, SortedSet<AbstractCredential<?>> credentials) {
        for (AbstractCredential<?> credential : credentials) {
            addSpecialty(table, (BoardCertifiedSpecialty) credential);
        }
    }

    private void addSpecialty(PdfPTable table, BoardCertifiedSpecialty specialty) {
        pdfGenerator.addValues(table, specialty.getSpecialtyType().getDisplay(), specialty.getSpecialtyType()
                .getBoard().getName(), specialty.getStatus().getDisplay(),
                pdfGenerator.formatMonthAndYearDate(specialty.getEffectiveDate()),
                pdfGenerator.formatMonthAndYearDate(specialty.getExpirationDate()));
    }

    private void addAddressContent(Paragraph contents, Address address) {
        contents.add(pdfGenerator.getValueChunk(address.getStreetAddress()));
        if (StringUtils.isNotBlank(address.getDeliveryAddress())) {
            contents.add(Chunk.NEWLINE);
            contents.add(pdfGenerator.getValueChunk(address.getDeliveryAddress()));
        }
        contents.add(Chunk.NEWLINE);
        contents.add(pdfGenerator.getValueChunk(address.getCity()));
        if (StringUtils.isNotBlank(address.getStateOrProvince())) {
            contents.add(pdfGenerator.getValueChunk(", " + address.getStateOrProvince()));
        }
        if (StringUtils.isNotBlank(address.getPostalCode())) {
            contents.add(pdfGenerator.getValueChunk(SPACE_CHARACTER));
            contents.add(pdfGenerator.getValueChunk(address.getPostalCode()));
        }
        if (!FirebirdConstants.US_COUNTRY_CODE.equals(address.getCountry())) {
            contents.add(Chunk.NEWLINE);
            contents.add(pdfGenerator.getValueChunk(address.getCountry()));
        }
    }

    void addCredentialsHeader(Document document) throws DocumentException {
        document.add(pdfGenerator.createSectionTitle(CREDENTIALS_HEADER_KEY));
    }

    void addContactInformationHeader(Document document) throws DocumentException {
        document.add(pdfGenerator.createSectionTitle(CONTACT_INFORMATION_HEADER_KEY));
    }

    void addPrimaryOrganizationHeader(Document document) throws DocumentException {
        document.add(pdfGenerator.createSectionTitle(PRIMARY_ORGANIZATION_HEADER_KEY));
    }

    void addInstructionsHeader(Document document, String header) throws DocumentException {
        document.add(new Paragraph(header, AbstractPdfWriterGenerator.INSTRUCTIONS_HEADER_FONT));
    }

    void addInstructions(Document document, String instructions) throws DocumentException {
        document.add(new Paragraph(instructions, AbstractPdfWriterGenerator.INSTRUCTIONS_FONT));
    }

}
