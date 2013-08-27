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

import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.data.ProtocolRegistrationConfiguration;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.GenericService;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

/**
 * Service for Investigational Protocols.
 */
@Local
@SuppressWarnings("PMD.TooManyMethods")
// facade service expected to expose many methods
public interface ProtocolService extends GenericService<Protocol> {

    /**
     * Creates a new, empty <code>Protocol</code> for use in the system.
     *
     * @return the new protocol.
     */
    Protocol create();

    /**
     * Returns the existing agents that start with the string given.
     *
     * @param startOfName get agents with names that start with this string
     * @return the matching agents.
     */
    List<ProtocolAgent> getAgents(String startOfName);

    /**
     * Adds an investigator to a <code>Protocol</code>. The <code>Person</code> may be a new or existing record with or
     * without an associated <code>InvestigatorProfile</code>. Calling this method will ensure that the person is saved
     * and a profile created if necessary.
     *
     * @param protocol add investigator to this protocol
     * @param investigator the investigator to add
     * @return the new registration.
     * @throws ValidationException If NES Validation Exceptions
     */
    InvestigatorRegistration addInvestigator(Protocol protocol, Person investigator) throws ValidationException;

    /**
     * Invites an investigator to register for the protocol. The system will send email to the investigator, update the
     * investigator's invitation status and provide a notification in the investigator's inbox (if they have an
     * account).
     *
     * @param registration registration belonging to the investigator to invite.
     */
    void invite(InvestigatorRegistration registration);

    /**
     * Returns a list of protocols that are visible to the provided user based on their verified sponsor roles.
     *
     * @param user retrieve protocols available to this user.
     * @param groupNames the list of Grid Grouper groups the user belongs to.
     * @return the list of protocols.
     */
    List<Protocol> getProtocols(FirebirdUser user, Set<String> groupNames);

    /**
     * Finalizes a registration packet for an investigator and all subinvestigators.
     *
     * @param registration the investigator registration for the packet.
     */
    void approvePacket(InvestigatorRegistration registration);

    /**
     * Deactivates a registration packet changing the status of all forms and registrations to INACTIVE.
     *
     * @param registration the registration to deactivate.
     * @param comments The Sponsor comments regarding the de-activation.
     */
    void deactivatePacket(InvestigatorRegistration registration, String comments);

    /**
     * Reactivates a registration packet changing the status of all forms and registrations to IN_PROGRESS.
     *
     * @param registration the registration to reactivate.
     * @param comments The Sponsor comments regarding the re-activation.
     */
    void reactivatePacket(InvestigatorRegistration registration, String comments);

    /**
     * Save changes to a protocol and notify investigators of the changes.
     *
     * @param original the state of the protocol before the changes.
     * @param revised the protocol to be saved.
     * @param comment explanation of why the protocol was modified.
     * @throws ValidationException if comment is not provided for any changes made to the protocol.
     */
    void updateProtocol(Protocol original, Protocol revised, String comment) throws ValidationException;

    /**
     * Removes a registration packet for an investigator and all subinvestigators.
     *
     * @param registration the investigator registration packet to remove.
     */
    void removePacket(InvestigatorRegistration registration);

    /**
     * Adds default configured form types to a protocol.
     *
     * @param registrationConfiguration the registration configuration for a protocol.
     */
    void addFormTypes(ProtocolRegistrationConfiguration registrationConfiguration);

    /**
     * Checks if another existing protocol already has a protocol number.
     *
     * @param protocol new or updated protocol to check
     * @return true if a duplicate number
     */
    boolean hasDuplicateProtocolNumber(Protocol protocol);
}
