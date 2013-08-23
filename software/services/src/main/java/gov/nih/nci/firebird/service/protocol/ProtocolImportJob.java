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
package gov.nih.nci.firebird.service.protocol;

import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.data.ProtocolPhase;
import gov.nih.nci.firebird.exception.ValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.csvreader.CsvReader;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Contains the list of protocol import details loaded from a source import file.
 */
@SuppressWarnings("PMD.TooManyMethods") // many helper methods used in reading import file
public class ProtocolImportJob implements Serializable {

    private static final long serialVersionUID = 1L;

    static final int PROTOCOL_NUMBER_COLUMN_INDEX = 0;
    private static final int PROTOCOL_TITLE_COLUMN_INDEX = 1;
    private static final int PROTOCOL_PHASE_COLUMN_INDEX = 2;
    static final int PROTOCOL_LEAD_ORGANIZATION_COLUMN_INDEX = 3;
    private static final int PROTOCOL_AGENTS_COLUMN_INDEX = 4;
    static final int PROTOCOL_INVESTIGATOR_IDS_COLUMN_INDEX = 5;
    private static final int EXPECTED_NUMBER_OF_COLUMNS = 6;

    private static final String PROTOCOL_NUMBER_FIELD_NAME = "Sponsor Protocol ID";
    private static final String PROTOCOL_TITLE_FIELD_NAME = "Title";
    private static final String PROTOCOL_AGENT_FIELD_NAME = "Agent";
    private static final String PROTOCOL_PHASE_FIELD_NAME = "Phase";

    private static final String MISSING_VALUE_MESSAGE_KEY = "validation.failure.protocol.import.missing.value";
    private static final String INVALID_PHASE_MESSAGE_KEY = "validation.failure.protocol.import.invalid.phase";
    private static final String EMPTY_FILE_MESSAGE_KEY = "validation.failure.protocol.import.empty.file";
    private static final String INVALID_NUMBER_OF_COLUMNS_MESSAGE_KEY =
            "validation.failure.protocol.import.invalid.number.of.columns";
    private static final String INVALID_LENGTH_MESSAGE_KEY =
            "validation.failure.protocol.import.invalid.length";
    private static final String DUPLICATE_PROTOCOL_NUMBER_MESSAGE_KEY =
            "validation.failure.protocol.import.duplicate.number.in.file";
    private static final String INVALID_MAPPING_MESSAGE_KEY =
            "validation.failure.protocol.import.invalid.lead.organization.mapping";

    private static final char FIELD_VALUES_DELIMITER_CHAR = ';';
    private static final String LEAD_ORGANIZATION_MAPPING_SEPARATOR = ":";

    private final List<ProtocolImportDetail> details = Lists.newArrayList();
    private final Organization sponsor;
    private ProtocolImportJobStatus status = ProtocolImportJobStatus.NOT_STARTED;

    ProtocolImportJob(Organization sponsor) {
        this.sponsor = sponsor;
    }

    /**
     * @return the sponsor
     */
    public Organization getSponsor() {
        return sponsor;
    }

    /**
     * @return the list of all protocol import details, one per line.
     */
    public synchronized List<ProtocolImportDetail> getDetails() {
        return Collections.unmodifiableList(Lists.newArrayList(details));
    }

    /**
     * @return the status
     */
    public ProtocolImportJobStatus getStatus() {
        return status;
    }

    void setStatus(ProtocolImportJobStatus status) {
        this.status = status;
    }

    void loadFromFile(File importFile, ResourceBundle resources) throws ValidationException {
        CsvReader reader = null;
        try {
            reader = new CsvReader(importFile.getAbsolutePath(), '\t');
            load(reader, resources);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Import file not found: " + importFile.getPath(), e);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected IO exception reading import file", e);
        } finally {
            close(reader);
        }
    }

    private void close(CsvReader reader) {
        if (reader != null) {
            reader.close();
        }
    }

    private void load(CsvReader reader, ResourceBundle resources) throws IOException, ValidationException {
        int lineNumber = 0;
        reader.skipLine(); // skip header line
        if (!reader.readRecord()) {
            ValidationFailure failure = ValidationFailure.create(resources, EMPTY_FILE_MESSAGE_KEY);
            throw new ValidationException(failure);
        }
        do {
            lineNumber++;
            String[] values = reader.getValues();
            checkNumberOfColumns(values, resources, lineNumber);
            createDetail(values, resources);
        } while (reader.readRecord());
        checkForDuplicateProtocolNumbers(resources);
    }

    private void checkNumberOfColumns(String[] values, ResourceBundle resources, int lineNumber)
            throws ValidationException {
        if (values.length != EXPECTED_NUMBER_OF_COLUMNS) {
            ValidationFailure failure = ValidationFailure.create(resources, INVALID_NUMBER_OF_COLUMNS_MESSAGE_KEY,
                                                                 lineNumber, values.length);
            throw new ValidationException(failure);
        }
    }

    private ProtocolImportDetail createDetail(String[] values, ResourceBundle resources) {
        int detailIndex = details.size();
        ProtocolImportDetail detail = new ProtocolImportDetail(detailIndex);
        detail.getProtocol().setSponsor(getSponsor());
        handleProtocolNumber(detail, resources, values[PROTOCOL_NUMBER_COLUMN_INDEX]);
        handleTitle(detail, resources, values[PROTOCOL_TITLE_COLUMN_INDEX]);
        handlePhase(detail, resources, values[PROTOCOL_PHASE_COLUMN_INDEX]);
        handleLeadOrganizations(detail, resources, values[PROTOCOL_LEAD_ORGANIZATION_COLUMN_INDEX]);
        handleAgents(detail, resources, values[PROTOCOL_AGENTS_COLUMN_INDEX]);
        handleInvestigatorIds(detail, values[PROTOCOL_INVESTIGATOR_IDS_COLUMN_INDEX]);
        details.add(detail);
        return detail;
    }

    private void handleProtocolNumber(ProtocolImportDetail detail, ResourceBundle resources, String value) {
        if (StringUtils.isBlank(value)) {
            detail.addFailure(resources, MISSING_VALUE_MESSAGE_KEY, PROTOCOL_NUMBER_COLUMN_INDEX,
                              PROTOCOL_NUMBER_FIELD_NAME);
        } else if (value.length() > Protocol.MAX_PROTOCOL_NUM_SIZE) {
            detail.addFailure(resources, INVALID_LENGTH_MESSAGE_KEY, PROTOCOL_NUMBER_COLUMN_INDEX,
                              PROTOCOL_NUMBER_FIELD_NAME, Protocol.MAX_PROTOCOL_NUM_SIZE, value.length());
        } else {
            detail.getProtocol().setProtocolNumber(value);
        }
    }

    private void handleTitle(ProtocolImportDetail detail, ResourceBundle resources, String title) {
        if (StringUtils.isBlank(title)) {
            detail.addFailure(resources, MISSING_VALUE_MESSAGE_KEY, PROTOCOL_TITLE_COLUMN_INDEX,
                              PROTOCOL_TITLE_FIELD_NAME);
        } else if (title.length() > Protocol.MAX_TITLE_LENGTH) {
            detail.addFailure(resources, INVALID_LENGTH_MESSAGE_KEY, PROTOCOL_TITLE_COLUMN_INDEX,
                              PROTOCOL_TITLE_FIELD_NAME, Protocol.MAX_TITLE_LENGTH, title.length());
        } else {
            detail.getProtocol().setProtocolTitle(title);
        }
    }

    private void handlePhase(ProtocolImportDetail detail, ResourceBundle resources, String phaseString) {
        if (StringUtils.isBlank(phaseString)) {
            detail.addFailure(resources, MISSING_VALUE_MESSAGE_KEY, PROTOCOL_PHASE_COLUMN_INDEX,
                              PROTOCOL_PHASE_FIELD_NAME);
        } else {
            ProtocolPhase phase = ProtocolPhase.getByDisplay(phaseString);
            if (phase == null) {
                detail.addFailure(resources, INVALID_PHASE_MESSAGE_KEY, PROTOCOL_PHASE_COLUMN_INDEX,
                                  phaseString);
            } else {
                detail.getProtocol().setPhase(phase);
            }
        }
    }

    private void handleLeadOrganizations(ProtocolImportDetail detail, ResourceBundle resources,
                                         String leadOrganizationMappings) {
        if (StringUtils.isNotBlank(leadOrganizationMappings)) {
            for (String mapping : splitValues(leadOrganizationMappings)) {
                handleLeadOrganizationMapping(detail, resources, mapping);
            }
        }
    }

    private void handleLeadOrganizationMapping(ProtocolImportDetail detail, ResourceBundle resources, String mapping) {
        String[] values = mapping.split(LEAD_ORGANIZATION_MAPPING_SEPARATOR);
        if (values.length == 2) {
            String organizationCtepId = values[0];
            String principalInvestigatorExternalId = values[1];
            detail.getLeadOrganizationMappings().put(organizationCtepId, principalInvestigatorExternalId);
        } else {
            detail.addFailure(resources, INVALID_MAPPING_MESSAGE_KEY, PROTOCOL_LEAD_ORGANIZATION_COLUMN_INDEX,
                              mapping);
        }
    }

    private void handleAgents(ProtocolImportDetail detail, ResourceBundle resources, String agents) {
        if (StringUtils.isNotBlank(agents)) {
            Set<String> agentNames = Sets.newHashSet(splitValues(agents));
            boolean validLengths = checkAgentLengths(detail, resources, agentNames);
            if (validLengths) {
                detail.getProtocol().updateAgents(agentNames);
            }
        }
    }

    private boolean checkAgentLengths(ProtocolImportDetail detail, ResourceBundle resources, Set<String> agentNames) {
        boolean validLengths = true;
        for (String agentName : agentNames) {
            if (agentName.length() > ProtocolAgent.MAX_AGENT_LENGTH) {
                validLengths = false;
                detail.addFailure(resources, INVALID_LENGTH_MESSAGE_KEY, PROTOCOL_AGENTS_COLUMN_INDEX,
                                  PROTOCOL_AGENT_FIELD_NAME, ProtocolAgent.MAX_AGENT_LENGTH, agentName.length());
            }
        }
        return validLengths;
    }

    private Iterable<String> splitValues(String valuesString) {
        return Splitter.on(FIELD_VALUES_DELIMITER_CHAR).omitEmptyStrings().trimResults().split(valuesString);
    }

    private void handleInvestigatorIds(ProtocolImportDetail detail, String investigatorIds) {
        if (StringUtils.isNotBlank(investigatorIds)) {
            detail.setInvestigatorExternalIds(Lists.newArrayList(splitValues(investigatorIds)));
        } else {
            List<String> emptyList = Collections.emptyList();
            detail.setInvestigatorExternalIds(emptyList);
        }
    }

    /**
     * @return all details that have been marked for import.
     */
    public List<ProtocolImportDetail> getDetailsMarkedForImport() {
        List<ProtocolImportDetail> markedDetails = Lists.newArrayList();
        for (ProtocolImportDetail detail : details) {
            if (detail.isMarkedForImport()) {
                markedDetails.add(detail);
            }
        }
        return markedDetails;
    }

    private void checkForDuplicateProtocolNumbers(ResourceBundle resources) {
        Map<String, ProtocolImportDetail> numberToDetailMap = new HashMap<String, ProtocolImportDetail>();
        Set<ProtocolImportDetail> detailsWithDuplicates = Sets.newHashSet();
        for (ProtocolImportDetail currentDetail : details) {
            String protocolNumber = currentDetail.getProtocol().getProtocolNumber();
            if (numberToDetailMap.containsKey(protocolNumber)) {
                ProtocolImportDetail detailWithDuplicateNumber = numberToDetailMap.get(protocolNumber);
                handleDuplicateNumbers(currentDetail, detailWithDuplicateNumber, detailsWithDuplicates, resources);
            } else {
                numberToDetailMap.put(protocolNumber, currentDetail);
            }
        }
    }

    private void handleDuplicateNumbers(ProtocolImportDetail currentDetail,
                                        ProtocolImportDetail detailWithDuplicateNumber,
                                        Set<ProtocolImportDetail> detailsWithDuplicates,
                                        ResourceBundle resources) {
        addDuplicateNumberValidationFailure(currentDetail, detailsWithDuplicates, resources);
        if (!detailsWithDuplicates.contains(detailWithDuplicateNumber)) {
            addDuplicateNumberValidationFailure(detailWithDuplicateNumber, detailsWithDuplicates, resources);
        }
    }

    private void addDuplicateNumberValidationFailure(ProtocolImportDetail detail,
                                                     Set<ProtocolImportDetail> detailsFlaggedWithDuplicates,
                                                     ResourceBundle resources) {
        detailsFlaggedWithDuplicates.add(detail);
        detail.addFailure(resources, DUPLICATE_PROTOCOL_NUMBER_MESSAGE_KEY, PROTOCOL_NUMBER_COLUMN_INDEX,
                          detail.getProtocol().getProtocolNumber());
    }

}
