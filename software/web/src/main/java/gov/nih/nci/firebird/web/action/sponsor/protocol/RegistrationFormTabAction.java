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

import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.ProtocolRegistrationConfiguration;
import gov.nih.nci.firebird.service.protocol.ProtocolService;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * Action class for handling the navigation and activities related to protocol registrations.
 */
public class RegistrationFormTabAction extends AbstractProtocolDetailsTabAction {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     *
     * @param protocolService the protocol service
     */
    @Inject
    public RegistrationFormTabAction(ProtocolService protocolService) {
        super(protocolService);
    }

    /**
     * Navigate to another page.
     *
     * @return the struts forward.
     */
    @Actions({
            @Action(value = "forms", results = { @Result(location = "forms_tab.jsp") }),
            @Action(value = "download", results = { @Result(type = "chain", params = { "actionName", "downloadFile",
                    "namespace", "/util/ajax" }) }) })
    public String navigationAction() {
        return SUCCESS;
    }

    /**
     * @param formType form type
     * @return whether or not this form type is supplementary
     */
    protected boolean isFormTypeSupplementary(FormType formType) {
        return Sets.newHashSet(formType.getInvestigatorDefault(), formType.getSubinvestigatorDefault()).contains(
                FormOptionality.SUPPLEMENTARY);
    }

    /**
     * JSON response for table data.
     *
     * @return SUCCESS.
     * @throws JSONException when serialization error occurs.
     */
    public String getJsonFormsTable() throws JSONException {
        ProtocolRegistrationConfiguration config = getProtocol().getRegistrationConfiguration();
        List<Row> forms = Lists.newArrayList();
        for (FormType formType : config.getAssociatedFormTypes()) {
            if (!isFormTypeSupplementary(formType)) {
                forms.add(new Row(formType, config));
            }
        }
        return JSONUtil.serialize(forms, null, null, false, true, false);
    }

    /**
     * simple bean to display a forms row.
     */
    public final class Row implements Serializable {
        private static final long serialVersionUID = 1L;

        private final Long id;
        private final String name;
        private final String description;
        private final FormOptionality invOptionality;
        private final FormOptionality subinvOptionality;
        private String sampleFileName;
        private Long sampleFileId;
        private final Set<FormOptionality> allowableInvestigatorFormOptionalities;
        private final Set<FormOptionality> allowableSubInvestigatorFormOptionalities;

        Row(FormType formType) {
            this(formType, null);
        }

        Row(FormType formType, ProtocolRegistrationConfiguration config) {
            this.id = formType.getId();
            this.name = formType.getName();
            this.description = formType.getDescription();
            this.allowableInvestigatorFormOptionalities = formType.getAllowableInvestigatorFormOptionalities();
            this.allowableSubInvestigatorFormOptionalities = formType.getAllowableSubInvestigatorFormOptionalities();
            if (config != null) {
                this.invOptionality = config.getInvestigatorOptionality(formType);
                this.subinvOptionality = config.getSubinvestigatorOptionality(formType);
            } else {
                this.invOptionality = formType.getInvestigatorDefault();
                this.subinvOptionality = formType.getSubinvestigatorDefault();
            }
            if (formType.getSample() != null) {
                this.sampleFileName = formType.getSample().getName();
                this.sampleFileId = formType.getSample().getId();
            }
        }

        /**
         * @return form type id
         */
        public Long getId() {
            return id;
        }

        /**
         * @return form type name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @return investigator form optionality
         */
        public FormOptionality getInvOptionality() {
            return invOptionality;
        }

        /**
         * @return subinvestigator form optionality
         */
        public FormOptionality getSubinvOptionality() {
            return subinvOptionality;
        }

        /**
         * @return form sample file id for downloading.
         */
        public Long getSampleFileId() {
            return sampleFileId;
        }

        /**
         * @return form example file id
         */
        public String getSampleFileName() {
            return sampleFileName;
        }

        /**
         * @return the allowableInvestigatorFormOptionalities
         */
        public Set<FormOptionality> getAllowableInvestigatorFormOptionalities() {
            return allowableInvestigatorFormOptionalities;
        }

        /**
         * @return the allowableSubInvestigatorFormOptionalities
         */
        public Set<FormOptionality> getAllowableSubInvestigatorFormOptionalities() {
            return allowableSubInvestigatorFormOptionalities;
        }

    }

}
