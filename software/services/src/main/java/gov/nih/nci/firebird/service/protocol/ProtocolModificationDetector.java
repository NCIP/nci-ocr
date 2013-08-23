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

import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.ProtocolLeadOrganization;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.data.ProtocolRevision;
import gov.nih.nci.firebird.data.ProtocolModification;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.commons.lang.ObjectUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Detect changes made to a protocol since the base snapshot was taken.
 */
@SuppressWarnings("PMD.TooManyMethods")
// modification check methods split out for readability
class ProtocolModificationDetector {

    private static final String INVESTIGATOR_MESSAGE_PREFIX = ".investigator.message";
    private static final String SPONSOR_MESSAGE_PREFIX = ".sponsor.message";

    private final Protocol original;
    private final Protocol revised;
    private final ResourceBundle resources;
    private final List<ProtocolModification> modifications = new ArrayList<ProtocolModification>();

    ProtocolModificationDetector(Protocol original, Protocol revised, ResourceBundle resources) {
        this.original = original;
        this.revised = revised;
        this.resources = resources;
        loadModifications();
    }

    void addModifications(ProtocolRevision revision) {
        for (ProtocolModification modification : modifications) {
            revision.addModification(modification);
        }
    }

    boolean isModificationDetected() {
        return !modifications.isEmpty();
    }

    private void loadModifications() {
        checkProtocolNumber();
        checkProtocolTitle();
        checkSponsor();
        checkLeadOrganizations();
        checkPhase();
        checkAgents();
        checkDocuments();
        checkRegistrationConfiguration();
    }

    private void checkProtocolNumber() {
        checkPropertyChange("protocol.change.number", original.getProtocolNumber(), revised.getProtocolNumber());
    }

    private void checkPropertyChange(String messageKey, Object oldValue, Object newValue) {
        if (!ObjectUtils.equals(oldValue, newValue)) {
            addModification(messageKey, oldValue, newValue);
        }
    }

    private void addModification(String descriptionKeyPrefix, Object... args) {
        String investigatorDescription = MessageFormat.format(
                resources.getString(descriptionKeyPrefix + INVESTIGATOR_MESSAGE_PREFIX), args);
        String sponsorDescription = MessageFormat.format(
                resources.getString(descriptionKeyPrefix + SPONSOR_MESSAGE_PREFIX), args);
        ProtocolModification modification = new ProtocolModification(investigatorDescription, sponsorDescription);
        modifications.add(modification);
    }

    private void checkProtocolTitle() {
        checkPropertyChange("protocol.change.title", original.getProtocolTitle(), revised.getProtocolTitle());
    }

    private void checkSponsor() {
        checkPropertyChange("protocol.change.sponsor", original.getSponsor().getName(), revised.getSponsor().getName());
    }

    private void checkLeadOrganizations() {
        Set<ProtocolLeadOrganization> previousLeadOrganizations = original.getLeadOrganizations();
        Set<ProtocolLeadOrganization> newLeadOrganizations = revised.getLeadOrganizations();

        addRemovedLeadOrganizationMessages(previousLeadOrganizations, newLeadOrganizations);
        addNewLeadOrganizationsMessages(previousLeadOrganizations, newLeadOrganizations);
    }

    private void addRemovedLeadOrganizationMessages(Set<ProtocolLeadOrganization> previousLeadOrganizations,
                                                    Set<ProtocolLeadOrganization> newLeadOrganizations) {
        checkForOrganizationsNotPresent(previousLeadOrganizations, newLeadOrganizations,
                                        "protocol.change.lead.organization.removed");
    }

    private void checkForOrganizationsNotPresent(Set<ProtocolLeadOrganization> previousLeadOrganizations,
            Set<ProtocolLeadOrganization> newLeadOrganizations, String messageKey) {
        for (ProtocolLeadOrganization leadOrganization : previousLeadOrganizations) {
            if (!containsLeadOrganization(newLeadOrganizations, leadOrganization)) {
                addModification(messageKey, leadOrganization.getOrganization().getName(), leadOrganization
                        .getPrincipalInvestigator().getDisplayName());
            }
        }
    }

    private boolean containsLeadOrganization(Set<ProtocolLeadOrganization> leadOrganizations,
            final ProtocolLeadOrganization leadOrganization) {
        return Iterables.any(leadOrganizations, new Predicate<ProtocolLeadOrganization>() {
            @Override
            public boolean apply(ProtocolLeadOrganization input) {
                return input.equals(leadOrganization);
            }
        });
    }

    private void addNewLeadOrganizationsMessages(Set<ProtocolLeadOrganization> previousLeadOrganizations,
                                                 Set<ProtocolLeadOrganization> newLeadOrganizations) {
        checkForOrganizationsNotPresent(newLeadOrganizations, previousLeadOrganizations,
                                        "protocol.change.lead.organization.added");
    }

    private void checkPhase() {
        checkPropertyChange("protocol.change.phase", original.getPhase().getDisplay(), revised.getPhase().getDisplay());
    }

    private void checkAgents() {
        checkAgentCollection("protocol.change.agent.removed", original.getAgents(), revised.getAgents());
        checkAgentCollection("protocol.change.agent.added", revised.getAgents(), original.getAgents());
    }

    private void checkAgentCollection(String messageKey, Set<ProtocolAgent> actual, Set<ProtocolAgent> expected) {
        for (ProtocolAgent agent : actual) {
            if (!expected.contains(agent)) {
                addModification(messageKey, agent.getName());
            }
        }
    }

    private void checkDocuments() {
        checkDocumentCollection("protocol.change.document.removed", original.getDocuments(), revised.getDocuments());
        checkDocumentCollection("protocol.change.document.added", revised.getDocuments(), original.getDocuments());
    }

    private void checkDocumentCollection(String messageKey, Set<FirebirdFile> actual, Set<FirebirdFile> expected) {
        for (FirebirdFile file : actual) {
            if (!expected.contains(file)) {
                addModification(messageKey, file.getName());
            }
        }
    }

    private void checkRegistrationConfiguration() {
        checkForOptionalityChanges("protocol.change.form.optionality.investigator", original
                .getRegistrationConfiguration().getInvestigatorConfiguration().getFormOptionalities(), revised
                .getRegistrationConfiguration().getInvestigatorConfiguration().getFormOptionalities());
        checkForOptionalityChanges("protocol.change.form.optionality.subinvestigator", original
                .getRegistrationConfiguration().getSubinvestigatorConfiguration().getFormOptionalities(), revised
                .getRegistrationConfiguration().getSubinvestigatorConfiguration().getFormOptionalities());
    }

    private void checkForOptionalityChanges(String messageKey, Map<FormType, FormOptionality> baseOptionality,
            Map<FormType, FormOptionality> modifiedOptionality) {
        Set<FormType> addedFormTypes = new HashSet<FormType>(modifiedOptionality.keySet());
        for (FormType formType : baseOptionality.keySet()) {
            FormOptionality optionality = baseOptionality.get(formType);
            if (!addedFormTypes.remove(formType)) {
                addModification(messageKey, formType.getDescription(), optionality.getDisplay(),
                        FormOptionality.NONE.getDisplay());
            } else if (optionality != modifiedOptionality.get(formType)) {
                addModification(messageKey, formType.getDescription(), optionality.getDisplay(), modifiedOptionality
                        .get(formType).getDisplay());
            }
        }

        for (FormType type : addedFormTypes) {
            addModification(messageKey, type.getDescription(), FormOptionality.NONE.getDisplay(), modifiedOptionality
                    .get(type).getDisplay());
        }
    }

}
