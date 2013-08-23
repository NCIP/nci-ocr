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
package gov.nih.nci.firebird.web.action.sponsor.protocol.export;

import gov.nih.nci.firebird.data.AbstractProtocolRegistration;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;
import gov.nih.nci.firebird.service.protocol.ProtocolService;
import gov.nih.nci.firebird.web.action.sponsor.protocol.AbstractProtocolAction;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.csvreader.CsvWriter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Export Protocols Tab Action.
 */
@Namespace("/sponsor/protocol/export")
public class ExportProtocolsTabAction extends AbstractProtocolAction implements ServletResponseAware {

    private static final long serialVersionUID = 1L;
    static final String[] EXPORT_HEADERS = {"Sponsor Protocol Id", "Investigator Name", "Investigator CTEP ID",
                                             "Investigator NES ID", "Registration Status", "Registration Status Date" };
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    private Set<Long> selectedProtocolIds = Sets.newHashSet();
    private HttpServletResponse response;

    /**
     * @param protocolService Protocol Service
     */
    @Inject
    public ExportProtocolsTabAction(ProtocolService protocolService) {
        super(protocolService);
    }

    /**
     * Navigate to the Export Protocols Tab.
     *
     * @return the struts forward.
     */
    @Action(value = "enterExportProtocols", results = @Result(location = "export_protocols_tab.jsp"))
    public String enter() {
        return SUCCESS;
    }

    /**
     * @return Protocol JSON String
     * @throws JSONException if there is a problem during serialization
     */
    public String getProtocolsJson() throws JSONException {
        return JSONUtil.serialize(getProtocolListings());
    }

    private List<ProtocolListing> getProtocolListings() {
        List<ProtocolListing> listings = Lists.newArrayList();
        for (Protocol protocol : getProtocols()) {
            listings.add(new ProtocolListing(protocol));
        }
        return listings;
    }

    private List<Protocol> getProtocols() {
        return getProtocolService().getProtocols(getCurrentUser(), getCurrentUserGroupNames());
    }

    /**
     * Writes the CSV formatted values to the output stream and returns NONE.
     *
     * @return NONE
     * @throws IOException when writing to output fails.
     */
    @Action(value = "exportProtocols", results = @Result(name = INPUT, location = "export_protocols_tab.jsp"))
    @Validations(expressions = @ExpressionValidator(
            expression = "!selectedProtocolIds.empty", key = "sponsor.protocol.export.empty"))
    public String exportProtocols() throws IOException {
        List<List<String>> personRecords = Lists.newArrayList();
        Set<Protocol> protocolsToExport = getProtocolsToExport();

        for (Protocol protocol : protocolsToExport) {
            for (AbstractProtocolRegistration registration : protocol.getAllRegistrations()) {
                List<String> record = Lists.newArrayList();
                record.add(protocol.getProtocolNumber());
                record.add(registration.getProfile().getPerson().getDisplayNameForList());
                String externalId = registration.getProfile().getPerson().getExternalId();
                record.add(registration.getProfile().getPerson().getCtepId());
                record.add(externalId);
                record.add(registration.getStatus().getDisplay());
                Date statusDate = registration.getStatusDate();
                if (statusDate != null) {
                    record.add(dateFormat.format(statusDate));
                } else {
                    record.add(null);
                }
                personRecords.add(record);
            }
        }

        response.setHeader("Content-Disposition", "attachment; filename=\""
                                                  + getText("sponsor.export.protocols.file.name") + "\"");
        writeCsv(EXPORT_HEADERS, personRecords);
        return NONE;
    }

    private Set<Protocol> getProtocolsToExport() {
        Set<Protocol> protocolsToExport = Sets.newHashSet();
        for (Long protocolId : getSelectedProtocolIds()) {
            protocolsToExport.add(getProtocolService().getById(protocolId));
        }
        return protocolsToExport;
    }

    private void writeCsv(String[] headers, List<List<String>> protocolData) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Pragma", "private");
        response.setHeader("Cache-Control", "private, must-revalidate");
        CsvWriter writer = new CsvWriter(new OutputStreamWriter(response.getOutputStream()), ',');

        if (headers != null) {
            writer.writeRecord(headers);
        }
        if (CollectionUtils.isNotEmpty(protocolData)) {
            for (List<String> record : protocolData) {
                writer.writeRecord(record.toArray(new String[] {}));
            }
        }
        writer.flush();
        writer.close();
    }

    /**
     * @return the selectedProtocolIds
     */
    public Set<Long> getSelectedProtocolIds() {
        return selectedProtocolIds;
    }

    /**
     * @param selectedProtocolIds the selectedProtocolIds to set
     */
    public void setSelectedProtocolIds(Set<Long> selectedProtocolIds) {
        this.selectedProtocolIds = selectedProtocolIds;
    }

    @Override
    public void setServletResponse(HttpServletResponse servletResponse) {
        this.response = servletResponse;
    }

    /**
     * Protocol Listing object.
     */
    @SuppressWarnings("ucd")
    // needs to be protected for JSONUtil.serialize()
    protected final class ProtocolListing {

        private final Long id;
        private final String title;
        private final String protocolId;
        private final String sponsor;
        private final String leadOrganizations;
        private final String agents;

        /**
         * @param protocol Protocol
         */
        ProtocolListing(Protocol protocol) {
            id = protocol.getId();
            title = protocol.getProtocolTitle();
            protocolId = protocol.getProtocolNumber();
            sponsor = protocol.getSponsor().getName();
            leadOrganizations = leadOrganizationsToString(protocol.getLeadOrganizations());
            agents = protocol.getAgentListForDisplay();
        }

        private String leadOrganizationsToString(Set<ProtocolLeadOrganization> leadOrganizationSet) {
            StrBuilder output = new StrBuilder();

            for (ProtocolLeadOrganization leadOrganization : leadOrganizationSet) {
                output.appendSeparator("<br>");
                output.append("<b>");
                output.append(leadOrganization.getOrganization().getName());
                output.append("</b> (");
                output.append(leadOrganization.getPrincipalInvestigator().getDisplayName());
                output.append(')');
            }

            return output.toString();
        }

        /**
         * @return the id
         */
        public Long getId() {
            return id;
        }

        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @return the number
         */
        public String getProtocolId() {
            return protocolId;
        }

        /**
         * @return the sponsor
         */
        public String getSponsor() {
            return sponsor;
        }

        /**
         * @return the leadOrganizations
         */
        public String getLeadOrganizations() {
            return leadOrganizations;
        }

        /**
         * @return the agents
         */
        public String getAgents() {
            return agents;
        }

    }

}
