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
package gov.nih.nci.firebird.service.investigatorprofile;

import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.CertifiedSpecialtyBoard;
import gov.nih.nci.firebird.data.CertifiedSpecialtyType;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.OrderingDesignee;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.OrganizationAssociation;
import gov.nih.nci.firebird.data.Person;
import gov.nih.nci.firebird.data.PracticeSiteType;
import gov.nih.nci.firebird.data.PrimaryOrganization;
import gov.nih.nci.firebird.data.ShippingDesignee;
import gov.nih.nci.firebird.data.SubInvestigator;
import gov.nih.nci.firebird.data.TrainingCertificate;
import gov.nih.nci.firebird.exception.AssociationAlreadyExistsException;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.GenericService;
import gov.nih.nci.firebird.service.file.FileMetadata;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ejb.Local;

/**
 * Provides profile management functionality.
 */
@Local
@SuppressWarnings("PMD.TooManyMethods")
// facade service expected to expose many methods
public interface InvestigatorProfileService extends GenericService<InvestigatorProfile> {

    /**
     * Associate a person to an investigator's profile.
     *
     * @param profile is the collection of professional information for an Investigator
     * @param person the primary person to associate with the profile. Must exist either in the database or in NES.
     * @throws ValidationException if association fails (due to person already being bound to someone else's profile)
     */
    void setPrimaryPerson(InvestigatorProfile profile, Person person)
            throws ValidationException;

    /**
     * Retrieve the latest NES organization data indexed by nesId and associate to the profile as a Firebird
     * Organization.
     *
     * @param profile is the collection of professional information for an Investigator
     * @param primaryOrganization primary organization
     * @throws ValidationException If NES Validation Exceptions occur.
     */
    void setPrimaryOrganization(InvestigatorProfile profile, PrimaryOrganization primaryOrganization)
            throws ValidationException;

    /**
     * Handles updated or newly created Organization information associated with a profile. Will create a request for
     * NES to add the organization and associate the investigator to the organization if new.
     *
     * @param profile the profile containing the new or updated organization.
     * @throws ValidationException If NES Validation Exceptions occur.
     */
    void createPrimaryOrganization(InvestigatorProfile profile)
            throws ValidationException;

    /**
     * Creates a Practice Site association from a profile to new or existing organization. The new association will
     * automatically be added to the profile.
     *
     * @param profile the investigator profile
     * @param organization the organization to associate
     * @param dataField additional Information for the association
     * @param practiceSiteType Used for Practice Sites, indicates which structural role it corresponds to.
     * @return the newly created association.
     * @throws AssociationAlreadyExistsException if the association to this organization/type already exists.
     * @throws ValidationException If NES Validation Exceptions Occur.
     */
    OrganizationAssociation addAssociatedPracticeSite(InvestigatorProfile profile, Organization organization,
            String dataField, PracticeSiteType practiceSiteType)
            throws AssociationAlreadyExistsException, ValidationException;

    /**
     * Creates an IRB association from a profile to new or existing organization. The new association will
     * automatically be added to the profile.
     *
     * @param profile the investigator profile
     * @param organization the organization to associate
     * @return the newly created association.
     * @throws AssociationAlreadyExistsException if the association to this organization/type already exists.
     * @throws ValidationException If NES Validation Exceptions Occur.
     */
    OrganizationAssociation addAssociatedInstitutionalReviewBoard(InvestigatorProfile profile,
                                                                  Organization organization)
            throws AssociationAlreadyExistsException, ValidationException;

    /**
     * Creates a Clinical Lab association from a profile to new or existing organization. The new association will
     * automatically be added to the profile.
     *
     * @param profile the investigator profile
     * @param organization the organization to associate
     * @return the newly created association.
     * @throws AssociationAlreadyExistsException if the association to this organization/type already exists.
     * @throws ValidationException If NES Validation Exceptions Occur.
     */
    OrganizationAssociation addAssociatedClinicalLab(InvestigatorProfile profile, Organization organization)
            throws AssociationAlreadyExistsException, ValidationException;

    /**
     * Removes an organization association from the profile.
     *
     * @param profile the investigator profile
     * @param association organization association
     */
    void deleteAssociatedOrganization(InvestigatorProfile profile, OrganizationAssociation association);

    /**
     * Finds an association from the given profile with the provided ID and updates the OHRP # if applicable otherwise
     * it throws an IllegalArgumentException.
     *
     * @param association The Association to update
     * @param ohrp additional Information for the association
     */
    void updateAssociationOhrp(OrganizationAssociation association, String ohrp);

    /**
     * Adds a credential to a profile, creating the association as well as handling persistence and any backend external
     * service updates.
     *
     * @param profile the investigator profile
     * @param credential the new credential
     * @param formTypesToSetToRevised the form types to set to revised. Only modifies forms that are part of returned
     *            registrations.
     * @throws CredentialAlreadyExistsException if this credential already exists in the profile.
     * @throws ValidationException If NES Validation Exceptions occur.
     */
    void saveCredential(InvestigatorProfile profile, AbstractCredential<?> credential,
            FormTypeEnum... formTypesToSetToRevised) throws CredentialAlreadyExistsException, ValidationException;

    /**
     * Add a certificate to a profile.
     *
     * @param profile the investigator profile.
     * @param certificate the certificate to add.
     * @param certificateFile the file containining the certificate image.
     * @param fileMetadata the file metadata describing/documenting the certificate.
     * @throws CredentialAlreadyExistsException if this credential already exists in the profile.
     * @throws IOException if the file could not be persisted.
     * @throws ValidationException If NES Validation Exceptions occur.
     */
    void saveCertificate(InvestigatorProfile profile, TrainingCertificate certificate, File certificateFile,
            FileMetadata fileMetadata) throws CredentialAlreadyExistsException, IOException, ValidationException;

    /**
     * Retrieves a profile by the primary associated person.
     *
     * @param person retrieve profile for this person
     * @return the profile or null if none found.
     */
    InvestigatorProfile getByPerson(Person person);

    /**
     * Retrieves a List of all Specialties and SubSpecialties that exist under a particular certification board.
     *
     * @param board the Certification Board to search under.
     * @return the List of Specialties and Subspecialties.
     */
    List<CertifiedSpecialtyType> getSpecialtiesByBoard(CertifiedSpecialtyBoard board);

    /**
     * Returns a list of profiles that match the criteria provided in the searchPerson parameter. Currently only name
     * and email are supported (substrings will be matched for these).
     *
     * @param searchTerm search term
     * @return the list of matching profiles ordered by person name.
     */
    List<InvestigatorProfile> search(String searchTerm);

    /**
     * Returns a list of CTEP investigator profiles that match the criteria provided in the searchPerson parameter.
     * Currently only name, email, and CTEP ID are supported (substrings will be matched for these).
     *
     * @param searchTerm search term
     * @return the list of matching profiles ordered by person name.
     */
    List<InvestigatorProfile> searchForCtepProfiles(String searchTerm);

    /**
     * Saves the profile and updates the forms with the given form types to revised of registrations with a returned
     * status.
     *
     * @param profile profile to save
     * @param formTypesToSetToRevised the form types to set to revised. Only modifies forms that are part of returned
     *            registrations.
     * @return the id of the newly saved object
     */
    Long save(InvestigatorProfile profile, FormTypeEnum... formTypesToSetToRevised);

    /**
     * Deletes a users certificate.
     *
     * @param profile profile
     * @param certificate certificate to delete
     */
    void deleteCertificate(InvestigatorProfile profile, TrainingCertificate certificate);

    /**
     * Associates the person to the investigator profile as a sub investigator.
     *
     * @param profile the investigator profile
     * @param person the person to associate as a sub investigator
     * @return the newly created sub investigator association.
     * @throws AssociationAlreadyExistsException if this person is already a sub investigator for this investigator
     *             profile
     * @throws ValidationException If NES validation errors occur
     */
    SubInvestigator addSubInvestigator(InvestigatorProfile profile, Person person)
            throws AssociationAlreadyExistsException, ValidationException;

    /**
     * Removes a sub investigator association from the profile.
     *
     * @param profile the investigator profile
     * @param subInvestigator the sub investigator association
     */
    void deleteAssociatedSubInvestigator(InvestigatorProfile profile, SubInvestigator subInvestigator);

    /**
     * Associates the person to the investigator profile as an ordering designee.
     *
     * @param profile the investigator profile
     * @param person the person to associate as an ordering designee
     * @return the newly created ordering designee association.
     * @throws AssociationAlreadyExistsException if this person is already an ordering designee for this investigator
     *             profile
     * @throws ValidationException If NES validation errors occur
     */
    OrderingDesignee addOrderingDesignee(InvestigatorProfile profile, Person person)
            throws AssociationAlreadyExistsException, ValidationException;

    /**
     * @param profile the investigator profile
     * @param shippingDesignee the shipping designee to set
     * @throws ValidationException If NES validation errors occur
     */
    void setShippingDesignee(InvestigatorProfile profile, ShippingDesignee shippingDesignee) throws ValidationException;

    /**
     * Removes an ordering designee association from the profile.
     *
     * @param profile the investigator profile
     * @param orderingDesignee the ordering designee association
     */
    void deleteAssociatedOrderingDesignee(InvestigatorProfile profile, OrderingDesignee orderingDesignee);

    /**
     * Removes the shipping designee association from the profile.
     *
     * @param profile the investigator profile
     * @param shippingDesignee the shipping designee association
     */
    void deleteAssociatedShippingDesignee(InvestigatorProfile profile, ShippingDesignee shippingDesignee);

}