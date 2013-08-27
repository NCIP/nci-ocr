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

import gov.nih.nci.firebird.data.Degree;
import gov.nih.nci.firebird.data.DegreeType;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.GenericDataRetrievalService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;

import java.text.ParseException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.google.inject.Inject;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action class to handle the back and forth of dealing with credential edits.
 */
@Namespace("/investigator/profile/credentials/ajax")
@InterceptorRef("profileManagementStack")
public class ManageDegreeCredentialsAction extends AbstractManageCredentialsAction {
    private static final long serialVersionUID = 1L;

    private static final String ISSUER_RESOURCE = "degree.issuer";

    /**
     * Constructor.
     *
     * @param profileService the profileService to set
     * @param stateLookup the stateLookupService to set
     * @param countryLookup country lookup service.
     * @param dataService the GenericDataRetrievalService to set
     */
    @Inject
    public ManageDegreeCredentialsAction(InvestigatorProfileService profileService,
            StateLookupService stateLookup,
            CountryLookupService countryLookup,
            GenericDataRetrievalService dataService) {
        super(profileService, dataService, stateLookup, countryLookup);
    }

    @Override
    public void prepare() {
        if (getDegree() == null) {
            setDegree(new Degree());
            getDegree().setIssuer(new Organization());
        }
        super.prepare();
    }

    /**
     * Using the credentialType parameter as a decider, this method will route traffic to the correct jsp page. If
     * provided an invalid credential type it will route to the close dialog jsp.
     *
     * @return the struts forward.
     */
    @Action(value = "manageDegreeCredentials", results = {
            @Result(name = FirebirdUIConstants.RETURN_SEARCH_PAGE, location = "manage_credentials_degree_search.jsp"),
            @Result(name = FirebirdUIConstants.RETURN_FIELDS_PAGE, location = "manage_credentials_degree_fields.jsp"),
            @Result(name = FirebirdUIConstants.RETURN_DELETE_CONFIRM_PAGE,
                    location = "manage_credentials_degree_delete.jsp") })
    public String manageCredentialsAjaxEnter() {
        if (getPage() == null || FirebirdUIConstants.RETURN_SEARCH_PAGE.equals(getPage())) {
            setPage(FirebirdUIConstants.RETURN_SEARCH_PAGE);
            setIssuerSearchKey(null);
        }

        return getPage();
    }

    /**
     * Save the new degree.
     *
     * @return the struts forward
     * @throws ParseException when date format is incorrect
     */
    @Validations(
            customValidators = { @CustomValidator(type = "hibernate", fieldName = "degree.issuer", parameters = {
                @ValidationParameter(name = "resourceKeyBase", value = "profile.organization"),
                @ValidationParameter(name = "excludes", value = "nesId") }) },
            requiredStrings = { @RequiredStringValidator(fieldName = "effectiveDate",
                    key = "error.effective.date.required.degree") },
            requiredFields = { @RequiredFieldValidator(
                    fieldName = "degreeTypeId", key = "error.credentials.degree.type.required") },
            fieldExpressions = {
                    @FieldExpressionValidator(
                            fieldName = "degree.issuer.postalAddress.stateOrProvince",
                            expression = "degree.issuer.postalAddress.stateOrProvinceValid",
                            key = "stateOrProvince.required") },
            regexFields = { @RegexFieldValidator(fieldName = "effectiveDate", key = "error.date.month.year.format",
                    regexExpression = "[01]?\\d/\\d{4}") })
    @Action(value = "saveDegree",
            results = { @Result(name = INPUT, location = "manage_credentials_degree_fields.jsp") })
    public String saveDegree() throws ParseException {
        getDegree().setEffectiveDate(getParsedEffectiveDate());

        try {
            return saveCredential();
        } catch (ValidationException e) {
            return handleValidationException(e, ISSUER_RESOURCE);
        }
    }

    /**
     * Delete the selected Degree.
     * @return the struts forward.
     */
    @Action("deleteDegree")
    public String deleteDegree() {
        return deleteCredential(FormTypeEnum.CV);
    }

    /**
     * @param degree the Degree credential to set
     */
    public void setDegree(Degree degree) {
        setCredential(degree);
    }

    /**
     * @return the credential as a degree
     */
    public Degree getDegree() {
        if (getCredential() instanceof Degree) {
            return (Degree) getCredential();
        } else {
            return null;
        }
    }

    /**
     * Returns a list of all degrees with the ranked degrees also at the top of the list.
     *
     * @return degrees with ranked on top
     */
    public List<DegreeType> getDegreesWithRankedOnTop() {
        List<DegreeType> degrees = getRankedDegrees();
        degrees.addAll(getDegrees());
        return degrees;
    }

    private List<DegreeType> getDegrees() {
        return getDataService().getAllSorted(DegreeType.class);
    }

    /**
     * @return the ranked degrees
     */
    public List<DegreeType> getRankedDegrees() {
        return getDataService().getAllRanked(DegreeType.class);
    }

    /**
     * @param degreeTypeId the degreeType to set
     */
    public void setDegreeTypeId(Long degreeTypeId) {
        if (degreeTypeId != null) {
            DegreeType newType = getDataService().getPersistentObject(DegreeType.class, degreeTypeId);
            getDegree().setDegreeType(newType);
        } else {
            getDegree().setDegreeType(null);
        }
    }

    /**
     * @return the degreeType
     */
    public Long getDegreeTypeId() {
        if (getDegree() == null) {
            return null;
        }

        return getDegree().getDegreeType().getId();
    }

}