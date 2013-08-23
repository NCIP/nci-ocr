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
package gov.nih.nci.firebird.selenium2.pages.sponsor.protocol;

import gov.nih.nci.firebird.commons.selenium2.util.WebElementUtils;
import gov.nih.nci.firebird.data.ProtocolPhase;
import gov.nih.nci.firebird.selenium2.pages.base.AbstractAuthorizedPage;
import gov.nih.nci.firebird.selenium2.pages.base.MessageHandler;
import gov.nih.nci.firebird.selenium2.pages.components.DataTable;
import gov.nih.nci.firebird.selenium2.pages.components.JQueryAutoComplete;
import gov.nih.nci.firebird.selenium2.pages.components.tags.SponsorSelectionTag;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public abstract class AbstractProtocolModificationPage<T extends AbstractProtocolModificationPage<T>>
        extends AbstractAuthorizedPage<T> {
    static final String NO_LEAD_ORG_SELECTED_MESSAGE_KEY = "sponsor.protocol.lead.organization.none";
    private static final String PROTOCOL_TITLE_ID = "protocolTitleField";
    private static final String PROTOCOL_NUMBER_ID = "protocolNumberField";
    private static final String PHASE_ID = "phaseField";
    private static final String AGENTS_ID = "agentDisplay";
    private static final String AGENT_AUTOCOMPLETE_ID = "agentAutosearch";
    private static final String ADD_AGENT_BTN = "addAgentButton";
    private static final String SAVE_BUTTON_ID = "saveButton";
    private static final String SELECT_LEAD_ORG_BUTTON = "selectLeadOrganizationBtn";
    private static final String LEAD_ORGANIZATION_TABLE_ID = "protocolLeadOrganizations";

    @FindBy(id = PROTOCOL_TITLE_ID)
    @CacheLookup
    private WebElement protocolTitleField;

    @FindBy(id = PROTOCOL_NUMBER_ID)
    @CacheLookup
    private WebElement protocolNumberField;

    @FindBy(id = PHASE_ID)
    @CacheLookup
    private WebElement phaseSelect;

    @FindBy(id = AGENTS_ID)
    private WebElement agentList;

    @FindBy(id = ADD_AGENT_BTN)
    @CacheLookup
    private WebElement addAgentButton;

    @FindBy(id = SAVE_BUTTON_ID)
    private WebElement saveButton;

    @FindBy(id = SELECT_LEAD_ORG_BUTTON)
    @CacheLookup
    private WebElement selectLeadOrgButton;

    private final DataTable<LeadOrganizationListing> leadOrganizationsTable;

    private final JQueryAutoComplete agentAutoComplete;

    private final SponsorSelectionTag sponsorSelectionTag;

    public AbstractProtocolModificationPage(WebDriver driver) {
        super(driver);
        sponsorSelectionTag = new SponsorSelectionTag(driver);
        agentAutoComplete = new JQueryAutoComplete(driver, AGENT_AUTOCOMPLETE_ID);
        leadOrganizationsTable = new DataTable<LeadOrganizationListing>(driver, LEAD_ORGANIZATION_TABLE_ID,
                                                                        LeadOrganizationListing.class, this);
    }

    @Override
    protected void assertLoaded() {
        super.assertLoaded();
        assertFindBysPresent();
    }

    String getNoLeadOrganizationMessage() {
        return getPropertyText(NO_LEAD_ORG_SELECTED_MESSAGE_KEY);
    }

    public ProtocolInformationTab clickSave() {
        saveButton.click();
        new MessageHandler(getDriver(), this).checkForFieldAndValidationErrors();
        ProtocolRegistrationPage registrationPage = new ProtocolRegistrationPage(getDriver());
        return new ProtocolInformationTab(getDriver(), registrationPage).waitUntilReady();
    }

    public boolean isSaveButtonPresent() {
        return WebElementUtils.isVisible(saveButton);
    }

    public CreateLeadOrganizationDialog clickSelectLeadOrganizationBtn() {
        selectLeadOrgButton.click();
        return new CreateLeadOrganizationDialog(getDriver(), this).waitUntilReady();
    }

    public List<LeadOrganizationListing> getSelectedLeadOrganizationListings() {
        return leadOrganizationsTable.getListings();
    }

    public void setProtocolTitle(String text) {
        type(protocolTitleField, text);
    }

    public String getProtocolTitle() {
        return getInputValue(protocolTitleField);
    }

    public void setProtocolNumber(String text) {
        type(protocolNumberField, text);
    }

    public String getProtocolNumber() {
        return getInputValue(protocolNumberField);
    }

    public void selectPhase(ProtocolPhase phase) {
        new Select(phaseSelect).selectByValue(phase.name());
    }

    public String getPhase() {
        return new Select(phaseSelect).getFirstSelectedOption().getText();
    }

    SponsorSelectionTag getSponsorSelectionTag() {
        return sponsorSelectionTag;
    }

    JQueryAutoComplete getAgentAutoComplete() {
        return agentAutoComplete;
    }

    public String getAgentSearchContents() {
        return agentAutoComplete.getSearchFieldContents();
    }

    public void typeAgent(String agent) {
        agentAutoComplete.searchFor(agent);
    }

    public void deleteAgent(String agent) {
        findElement(By.xpath(getAgentDeleteXpath(agent))).click();
    }

    private String getAgentDeleteXpath(String agent) {
        return "//ul/li/div[contains(.,'" + agent + "')]//a";
    }

    public List<String> getAgentNames() {
        return Lists.newArrayList(Splitter.on('\n').omitEmptyStrings().split(agentList.getText()));
    }

    public void clickAdd() {
        addAgentButton.click();
    }

    public abstract AbstractProtocolModificationPageHelper getHelper();

}