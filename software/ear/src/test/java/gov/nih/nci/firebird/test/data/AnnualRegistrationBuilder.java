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

import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.AnnualRegistrationConfiguration;
import gov.nih.nci.firebird.data.AnnualRegistrationType;
import gov.nih.nci.firebird.data.ClinicalLaboratory;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.LaboratoryCertificate;
import gov.nih.nci.firebird.data.LaboratoryCertificateType;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import gov.nih.nci.firebird.test.nes.TargetGridResources;

import java.util.Date;
import java.util.EnumSet;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Hibernate;

import com.google.common.collect.Iterables;

public class AnnualRegistrationBuilder extends AbstractDataComponentBuilder<AnnualRegistration> {

    private static final EnumSet<RegistrationStatus> SUBMITTED_STATUSES = EnumSet.of(RegistrationStatus.SUBMITTED,
            RegistrationStatus.ACCEPTED,
            RegistrationStatus.APPROVED,
            RegistrationStatus.IN_REVIEW,
            RegistrationStatus.RETURNED,
            RegistrationStatus.REVIEW_ON_HOLD);

    private AnnualRegistration registration = new AnnualRegistration();
    private final InvestigatorProfile profile;

    protected AnnualRegistrationBuilder(TargetGridResources gridResources, TestDataLoader dataLoader,
            FirebirdUser investigator, AnnualRegistrationConfiguration configuration) {
        super(gridResources, dataLoader);
        this.profile = Iterables.getFirst(investigator.getActiveProfiles(), null);
        registration.setProfile(profile);
        registration.setConfiguration(configuration);
        registration.configureForms();
        withStatus(RegistrationStatus.IN_PROGRESS);
        withType(AnnualRegistrationType.INITIAL);
        withStatusDate(new Date());
    }

    public AnnualRegistrationBuilder withType(AnnualRegistrationType type) {
        registration.setAnnualRegistrationType(type);
        return this;
    }

    public AnnualRegistrationBuilder withStatus(RegistrationStatus status) {
        registration.setStatus(status);
        if (SUBMITTED_STATUSES.contains(status)) {
            withRenewalDate(DateUtils.addYears(new Date(), 1));
        }
        return this;
    }

    public AnnualRegistrationBuilder withStatusDate(Date statusDate) {
        registration.setStatusDate(statusDate);
        return this;
    }

    public AnnualRegistrationBuilder withRenewalDate(Date renewalDate) {
        registration.setRenewalDate(renewalDate);
        return this;
    }

    public AnnualRegistrationBuilder withDueDate(Date dueDate) {
        registration.setDueDate(dueDate);
        return this;
    }

    public AnnualRegistrationBuilder withApprovalAcknowledged() {
        withApprovalAcknowledgedByInvestigator();
        withApprovalAcknowledgedByCoordinator();
        return this;
    }

    public AnnualRegistrationBuilder withApprovalAcknowledgedByInvestigator() {
        registration.setApprovalAcknowledgedByInvestigator(true);
        return this;
    }

    public AnnualRegistrationBuilder withApprovalAcknowledgedByCoordinator() {
        registration.setApprovalAcknowledgedByCoordinator(true);
        return this;
    }

    public AnnualRegistrationBuilder renewalOf(AnnualRegistration renewedRegistration) {
        withStatus(RegistrationStatus.IN_PROGRESS);
        withType(AnnualRegistrationType.RENEWAL);
        renewedRegistration.setRenewal(registration);
        return this;
    }

    public AnnualRegistrationBuilder complete() {
        registration.getForm1572().setPhaseOne(true);
        registration.getForm1572().getPracticeSites()
                .add(getOrganizationWithRoleType(OrganizationRoleType.PRACTICE_SITE));
        Organization clinicalLabOrg = getOrganizationWithRoleType(OrganizationRoleType.CLINICAL_LABORATORY);
        ClinicalLaboratory clinicalLab = (ClinicalLaboratory) clinicalLabOrg
                .getRole(OrganizationRoleType.CLINICAL_LABORATORY);
        LaboratoryCertificate cert = new LaboratoryCertificate(LaboratoryCertificateType.CAP);
        cert.setEffectiveDate(DateUtils.addYears(new Date(), -1));
        cert.setExpirationDate(DateUtils.addYears(new Date(), 1));
        cert.setCertificateFile(FirebirdFileFactory.getInstance().create());
        clinicalLab.addCertificate(cert);
        registration.getForm1572().getLabs().add(clinicalLabOrg);
        registration.getForm1572().getIrbs().add(getOrganizationWithRoleType(OrganizationRoleType.IRB));
        registration.getFinancialDisclosure().setEquityInSponsor(false);
        registration.getFinancialDisclosure().setFinancialInterest(false);
        registration.getFinancialDisclosure().setMonetaryGain(false);
        registration.getFinancialDisclosure().setOtherSponsorPayments(false);
        return this;
    }

    private Organization getOrganizationWithRoleType(OrganizationRoleType roleType) {
        return profile.getOrganizationAssociations(roleType).iterator().next().getOrganizationRole().getOrganization();
    }

    @Override
    AnnualRegistration getObject() {
        return registration;
    }

    static void initialize(AnnualRegistration registration) {
        Hibernate.initialize(registration);
        if (registration.getAdditionalAttachmentsForm() != null) {
            Hibernate.initialize(registration.getAdditionalAttachmentsForm().getAdditionalAttachments());
        }
        if (registration.getFinancialDisclosure() != null) {
            Hibernate.initialize(registration.getFinancialDisclosure().getSupportingDocumentation());
        }
        if (registration.getForm1572() != null) {
            Hibernate.initialize(registration.getForm1572().getClinicalLabCertificates());
        }
        Hibernate.initialize(registration.getSupplementalInvestigatorDataForm());
        AnnualRegistrationConfigurationBuilder.initialize(registration.getConfiguration());
    }

    @Override
    void setObject(AnnualRegistration registration) {
        this.registration = registration;
    }

    @Override
    protected void build() {
        super.build();
        if (SUBMITTED_STATUSES.contains(registration.getStatus())) {
            addPdfFilesToForms();
        }
    }

    private void addPdfFilesToForms() {
        for (AbstractRegistrationForm form : registration.getForms()) {
            form.submitForm();
            form.setFlattenedPdf(FirebirdFileFactory.getInstance().create());
            form.setSignedPdf(FirebirdFileFactory.getInstance().create());
        }
    }


}
