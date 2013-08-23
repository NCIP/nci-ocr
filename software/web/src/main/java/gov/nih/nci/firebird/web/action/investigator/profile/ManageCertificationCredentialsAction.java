/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The NCI OCR
 * Software was developed in conjunction with the National Cancer Institute
 * (NCI) by NCI employees and 5AM Solutions, Inc. (5AM). To the extent
 * government employees are authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 *
 * This NCI OCR Software License (the License) is between NCI and You. You (or
 * Your) shall mean a organization or an entity, and all other entities that control,
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
package gov.nih.nci.firebird.web.action.investigator.profile;

import gov.nih.nci.firebird.data.Certification;
import gov.nih.nci.firebird.data.CertificationType;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.GenericDataRetrievalService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.google.inject.Inject;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action class to handle the back and forth of dealing with credential edits.
 */
@Namespace("/investigator/profile/credentials/ajax")
@InterceptorRef(value = "profileManagementStack")
public class ManageCertificationCredentialsAction extends AbstractManageCredentialsAction {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     *
     * @param profileService the profileService to set
     * @param dataService the GenericDataRetrievalService to set
     * @param stateLookup the stateLookupService to set
     * @param countryLookup country lookup service.
     */
    @Inject
    public ManageCertificationCredentialsAction(InvestigatorProfileService profileService,
            GenericDataRetrievalService dataService, StateLookupService stateLookup,
            CountryLookupService countryLookup) {
        super(profileService, dataService, stateLookup, countryLookup);
    }

    /**
     * @return the struts forward.
     */
    @Action(value = "manageCertificationCredentials",
            results = {
                    @Result(name = FirebirdUIConstants.RETURN_FIELDS_PAGE,
                            location = "manage_credentials_certification.jsp"),
                    @Result(name = FirebirdUIConstants.RETURN_DELETE_CONFIRM_PAGE,
                            location = "manage_credentials_certification_delete.jsp") })
    public String manageCredentialsAjaxEnter() {
        if (getPage() == null) {
            setPage(FirebirdUIConstants.RETURN_FIELDS_PAGE);
        }
        return getPage();
    }

    /**
     * Save the new certification.
     *
     * @return the struts forward
     * @throws ParseException when date format is incorrect
     */
    @Validations(requiredFields = {
                    @RequiredFieldValidator(fieldName = "certificationTypeId",
                            key = "error.credentials.certification.type.required"),
                    @RequiredFieldValidator(fieldName = "effectiveDate",
                            key = "error.effective.date.required") },
            regexFields = {
                    @RegexFieldValidator(fieldName = "effectiveDate", key = "error.date.month.year.format",
                            regexExpression = "[01]?\\d/\\d{4}"),
                    @RegexFieldValidator(fieldName = "expirationDate", key = "error.date.month.year.format",
                            regexExpression = "[01]?\\d/\\d{4}") },
            fieldExpressions = {
                    @FieldExpressionValidator(
                            fieldName = "expirationDate",
                            expression = "certificationTypeId != null "
                                + "&& certification.certificationType.expires ? "
                                + "(expirationDate != null && expirationDate.trim() != '') : true",
                            key = "error.credentials.certification.expiration.date.required") })
    @Action(value = "saveCertification", results = {
            @Result(name = INPUT, location = "manage_credentials_certification.jsp") })
    public String saveCertification() throws ParseException {
        getCertification().setEffectiveDate(getParsedEffectiveDate());
        try {
            if (!StringUtils.isEmpty(getExpirationDate())) {
                getCertification().setExpirationDate(getParsedExpirationDate());
                validateExpirationAfterEffectiveDates();
            }
            return saveCredential();
        } catch (ValidationException e) {
            return handleValidationException(e);
        }
    }

    /**
     * Sends a call to delete the selected credential.
     *
     * @return the struts forward.
     */
    @Action("deleteCertification")
    public String deleteCertification() {
        return deleteCredential(FormTypeEnum.CV);
    }

    /**
     * Returns a list of all certifications with the ranked certifications also at the top of the list.
     *
     * @return certifications with ranked on top
     */
    public List<CertificationType> getCertificationsWithRankedOnTop() {
        List<CertificationType> certifications = getRankedCertifications();
        certifications.addAll(getCertifications());
        return certifications;
    }

    private List<CertificationType> getCertifications() {
        return getDataService().getAllSorted(CertificationType.class);
    }

    /**
     * @return the ranked certification
     */
    public List<CertificationType> getRankedCertifications() {
        return getDataService().getAllRanked(CertificationType.class);
    }

    /**
     * @param certification the Certification credential to set
     */
    public void setCertification(Certification certification) {
        setCredential(certification);
    }

    /**
     * @return the credential as a certification
     */
    public Certification getCertification() {
        if (getCredential() instanceof Certification) {
            return (Certification) getCredential();
        } else {
            return null;
        }
    }

    /**
     * @return the typeId
     */
    public Long getCertificationTypeId() {
        return (getCertification().getCertificationType() == null) ? null : getCertification().getCertificationType()
                .getId();
    }

    /**
     * @param typeId the certification type id
     */
    public void setCertificationTypeId(Long typeId) {
        if (getCertification() == null) {
            setCertification(new Certification());
        }
        getCertification().setCertificationType(getDataService().getPersistentObject(CertificationType.class, typeId));
    }
}