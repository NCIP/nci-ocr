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

import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.pdf.PdfService;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * Uses iText to create PDF output containing Supplemental Investigator Data for CTEP.
 */
class SupplementalInvestigatorDataFormPdfGenerator extends AbstractPdfWriterGenerator {

    private static final int INVESTIGATOR_NAME_INDEX = 1;
    private static final int INVESTIGATOR_ADDRESS_INDEX = 2;
    private static final int INVESTIGATOR_EMAIL_INDEX = 3;
    private static final int INVESTIGATOR_PHONE_NUMBER_INDEX = 4;
    private static final int INVESTIGATOR_PROVIDER_NUMBER_INDEX = 5;
    private static final int INVESTIGATOR_CTEP_ID_INDEX = 6;

    private static final Logger LOG = Logger.getLogger(CvPdfGenerator.class);

    private final ProfileContentPdfHelper contentHelper = new ProfileContentPdfHelper(this);

    SupplementalInvestigatorDataFormPdfGenerator(AbstractRegistration registration, PdfService pdfService,
            FileService fileService, ResourceBundle resources) {
        super(registration, pdfService, fileService, resources);
    }

    @Override
    AbstractRegistrationForm getForm() {
        return getRegistration().getSupplementalInvestigatorDataForm();
    }

    @Override
    AnnualRegistration getRegistration() {
        return (AnnualRegistration) super.getRegistration();
    }

    @Override
    String getTitleKey() {
        return "sidf.title";
    }

    @Override
    void addBody() throws DocumentException, IOException {
        contentHelper.addInstructionsHeader(getDocument(), getFromResources("sidf.omb.header"));
        contentHelper.addInstructions(getDocument(), getFromResources("sidf.omb.text"));
        addContactInformationSection();
        int lastIndex = addPrimaryOrganizationSection(INVESTIGATOR_CTEP_ID_INDEX + 1);
        addCredentialsSection(lastIndex);
    }

    private void addContactInformationSection() throws DocumentException {
        contentHelper.addContactInformationHeader(getDocument());
        addSpacerRow();
        PdfPTable table = createTable(TWO_COLUMNS);
        contentHelper.addInvestigatorName(table, INVESTIGATOR_NAME_INDEX);
        contentHelper.addInvestigatorEmail(table, INVESTIGATOR_EMAIL_INDEX);
        contentHelper.addInvestigatorAddress(table, INVESTIGATOR_ADDRESS_INDEX);

        PdfPTable rightTable = createTable(ONE_COLUMN);
        contentHelper.addInvestigatorPhoneNumber(rightTable, INVESTIGATOR_PHONE_NUMBER_INDEX);
        contentHelper.addInvestigatorProviderNumber(rightTable, INVESTIGATOR_PROVIDER_NUMBER_INDEX);
        contentHelper.addInvestigatorCtepId(rightTable, INVESTIGATOR_CTEP_ID_INDEX);
        PdfPCell tableCell = new PdfPCell(rightTable);
        tableCell.disableBorderSide(Rectangle.BOX);
        table.addCell(tableCell);
        getDocument().add(table);
    }

    private int addPrimaryOrganizationSection(int startIndex) throws DocumentException {
        int index = startIndex;
        contentHelper.addPrimaryOrganizationHeader(getDocument());
        addSpacerRow();
        PdfPTable table = createTable(TWO_COLUMNS);
        contentHelper.addOrganizationName(table, index++);
        contentHelper.addOrganizationCtepId(table, index++);
        contentHelper.addOrganizationAddress(table, index++);

        PdfPTable rightTable = createTable(ONE_COLUMN);
        contentHelper.addOrganizationEmail(rightTable, index++);
        contentHelper.addOrganizationPhoneNumber(rightTable, index++);
        PdfPCell tableCell = new PdfPCell(rightTable);
        tableCell.disableBorderSide(Rectangle.BOX);
        table.addCell(tableCell);

        getDocument().add(table);
        return index;
    }

    private void addCredentialsSection(int lastIndex) throws DocumentException {
        int index = lastIndex;
        contentHelper.addCredentialsHeader(getDocument());
        addSpacerRow();
        contentHelper.addWorkHistory(index++);
        contentHelper.addLicenses(index++);
        contentHelper.addDegrees(index++);
        contentHelper.addInternships(index++);
        contentHelper.addResidencies(index++);
        contentHelper.addFellowships(index++);
        contentHelper.addSpecialties(index++);
        addPhrpCertificates(index++);
    }

    @Override
    Logger getLog() {
        return LOG;
    }

    private void addPhrpCertificates(int index) throws DocumentException {
        PdfPTable table = createTable(TWO_COLUMNS);
        addPhrpCertificateHeadings(table, index);

        List<TrainingCertificate> phrpCertificates = getPhrpCertificates();
        if (phrpCertificates.isEmpty()) {
            addCellAndCompleteRow(table, getValueNone());
        } else {
            addPhrpCertificates(table, phrpCertificates);
        }
        getDocument().add(table);
    }

    private void addPhrpCertificateHeadings(PdfPTable table, int index) {
        addHeadings(table, index, "cv.phrp.certificates.heading", "cv.phrp.certification.issuer.heading",
                "cv.phrp.certificate.effective_date.heading");
    }

    private List<TrainingCertificate> getPhrpCertificates() {
        List<TrainingCertificate> phrpCertificates = Lists.newArrayList();
        for (TrainingCertificate trainingCertificate : getProfile().getHumanResearchCertificates()) {
            phrpCertificates.add(trainingCertificate);
        }
        return phrpCertificates;
    }

    private void addPhrpCertificates(PdfPTable table, List<TrainingCertificate> trainingCertificates) {
        for (TrainingCertificate trainingCertificate : trainingCertificates) {
            addPhrpCertificate(table, trainingCertificate);
        }
    }

    private void addPhrpCertificate(PdfPTable table, TrainingCertificate trainingCertificate) {
        Organization issuer = trainingCertificate.getIssuer();
        addValues(table,
                issuer != null ? issuer.getName() : "",
                formatMonthAndYearDate(trainingCertificate.getEffectiveDate()));
    }

}
