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
package gov.nih.nci.firebird.selenium2.pages.login;

import gov.nih.nci.firebird.data.Organization;
import gov.nih.nci.firebird.selenium2.pages.base.AbstractFirebirdPage;
import gov.nih.nci.firebird.selenium2.pages.base.MessageHandler;
import gov.nih.nci.firebird.selenium2.pages.components.tags.AddressFormTag;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * /user/request_account.jsp
 */
public class RequestAccountPage extends AbstractFirebirdPage<RequestAccountPage> {

    private static final String PERSON_FIELD_PREFIX = "newAccount.person";
    private static final String HAS_LDAP_ACCOUNT_RB = "existingLdapAccountSelect_yes";
    private static final String DOES_NOT_HAVE_LDAP_ACCOUNT_RB = "existingLdapAccountSelect_no";
    private static final String USERNAME_TEXTFIELD_ID = "username";
    private static final String PASSWORD_TEXTFIELD_ID = "password";
    private static final String FIRST_NAME_TEXTFIELD_ID = "newAccount.personFirstName";
    private static final String LAST_NAME_TEXTFIELD_ID = "newAccount.personLastName";
    private static final String EMAIL_TEXTFIELD_ID = "newAccount.personEmail";
    private static final String PHONE_NUMBER_TEXTFIELD_ID = "newAccount.person.phoneNumber";
    private static final String ORGANIZATION_TEXTFIELD_ID = "newAccount.primaryOrganization.organization.name";
    private static final String REQUEST_ACCOUNT_BUTTON_ID = "requestAccountButton";

    private static final String ROLE_SELECTION_CHECKBOX_LOCATOR_PREFIX = "input[name='newAccount.roles'][value='";
    private static final String ROLE_SELECTION_CHECKBOX_LOCATOR_SUFFIX = "']";
    private static final String INVESTIGATOR_LOCATOR = ROLE_SELECTION_CHECKBOX_LOCATOR_PREFIX + "INVESTIGATOR"
            + ROLE_SELECTION_CHECKBOX_LOCATOR_SUFFIX;
    private static final String COORDINATOR_LOCATOR = ROLE_SELECTION_CHECKBOX_LOCATOR_PREFIX
            + "REGISTRATION_COORDINATOR" + ROLE_SELECTION_CHECKBOX_LOCATOR_SUFFIX;

    private static final String SPONSOR_ROLE_PREFIX = "sponsor_";
    private static final String DELEGATE_ROLE_PREFIX = "sponsorDelegate_";

    @FindBy(id = HAS_LDAP_ACCOUNT_RB)
    private WebElement hasLdapAccountRadio;
    @FindBy(id = DOES_NOT_HAVE_LDAP_ACCOUNT_RB)
    private WebElement doesNotHaveLdapAccountRadio;
    @FindBy(id = USERNAME_TEXTFIELD_ID)
    private WebElement usernameField;
    @FindBy(id = PASSWORD_TEXTFIELD_ID)
    private WebElement passwordField;

    @FindBy(id = FIRST_NAME_TEXTFIELD_ID)
    private WebElement firstNameField;
    @FindBy(id = LAST_NAME_TEXTFIELD_ID)
    private WebElement lastNameField;
    @FindBy(id = EMAIL_TEXTFIELD_ID)
    private WebElement emailField;
    @FindBy(id = PHONE_NUMBER_TEXTFIELD_ID)
    private WebElement phoneNumberField;

    @FindBy(id = ORGANIZATION_TEXTFIELD_ID)
    private WebElement organizationField;
    @FindBy(css = INVESTIGATOR_LOCATOR)
    private WebElement investigatorCheckbox;
    @FindBy(css = COORDINATOR_LOCATOR)
    private WebElement coordinatorCheckbox;
    @FindBy(id = REQUEST_ACCOUNT_BUTTON_ID)
    private WebElement requestAccountButton;

    private final AddressFormTag addressFormTag;
    private final RequestAccountPageHelper helper = new RequestAccountPageHelper(this);

    protected RequestAccountPage(WebDriver driver) {
        super(driver);
        addressFormTag = new AddressFormTag(getDriver(), PERSON_FIELD_PREFIX + ".postalAddress");
    }

    public String getCountry() {
        return getAddressFormTag().getCountry();
    }

    public void selectCountry(String countryCode) {
        getAddressFormTag().selectCountry(countryCode);
    }

    AddressFormTag getAddressFormTag() {
        return addressFormTag.waitUntilReady();
    }

    public void selectHasLdapAccountRadio() {
        hasLdapAccountRadio.click();
    }

    public void selectDoesNotHaveLdapAccountRadio() {
        doesNotHaveLdapAccountRadio.click();
    }

    public void typeUsername(String username) {
        type(usernameField, username);
    }

    public void typePassword(String password) {
        type(passwordField, password);
    }

    public void typeFirstName(String firstName) {
        type(firstNameField, firstName);
    }

    public void typeLastName(String lastName) {
        type(lastNameField, lastName);
    }

    public void typeEmailAddress(String email) {
        type(emailField, email);
    }

    public void typePhoneNumber(String phoneNumber) {
        type(phoneNumberField, phoneNumber);
    }

    public void typeOrganization(String organization) {
        type(organizationField, organization);
    }

    public void selectInvestigatorRoleCheckbox() {
        if (!investigatorCheckbox.isSelected()) {
            investigatorCheckbox.click();
        }
    }

    public void selectCoordinatorRoleCheckbox() {
        if (!coordinatorCheckbox.isSelected()) {
            coordinatorCheckbox.click();
        }
    }

    public void selectSponsorRoleRadio(Organization sponsor) {
        WebElement radioButton = getDriver().findElement(By.id(SPONSOR_ROLE_PREFIX + sponsor.getNesId()));
        if (!radioButton.isSelected()) {
            radioButton.click();
        }
    }

    public void selectSponsorDelegateRoleRadio(Organization sponsor) {
        WebElement radioButton = getDriver().findElement(By.id(DELEGATE_ROLE_PREFIX + sponsor.getNesId()));
        if (!radioButton.isSelected()) {
            radioButton.click();
        }
    }

    public void clickRequestAccount() {
        requestAccountButton.click();
        new MessageHandler(getDriver(), this).checkForFieldAndValidationErrors();
    }

    public RequestAccountPageHelper getHelper() {
        return helper;
    }

    @Override
    protected void assertLoaded() {
        assertElementsWithIdsPresent(REQUEST_ACCOUNT_BUTTON_ID);
    }

}
