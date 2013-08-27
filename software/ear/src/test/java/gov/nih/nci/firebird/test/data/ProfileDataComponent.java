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
package gov.nih.nci.firebird.test.data;

import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.Degree;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.PrimaryOrganizationType;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.ValueGenerator;
import gov.nih.nci.firebird.test.nes.NesTestDataSource;
import gov.nih.nci.firebird.test.nes.TargetGridResources;

import java.util.List;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;

public class ProfileDataComponent extends AbstractDataComponent {

    private final TargetGridResources gridResources;
    private final CredentialTypesDataComponent credentialTypesData;
    private InvestigatorProfile profile;

    public ProfileDataComponent(AbstractTestDataSet dataSet,
            TargetGridResources gridResources,
            CredentialTypesDataComponent credentialTypesData) {
        super(dataSet);
        this.gridResources = gridResources;
        this.credentialTypesData = credentialTypesData;
    }

    @Override
    void configureData() throws Exception {
        profile = new InvestigatorProfile();
        profile.setPerson(getNesPerson());
        profile.setPrimaryOrganization(new PrimaryOrganization(getNesOrganization(),
                PrimaryOrganizationType.HEALTH_CARE_FACILITY));
    }

    public void setUpCompleteProfile() throws AssociationAlreadyExistsException, CredentialAlreadyExistsException {
        setUpAssociatedOrganizations();
        setUpCredentialsWithMd();
    }

    public void setUpCompleteProfileNoMd() throws AssociationAlreadyExistsException, CredentialAlreadyExistsException {
        setUpAssociatedOrganizations();
        setUpCredentialsNoMd();
    }

    private void setUpAssociatedOrganizations() throws AssociationAlreadyExistsException {
        OrganizationAssociation association = profile.addOrganizationAssociation(setUpPracticeSiteWithOhrp(), OrganizationRoleType.PRACTICE_SITE);
        ((PracticeSite) association.getOrganizationRole()).setType(PracticeSiteType.CLINICAL_CENTER);
        association = profile.addOrganizationAssociation(setUpPracticeSiteWithOhrp(), OrganizationRoleType.PRACTICE_SITE);
        ((PracticeSite) association.getOrganizationRole()).setType(PracticeSiteType.CANCER_CENTER);
        profile.addOrganizationAssociation(getNesDataSource().getClinicalLab().getOrganization(), OrganizationRoleType.CLINICAL_LABORATORY);
        profile.addOrganizationAssociation(getNesDataSource().getClinicalLab().getOrganization(), OrganizationRoleType.CLINICAL_LABORATORY);
        profile.addOrganizationAssociation(getNesDataSource().getIrb().getOrganization(), OrganizationRoleType.IRB);
        profile.addOrganizationAssociation(getNesDataSource().getIrb().getOrganization(), OrganizationRoleType.IRB);
    }

    private Organization setUpPracticeSiteWithOhrp() {
        PracticeSite practiceSite = getNesDataSource().getPracticeSite();
        practiceSite.setOhrpAssuranceNumber(ValueGenerator.getUniqueOhrpNumber());
        return practiceSite.getOrganization();
    }

    public void setUpCredentialsWithMd() throws CredentialAlreadyExistsException {
        Degree degree = CredentialFactory.getInstance().createDegree(credentialTypesData.getMdEquivalentDegreeType());
        degree.setIssuer(getNesOrganization());
        profile.addCredential(degree);
        setUpCredentials();
    }

    private void setUpCredentialsNoMd() throws CredentialAlreadyExistsException {
        Degree degree = CredentialFactory.getInstance().createDegree(credentialTypesData.getNonMdEquivalentDegreeType());
        degree.setIssuer(getNesOrganization());
        profile.addCredential(degree);
        setUpCredentials();
    }

    private void setUpCredentials() throws CredentialAlreadyExistsException {
        profile.addCredential(CredentialFactory.getInstance().createLicense(credentialTypesData.getLicenseTypes().get(0)));
        profile.addCredential(CredentialFactory.getInstance().createSpecialty(credentialTypesData.getSpecialtyTypes().get(0)));
        profile.addCredential(CredentialFactory.getInstance().createCertification(credentialTypesData.getCertificationTypes().get(0)));
        TrainingCertificate certificate = CredentialFactory.getInstance().createCertificate(CertificateType.HUMAN_RESEARCH_CERTIFICATE);
        profile.addCredential(certificate);
    }

    private NesTestDataSource getNesDataSource() {
        return gridResources.getNesTestDataSource();
    }

    private Organization getNesOrganization() {
        return getNesDataSource().getOrganization();
    }

    private Person getNesPerson() {
        return getNesDataSource().getPerson();
    }

    public InvestigatorProfile getProfile() {
        return profile;
    }

    @Override
    protected void addObjectsToSave(List<PersistentObject> objectsToSave) {
        objectsToSave.add(getProfile());
    }

    @Override
    public void reload() {
        profile = reload(profile);
        initializeRegistrations();
    }

    private void initializeRegistrations() {
        for (AbstractProtocolRegistration  protocolRegistration : profile.getCurrentProtocolRegistrations()) {
            AbstractProtocolRegistrationDataComponent.intialize(protocolRegistration);
        }
        for (AnnualRegistration annualRegistration : profile.getAnnualRegistrations()) {
            AnnualRegistrationDataComponent.initialize(annualRegistration);
        }
    }

    public void configureWithCredentials() throws CredentialAlreadyExistsException {
        profile.addCredential(CredentialFactory.getInstance().createCertificate());
    }

}
