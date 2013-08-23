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
package gov.nih.nci.firebird.web.action;

import gov.nih.nci.firebird.data.AbstractRegistration;
import gov.nih.nci.firebird.data.AbstractRegistrationForm;
import gov.nih.nci.firebird.data.ProtocolForm1572;
import gov.nih.nci.firebird.data.FormStatus;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.InvestigatorRegistration;
import gov.nih.nci.firebird.service.registration.BaseRegistrationService;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.Preparable;

/**
 * Download a signed or generated form file.
 */
public abstract class AbstractFormDownloadAction extends FirebirdActionSupport 
implements ServletResponseAware, Preparable {

    private static final long serialVersionUID = 1L;
    private static final String FILE_ATTRIBUTE_NAME = "file";
    private AbstractRegistration registration = new InvestigatorRegistration();
    private AbstractRegistrationForm registrationForm = new ProtocolForm1572();
    private HttpServletResponse servletResponse;


    @Override
    public void prepare() {
        lookupRegistration();
        lookupRegistrationForm();
    }

    private void lookupRegistration() {
        if (getRegistration() != null && getRegistration().getId() != null) {
            setRegistration(getRegistrationService().getById(getRegistration().getId()));
        }        
        if (getRegistration() == null) {
            throw new IllegalArgumentException("Invalid Registration Id!");
        }
    }
    
    /**
     * @return the registration service associated with this action.
     */
    protected abstract BaseRegistrationService<? extends AbstractRegistration> getRegistrationService();

    private void lookupRegistrationForm() {
        if (getRegistrationForm() != null && getRegistrationForm().getId() != null) {
            Long formIdToLookup = getRegistrationForm().getId();
            AbstractRegistrationForm form = getRegistration().findFormById(formIdToLookup);
            if (form != null) {
                setRegistrationForm(form);
            } else {
                throw new IllegalArgumentException("Could not find a form using the id provided!");
            }
        } else {
            throw new IllegalArgumentException("Invalid Form ID provided!");
        }
    }

    /**
     * Sends an output stream of the requested form back to the user. 
     * 
     * @return null
     * @throws IOException if a problem occurs with the output stream.
     */
    @Action(value = "downloadForm", results = { @Result(name = SUCCESS, type = "chain", params = { "actionName",
            "downloadFile", "namespace", "/util/ajax" }) })
    public String downloadRegistrationForm() throws IOException {
        if (displayGeneratedFile()) {
            getRequest().setAttribute(FILE_ATTRIBUTE_NAME, getRegistrationForm().getFileToReview());
            return SUCCESS;
        } else {
            return viewPdfInProgress(getRegistrationForm());
        }
    }

    private boolean displayGeneratedFile() {
        boolean isInactive = FormStatus.INACTIVE == getRegistrationForm().getFormStatus();
        boolean isLocked = getRegistration().isLockedForInvestigator();
        boolean isUploadedCertificate = getRegistrationForm().getFormType().getFormTypeEnum() 
            == FormTypeEnum.HUMAN_RESEARCH_CERTIFICATE;
        return isUploadedCertificate || (isLocked && !isInactive);
    }

    /**
     * Common method to retrieve a document from a form and write it to the output stream.
     * 
     * @param form the form to output
     * @return NONE
     * @throws IOException if a problem occurs writing to the output stream.
     */
    private String viewPdfInProgress(AbstractRegistrationForm form) throws IOException {
        String filename = form.getFormFileBaseName() + ".pdf";
        ServletOutputStream servletOutputStream = preparePdfOutputStream(filename);
        getRegistrationService().generatePdf(form, servletOutputStream);
        servletOutputStream.flush();
        return NONE;
    }

    /**
     * Prepares the servlet output stream to write file content.
     * 
     * @param filename the name of the PDF file
     * @return the servlet output stream
     * @throws IOException if a problem occurs writing to the output stream.
     */
    private ServletOutputStream preparePdfOutputStream(String filename) throws IOException {
        servletResponse.setContentType("application/pdf");
        servletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        servletResponse.setHeader("Pragma", "private");
        servletResponse.setHeader("Cache-Control", "private, must-revalidate");
        return servletResponse.getOutputStream();
    }

    /**
     * @return the registrationForm
     */
    public AbstractRegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    /**
     * @param registrationForm the registrationForm to set
     */
    public void setRegistrationForm(AbstractRegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    /**
     * @param registration the registration to set
     */
    public void setRegistration(AbstractRegistration registration) {
        this.registration = registration;
    }

    /**
     * @return the registration
     */
    public AbstractRegistration getRegistration() {
        return registration;
    }

    /**
     * @param httpResponse http response to write file to.
     */
    public void setServletResponse(HttpServletResponse httpResponse) {
        this.servletResponse = httpResponse;
    }
}