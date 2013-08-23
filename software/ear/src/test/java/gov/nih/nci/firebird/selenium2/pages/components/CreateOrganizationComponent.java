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
package gov.nih.nci.firebird.selenium2.pages.components;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.selenium2.support.AbstractLoadableComponent;
import gov.nih.nci.firebird.commons.selenium2.support.IdentifiableComponentFactory;
import gov.nih.nci.firebird.commons.selenium2.util.FormUtils;
import gov.nih.nci.firebird.commons.selenium2.util.JQueryUtils;
import gov.nih.nci.firebird.selenium2.pages.components.tags.AddressFormTag;
import gov.nih.nci.firebird.selenium2.pages.investigator.profile.CreatePracticeSiteComponent;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Reusable component for components to use that have the "Create New Organization" option.
 */
public class CreateOrganizationComponent extends AbstractLoadableComponent<CreateOrganizationComponent> implements
        OrganizationForm {

    private static final String NAME_ID_SUFFIX = "name";
    private static final String EMAIL_ID_SUFFIX = "email";
    private static final String PHONE_ID_SUFFIX = "phoneNumber";

    private final By nameLocator;
    private final By emailLocator;
    private final By phoneNumberLocator;

    private final AddressFormTag addressForm;

    private String fieldIdPrefix;
    private final OrganizationFormHelper helper;

    public CreateOrganizationComponent(WebDriver driver, String fieldIdPrefix) {
        super(driver);
        this.fieldIdPrefix = fieldIdPrefix;
        nameLocator = By.id(getFieldId(NAME_ID_SUFFIX));
        emailLocator = By.id(getFieldId(EMAIL_ID_SUFFIX));
        phoneNumberLocator = By.id(getFieldId(PHONE_ID_SUFFIX));
        addressForm = new AddressFormTag(getDriver(), fieldIdPrefix + ".postalAddress");
        helper = new OrganizationFormHelper(this);
    }

    public OrganizationFormHelper getHelper() {
        return helper;
    }

    private String getFieldId(String fieldIdSuffix) {
        return fieldIdPrefix + "." + fieldIdSuffix;
    }

    @Override
    public void typeAddress1(String value) {
        addressForm.typeAddress1(value);
    }

    @Override
    public String getAddress1() {
        return addressForm.getAddress1();
    }

    @Override
    public void typeAddress2(String value) {
        addressForm.typeAddress2(value);
    }

    @Override
    public String getAddress2() {
        return addressForm.getAddress2();
    }

    @Override
    public void typeCity(String value) {
        addressForm.typeCity(value);
    }

    @Override
    public String getCity() {
        return addressForm.getCity();
    }

    @Override
    public void selectUsState(String value) {
        addressForm.selectUsState(value);
    }

    @Override
    public String getUsState() {
        return addressForm.getUsState();
    }

    @Override
    public void typeStateOrProvince(String value) {
        addressForm.typeStateOrProvince(value);
    }

    @Override
    public String getStateOrProvince() {
        return addressForm.getStateOrProvince();
    }

    @Override
    public String getStateOrProvinceLabel() {
        return addressForm.getStateOrProvinceLabel();
    }

    @Override
    public void typeZip(String value) {
        addressForm.typeZip(value);
    }

    @Override
    public String getZipLabel() {
        return addressForm.getZipLabel();
    }

    @Override
    public String getZip() {
        return addressForm.getZip();
    }

    @Override
    public void selectCountry(String value) {
        addressForm.selectCountry(value);
    }

    @Override
    public String getCountry() {
        return addressForm.getCountry();
    }

    @Override
    public void typeName(String value) {
        FormUtils.type(getDriver().findElement(nameLocator), value);
    }

    @Override
    public String getName() {
        return FormUtils.getInputValue(getDriver().findElement(nameLocator));
    }

    @Override
    public void typeEmail(String value) {
        FormUtils.type(getDriver().findElement(emailLocator), value);
    }

    @Override
    public String getEmail() {
        return FormUtils.getInputValue(getDriver().findElement(emailLocator));
    }

    @Override
    public void typePhone(String value) {
        JQueryUtils.typeInMaskedField(getDriver(), getFieldId(PHONE_ID_SUFFIX), value);
    }

    @Override
    public String getPhone() {
        return FormUtils.getInputValue(getDriver().findElement(phoneNumberLocator));
    }

    public AddressFormTag getAddressForm() {
        return addressForm.waitUntilReady();
    }

    public static IdentifiableComponentFactory<CreateOrganizationComponent> getFactory(String fieldIdPrefix) {
        return new Factory(fieldIdPrefix);
    }

    @Override
    protected void assertLoaded() {
        assertElementsWithIdsPresent(getFieldId(NAME_ID_SUFFIX), getFieldId(EMAIL_ID_SUFFIX),
                getFieldId(PHONE_ID_SUFFIX));
        assertFalse(isPresent(By.id(CreatePracticeSiteComponent.OHRP_NUMBER_TEXTBOX_ID)));
    }

    private static class Factory extends IdentifiableComponentFactory<CreateOrganizationComponent> {

        private final String fieldIdPrefix;

        public Factory(String fieldIdPrefix) {
            this.fieldIdPrefix = fieldIdPrefix;
        }

        @Override
        protected CreateOrganizationComponent getInstance(WebDriver driver) {
            return new CreateOrganizationComponent(driver, fieldIdPrefix);
        }

    }

}
