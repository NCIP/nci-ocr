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
package gov.nih.nci.firebird.data;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.test.CredentialFactory;
import gov.nih.nci.firebird.test.GuiceTestRunner;
import gov.nih.nci.firebird.test.InvestigatorProfileFactory;
import gov.nih.nci.firebird.test.OrganizationFactory;
import gov.nih.nci.firebird.test.RegistrationFactory;

import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(GuiceTestRunner.class)
public class ProtocolForm1572ValidatorTest extends AbstractRegistrationFormTest {
    private ProtocolForm1572 form;
    private InvestigatorRegistration registration;
    private ProtocolForm1572Validator validator;
    @Inject
    private ResourceBundle resources;

    @Before
    public void setUp() {
        super.setUp();
        registration = RegistrationFactory.getInstance().createInvestigatorRegistration();
        form = registration.getForm1572();
        validator = new ProtocolForm1572Validator(form);
    }

    @Test
    public void testValidate_EmptyFormNoMd() throws Exception {
        ValidationResult result = new ValidationResult();
        validator.validate(result, getResources());
        assertFalse(result.isValid());
        assertEquals(3, result.getFailures().size());
        assertTrue(hasError(result, getErrorMessage("validation.failure.missing.practice.site")));
        assertTrue(hasError(result, getErrorMessage("validation.failure.missing.institutional.review.board")));
        assertTrue(hasError(result, getErrorMessage("validation.failure.missing.clinical.laboratory")));
        assertTrue(form.getInvalidEntityIds().isEmpty());
    }

    @Test
    public void testValidate_EmptyFormWithMd() throws Exception {
        ValidationResult result = new ValidationResult();
        registration.getProfile().addCredential(CredentialFactory.getInstance().createDegree("M.D.", true));
        validator.validate(result, getResources());
        assertFalse(result.isValid());
        assertEquals(3, result.getFailures().size());
        assertTrue(hasError(result, getErrorMessage("validation.failure.missing.practice.site")));
        assertTrue(hasError(result, getErrorMessage("validation.failure.missing.institutional.review.board")));
        assertTrue(hasError(result, getErrorMessage("validation.failure.missing.clinical.laboratory")));
        assertTrue(form.getInvalidEntityIds().isEmpty());
    }

    @Test
    public void testValidate_UnCurated() throws Exception {
        ValidationResult result = new ValidationResult();
        registration.getProfile().addCredential(CredentialFactory.getInstance().createDegree("M.D.", true));
        ClinicalLaboratory lab = (ClinicalLaboratory) registration.getProfile()
                .addOrganizationAssociation(createUncuratedOrganization(), OrganizationRoleType.CLINICAL_LABORATORY)
                .getOrganizationRole();
        InstitutionalReviewBoard irb = (InstitutionalReviewBoard) registration.getProfile()
                .addOrganizationAssociation(createUncuratedOrganization(), OrganizationRoleType.IRB).getOrganizationRole();
        PracticeSite site = (PracticeSite) registration.getProfile()
                .addOrganizationAssociation(createUncuratedOrganization(), OrganizationRoleType.PRACTICE_SITE)
                .getOrganizationRole();
        form.getPracticeSites().add(site.getOrganization());
        form.getLabs().add(lab.getOrganization());
        form.getIrbs().add(irb.getOrganization());
        registration.getSubinvestigatorRegistrations().add(
                RegistrationFactory.getInstance().createSubinvestigatorRegistration(registration));
        validator.validate(result, getResources());
        assertEquals(4, result.getFailures().size());
        assertTrue(hasError(
                result,
                getErrorMessage("validation.failure.uncurated", irb.getOrganization().getName() + " role as "
                        + irb.getRoleType().getDisplay(), form.getFormType().getName(), irb.getCurationStatus().getDisplay())));
        assertTrue(hasError(
                result,
                getErrorMessage("validation.failure.uncurated", lab.getOrganization().getName() + " role as "
                        + lab.getRoleType().getDisplay(), form.getFormType().getName(), lab.getCurationStatus().getDisplay())));
        assertTrue(hasError(
                result,
                getErrorMessage("validation.failure.uncurated", site.getOrganization().getName() + " role as "
                        + site.getRoleType().getDisplay(), form.getFormType().getName(), site.getCurationStatus()
                        .getDisplay())));
        assertTrue(hasError(result, getErrorMessage("validation.failure.missing.clinical.laboratory.certificate")));
        assertEquals(3, form.getInvalidEntityIds().size());
        assertTrue(form.getInvalidEntityIds().contains(site.getOrganization().getId()));
        assertTrue(form.getInvalidEntityIds().contains(irb.getOrganization().getId()));
        assertTrue(form.getInvalidEntityIds().contains(lab.getOrganization().getId()));
    }

    private Organization createUncuratedOrganization() {
        Organization organization = OrganizationFactory.getInstanceWithId().create();
        organization.setCurationStatus(CurationStatus.PENDING);
        return organization;
    }

    @Test
    public void testValidate_Success() throws Exception {
        ValidationResult result = new ValidationResult();
        registration.getProfile().addCredential(CredentialFactory.getInstance().createDegree("M.D.", true));
        Organization organization = OrganizationFactory.getInstance().create();
        organization.setCurationStatus(CurationStatus.ACTIVE);
        ClinicalLaboratory lab = (ClinicalLaboratory) registration.getProfile()
                .addOrganizationAssociation(organization, OrganizationRoleType.CLINICAL_LABORATORY)
                .getOrganizationRole();
        lab.addCertificate(new LaboratoryCertificate(LaboratoryCertificateType.CAP));
        registration.getProfile().addOrganizationAssociation(organization, OrganizationRoleType.IRB)
                .getOrganizationRole();
        registration.getProfile().addOrganizationAssociation(organization, OrganizationRoleType.PRACTICE_SITE)
                .getOrganizationRole();
        form.getPracticeSites().add(organization);
        form.getLabs().add(organization);
        form.getIrbs().add(organization);
        registration.getSubinvestigatorRegistrations().add(
                RegistrationFactory.getInstance().createSubinvestigatorRegistration(registration));
        System.setProperty("registration.validation.require.nes.status.active", "true");
        // Uncurated Organization roles that will not be part of the 1572
        Organization uncuratedOrganization = OrganizationFactory.getInstance().create();
        registration.getProfile().addOrganizationAssociation(uncuratedOrganization,
                OrganizationRoleType.CLINICAL_LABORATORY);
        registration.getProfile().addOrganizationAssociation(uncuratedOrganization, OrganizationRoleType.PRACTICE_SITE);
        registration.getProfile().addOrganizationAssociation(uncuratedOrganization, OrganizationRoleType.IRB);
        validator.validate(result, getResources());
        assertTrue(result.isValid());
        assertTrue(form.getInvalidEntityIds().isEmpty());
    }

    @Test
    public void testValidateCurationStatus_OrgRole() throws Exception {
        ValidationResult result = new ValidationResult();
        final InvestigatorProfile profile = InvestigatorProfileFactory.getInstance().create();

        final Organization practiceSite = OrganizationFactory.getInstance().create();
        final Organization lab = OrganizationFactory.getInstance().create();
        final OrganizationAssociation uncuratedAssoc = profile.addOrganizationAssociation(practiceSite,
                OrganizationRoleType.PRACTICE_SITE);
        final OrganizationAssociation curatedAssoc = profile.addOrganizationAssociation(lab,
                OrganizationRoleType.CLINICAL_LABORATORY);
        practiceSite.setCurationStatus(CurationStatus.PENDING);

        AbstractRegistrationForm form = registration.getForm1572();

        result = new ValidationResult();
        System.setProperty("registration.validation.require.nes.status.active", "false");
        validator.validateCurationStatus(uncuratedAssoc.getOrganizationRole(), result, getResources());
        assertTrue(result.isValid());
        System.setProperty("registration.validation.require.nes.status.active", "true");

        validator.validateCurationStatus(uncuratedAssoc.getOrganizationRole(), result, resources);
        assertEquals(1, result.getFailures().size());
        hasError(
                result,
                getErrorMessage("validation.failure.uncurated", practiceSite.getName() + " role as "
                        + uncuratedAssoc.getType().getDisplay(), form.getFormType().getName(), uncuratedAssoc
                        .getOrganizationRole().getCurationStatus().name()));
        assertEquals(1, form.getInvalidEntityIds().size());
        assertTrue(form.getInvalidEntityIds().contains(uncuratedAssoc.getOrganizationRole().getOrganization().getId()));
        result = new ValidationResult();
        validator.validateCurationStatus(curatedAssoc.getOrganizationRole(), result, resources);
        assertEquals(0, result.getFailures().size());
    }

}
