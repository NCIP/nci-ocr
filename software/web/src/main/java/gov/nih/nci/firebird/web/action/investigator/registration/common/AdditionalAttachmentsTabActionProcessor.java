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
package gov.nih.nci.firebird.web.action.investigator.registration.common;

import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.AdditionalAttachmentsForm;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.service.registration.BaseRegistrationService;
import gov.nih.nci.firebird.web.action.FirebirdActionSupport;
import gov.nih.nci.firebird.web.common.Struts2UploadedFileInfo;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Processor for Additional Attachments Tab actions.
 *
 * @param <T> registration type
 */
public class AdditionalAttachmentsTabActionProcessor<T extends AbstractRegistration> {

    private final BaseRegistrationService<T> registrationService;
    private final FirebirdActionSupport action;
    private T registration;

    /**
     * @param registrationService registration service
     * @param action action
     */
    public AdditionalAttachmentsTabActionProcessor(BaseRegistrationService<T> registrationService,
            FirebirdActionSupport action) {
        this.registrationService = registrationService;
        this.action = action;
    }

    /**
     * @param registration the registration to set
     */
    public void setRegistration(T registration) {
        this.registration = registration;
    }

    /**
     * Retrieves the selected file from the user's profile if it exists.
     *
     * @param selectedFile selected file
     * @return file from user's profile if it exists. Otherwise, null
     */
    public FirebirdFile findAttachedFileInProfile(FirebirdFile selectedFile) {
        for (FirebirdFile attachedFile : registration.getProfile().getUploadedFiles()) {
            if (attachedFile.getId().equals(selectedFile.getId())) {
                return attachedFile;
            }
        }
        return null;
    }

    /**
     * add a certificate to the Selected Certificates set in the human research certificate form.
     *
     * @param selectedFile selected file
     *
     * @return NONE
     */
    public String selectFile(FirebirdFile selectedFile) {
        registrationService.selectAdditionalAttachment(registration, selectedFile);
        return ActionSupport.NONE;
    }

    private AdditionalAttachmentsForm getForm() {
        return registration.getAdditionalAttachmentsForm();
    }

    /**
     * delete a file from the attached documents.
     *
     * @param selectedFile selected file
     *
     * @return NONE
     */
    public String deselectFile(FirebirdFile selectedFile) {
        registrationService.deselectAdditionalAttachment(registration, selectedFile);
        return ActionSupport.NONE;
    }

    /**
     * upload a file and add it to the profile.
     *
     * @param dataFile uploaded file's data
     * @throws IOException if the file cannot be converted into a byte[].
     */
    public void upload(Struts2UploadedFileInfo dataFile) throws IOException {
        action.resolveContentType(dataFile);
        registrationService.uploadAndSelectAdditionalAttachment(registration, dataFile.getData(),
                dataFile.getMetadata());
        action.addActionMessage(action.getText("registration.attachments.upload.file.success",
                dataFile.getDataFileName()));
    }

    /**
     * @return the JSON result of the Set of documents.
     * @throws JSONException a problem occurred when serializing.
     */
    public String getFilesAsJson() throws JSONException {
        Collection<Pattern> excludes = Lists.newArrayList(Pattern.compile(".*\\.byteDataSource"));
        Set<? extends FirebirdFile> files = getFileList();
        return JSONUtil.serialize(files, excludes, null, false, true, false);
    }

    private Set<? extends FirebirdFile> getFileList() {
        if (registration.isLockedForInvestigator()) {
            return getForm().getAdditionalAttachments();
        } else {
            return getFileListings();
        }
    }

    private Set<FileListing> getFileListings() {
        Set<FileListing> listings = Sets.newHashSet();
        for (FirebirdFile file : registration.getProfile().getUploadedFiles()) {
            boolean isSelected = getForm().getAdditionalAttachments().contains(file);
            listings.add(new FileListing(file, isSelected));
        }
        return listings;
    }

    /**
     * an inner class to provide a structure for tabular data.
     */
    public static final class FileListing extends FirebirdFile {

        private static final long serialVersionUID = 1L;
        private final boolean selected;

        FileListing(FirebirdFile file, boolean selected) {
            this.setId(file.getId());
            this.setName(file.getName());
            this.setDescription(file.getDescription());
            this.setUploadDate(file.getUploadDate());
            this.selected = selected;
        }

        /**
         * @return if the file is selected.
         */
        public boolean isSelected() {
            return selected;
        }

    }

}
