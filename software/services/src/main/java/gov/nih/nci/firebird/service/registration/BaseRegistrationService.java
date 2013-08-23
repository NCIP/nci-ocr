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

import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorProfile;
import gov.nih.nci.firebird.data.RegistrationStatus;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.GenericService;
import gov.nih.nci.firebird.service.file.FileMetadata;
import gov.nih.nci.firebird.service.messages.FirebirdMessage;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

/**
 * Registration Service.
 *
 * @param <T>
 */
@Local
@SuppressWarnings({ "PMD.TooManyMethods" })
//facade service expected to expose many methods
public interface BaseRegistrationService<T extends AbstractRegistration> extends GenericService<T> {

    /**
     * Returns all registrations with the requested status.
     * 
     * @param status get registrations with this status
     * @return the matching registrations
     */
    List<T> getByStatus(RegistrationStatus status);

    /**
     * Returns the completed, flattened version of this form as a PDF. An IllegalArgumentException will be thrown if the
     * form doesn't support a PDF output version.
     *
     * @param form generate PDF for this form.
     * @param outputStream write the PDF content to this stream
     */
    void generatePdf(AbstractRegistrationForm form, OutputStream outputStream);

    /**
     * Saves the registration and updates the forms with the given form types to revised of the registration has a
     * returned status.
     *
     * @param registration registration to save
     * @param formTypesToSetToRevised the form types to set to revised. Only modifies forms if the registration is in
     *            returned status.
     */
    void save(T registration, FormTypeEnum... formTypesToSetToRevised);

    /**
     * Sets all returned registrations forms of the given type to Revised.
     *
     * @param profile profile to retrieve registrations from
     * @param formTypes form types to change to revised
     */
    void setReturnedOrRevisedRegistrationsFormStatusesToRevised(InvestigatorProfile profile, FormTypeEnum... formTypes);

    /**
     * @param profile profile to retrieve registrations from
     * @return set of all of the current users registrations that are in the Returned status.
     */
    Set<T> getReturnedOrRevisedRegistrations(InvestigatorProfile profile);

    /**
     * Sets the status of the forms matching the given types in the passed in registrations to Revised.
     *
     * @param registrations registrations to save
     * @param formTypes the form types to set to revised.
     */
    void setRegistrationFormStatusesToRevisedIfReviewed(Set<T> registrations, FormTypeEnum... formTypes);

    /**
     * Saves the registrations.
     *
     * @param registrations registrations to save
     */
    void save(Set<T> registrations);

    /**
     * Returns all the profile's registrations of type T.
     *
     * @param profile profile to retrieve registrations from
     * @return registrations of this type
     */
    Set<T> getRegistrations(InvestigatorProfile profile);

    /**
     * Method used to add a document onto a Financial Disclosure as supplementary documentation.
     *
     * @param registration the registration to add the file to
     * @param file the file to be used for the FirebirdFile
     * @param fileMetadata the file metadata to be used for the FirebirdFile
     * @throws IOException if a problem occurs saving the file.
     */
    void addFinancialDisclosureFile(T registration, File file, FileMetadata fileMetadata) throws IOException;

    /**
     * Adds the file to the user's profile then selects it as an additional attachment in the registration.
     *
     * @param registration the registration to add the file to
     * @param file the file to be used for the FirebirdFile
     * @param fileMetadata the file metadata to be used for the FirebirdFile
     * @throws IOException if a problem occurs saving the file.
     */
    void uploadAndSelectAdditionalAttachment(T registration, File file, FileMetadata fileMetadata) throws IOException;

    /**
     * Selects the given file as an attachment for the registration.
     *
     * @param registration registration to add attachment to
     * @param attachment file to attach to registration
     */
    void selectAdditionalAttachment(T registration, FirebirdFile attachment);

    /**
     * Deselects the given attachment from the registration.
     *
     * @param registration registration to remove attachment from
     * @param attachment file to unattach to registration
     */
    void deselectAdditionalAttachment(T registration, FirebirdFile attachment);

    /**
     * Validates forms to ensure that they are complete and ready for review, updating status to Complete if they are.
     *
     * @param registration the registration to be submitted for sponsor review
     * @throws ValidationException if validation problems exist.
     */
    void checkFormCompletionStatus(T registration) throws ValidationException;

    /**
     * Validates forms which aren't reviewed or revised to ensure that they are complete and ready for review, updating
     * status to Complete if they are.
     *
     * @param registration the registration to be submitted for sponsor review
     * @throws ValidationException if validation problems exist.
     */
    void checkUnReviewedAndUnRevisedFormCompletionStatus(T registration) throws ValidationException;

    /**
     * Performs validation and marks forms and registration as COMPLETE if all validation passes. Validation errors are
     * flagged and the registration is marked INCOMPLETE otherwise.
     *
     * @param registration the registration to be submitted for sponsor review
     * @throws ValidationException if validation problems exist.
     */
    void prepareForSubmission(T registration) throws ValidationException;

    /**
     * Sends an email notifying Investigator that a Registration Coordinator has completed one of their registrations
     * and that it is now ready for signing and submission.
     *
     * @param coordinator Registration coordinator who completed the registration
     * @param registration registration
     */
    void sendCoordinatorCompletedRegistrationEmail(FirebirdUser coordinator, T registration);

    /**
     * Returns the email message that is to be sent to the investigator informing them that the registration coordinator
     * has completed there registration on their behalf.
     *
     * @param coordinator registration coordinator who completed the registration
     * @param registration registration
     * @return email message that is to be sent to the investigator
     */
    FirebirdMessage getCoordinatorCompletedRegistrationEmailMessage(FirebirdUser coordinator, T registration);

    /**
     * Signs and submits a registration for review by the sponsor.
     *
     * @param registration the registration to be submitted for review.
     * @param user the user signing the registration forms
     * @param password the user's password
     */
    void signAndSubmit(T registration, FirebirdUser user, String password);

}
