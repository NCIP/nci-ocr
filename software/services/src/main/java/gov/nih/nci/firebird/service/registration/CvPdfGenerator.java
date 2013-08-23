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

import com.google.common.collect.Lists;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.Certification;
import gov.nih.nci.firebird.data.CredentialType;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.pdf.PdfService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;

/**
 * Generates a PDF of an investigator's CV.
 */
@SuppressWarnings("PMD.TooManyMethods")
// more readable when each CV section has its own method
class CvPdfGenerator extends AbstractPdfWriterGenerator {

    private static final int INVESTIGATOR_NAME_INDEX = 1;
    private static final int INVESTIGATOR_EXTERNAL_ID_INDEX = 2;
    private static final int INVESTIGATOR_ADDRESS_INDEX = 3;
    private static final int INVESTIGATOR_EMAIL_INDEX = 4;
    private static final int INVESTIGATOR_PHONE_NUMBER_INDEX = 5;
    private static final int INVESTIGATOR_PROVIDER_NUMBER_INDEX = 6;
    private static final int INVESTIGATOR_CTEP_ID_INDEX = 7;

    private static final Logger LOG = Logger.getLogger(CvPdfGenerator.class);

    private final ProfileContentPdfHelper contentHelper = new ProfileContentPdfHelper(this);

    CvPdfGenerator(AbstractRegistration registration, PdfService pdfService, FileService fileService,
            ResourceBundle resources) {
        super(registration, pdfService, fileService, resources);
    }

    @Override
    void addBody() throws DocumentException, IOException {
        addContactInformationSection();
        addCredentialsSection();
    }

    private void addContactInformationSection() throws DocumentException {
        contentHelper.addInstructionsHeader(getDocument(), getFromResources("cv.omb.header"));
        contentHelper.addInstructions(getDocument(), getFromResources("cv.omb.text"));
        contentHelper.addContactInformationHeader(getDocument());
        addSpacerRow();
        PdfPTable table = createTable(TWO_COLUMNS);
        contentHelper.addInvestigatorName(table, INVESTIGATOR_NAME_INDEX);
        contentHelper.addInvestigatorEmail(table, INVESTIGATOR_EMAIL_INDEX);
        contentHelper.addInvestigatorExternalId(table, INVESTIGATOR_EXTERNAL_ID_INDEX);
        contentHelper.addInvestigatorPhoneNumber(table, INVESTIGATOR_PHONE_NUMBER_INDEX);
        contentHelper.addInvestigatorAddress(table, INVESTIGATOR_ADDRESS_INDEX);

        PdfPTable rightTable = createTable(ONE_COLUMN);
        contentHelper.addInvestigatorProviderNumber(rightTable, INVESTIGATOR_PROVIDER_NUMBER_INDEX);
        contentHelper.addInvestigatorCtepId(rightTable, INVESTIGATOR_CTEP_ID_INDEX);
        PdfPCell tableCell = new PdfPCell(rightTable);
        tableCell.disableBorderSide(Rectangle.BOX);
        table.addCell(tableCell);
        getDocument().add(table);
    }

    private void addCredentialsSection() throws DocumentException, IOException {
        contentHelper.addCredentialsHeader(getDocument());
        addSpacerRow();
        int index = INVESTIGATOR_CTEP_ID_INDEX + 1;
        contentHelper.addWorkHistory(index++);
        contentHelper.addLicenses(index++);
        contentHelper.addDegrees(index++);
        contentHelper.addInternships(index++);
        contentHelper.addResidencies(index++);
        contentHelper.addFellowships(index++);
        contentHelper.addSpecialties(index++);
        addCertifications(index++);
        addTrainingCertificates(index++);
        addClinicalExperience(index++);
    }

    private void addCertifications(int index) throws DocumentException {
        PdfPTable table = createTable(THREE_COLUMNS);
        addCertificationHeadings(table, index);
        SortedSet<AbstractCredential<?>> credentials = getProfile().getCurrentCredentials(CredentialType.CERTIFICATION);
        if (credentials.isEmpty()) {
            addCellAndCompleteRow(table, getValueNone());
        } else {
            addCertifications(table, credentials);
        }
        getDocument().add(table);
    }

    private void addCertifications(PdfPTable table, SortedSet<AbstractCredential<?>> credentials) {
        for (AbstractCredential<?> credential : credentials) {
            addCertification(table, (Certification) credential);
        }
    }

    private void addCertificationHeadings(PdfPTable table, int index) {
        addHeadings(table, index, "cv.certifications.heading", "cv.certification.type.heading",
                "cv.certification.effective_date.heading", "cv.certification.expiration_date.heading");
    }

    private void addCertification(PdfPTable table, Certification certification) {
        addValues(table, certification.getCertificationType().getName(),
                formatMonthAndYearDate(certification.getEffectiveDate()),
                formatMonthAndYearDate(certification.getExpirationDate()));
    }

    private void addTrainingCertificates(int index) throws DocumentException {
        PdfPTable table = createTable(FOUR_COLUMNS);
        addTrainingCertificateHeadings(table, index);
        List<TrainingCertificate> trainingCertificates = getTrainingCertificates();
        if (trainingCertificates.isEmpty()) {
            addCellAndCompleteRow(table, getValueNone());
        } else {
            addTrainingCertificates(table, trainingCertificates);
        }
        getDocument().add(table);
    }

    private void addTrainingCertificateHeadings(PdfPTable table, int index) {
        addHeadings(table, index, "cv.training.certificates.heading", "cv.training.certificate.heading",
                "cv.training.certification.issuer.heading", "cv.training.certificate.effective_date.heading",
                "cv.training.certificate.expiration_date.heading");
    }

    private List<TrainingCertificate> getTrainingCertificates() {
        List<TrainingCertificate> trainingCertificates = Lists.newArrayList();
        for (AbstractCredential<?> credential : getProfile().getCurrentCredentials(CredentialType.CERTIFICATE)) {
            trainingCertificates.add((TrainingCertificate) credential);
        }
        return trainingCertificates;
    }

    private void addTrainingCertificates(PdfPTable table, List<TrainingCertificate> trainingCertificates) {
        for (TrainingCertificate trainingCertificate : trainingCertificates) {
            addTrainingCertificate(table, trainingCertificate);
        }
    }

    private void addTrainingCertificate(PdfPTable table, TrainingCertificate trainingCertificate) {
        Organization issuer = trainingCertificate.getIssuer();
        addValues(table, getFromResources(trainingCertificate.getCertificateType().getNameProperty()),
                issuer != null ? issuer.getName() : StringUtils.EMPTY,
                formatMonthAndYearDate(trainingCertificate.getEffectiveDate()),
                formatMonthAndYearDate(trainingCertificate.getExpirationDate()));
    }

    private void addClinicalExperience(int index) throws DocumentException, IOException {
        PdfPTable table = createTable(ONE_COLUMN);
        addHeadings(table, index, "cv.clinical_experience.heading");
        PdfPCell cell = createCell();
        if (getProfile().getClinicalResearchExperience() != null) {
            StringReader experienceReader = new StringReader(getProfile().getClinicalResearchExperience().getText());
            List<Element> experienceElements = HTMLWorker.parseToList(experienceReader, null);
            for (Element element : experienceElements) {
                cell.addElement(element);
            }
        }
        table.addCell(cell);
        getDocument().add(table);
    }

    @Override
    String getTitleKey() {
        return "cv.title";
    }

    @Override
    AbstractRegistrationForm getForm() {
        return getRegistration().getCurriculumVitaeForm();
    }

    @Override
    AbstractProtocolRegistration getRegistration() {
        return (AbstractProtocolRegistration) super.getRegistration();
    }

    @Override
    Logger getLog() {
        return LOG;
    }
}
