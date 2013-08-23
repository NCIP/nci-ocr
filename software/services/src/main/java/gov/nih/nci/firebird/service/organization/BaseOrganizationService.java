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
package gov.nih.nci.firebird.service.organization;

import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationRoleType;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.PrimaryOrganizationType;
import gov.nih.nci.firebird.exception.ValidationException;

import java.util.List;

/**
 * Service interface for working with Organization entities.
 */
public interface BaseOrganizationService {

    /**
     * Returns all organization that match the given search term. The term may be either a name (substring) or external
     * identifier (e.g. CTEP ID)
     *
     * @param term the search term
     * @param type the type of organization to search for
     * @return the matching organizations
     */
    List<Organization> search(String term, OrganizationRoleType type);

    /**
     * Returns the organizations with the given alternate identifier.
     *
     * @param alternateIdentifier the alternate identifier
     * @param type the type of organization to search for
     * @return the organizations with the given alternate identifier
     */
    List<Organization> getByAlternateIdentifier(String alternateIdentifier, OrganizationRoleType type);

    /**
     * Retrieves an organization by external identifier.
     *
     * @param externalId the external identifier
     * @return the matching organization
     * @throws InvalidatedOrganizationException if the corresponding organization record was marked invalid in the
     *             external data source
     */
    Organization getByExternalId(String externalId) throws InvalidatedOrganizationException;

    /**
     * Checks the data within an Organization object to ensure that it can be saved to any external data source.
     *
     * @param organization the organization to validate.
     * @param type the type of the organization
     * @param args additional arguments required for validating of specific organization types
     * @throws ValidationException if there are validation issues
     */
    void validate(Organization organization, OrganizationRoleType type, Object... args) throws ValidationException;

    /**
     * Saves a new organization.
     *
     * @param organization the organization to create
     * @param type the type of the new organization
     * @param args additional arguments required for creation of specific organization types
     * @throws ValidationException if there are validation issues
     */
    void create(Organization organization, OrganizationRoleType type, Object... args) throws ValidationException;

    /**
     * Synchronizes the data in the local organization record with any updates in any external record immediately. 
     * If the external record has been invalidated, creates a new external record.
     *
     * @param organization the organization to refresh.
     */
    void refreshNow(Organization organization);

    /**
     * Synchronizes the data in the local organization record with any updates in any external record if the
     * current record is considered out of date.
     *
     * @param organization the organization to refresh.
     */
    void refreshIfStale(Organization organization);

    /**
     * Retrieves and returns the PrimaryOrganizationType for the given organization.
     *
     * @param organization retrieve type for this organization
     * @return the type
     */
    PrimaryOrganizationType getPrimaryOrganizationType(Organization organization);

    /**
     * Retrieves and returns the PracticeSiteType for the given organization.
     *
     * @param organization retrieve type for this organization
     * @return the type
     */
    PracticeSiteType getPracticeSiteType(Organization organization);

}
