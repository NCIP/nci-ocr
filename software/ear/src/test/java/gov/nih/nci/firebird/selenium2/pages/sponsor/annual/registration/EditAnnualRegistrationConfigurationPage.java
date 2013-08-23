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
package gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration;

import gov.nih.nci.firebird.commons.selenium2.util.FileDownloadUtils;
import gov.nih.nci.firebird.commons.selenium2.util.FormUtils;
import gov.nih.nci.firebird.commons.selenium2.util.TableUtils;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.selenium2.pages.base.AbstractMenuPage;
import gov.nih.nci.firebird.selenium2.pages.base.TableListing;
import gov.nih.nci.firebird.selenium2.pages.components.tags.MessagesTag;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.ProtocolsListPage;
import gov.nih.nci.firebird.selenium2.pages.util.FirebirdTableUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * sponsor/annual/registration/required_forms.jsp
 */
public class EditAnnualRegistrationConfigurationPage extends AbstractMenuPage<EditAnnualRegistrationConfigurationPage> {
    private static final String FORM_TYPE_SELECT_BOX_ID = "formType";
    private static final String ADD_FORM_BUTTON_ID = "addFormButton";
    private static final String SAVE_BUTTON_ID = "saveButton";
    private static final String CANCEL_BUTTON_ID = "cancelButton";
    private static final String TABLE_ID = "annualRegistrationsTable";

    @FindBy(id = FORM_TYPE_SELECT_BOX_ID)
    private WebElement formTypeSelectBox;

    @FindBy(id = ADD_FORM_BUTTON_ID)
    private WebElement addFormButton;

    @FindBy(id = SAVE_BUTTON_ID)
    private WebElement saveButton;

    @FindBy(id = CANCEL_BUTTON_ID)
    private WebElement cancelButton;

    @FindBy(id = TABLE_ID)
    private WebElement annualRegistrationsTable;

    private final EditAnnualRegistrationConfigurationPageHelper helper;

    public EditAnnualRegistrationConfigurationPage(WebDriver driver) {
        super(driver);
        helper = new EditAnnualRegistrationConfigurationPageHelper(this);
    }

    public EditAnnualRegistrationConfigurationPageHelper getHelper() {
        return helper;
    }

    public List<RegistrationFormListing> getListings() {
        return FirebirdTableUtils.transformDataTableRows(this, annualRegistrationsTable, RegistrationFormListing.class);
    }

    public void selectFormType(FormType formType) {
        FormUtils.selectByVisibleText(formTypeSelectBox, formType.getName());
    }

    public void clickAddFormButton() {
        addFormButton.click();
    }

    public void clickSaveButton() {
        saveButton.click();
        waitForSuccessMessage();
    }

    private void waitForSuccessMessage() {
        final MessagesTag messagesTag = new MessagesTag(getDriver()).waitUntilReady();
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return messagesTag.getSuccessMessages().contains(
                        getPropertyText("sponsor.annual.registrations.save.success.message"));
            }
        });
    }

    public void clickCancelButton() {
        cancelButton.click();
    }

    @Override
    protected void assertLoaded() {
        super.assertLoaded();
        assertFindBysPresent();
    }

    public class RegistrationFormListing implements TableListing {

        private static final String REQUIRED_RADIO_BUTTON_PREFIX = "required_";
        private static final String OPTIONAL_RADIO_BUTTON_PREFIX = "optional_";

        private static final int DOWNLOAD_SAMPLE_COLUMN = 0;
        private static final int DESCRIPTION_COLUMN = 1;
        private static final int OPTIONALITY_COLUMN = 2;
        private static final int EDIT_REMOVE_COLUMN = 3;

        private final Long id;
        private final String description;
        private final WebElement downloadSampleLink;
        private final WebElement removeButton;
        private final WebElement editRemoveColumn;
        private final WebElement requiredRadioButtonLabel;
        private final WebElement optionalRadioButtonLabel;
        private final WebElement requiredRadioButton;
        private final WebElement optionalRadioButton;

        public RegistrationFormListing(WebElement row) {
            this.id = Long.valueOf(ProtocolsListPage.getId(row));
            List<WebElement> cells = TableUtils.getCells(row);
            this.downloadSampleLink = cells.get(DOWNLOAD_SAMPLE_COLUMN).findElement(By.tagName("a"));
            this.description = cells.get(DESCRIPTION_COLUMN).getText();
            editRemoveColumn = cells.get(EDIT_REMOVE_COLUMN);
            this.removeButton = editRemoveColumn.findElement(By.className("deleteButton"));

            this.requiredRadioButtonLabel = cells.get(OPTIONALITY_COLUMN).findElement(
                    By.cssSelector("label[for='" + REQUIRED_RADIO_BUTTON_PREFIX + id + "']"));
            this.optionalRadioButtonLabel = cells.get(OPTIONALITY_COLUMN).findElement(
                    By.cssSelector("label[for='" + OPTIONAL_RADIO_BUTTON_PREFIX + id + "']"));
            this.requiredRadioButton = cells.get(OPTIONALITY_COLUMN).findElement(
                    By.id(REQUIRED_RADIO_BUTTON_PREFIX + id));
            this.optionalRadioButton = cells.get(OPTIONALITY_COLUMN).findElement(
                    By.id(OPTIONAL_RADIO_BUTTON_PREFIX + id));
        }

        @Override
        public Long getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public FormOptionality getOptionality() {
            if (requiredRadioButton.isSelected()) {
                return FormOptionality.REQUIRED;
            } else if (optionalRadioButton.isSelected()) {
                return FormOptionality.OPTIONAL;
            }
            return null;
        }

        // labels is what is clickable when using the jQuery button sets
        public void setOptionality(FormOptionality optionality) {
            if (FormOptionality.REQUIRED == optionality) {
                requiredRadioButtonLabel.click();
            } else if (FormOptionality.OPTIONAL == optionality) {
                optionalRadioButtonLabel.click();
            } else {
                throw new IllegalArgumentException(optionality.getDisplay() + " is not an allowable optionality.");
            }
        }

        public void clickRemoveButton() {
            removeButton.click();
        }

        public EditAnnual1572FormDialog clickEditButton() {
            WebElement editButton = editRemoveColumn.findElement(By.className("editButton"));
            editButton.click();
            return new EditAnnual1572FormDialog(getDriver(), EditAnnualRegistrationConfigurationPage.this).waitUntilReady();
        }

        public File clickDownloadSample() throws IOException {
            return FileDownloadUtils.clickDownloadLink(getDriver(), downloadSampleLink).getFile();
        }
    }

}
