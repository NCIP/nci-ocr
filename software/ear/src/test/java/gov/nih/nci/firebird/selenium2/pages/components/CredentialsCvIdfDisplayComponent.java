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
package gov.nih.nci.firebird.selenium2.pages.components;

import static gov.nih.nci.firebird.test.util.FirebirdPropertyUtils.getPropertyText;
import static gov.nih.nci.firebird.common.FirebirdDateUtils.getAsMonthAndYearOrEmptyString;
import static gov.nih.nci.firebird.commons.selenium2.util.TableUtils.getCells;
import gov.nih.nci.firebird.common.FirebirdDateUtils;
import gov.nih.nci.firebird.commons.selenium2.support.AbstractLoadableComponent;
import gov.nih.nci.firebird.commons.selenium2.util.WebElementUtils;
import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.BoardCertifiedSpecialty;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.Certification;
import gov.nih.nci.firebird.data.Degree;
import gov.nih.nci.firebird.data.Fellowship;
import gov.nih.nci.firebird.data.Internship;
import gov.nih.nci.firebird.data.MedicalLicense;
import gov.nih.nci.firebird.data.Residency;
import gov.nih.nci.firebird.data.SpecialtyCredential;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.data.WorkHistory;
import gov.nih.nci.firebird.selenium2.pages.base.TableListing;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Component to handle the corresponding Credentials sections displayed on the CV and IDF forms.
 */
public final class CredentialsCvIdfDisplayComponent extends AbstractLoadableComponent<CredentialsCvIdfDisplayComponent> {

    private static final String WORK_HISTORY_TABLE_ID = "workHistoryTable";
    private static final String DEGREE_TABLE_ID = "degreesTable";
    private static final String INTERNSHIP_TABLE_ID = "internshipsTable";
    private static final String RESIDENCIES_TABLE_ID = "residenciesTable";
    private static final String FELLOWSHIP_TABLE_ID = "fellowshipsTable";
    private static final String LICENSE_TABLE_ID = "licensesTable";
    private static final String SPECIALTY_TABLE_ID = "specialtiesTable";
    private static final String CERTIFICATE_TABLE_ID = "certificatesTable";
    private static final String CERTIFICATION_TABLE_ID = "certificationsTable";
    private static final String CLINICAL_RESEARCH_EXPERIENCE_SECTION_CSS = "#experienceSection .richText";

    private final DataTable<WorkHistoryListing> workHistoryTable;
    private final DataTable<DegreeListing> degreeTable;
    private final DataTable<InternshipListing> internshipTable;
    private final DataTable<ResidencyListing> residencyTable;
    private final DataTable<FellowshipListing> fellowshipTable;
    private final DataTable<LicenseListing> licenseTable;
    private final DataTable<SpecialtyListing> specialtyTable;
    private final DataTable<CertificateListing> certificateTable;
    private final DataTable<CertificationListing> certificationTable;

    @FindBy(css = CLINICAL_RESEARCH_EXPERIENCE_SECTION_CSS)
    private WebElement researchExperienceText;

    private final CredentialsCvIdfDisplayComponentHelper helper = new CredentialsCvIdfDisplayComponentHelper(this);

    public CredentialsCvIdfDisplayComponent(WebDriver driver) {
        super(driver);
        workHistoryTable = new DataTable<WorkHistoryListing>(driver, WORK_HISTORY_TABLE_ID, WorkHistoryListing.class,
                this);
        degreeTable = new DataTable<DegreeListing>(driver, DEGREE_TABLE_ID, DegreeListing.class, this);
        internshipTable = new DataTable<InternshipListing>(driver, INTERNSHIP_TABLE_ID, InternshipListing.class, this);
        residencyTable = new DataTable<ResidencyListing>(driver, RESIDENCIES_TABLE_ID, ResidencyListing.class, this);
        fellowshipTable = new DataTable<FellowshipListing>(driver, FELLOWSHIP_TABLE_ID, FellowshipListing.class, this);
        licenseTable = new DataTable<LicenseListing>(driver, LICENSE_TABLE_ID, LicenseListing.class, this);
        specialtyTable = new DataTable<SpecialtyListing>(driver, SPECIALTY_TABLE_ID, SpecialtyListing.class, this);
        certificateTable = new DataTable<CertificateListing>(driver, CERTIFICATE_TABLE_ID, CertificateListing.class,
                this);
        certificationTable = new DataTable<CertificationListing>(driver, CERTIFICATION_TABLE_ID,
                CertificationListing.class, this);
    }

    @Override
    protected void assertLoaded() {
        assertElementsWithIdsPresent(WORK_HISTORY_TABLE_ID, DEGREE_TABLE_ID, INTERNSHIP_TABLE_ID, RESIDENCIES_TABLE_ID,
                FELLOWSHIP_TABLE_ID, LICENSE_TABLE_ID, SPECIALTY_TABLE_ID, CERTIFICATE_TABLE_ID);
    }

    public CredentialsCvIdfDisplayComponentHelper getHelper() {
        return helper;
    }

    public List<WorkHistoryListing> getWorkHistoryListings() {
        return workHistoryTable.getListings();
    }

    public List<DegreeListing> getDegreeListings() {
        return degreeTable.getListings();
    }

    public List<InternshipListing> getInternshipListings() {
        return internshipTable.getListings();
    }

    public List<ResidencyListing> getResidencyListings() {
        return residencyTable.getListings();
    }

    public List<FellowshipListing> getFellowshipListings() {
        return fellowshipTable.getListings();
    }

    public List<LicenseListing> getLicenseListings() {
        return licenseTable.getListings();
    }

    public List<SpecialtyListing> getSpecialtyListings() {
        return specialtyTable.getListings();
    }

    public List<CertificateListing> getCertificateListings() {
        return certificateTable.getListings();
    }

    public List<CertificationListing> getCertificationListings() {
        return certificationTable.getListings();
    }

    public String getReseachExperience() {
        return researchExperienceText.getText();
    }

    public interface CredentialListing<C extends AbstractCredential<?>> {
        boolean isEquivalent(C credential);
    }

    public class WorkHistoryListing implements TableListing, CredentialListing<WorkHistory> {

        private static final int POSITION_COLUMN = 0;
        private static final int ISSUER_COLUMN = 1;
        private static final int START_DATE_COLUMN = 2;
        private static final int END_DATE_COLUMN = 3;

        private final Long id;
        private final String position;
        private final String issuer;
        private final String startDate;
        private final String endDate;

        public WorkHistoryListing(WebElement row) {
            id = Long.valueOf(WebElementUtils.getId(row));
            List<WebElement> cells = getCells(row);
            position = cells.get(POSITION_COLUMN).getText();
            issuer = cells.get(ISSUER_COLUMN).getText();
            startDate = cells.get(START_DATE_COLUMN).getText();
            endDate = cells.get(END_DATE_COLUMN).getText();
        }

        @Override
        public Long getId() {
            return id;
        }

        public String getPosition() {
            return position;
        }

        public String getIssuer() {
            return issuer;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public boolean isEquivalent(WorkHistory history) {
            return new EqualsBuilder().append(history.getId(), getId())
                    .append(history.getPosition(), getPosition())
                    .append(getAsMonthAndYearOrEmptyString(history.getEffectiveDate()), getStartDate())
                    .append(getAsMonthAndYearOrEmptyString(history.getExpirationDate()), getEndDate())
                    .append(history.getIssuer().getName(), getIssuer())
                    .isEquals();
        }
    }

    public class DegreeListing implements TableListing, CredentialListing<Degree> {

        private static final int DEGREE_COLUMN = 0;
        private static final int DATE_GRANTED_COLUMN = 1;
        private static final int INSTITUTION_COLUMN = 2;

        private final Long id;
        private final String degree;
        private final String institution;
        private final String dateGranted;

        public DegreeListing(WebElement row) {
            id = Long.valueOf(WebElementUtils.getId(row));
            List<WebElement> cells = getCells(row);
            degree = cells.get(DEGREE_COLUMN).getText();
            institution = cells.get(INSTITUTION_COLUMN).getText();
            dateGranted = cells.get(DATE_GRANTED_COLUMN).getText();
        }

        @Override
        public Long getId() {
            return id;
        }

        public String getDegree() {
            return degree;
        }

        public String getInstitutionInfo() {
            return institution;
        }

        public String getDateGranted() {
            return dateGranted;
        }

        public boolean isEquivalent(Degree degree) {
            return new EqualsBuilder().append(getDegree(), degree.getDegreeType().getName())
                    .append(getInstitutionInfo(), degree.getIssuer().getName())
                    .append(getDateGranted(), FirebirdDateUtils.getAsYearOrEmptyString(degree.getEffectiveDate()))
                    .isEquals();
        }

    }

    public class LicenseListing implements TableListing, CredentialListing<MedicalLicense> {

        private static final int TYPE_COLUMN = 0;
        private static final int NUMBER_COLUMN = 1;
        private static final int LOCATION_COLUMN = 2;
        private static final int EXPIRATION_DATE_COLUMN = 3;

        private final Long id;
        private final String type;
        private final String number;
        private final String location;
        private final String expirationDate;

        public LicenseListing(WebElement row) {
            id = Long.valueOf(WebElementUtils.getId(row));
            List<WebElement> cells = getCells(row);
            type = cells.get(TYPE_COLUMN).getText();
            number = cells.get(NUMBER_COLUMN).getText();
            location = cells.get(LOCATION_COLUMN).getText();
            expirationDate = cells.get(EXPIRATION_DATE_COLUMN).getText();
        }

        @Override
        public Long getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getNumber() {
            return number;
        }

        public String getLocation() {
            return location;
        }

        public String getExpirationDate() {
            return expirationDate;
        }

        public boolean isEquivalent(MedicalLicense license) {
            return new EqualsBuilder().append(getType(), license.getLicenseType().getName())
                    .append(getNumber(), license.getLicenseId())
                    .append(getLocation(), license.getStateAndCountryString())
                    .append(getExpirationDate(), getAsMonthAndYearOrEmptyString(license.getExpirationDate()))
                    .isEquals();
        }

    }

    public class SpecialtyCredentialListing<C extends AbstractCredential<?> & SpecialtyCredential> implements
            TableListing, CredentialListing<C> {
        private final Long id;
        private final String specialty;
        private final String location;
        private final String startDate;
        private final String endDate;
        private String certifyingBoard;

        public SpecialtyCredentialListing(WebElement row) {
            this.id = Long.valueOf(WebElementUtils.getId(row));
            int i = 0;
            List<WebElement> cells = getCells(row);

            if (cells.size() == 5) {
                this.certifyingBoard = cells.get(i++).getText();
            }

            this.specialty = cells.get(i++).getText();
            this.location = cells.get(i++).getText();
            this.startDate = cells.get(i++).getText();
            this.endDate = cells.get(i++).getText();
        }

        @Override
        public Long getId() {
            return id;
        }

        public String getSpecialty() {
            return specialty;
        }

        public String getLocation() {
            return location;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getCertifyingBoard() {
            return certifyingBoard;
        }

        public boolean isEquivalent(C specialtyCredential) {

            EqualsBuilder builder = new EqualsBuilder()
                    .append(getSpecialty(), specialtyCredential.getSpecialty().getName())
                    .append(getStartDate(), getAsMonthAndYearOrEmptyString(specialtyCredential.getEffectiveDate()))
                    .append(getEndDate(), getAsMonthAndYearOrEmptyString(specialtyCredential.getExpirationDate()));

            if (specialtyCredential instanceof Internship) {
                builder.append(getCertifyingBoard(), specialtyCredential.getSpecialty().getCertifyingBoard().getName());
            }

            return builder.isEquals();
        }
    }

    public class InternshipListing extends SpecialtyCredentialListing<Internship> {
        public InternshipListing(WebElement row) {
            super(row);
        }
    }

    public class ResidencyListing extends SpecialtyCredentialListing<Residency> {
        public ResidencyListing(WebElement row) {
            super(row);
        }
    }

    public class FellowshipListing extends SpecialtyCredentialListing<Fellowship> {
        public FellowshipListing(WebElement row) {
            super(row);
        }
    }

    public class SpecialtyListing implements TableListing, CredentialListing<BoardCertifiedSpecialty> {

        private static final int BOARD_COLUMN = 0;
        private static final int SPECIALTY_COLUMN = 1;
        private static final int ELIGIBILITY_COLUMN = 2;
        private static final int EFFECTIVE_DATE_COLUMN = 3;
        private static final int EXPIRATION_DATE_COLUMN = 4;

        private final Long id;
        private final String board;
        private final String specialty;
        private final String eligibility;
        private final String effectiveDate;
        private final String expirationDate;

        public SpecialtyListing(WebElement row) {
            id = Long.valueOf(WebElementUtils.getId(row));
            List<WebElement> cells = getCells(row);
            board = cells.get(BOARD_COLUMN).getText();
            specialty = cells.get(SPECIALTY_COLUMN).getText();
            eligibility = cells.get(ELIGIBILITY_COLUMN).getText();
            effectiveDate = cells.get(EFFECTIVE_DATE_COLUMN).getText();
            expirationDate = cells.get(EXPIRATION_DATE_COLUMN).getText();
        }

        @Override
        public Long getId() {
            return id;
        }

        public String getBoard() {
            return board;
        }

        public String getSpecialty() {
            return specialty;
        }

        public String getEligibility() {
            return eligibility;
        }

        public String getEffectiveDate() {
            return effectiveDate;
        }

        public String getExpirationDate() {
            return expirationDate;
        }

        public boolean isEquivalent(BoardCertifiedSpecialty specialty) {
            return new EqualsBuilder()
                    .append(getBoard(), specialty.getSpecialtyType().getBoard().getName())
                    .append(getSpecialty(), specialty.getSpecialtyType().getDisplay())
                    .append(getEligibility(), specialty.getStatus().getDisplay())
                    .append(getEffectiveDate(), getAsMonthAndYearOrEmptyString(specialty.getEffectiveDate()))
                    .append(getExpirationDate(), getAsMonthAndYearOrEmptyString(specialty.getExpirationDate()))
                    .isEquals();
        }

    }

    public class CertificateListing implements TableListing, CredentialListing<TrainingCertificate> {

        private final Long id;
        private final String certificate;
        private final String organizationName;
        private final String effectiveDate;
        private final String expirationDate;

        public CertificateListing(WebElement row) {
            id = Long.valueOf(WebElementUtils.getId(row));
            List<WebElement> cells = getCells(row);
            int i = 0;
            if (cells.size() == 3) {
                certificate = getPropertyText(CertificateType.HUMAN_RESEARCH_CERTIFICATE.getNameProperty());
            } else {
                certificate = cells.get(i++).getText();
            }
            organizationName = cells.get(i++).getText();
            effectiveDate = cells.get(i++).getText();
            expirationDate = cells.get(i++).getText();
        }

        @Override
        public Long getId() {
            return id;
        }

        public String getCertificate() {
            return certificate;
        }

        public String getEffectiveDate() {
            return effectiveDate;
        }

        public String getExpirationDate() {
            return expirationDate;
        }

        public String getOrganizationName() {
            return organizationName;
        }

        @Override
        public boolean isEquivalent(TrainingCertificate credential) {
            return new EqualsBuilder()
                    .append(getPropertyText(credential.getCertificateType().getNameProperty()), getCertificate())
                    .append(getAsMonthAndYearOrEmptyString(credential.getEffectiveDate()), getEffectiveDate())
                    .append(getAsMonthAndYearOrEmptyString(credential.getExpirationDate()), getExpirationDate())
                    .append(credential.getIssuer().getName(), getOrganizationName()).isEquals();
        }
    }

    public class CertificationListing implements TableListing, CredentialListing<Certification> {

        private static final int CERTIFICATION_COLUMN = 0;
        private static final int EFFECTIVE_DATE_COLUMN = 1;
        private static final int EXPIRATION_DATE_COLUMN = 2;

        private final Long id;
        private final String certification;
        private final String effectiveDate;
        private final String expirationDate;

        public CertificationListing(WebElement row) {
            id = Long.valueOf(WebElementUtils.getId(row));
            List<WebElement> cells = getCells(row);
            certification = cells.get(CERTIFICATION_COLUMN).getText();
            effectiveDate = cells.get(EFFECTIVE_DATE_COLUMN).getText();
            expirationDate = cells.get(EXPIRATION_DATE_COLUMN).getText();
        }

        @Override
        public Long getId() {
            return id;
        }

        public String getCertification() {
            return certification;
        }

        public String getEffectiveDate() {
            return effectiveDate;
        }

        public String getExpirationDate() {
            return expirationDate;
        }

        @Override
        public boolean isEquivalent(Certification credential) {
            return new EqualsBuilder()
                    .append(credential.getCertificationType().getName(), getCertification())
                    .append(getAsMonthAndYearOrEmptyString(credential.getEffectiveDate()), getEffectiveDate())
                    .append(getAsMonthAndYearOrEmptyString(credential.getExpirationDate()), getExpirationDate())
                    .isEquals();
        }
    }

}
