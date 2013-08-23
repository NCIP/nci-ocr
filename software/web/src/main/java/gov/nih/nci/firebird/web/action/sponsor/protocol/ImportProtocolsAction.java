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
package gov.nih.nci.firebird.web.action.sponsor.protocol;

import static org.apache.commons.lang3.StringUtils.*;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.protocol.ProtocolImportDetail;
import gov.nih.nci.firebird.service.protocol.ProtocolImportJob;
import gov.nih.nci.firebird.service.protocol.ProtocolImportService;
import gov.nih.nci.firebird.service.protocol.ProtocolService;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;

import java.io.File;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Namespaces;
import org.apache.struts2.convention.annotation.Result;
import org.jboss.ejb3.common.proxy.plugins.async.AsyncUtils;

import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action class for handling the navigation and activities related to importing protocol registrations.
 */
@Namespaces(value = { @Namespace("/sponsor/representative/protocol"),
        @Namespace("/sponsor/representative/protocol/ajax") })
@Result(name = ActionSupport.INPUT, location = "protocol_import.jsp")
public class ImportProtocolsAction extends AbstractProtocolAction {
    private static final long serialVersionUID = 1L;

    static final String JOB_IN_PROGRESS_ERROR_KEY = "import.protocol.job.in.progress";

    private final ProtocolImportService protocolImportService;

    private File importFile;
    private Organization sponsor;
    private String sponsorExternalId;
    private List<Integer> selectedIndexForImport;

    /**
     * @param protocolService the protocol service
     * @param protocolImportService the protocol import service
     */
    @Inject
    public ImportProtocolsAction(ProtocolService protocolService, ProtocolImportService protocolImportService) {
        super(protocolService);
        this.protocolImportService = protocolImportService;
    }

    @Override
    public void prepare() {
        super.prepare();
        if (!isEmpty(getSponsorExternalId())) {
            setSponsor(getOrganization(getSponsorExternalId()));
        }
    }

    /**
     * Navigate to the Single Protocol View page or the browse protocols list page.
     *
     * @return the struts forward.
     */
    @Actions(value = {
            @Action("enterProtocolImport"),
            @Action(value = "viewProtocolDetails", results = @Result(name = INPUT, location = "protocol_details.jsp")),
            @Action(value = "viewProtocolImportMessages", results = @Result(name = INPUT,
                    location = "protocol_import_messages.jsp")) })
    public String enter() {
        return INPUT;
    }

    /**
     * Upload the import file and send it off for initial validation and processing.
     *
     * @return the struts forward.
     */
    @Action(value = "uploadImportFile", results = @Result(type = "redirectAction", params = { "actionName",
            "enterProtocolImport" }))
    @Validations(expressions = @ExpressionValidator(
            expression = "sponsor.id == null || currentUser.isSponsorRepresentative(sponsor)",
            key = "sponsor.verification.not.representative"), requiredFields = {
            @RequiredFieldValidator(fieldName = "sponsor.externalId", key = "import.protocol.sponsor.required"),
            @RequiredFieldValidator(fieldName = "importFile", key = "import.protocol.export.file.required") })
    public String uploadImportFile() {
        ProtocolImportJob importJob = getProtocolImportJob();

        if (importJob == null) {
            try {
                importJob = getProtocolImportService().createImportJob(importFile, sponsor);
                getAsyncProtocolImportService().validate(importJob);
                getSession(false).setAttribute(FirebirdUIConstants.PROTOCOL_IMPORT_JOB, importJob);
            } catch (ValidationException e) {
                handleValidationException(e);
            }
        } else {
            addActionError(getText(JOB_IN_PROGRESS_ERROR_KEY));
        }
        return SUCCESS;
    }

    private ProtocolImportService getAsyncProtocolImportService() {
        return AsyncUtils.mixinAsync(getProtocolImportService());
    }

    private ProtocolImportService getProtocolImportService() {
        return protocolImportService;
    }

    /**
     * @return the struts forward.
     */
    @Action(value = "retrieveImportJobState", results = { @Result(type = "json", params = { "root",
            "protocolImportJob", "enumAsBean", "true", "excludeProperties", "*\\.roles, " }) })
    public String retrieveImportJobState() {
        return SUCCESS;
    }

    /**
     * Upload the import file and send it off for initial validation and processing.
     *
     * @return the struts forward.
     */
    @Action(value = "importRecords",
            results = @Result(name = INPUT, type = "json", params = { "root", "actionErrors" }))
    @Validations(expressions = {
            @ExpressionValidator(expression = "currentUser.isSponsorRepresentative(sponsor)",
                    key = "sponsor.verification.not.representative"),
            @ExpressionValidator(
                    expression = "selectedIndexForImport != null && !selectedIndexForImport.empty",
                    key = "import.protocol.selection.required") })
    public String importRecords() {
        ProtocolImportJob importJob = getProtocolImportJob();
        markSelectedRecordsForImport(importJob);
        getAsyncProtocolImportService().importProtocols(importJob);
        return NONE;
    }

    private void markSelectedRecordsForImport(ProtocolImportJob importJob) {
        for (ProtocolImportDetail detail : importJob.getDetails()) {
            boolean markImport = getSelectedIndexForImport().contains(detail.getIndex());
            detail.setMarkedForImport(markImport);
        }
    }

    /**
     * Upload the import file and send it off for initial validation and processing.
     *
     * @return the struts forward.
     */
    @Action("cancelImport")
    public String cancelImport() {
        getSession(false).removeAttribute(FirebirdUIConstants.PROTOCOL_IMPORT_JOB);
        return INPUT;
    }

    public ProtocolImportJob getProtocolImportJob() {
        return (ProtocolImportJob) getSessionAttribute(FirebirdUIConstants.PROTOCOL_IMPORT_JOB);
    }

    public File getImportFile() {
        return importFile;
    }

    public void setImportFile(File importFile) {
        this.importFile = importFile;
    }

    public String getSponsorExternalId() {
        return sponsorExternalId;
    }

    public void setSponsorExternalId(String sponsorExternalId) {
        this.sponsorExternalId = sponsorExternalId;
    }

    public Organization getSponsor() {
        return sponsor;
    }

    private void setSponsor(Organization sponsor) {
        this.sponsor = sponsor;
    }

    public List<Integer> getSelectedIndexForImport() {
        return selectedIndexForImport;
    }

    public void setSelectedIndexForImport(List<Integer> selectedIndexesForImport) {
        this.selectedIndexForImport = selectedIndexesForImport;
    }

}