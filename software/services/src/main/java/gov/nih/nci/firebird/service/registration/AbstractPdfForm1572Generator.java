/*
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

import static gov.nih.nci.firebird.data.OrganizationRoleType.CLINICAL_LABORATORY;
import static gov.nih.nci.firebird.data.OrganizationRoleType.IRB;
import static gov.nih.nci.firebird.data.OrganizationRoleType.PRACTICE_SITE;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.Form1572;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.PracticeSite;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.pdf.PdfFormField;
import gov.nih.nci.firebird.service.pdf.PdfFormValues;
import gov.nih.nci.firebird.service.pdf.PdfService;
import org.apache.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Base class for Form1572 and related forms PDF generation.
 */
abstract class AbstractPdfForm1572Generator extends AbstractPdfFormGenerator {

    private static final int MAXIMUM_LINES_IN_ORGANIZATIONS_FIELD = 2;
    private static final int MAXIMUM_LINES_IN_SUBINVESTIGATORS_FIELD = 5;
    static final String FIELD_OPTION_YES = "Yes";

    private static final Logger LOG = Logger.getLogger(AbstractPdfForm1572Generator.class);

    private final ResourceBundle resources;

    AbstractPdfForm1572Generator(AbstractRegistration registration, PdfService pdfService,
            FileService fileService, ResourceBundle resources) {
        super(registration, pdfService, fileService);
        this.resources = resources;
    }

    @Override
    PdfFormValues getValues() {
        PdfFormValues values = new PdfFormValues();
        values.setValue(Form1572Field.INVESTIGATOR_NAME_AND_ADDRESS_TEXT, getInvestigatorNameAndAddress());
        values.setValue(Form1572Field.CV_CHECKBOX, FIELD_OPTION_YES);
        setValue(
                values,
                Form1572Field.PRACTICE_SITES_TEXT,
                getAssociatedOrganizationText(PRACTICE_SITE),
                MAXIMUM_LINES_IN_ORGANIZATIONS_FIELD,
                resources.getString("form1572.practice_sites.header"));
        setValue(
                values,
                Form1572Field.CLINCIAL_LABS_TEXT,
                getAssociatedOrganizationText(CLINICAL_LABORATORY),
                MAXIMUM_LINES_IN_ORGANIZATIONS_FIELD,
                resources.getString("form1572.labs.header"));
        setValue(
                values,
                Form1572Field.IRB_TEXT,
                getAssociatedOrganizationText(IRB),
                MAXIMUM_LINES_IN_ORGANIZATIONS_FIELD,
                resources.getString("form1572.irbs.header"));
        setValue(
                values,
                Form1572Field.SUBINVESTIGATORS_TEXT,
                getSubinvestigatorsFieldText(),
                MAXIMUM_LINES_IN_SUBINVESTIGATORS_FIELD,
                resources.getString("form1572.subinvestigators.header"));
        values.setValue(Form1572Field.PROTOCOL_TEXT, getProtocolText());
        selectPhaseCheckbox(values);
        return values;
    }

    private void setValue(PdfFormValues values, Form1572Field field, String value, int maximumLinesInField,
            String fieldHeader) {
        List<String> lines = Lists.newArrayList(Splitter.on(NEWLINE).split(value));
        if (lines.size() <= maximumLinesInField) {
            values.setValue(field, value);
        } else {
            String fieldValue = getTruncatedFieldValue(maximumLinesInField, lines);
            values.setValue(field, fieldValue);
            String overflowValue = getFieldOverflowValue(maximumLinesInField, lines);
            values.addAdditionalContent(fieldHeader, overflowValue);
        }
    }

    private String getTruncatedFieldValue(int maximumLinesInField, List<String> lines) {
        StringBuilder sb = new StringBuilder();
        sb.append(Joiner.on(NEWLINE).join(lines.subList(0, maximumLinesInField)));
        sb.append(NEWLINE);
        sb.append(resources.getString("form1572.see_attached"));
        return sb.toString();
    }

    private String getFieldOverflowValue(int maximumLinesInField, List<String> lines) {
        return Joiner.on(NEWLINE).join(lines.subList(maximumLinesInField, lines.size()));
    }

    abstract void selectPhaseCheckbox(PdfFormValues values);

    abstract String getProtocolText();

    private String getInvestigatorNameAndAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(getInvestigator().getDisplayName());
        sb.append(NEWLINE);
        sb.append(getInvestigator().getPostalAddress().toString());
        return sb.toString();
    }

    private String getAssociatedOrganizationText(final OrganizationRoleType organizationType) {
        Set<Organization> organizations = ((Form1572) getForm()).getAssociatedOrganizations(organizationType);
        Iterable<String> organizationLines = Iterables.transform(organizations,
            new Function<Organization, String>() {
                @Override
                public String apply(Organization organization) {
                    return getOrganizationLine(organization, organizationType);
                }
        });
        return Joiner.on(NEWLINE).join(organizationLines);
    }

    abstract String getSubinvestigatorsFieldText();

    private String getOrganizationLine(Organization organization, OrganizationRoleType organizationType) {
        StringBuilder sb = new StringBuilder();
        sb.append(organization.getName());
        sb.append(", ");
        sb.append(organization.getPostalAddress().toOneLineString());
        if (organizationType == OrganizationRoleType.PRACTICE_SITE) {
            addPracticeSiteFields(organization, sb);
        }
        return sb.toString();
    }

    private void addPracticeSiteFields(Organization organization, StringBuilder sb) {
        if (!isEmpty(organization.getCtepId())) {
            sb.append(", " + organization.getCtepId());
        }
        PracticeSite practiceSite = (PracticeSite) organization.getRole(OrganizationRoleType.PRACTICE_SITE);
        if (practiceSite != null && !isEmpty(practiceSite.getOhrpAssuranceNumber())) {
            sb.append(", " + practiceSite.getOhrpAssuranceNumber());
        }
    }

    @Override
    Collection<FirebirdFile> getAttachments() {
        return Collections.emptySet();
    }

    @Override
    Logger getLog() {
        return LOG;
    }

    @Override
    PdfFormField getDateSignedField() {
        return Form1572Field.DATE_SIGNED_TEXT;
    }
}
