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
package gov.nih.nci.firebird.web.action.investigator.registration;

import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.SubInvestigator;
import gov.nih.nci.firebird.data.SubInvestigatorRegistration;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.registration.ProtocolRegistrationService;
import gov.nih.nci.firebird.web.action.AbstractProtocolRegistrationAction;
import gov.nih.nci.firebird.web.common.RegistrationJsonConverter;

import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.opensymphony.xwork2.Preparable;

/**
 * subinvestigator tab action.
 */
@Namespace("/investigator/registration/ajax/subinvestigator")
@InterceptorRef(value = "registrationManagementStack")
public class SubinvestigatorTabAction extends AbstractProtocolRegistrationAction implements Preparable {

    private static final long serialVersionUID = 1L;

    private SubInvestigatorRegistration subinvestigatorRegistration;
    private List<String> selectedExternalIds = Lists.newArrayList();
    private List<Long> invitedRegistrationIds = Lists.newArrayList();
    private final ResourceBundle resources;
    private final Set<Long> invalidSubinvestigatorIds = Sets.newHashSet();

    /**
     * Creates an action instance.
     *
     * @param registrationService registration service
     * @param profileService profile service
     * @param resources FIREBIRD resource bundle
     */
    @Inject
    public SubinvestigatorTabAction(ProtocolRegistrationService registrationService,
            InvestigatorProfileService profileService, ResourceBundle resources) {
        super(registrationService, profileService);
        this.resources = resources;
    }

    @Override
    public void prepare() {
        super.prepare();
        if (subinvestigatorRegistration != null && subinvestigatorRegistration.getId() != null) {
            subinvestigatorRegistration = (SubInvestigatorRegistration) getRegistrationService().getById(
                    subinvestigatorRegistration.getId());
        }
    }

    /**
     * Common enter action for entering the different pages.
     *
     * @return SUCCESS
     */
    @Actions({ @Action(value = "listFromProfile", results = { @Result(location = "addFromProfile.jsp") }),
            @Action(value = "enterInvite", results = { @Result(location = "invite.jsp") }),
            @Action(value = "confirmDelete", results = { @Result(location = "confirm_remove_subinvestigator.jsp") }) })
    public String enter() {
        return SUCCESS;
    }

    /**
     * Enter action for entering the tab. Validates submission status if necessary.
     *
     * @return SUCCESS
     */
    @Action(value = "list", results = @Result(location = "subinvestigators.jsp"))
    public String enterTab() {
        addValidationFailuresIfNecessary();
        return SUCCESS;
    }

    private void addValidationFailuresIfNecessary() {
        if (RegistrationStatus.INCOMPLETE == getRegistration().getStatus()) {
            ValidationResult result = new ValidationResult();
            getRegistration().checkForUninvitedSubInvestigators(resources, result);
            if (!result.isValid()) {
                handleValidationException(new ValidationException(result));
                populateInvalidSubinvestigatorIds();
            }
        }
    }

    private void populateInvalidSubinvestigatorIds() {
        for (Person subInvestigator : getRegistration().getUninvitedSubInvestigators()) {
            getInvalidSubinvestigatorIds().add(subInvestigator.getId());
        }
    }

    /**
     * @return All subinvestigator ids which have failed validation
     */
    public Set<Long> getInvalidSubinvestigatorIds() {
        return invalidSubinvestigatorIds;
    }

    /**
     * add a person to the list of subinvestigators from the primary investigator's profile.
     *
     * @return SUCCESS
     */
    @Action("addFromProfile")
    public String addFromProfile() {
        filterExistingSubinvestigators();
        getRegistrationService().createSubinvestigatorRegistrations(getRegistration(), selectedExternalIds);
        getRegistrationService().setReturnedOrRevisedRegistrationsFormStatusesToRevised(getRegistration().getProfile(),
                FormTypeEnum.FORM_1572);

        return closeDialog();
    }

    /**
     * delete a person from the list subinvestigators.
     *
     * @return SUCCESS
     */
    @Action("delete")
    public String delete() {
        getRegistrationService().removeSubInvestigatorRegistrationAndNotify(getSubinvestigatorRegistration());
        return closeDialog();
    }

    /**
     * send an invitation to investigatorsTab involved in the protocol.
     *
     * @return close dialog if all went well.
     */
    @Action(value = "invite", results = { @Result(name = ERROR, location = "invite.jsp") })
    public String invite() {
        for (SubInvestigatorRegistration registration : getRegistration().getSubinvestigatorRegistrations()) {
            if (getInvitedRegistrationIds().contains(registration.getId())) {
                getRegistrationService().inviteToRegistration(registration);
            }
        }

        return closeDialog();
    }

    public List<Long> getInvitedRegistrationIds() {
        return invitedRegistrationIds;
    }

    public void setInvitedRegistrationIds(List<Long> invitedRegistrationIds) {
        this.invitedRegistrationIds = invitedRegistrationIds;
    }

    /**
     * @return sorted list of available subinvestigators in profile.
     */
    public SortedSet<Person> getUnaddedProfileSubinvestigators() {
        Set<SubInvestigator> subInvestigators = getRegistration().getProfile().getSubInvestigators();
        SortedSet<Person> list = new TreeSet<Person>(Person.NAME_COMPARATOR);
        for (SubInvestigator subInvestigator : subInvestigators) {
            if (findExistingReg(subInvestigator.getPerson().getExternalId()) == null) {
                list.add(subInvestigator.getPerson());
            }
        }

        return list;
    }

    /**
     * @return a json list of available subinvestigators in profile.
     * @throws JSONException if JSON serialization fails.
     */
    public String getProfileSubinvestigatorsAsJson() throws JSONException {
        return JSONUtil.serialize(getUnaddedProfileSubinvestigators());
    }

    /**
     * JSON response for table data.
     *
     * @return table data to display as a JSON string.
     * @throws JSONException if serialization fails
     */
    public String getRegistrationsAsJson() throws JSONException {
        return RegistrationJsonConverter.convertToJson(this, getRegistration().getSubinvestigatorRegistrations());
    }

    @Override
    public InvestigatorRegistration getRegistration() {
        return (InvestigatorRegistration) super.getRegistration();
    }

    /**
     * @return selected subinvestigator registration.
     */
    public SubInvestigatorRegistration getSubinvestigatorRegistration() {
        return subinvestigatorRegistration;
    }

    /**
     * @param subinvestigatorRegistration selected subinvestigator registration.
     */
    public void setSubinvestigatorRegistration(SubInvestigatorRegistration subinvestigatorRegistration) {
        this.subinvestigatorRegistration = subinvestigatorRegistration;
    }

    /**
     * @return external ids of the selected subinvestigators.
     */
    public List<String> getSelectedExternalIds() {
        return selectedExternalIds;
    }

    /**
     * @param selectedExternalIds external ids of the selected subinvestigators.
     */
    public void setSelectedExternalIds(List<String> selectedExternalIds) {
        this.selectedExternalIds = selectedExternalIds;
    }

    private void filterExistingSubinvestigators() {
        for (Iterator<String> idIterator = selectedExternalIds.iterator(); idIterator.hasNext();) {
            SubInvestigatorRegistration reg = findExistingReg(idIterator.next());
            if (reg != null) {
                idIterator.remove();
            }
        }
    }

    private SubInvestigatorRegistration findExistingReg(String externalId) {
        for (SubInvestigatorRegistration reg : getRegistration().getSubinvestigatorRegistrations()) {
            String subInvestigatorExternalId = reg.getProfile().getPerson().getExternalId();
            if (subInvestigatorExternalId.equals(externalId)) {
                return reg;
            }
        }
        return null;
    }
}
