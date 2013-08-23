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

import gov.nih.nci.firebird.common.FirebirdDateUtils;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.data.AbstractCredential;
import gov.nih.nci.firebird.data.Country;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.data.State;
import gov.nih.nci.firebird.exception.CredentialAlreadyExistsException;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.GenericDataRetrievalService;
import gov.nih.nci.firebird.service.investigatorprofile.InvestigatorProfileService;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.lookup.StateLookupService;
import gov.nih.nci.firebird.service.organization.InvalidatedOrganizationException;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Action class to handle the back and forth of dealing with credential edits.
 */
abstract class AbstractManageCredentialsAction extends AbstractProfileAction {
    private static final long serialVersionUID = 1L;

    private final GenericDataRetrievalService dataService;
    private final StateLookupService stateLookup;
    private final CountryLookupService countryLookup;
    private List<Country> countries;
    private List<State> states;
    private String page;
    private String effectiveDate;
    private String expirationDate;
    private AbstractCredential<?> credential;
    private String issuingOrganizationExternalId;
    private Long id;
    private List<AbstractCredential<?>> existingCredentials = Lists.newArrayList();

    /**
     * Constructor.
     *
     * @param profileService the profileService to set
     * @param dataService the GenericDataRetrievalService to set
     * @param stateLookup the stateLookupService to set
     * @param countryLookup country lookup service.
     */
    @Inject
    public AbstractManageCredentialsAction(InvestigatorProfileService profileService,
            GenericDataRetrievalService dataService, StateLookupService stateLookup,
            CountryLookupService countryLookup) {
        super(profileService);
        this.dataService = dataService;
        this.stateLookup = stateLookup;
        this.countryLookup = countryLookup;
    }

    @Override
    public void prepare() {
        super.prepare();
        Organization issuer = findIssuer();
        findCredential();
        if (credential != null && getProfile() != null) {
            existingCredentials = Lists.newArrayList(getProfile().getCredentials(credential.getType()));
        }
        initializeStringDates();
        if (issuer != null) {
            credential.setIssuer(issuer);
        }
        states = stateLookup.getAll();
        countries = countryLookup.getAll();
    }

    private Organization findIssuer() {
        Organization issuer = null;
        if (StringUtils.isNotEmpty(getIssuingOrganizationExternalId())) {
            try {
                issuer = getOrganizationService().getByExternalId(getIssuingOrganizationExternalId());
            } catch (InvalidatedOrganizationException e) {
                addActionError(getText("organization.search.selected.organization.unavailable"));
            }
        }
        return issuer;
    }

    private void findCredential() {
        if (getId() != null) {
            credential = getProfile().getCredential(getId());
        }
    }

    private void initializeStringDates() {
        if (getCredential() != null) {
            effectiveDate = formatDate(credential.getEffectiveDate());
            expirationDate = formatDate(credential.getExpirationDate());
        }
    }

    /**
     * Common Save method for all Credential Types.
     *
     * @return the results of the save
     * @throws ValidationException if nes validation errors occurred.
     */
    protected String saveCredential() throws ValidationException {
        try {
            getProfileService().saveCredential(getProfile(), credential, FormTypeEnum.CV);
        } catch (CredentialAlreadyExistsException ae) {
            handleCredentialAlreadyExists();
            return INPUT;
        }
        return closeDialog();
    }

    /**
     * creates the Action error for if a credential already exists.
     */
    protected void handleCredentialAlreadyExists() {
        addActionError(getText("error.credentials.already.exists", new String[] {getCredential().getType()
                .getDisplay() }));
    }

    /**
     * Delete a Credential from the investigator profile.
     *
     * @param formTypesToSetToRevised the form types to set to revised. Only modifies forms that are part of returned
     *            registrations.
     * @return the struts forward.
     */
    protected String deleteCredential(FormTypeEnum... formTypesToSetToRevised) {
        getProfile().getCredentials().remove(credential);
        getProfileService().save(getProfile(), formTypesToSetToRevised);
        return closeDialog();
    }

    /**
     * @return the dataService
     */
    protected GenericDataRetrievalService getDataService() {
        return dataService;
    }

    /**
     * @return the credential
     */
    public AbstractCredential<?> getCredential() {
        return credential;
    }

    /**
     * @param credential the credential to set
     */
    protected void setCredential(AbstractCredential<?> credential) {
        this.credential = credential;
    }

    /**
     * @param page the page to set
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * @return the page
     */
    public String getPage() {
        return page;
    }

    /**
     * @param effectiveDate the effectiveDate to set
     */
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * @return the effectiveDate
     */
    public String getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * returns the parsed effective date using the final date format.
     *
     * @return the parsed effective date.
     * @throws ParseException When effective date is not valid.
     */
    public Date getParsedEffectiveDate() throws ParseException {
        return FirebirdDateUtils.parseMonthAndYearStringOrNull(getEffectiveDate());
    }

    /**
     * @return credential expiration date.
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate credential expiration date.
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * returns the parsed expiration date using the final date format.
     *
     * @return the parsed expiration date.
     * @throws ParseException When expiration date is not valid.
     */
    public Date getParsedExpirationDate() throws ParseException {
        return FirebirdDateUtils.parseMonthAndYearStringOrNull(getExpirationDate());
    }

    /**
     * @throws ParseException When dates are not valid
     * @throws ValidationException if Expiration date is before Effective date
     */
    protected void validateExpirationAfterEffectiveDates() throws ParseException, ValidationException {
        if (isEffectiveBeforeOrEqualToExpirationDate(getParsedEffectiveDate(), getParsedExpirationDate())) {
            throw new ValidationException(new ValidationFailure("expirationDate",
                    getText("error.expiration.date.before.effective")));
        }
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the dialog id the content was opened in.
     */
    public String getDialogId() {
        return "profileDialog";
    }

    /**
     * @return the issuingOrganizationExternalId
     */
    public String getIssuingOrganizationExternalId() {
        return issuingOrganizationExternalId;
    }

    /**
     * @param issuingOrganizationExternalId the issuingOrganizationExternalId to set
     */
    public void setIssuingOrganizationExternalId(String issuingOrganizationExternalId) {
        this.issuingOrganizationExternalId = issuingOrganizationExternalId;
    }

    /**
     * @return the list of US states
     */
    public List<State> getStates() {
        return states;
    }

    /**
     * @return the list of all countries.
     */
    public List<Country> getCountries() {
        return countries;
    }

    /**
     * @return the existingCredentials
     */
    List<AbstractCredential<?>> getExistingCredentials() {
        return existingCredentials;
    }

}