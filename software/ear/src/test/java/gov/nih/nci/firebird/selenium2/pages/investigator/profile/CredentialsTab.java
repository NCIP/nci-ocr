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
package gov.nih.nci.firebird.selenium2.pages.investigator.profile;

import gov.nih.nci.firebird.selenium2.pages.components.AbstractTab;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * investigator/profile/credentials/ajax/credentials_tab.jsp
 */
public class CredentialsTab extends AbstractTab<CredentialsTab> {

    public static final String TAB_ID = "credentialsTab";
    private static final String LICENSES_SECTION_HEADER_SELECTOR = "#licensesSection h3";
    private static final String DEGREES_SECTION_HEADER_SELECTOR = "#degreesSection h3";
    private static final String SPECIALTIES_SECTION_HEADER_SELECTOR = "#specialtiesSection h3";
    private static final String CERTIFICATIONS_SECTION_HEADER_SELECTOR = "#certificationsSection h3";
    private static final String CERTIFICATES_SECTION_HEADER_SELECTOR = "#certificatesSection h3";
    private static final String EXPERIENCE_SECTION_HEADER_SELECTOR = "#experienceSection h3";
    private static final String INTERNSHIP_SECTION_HEADER_SELECTOR = "#internshipsSection h3";
    private static final String FELLOWSHIP_SECTION_HEADER_SELECTOR = "#fellowshipsSection h3";
    private static final String RESIDENCY_SECTION_HEADER_SELECTOR = "#residenciesSection h3";
    private static final String WORK_HISTORY_SECTION_HEADER_SELECTOR = "#workHistorySection h3";

    @FindBy(css = LICENSES_SECTION_HEADER_SELECTOR)
    private WebElement licensesSectionHeader;
    @FindBy(css = DEGREES_SECTION_HEADER_SELECTOR)
    private WebElement degreesSectionHeader;
    @FindBy(css = SPECIALTIES_SECTION_HEADER_SELECTOR)
    private WebElement specialtiesSectionHeader;
    @FindBy(css = CERTIFICATIONS_SECTION_HEADER_SELECTOR)
    private WebElement certificationsSectionHeader;
    @FindBy(css = CERTIFICATES_SECTION_HEADER_SELECTOR)
    private WebElement certificatesSectionHeader;
    @FindBy(css = EXPERIENCE_SECTION_HEADER_SELECTOR)
    private WebElement experienceSectionHeader;
    @FindBy(css = INTERNSHIP_SECTION_HEADER_SELECTOR)
    private WebElement internshipSectionHeader;
    @FindBy(css = FELLOWSHIP_SECTION_HEADER_SELECTOR)
    private WebElement fellowshipSectionHeader;
    @FindBy(css = RESIDENCY_SECTION_HEADER_SELECTOR)
    private WebElement residencySectionHeader;
    @FindBy(css = WORK_HISTORY_SECTION_HEADER_SELECTOR)
    private WebElement workHistorySectionHeader;

    private final DegreeSection degreeSection;
    private final LicenseSection licenseSection;
    private final SpecialtySection specialtySection;
    private final CertificationSection certificationSection;
    private final TrainingCertificateSection trainingSection;
    private final ExperienceSection experienceSection;
    private final InternshipSection internshipSection;
    private final FellowshipSection fellowshipSection;
    private final ResidencySection residencySection;
    private final WorkHistorySection workHistorySection;
    private final InvestigatorProfilePage page;

    public CredentialsTab(WebDriver driver, InvestigatorProfilePage page) {
        super(driver);
        this.page = page;
        degreeSection = new DegreeSection(driver);
        licenseSection = new LicenseSection(driver);
        specialtySection = new SpecialtySection(driver);
        certificationSection = new CertificationSection(driver);
        trainingSection = new TrainingCertificateSection(driver);
        experienceSection = new ExperienceSection(driver);
        internshipSection = new InternshipSection(getDriver());
        fellowshipSection = new FellowshipSection(getDriver());
        residencySection = new ResidencySection(getDriver());
        workHistorySection = new WorkHistorySection(getDriver());
    }

    public DegreeSection getDegreeSection() {
        degreesSectionHeader.click();
        return degreeSection.waitUntilReady();
    }

    public LicenseSection getLicenseSection() {
        licensesSectionHeader.click();
        return licenseSection.waitUntilReady();
    }

    public SpecialtySection getSpecialtySection() {
        specialtiesSectionHeader.click();
        return specialtySection.waitUntilReady();
    }

    public CertificationSection getCertificationSection() {
        certificationsSectionHeader.click();
        return certificationSection.waitUntilReady();
    }

    public TrainingCertificateSection getTrainingSection() {
        certificatesSectionHeader.click();
        return trainingSection.waitUntilReady();
    }

    public ExperienceSection getExperienceSection() {
        experienceSectionHeader.click();
        return experienceSection.waitUntilReady();
    }

    public InternshipSection getInternshipSection() {
        internshipSectionHeader.click();
        return internshipSection.waitUntilReady();
    }

    public FellowshipSection getFellowshipSection() {
        fellowshipSectionHeader.click();
        return fellowshipSection.waitUntilReady();
    }

    public ResidencySection getResidencySection() {
        residencySectionHeader.click();
        return residencySection.waitUntilReady();
    }

    public WorkHistorySection getWorkHistorySection() {
        workHistorySectionHeader.click();
        return workHistorySection.waitUntilReady();
    }

    @Override
    public InvestigatorProfilePage getPage() {
        return page;
    }

    @Override
    protected String getTabId() {
        return TAB_ID;
    }

}
