/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a organization or an entity, and all other entities that control,
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

package gov.nih.nci.firebird.selenium2.tests.profile.credentials;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.test.TestFileUtils;
import gov.nih.nci.firebird.data.CertificateType;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.nes.organization.OrganizationEntityIntegrationService;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.EditTrainingCertificateDialog;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.TrainingCertificateSection;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure;
import gov.nih.nci.firebird.selenium2.pages.util.ExpectedValidationFailure.FailingAction;
import gov.nih.nci.firebird.test.util.FirebirdPropertyUtils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TrainingCertificateCredentialsTest extends AbstractCredentialsTabTest {

    private TrainingCertificateSection trainingCertificateSection;
    private File certificateFile;

    @Inject
    @Named("nih.oer.organization.nes.id")
    String nihOerOrganizationExternalId;
    private Organization nihOerIssuer;
    @Inject
    OrganizationEntityIntegrationService organizationService;
    private TrainingCertificate certificateWithNihOerIssuer;
    private TrainingCertificate certificateWithExistingIssuer;
    private TrainingCertificate certificateWithNewIssuer;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        trainingCertificateSection = goToCredentialsTab().getTrainingSection();
        certificateFile = TestFileUtils.createTemporaryFile();
        nihOerIssuer = organizationService.getById(nihOerOrganizationExternalId);
        certificateWithNihOerIssuer = createTestCertificate(nihOerIssuer);
        certificateWithNihOerIssuer.setEffectiveDate(DateUtils.addMonths(certificateWithNihOerIssuer.getExpirationDate(), -2));
        certificateWithExistingIssuer = createTestCertificate(getExistingIssuer());
        certificateWithExistingIssuer.setEffectiveDate(DateUtils.addMonths(certificateWithExistingIssuer.getExpirationDate(), -2));
        certificateWithNewIssuer = createTestCertificate(getNewIssuer());
        certificateWithNewIssuer.setEffectiveDate(DateUtils.addMonths(certificateWithNewIssuer.getExpirationDate(), -2));
    }

    @Test
    public void testTrainingCertificates() throws IOException {
        checkEffectiveDateRequiredToggling();
        addCertificateWithNewIssuer();
        addNihOerIssuedCertificate();
        addCertificateWithExistingIssuer();
        checkDuplicatesAreNotAllowed();
        checkEditingCertificates();
        deleteAndReAddCertificateWithNihOerIssuer();
    }

    private void checkEffectiveDateRequiredToggling() {
        EditTrainingCertificateDialog certificateDialog = trainingCertificateSection.clickAddCertificate();
        assertFalse(certificateDialog.isEffectiveDateRequired());
        certificateDialog.selectCertificateType(FirebirdPropertyUtils.getPropertyText(CertificateType.HUMAN_RESEARCH_CERTIFICATE.getNameProperty()));
        assertTrue(certificateDialog.isEffectiveDateRequired());
        certificateDialog.selectCertificateType(null);
        assertFalse(certificateDialog.isEffectiveDateRequired());
    }

    private void addNihOerIssuedCertificate() throws IOException {
        EditTrainingCertificateDialog certificateDialog = trainingCertificateSection.clickAddCertificate();
        checkEffectiveDateRequiredForNihOerIssuedCertificate(certificateDialog, certificateWithNihOerIssuer);
        certificateDialog.getHelper().enterTrainingCertificateData(certificateWithNihOerIssuer, certificateFile, true);
        certificateDialog.clickSave();
        trainingCertificateSection.getHelper().verifyCertificateListed(certificateWithNihOerIssuer);
    }

    private void checkEffectiveDateRequiredForNihOerIssuedCertificate(
            final EditTrainingCertificateDialog certificateDialog, TrainingCertificate certificate) throws IOException {
        certificateDialog.getHelper().enterTrainingCertificateData(certificate, certificateFile, true);
        certificateDialog.getHelper().setEffectiveDate(null);
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "error.effective.date.required.for.human.research.certificates");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                certificateDialog.clickSave();
            }
        });
    }

    private void addCertificateWithExistingIssuer() throws IOException {
        EditTrainingCertificateDialog certificateDialog = trainingCertificateSection.clickAddCertificate();
        checkRequiredFields(certificateDialog);
        checkForExpirationBeforeEffectiveDateError(certificateDialog);
        certificateDialog.getHelper().enterTrainingCertificateData(certificateWithExistingIssuer, certificateFile, false);
        certificateDialog.clickSave();
        trainingCertificateSection.getHelper().verifyCertificateListed(certificateWithExistingIssuer);
    }

    private void checkRequiredFields(final EditTrainingCertificateDialog certificateDialog) {
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "error.credentials.organization.required");
        expectedValidationFailure.addExpectedMessage("error.credentials.certificate.file.required");
        expectedValidationFailure.addExpectedMessage("error.credentials.certificate.type.required");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                certificateDialog.clickSave();
            }
        });
    }

    private void checkForExpirationBeforeEffectiveDateError(final EditTrainingCertificateDialog certificateDialog)
            throws IOException {
        certificateDialog.getHelper().enterTrainingCertificateData(certificateWithExistingIssuer, certificateFile, false);
        Date today = new Date();
        certificateDialog.getHelper().setEffectiveDate(DateUtils.setMonths(today, Calendar.DECEMBER));
        certificateDialog.getHelper().setExpirationDate(DateUtils.setMonths(today, Calendar.OCTOBER));
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "error.expiration.date.before.effective");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                certificateDialog.clickSave();
            }
        });
    }

    private void addCertificateWithNewIssuer() throws IOException {
        EditTrainingCertificateDialog certificateDialog = trainingCertificateSection.clickAddCertificate();
        checkRequiredFieldsForCreatingNewIssuer(certificateDialog);
        certificateDialog.getHelper().setTrainingCertificateWithNewIssuer(certificateWithNewIssuer, certificateFile);
        certificateDialog.clickSave();
        trainingCertificateSection.getHelper().verifyCertificateListed(certificateWithNewIssuer);
    }

    private void checkRequiredFieldsForCreatingNewIssuer(final EditTrainingCertificateDialog certificateDialog) {
        certificateDialog.clickCreateNewIssuer();
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "error.credentials.certificate.file.required");
        expectedValidationFailure.addExpectedMessage("error.credentials.certificate.type.required");
        expectedValidationFailure.addExpectedRequiredFields("profile.organization.name", "profile.organization.email",
                "profile.organization.postalAddress.streetAddress", "profile.organization.postalAddress.city",
                "profile.organization.postalAddress.postalCode");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                certificateDialog.clickSave();
            }
        });
    }

    private void checkDuplicatesAreNotAllowed() throws IOException {
        checkDuplicateNihOerIssuedCertificate();
        checkCertificateWithExistingIssuer();
    }

    private void checkDuplicateNihOerIssuedCertificate() throws IOException {
        checkDuplicateNotAllowed(certificateWithNihOerIssuer);
    }

    private void checkDuplicateNotAllowed(TrainingCertificate certificate) throws IOException {
        final EditTrainingCertificateDialog certificateDialog = trainingCertificateSection.clickAddCertificate();
        boolean nihOerIssued = certificate.getIssuer().equals(nihOerIssuer);
        certificateDialog.getHelper().enterTrainingCertificateData(certificate, certificateFile, nihOerIssued);
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "error.duplicate.certificate.for.issuer");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                certificateDialog.clickSave();
            }
        });
        certificateDialog.clickCancel();
    }

    private void checkCertificateWithExistingIssuer() throws IOException {
        checkDuplicateNotAllowed(certificateWithExistingIssuer);
    }

    private void checkEditingCertificates() throws IOException {
        checkCantDuplicateByEditing(certificateWithExistingIssuer, certificateWithNihOerIssuer);
        checkCantDuplicateByEditing(certificateWithNihOerIssuer, certificateWithExistingIssuer);
        changeIssuer(certificateWithNihOerIssuer, getExistingIssuer());
        changeIssuer(certificateWithExistingIssuer, nihOerIssuer);
    }

    private void checkCantDuplicateByEditing(TrainingCertificate certificateToEdit,
            TrainingCertificate certificateToDuplicate) throws IOException {
        final EditTrainingCertificateDialog certificateDialog = trainingCertificateSection.getHelper()
                .getListing(certificateToEdit).clickEditLink();
        boolean nihOerIssued = certificateToDuplicate.getIssuer().equals(nihOerIssuer);
        certificateDialog.getHelper().enterTrainingCertificateData(certificateToDuplicate, certificateFile, nihOerIssued);
        ExpectedValidationFailure expectedValidationFailure = new ExpectedValidationFailure(
                "error.duplicate.certificate.for.issuer");
        expectedValidationFailure.assertFailureOccurs(new FailingAction() {
            @Override
            public void perform() {
                certificateDialog.clickSave();
            }
        });
        certificateDialog.clickCancel();
    }

    private void changeIssuer(TrainingCertificate certificateToEdit, Organization newIssuer) throws IOException {
        EditTrainingCertificateDialog certificateDialog = trainingCertificateSection.getHelper()
                .getListing(certificateToEdit).clickEditLink();
        boolean nihOerIssued = newIssuer.equals(nihOerIssuer);
        Organization oldIssuer = certificateToEdit.getIssuer();
        certificateToEdit.setIssuer(newIssuer);
        certificateToEdit.setEffectiveDate(DateUtils.addMonths(certificateToEdit.getExpirationDate(), -2));
        certificateDialog.getHelper().enterTrainingCertificateData(certificateToEdit, certificateFile, nihOerIssued);
        certificateDialog.clickSave();
        trainingCertificateSection.getHelper().verifyCertificateListed(certificateToEdit);
        certificateToEdit.setIssuer(oldIssuer);
    }

    private void deleteAndReAddCertificateWithNihOerIssuer() throws IOException {
        trainingCertificateSection.getHelper().verifyCertificateListed(certificateWithNihOerIssuer);
        trainingCertificateSection.getHelper().getListing(certificateWithNihOerIssuer).clickDeleteLink().clickDelete();
        assertNull(trainingCertificateSection.getHelper().getListing(certificateWithNihOerIssuer));
        addNihOerIssuedCertificate();
    }

}
