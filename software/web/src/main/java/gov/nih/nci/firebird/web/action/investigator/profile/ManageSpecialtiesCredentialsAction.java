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

import gov.nih.nci.firebird.data.BoardCertifiedSpecialty;
import gov.nih.nci.firebird.data.CertifiedSpecialtyBoard;
import gov.nih.nci.firebird.data.CertifiedSpecialtyType;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.GenericDataRetrievalService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

/**
 * Action class to handle the back and forth of dealing with credential edits.
 */
@Namespace("/investigator/profile/credentials/ajax")
@InterceptorRef(value = "profileManagementStack")
public class ManageSpecialtiesCredentialsAction extends AbstractManageCredentialsAction {
    private static final long serialVersionUID = 1L;

    private Map<String, List<CertifiedSpecialtyType>> jsonResult;
    private CertifiedSpecialtyBoard board;

    private final List<CertifiedSpecialtyType> emptyList = Collections.emptyList();

    /**
     * Constructor.
     *
     * @param profileService the profileService to set
     * @param dataService the GenericDataRetrievalService to set
     * @param stateLookup the stateLookupService to set
     * @param countryLookup country lookup service.
     */
    @Inject
    public ManageSpecialtiesCredentialsAction(InvestigatorProfileService profileService,
            GenericDataRetrievalService dataService, StateLookupService stateLookup,
            CountryLookupService countryLookup) {
        super(profileService, dataService, stateLookup, countryLookup);
    }

    /**
     * Using the credentialType parameter as a decider, this method will route traffic to the correct jsp page. If
     * provided an invalid credential type it will route to the close dialog jsp.
     *
     * @return the struts forward.
     */
    @Action(value = "manageSpecialtyCredentials",
            results = {
                    @Result(name = FirebirdUIConstants.RETURN_FIELDS_PAGE,
                            location = "manage_credentials_specialty.jsp"),
                    @Result(name = FirebirdUIConstants.RETURN_DELETE_CONFIRM_PAGE,
                            location = "manage_credentials_specialty_delete.jsp") })
    public String manageCredentialsAjaxEnter() {
        if (getPage() == null) {
            return FirebirdUIConstants.RETURN_FIELDS_PAGE;
        }

        if (getSpecialty() != null) {
            board = getSpecialty().getSpecialtyType().getBoard();
        }

        return getPage();
    }

    /**
     * Save the new specialty.
     *
     * @return the struts forward
     * @throws ParseException when date format is incorrect
     */
    @Validations(
            requiredFields = {
                    @RequiredFieldValidator(fieldName = "specialtyTypeId",
                            key = "error.credentials.specialty.type.required"),
                    @RequiredFieldValidator(fieldName = "effectiveDate",
                            key = "error.effective.date.required"),
                            @RequiredFieldValidator(fieldName = "expirationDate",
                            key = "error.expiration.date.required"),
                    @RequiredFieldValidator(fieldName = "specialty.status",
                            key = "error.credentials.status.required"),
                    @RequiredFieldValidator(fieldName = "specialtyBoardId",
                            key = "error.credentials.specialty.board.required") },
            regexFields = {
                    @RegexFieldValidator(fieldName = "effectiveDate", key = "error.date.month.year.format",
                            regexExpression = "[01]?\\d/\\d{4}"),
                    @RegexFieldValidator(fieldName = "expirationDate", key = "error.date.month.year.format",
                            regexExpression = "[01]?\\d/\\d{4}") })
    @Action(value = "saveSpecialty", results = { @Result(name = INPUT, location = "manage_credentials_specialty.jsp") })
    public String saveSpecialty() throws ParseException {
        getSpecialty().setEffectiveDate(getParsedEffectiveDate());
        getSpecialty().setExpirationDate(getParsedExpirationDate());

        try {
            validateExpirationAfterEffectiveDates();
            return saveCredential();
        } catch (ValidationException e) {
            return handleValidationException(e);
        }
    }

    /**
     * Delete the selected Specialty.
     * @return the struts forward.
     */
    @Action("deleteSpecialty")
    public String deleteSpecialty() {
        return deleteCredential(FormTypeEnum.CV);
    }

    /**
     * Method to support the AJAX call to retrieve a list of Specialty Boards.
     *
     * @return the JSON list result of boards
     */
    public List<CertifiedSpecialtyBoard> getSpecialtyBoards() {
        return getDataService().getAllSorted(CertifiedSpecialtyBoard.class);
    }

    /**
     * Method to support the AJAX call to retrieve a list of specialties and subspecialties based on a selected Board.
     *
     * @return the JSON List of Specialties.
     */
    @Action(value = "specialtyTypes",
            results = { @Result(name = SUCCESS, type = "json",
            params = { "root", "jsonResult", "enumAsBean", "true"  }) })
    public String getSpecialties() {
        jsonResult = Maps.newHashMap();
        if (board != null) {
            jsonResult.put("specialties", getProfileService().getSpecialtiesByBoard(board));
        } else {
            jsonResult.put("specialties", emptyList);
        }
        return SUCCESS;
    }

    /**
     * @param specialty the Specialties credential to set
     */
    public void setSpecialty(BoardCertifiedSpecialty specialty) {
        setCredential(specialty);
    }

    /**
     * @return the credential as a specialty
     */
    public BoardCertifiedSpecialty getSpecialty() {
        if (getCredential() instanceof BoardCertifiedSpecialty) {
            return (BoardCertifiedSpecialty) getCredential();
        } else {
            return null;
        }
    }

    /**
     * @return the jsonResult
     */
    public Map<String, List<CertifiedSpecialtyType>> getJsonResult() {
        return jsonResult;
    }

    /**
     * @return the boardId
     */
    public Long getSpecialtyBoardId() {
        return (board == null) ? null : board.getId();
    }

    /**
     * @param boardId the boardId to set
     */
    public void setSpecialtyBoardId(Long boardId) {
        board = getDataService().getPersistentObject(CertifiedSpecialtyBoard.class, boardId);
    }

    /**
     * @return the typeId
     */
    public Long getSpecialtyTypeId() {
        return (getSpecialty().getSpecialtyType() == null) ? null : getSpecialty().getSpecialtyType().getId();
    }

    /**
     * @param typeId the boardId to set
     */
    public void setSpecialtyTypeId(Long typeId) {
        getSpecialty().setSpecialtyType(getDataService().getPersistentObject(CertifiedSpecialtyType.class, typeId));
    }


}