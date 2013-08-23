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
package gov.nih.nci.firebird.web.action.investigator.annual.registration;

import gov.nih.nci.firebird.data.Address;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.CtepFinancialDisclosure;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.service.annual.registration.AnnualRegistrationService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.web.action.investigator.registration.common.FinancialDisclosureTabProcessor;
import gov.nih.nci.firebird.web.common.Struts2UploadedFileInfo;

import java.util.List;
import java.util.ResourceBundle;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Financial Disclosure tab action.
 */
@Namespace("/investigator/annual/registration/ajax/financialdisclosure")
@Result(location = "financial_disclosure_tab.jsp")
@InterceptorRef("registrationManagementStack")
public class FinancialDisclosureTabAction extends AbstractAnnualRegistrationTabAction {

    private static final long serialVersionUID = 1L;

    private Long fileId;
    private Struts2UploadedFileInfo document;
    private FinancialDisclosureTabProcessor<AnnualRegistration> processor =
            new FinancialDisclosureTabProcessor<AnnualRegistration>(null, null);

    /**
     * Creates an action instance.
     *
     * @param registrationService registration service
     * @param profileService profile service
     * @param resources FIREBIRD resource bundle
     */
    @Inject
    public FinancialDisclosureTabAction(AnnualRegistrationService registrationService,
            InvestigatorProfileService profileService, ResourceBundle resources) {
        super(registrationService, profileService, resources);
        this.processor = new FinancialDisclosureTabProcessor<AnnualRegistration>(registrationService, this);
    }

    @Override
    public void prepare() {
        super.prepare();
        if (document != null) {
            resolveContentType(document);
        }
        processor.setRegistration(getRegistration());
        if (getRegistration() != null) {
            preloadPharmaceuticalCompanies();
        }
    }


    /**
     * Without doing this, calling getPharmaceuticalCompaniesJson after an ajax tab reload will fail. No managed
     * connection.
     */
    private void preloadPharmaceuticalCompanies() {
        for (Organization pharmaceuticalCompany : getForm().getPharmaceuticalCompanies()) {
            pharmaceuticalCompany.getId();
        }
    }

    /**
     * Saves the Financial Disclosure.
     *
     * @return SUCCESS
     */
    @Action("view")
    public String viewFinancialDisclosure() {
        return processor.viewFinancialDisclosure();
    }

    /**
     * Saves the Financial Disclosure.
     *
     * @return SUCCESS
     */
    @Action("save")
    public String saveFinancialDisclosure() {
        return processor.saveFinancialDisclosure();
    }

    /**
     * delete a document from the financial disclosure.
     *
     * @return SUCCESS
     */
    @Action("delete")
    public String deleteDocument() {
        return processor.deleteDocument(getFileId());
    }

    /**
     * add a document to the financial disclosure.
     *
     * @return SUCCESS
     */
    @Action(value = "addDocument", results = @Result(name = INPUT, location = "financial_disclosure_tab.jsp"))
    @Validations(requiredFields =
    @RequiredFieldValidator(fieldName = "document.data", key = "investigator.profile.fileNotSelected"))
    public String addDocument() {
        return processor.addDocument(getDocument());
    }

    /**
     * @return the JSON result of the Set of documents.
     * @throws JSONException a problem occurred when serializing.
     */
    public String getFilesAsJson() throws JSONException {
        return processor.getFilesAsJson();
    }

    /**
     * Return a JSON string representation of the Pharmaceutical Companies.
     *
     * @return the JSON String representation of the Pharmaceutical Companies.
     * @throws JSONException if a problem occurs.
     */
    public String getPharmaceuticalCompaniesJson() throws JSONException {
        return JSONUtil.serialize(getPharmaceuticalCompanyListings());
    }

    private List<PharmaceuticalCompanyListings> getPharmaceuticalCompanyListings() {
        List<PharmaceuticalCompanyListings> pharmaceuticalCompanyListings = Lists.newArrayList();
        for (Organization organization : getForm().getPharmaceuticalCompanies()) {
            pharmaceuticalCompanyListings.add(new PharmaceuticalCompanyListings(organization));
        }
        return pharmaceuticalCompanyListings;
    }

    /**
     * @return the fileId
     */
    public Long getFileId() {
        return fileId;
    }

    /**
     * @param fileId the fileId to set
     */
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    /**
     * @return the document
     */
    public Struts2UploadedFileInfo getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(Struts2UploadedFileInfo document) {
        this.document = document;
    }

    @Override
    public CtepFinancialDisclosure getForm() {
        return getRegistration().getFinancialDisclosure();
    }

    /**
     * @param processor the processor to set
     */
    @SuppressWarnings("ucd")
    // used to inject mock processor from tests
    void setProcessor(FinancialDisclosureTabProcessor<AnnualRegistration> processor) {
        this.processor = processor;
    }

    /**
     * Table listing object for Pharmaceutical Companies.
     */
    @SuppressWarnings("ucd")
    // needs to be protected for JSONUtil.serialize()
    protected final class PharmaceuticalCompanyListings {
        private final String externalId;
        private final String name;
        private final String ctepId;
        private final String email;
        private final String phoneNumber;
        private final Address postalAddress;

        /**
         * @param pharmaceuticalCompany Pharmaceutical Company
         */
        PharmaceuticalCompanyListings(Organization pharmaceuticalCompany) {
            this.externalId = pharmaceuticalCompany.getExternalId();
            this.name = pharmaceuticalCompany.getName();
            this.ctepId = pharmaceuticalCompany.getCtepId();
            this.email = pharmaceuticalCompany.getEmail();
            this.phoneNumber = pharmaceuticalCompany.getPhoneNumber();
            this.postalAddress = pharmaceuticalCompany.getPostalAddress();
        }

        public String getExternalId() {
            return externalId;
        }

        public String getName() {
            return name;
        }

        public String getCtepId() {
            return ctepId;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public Address getPostalAddress() {
            return postalAddress;
        }
    }

}
