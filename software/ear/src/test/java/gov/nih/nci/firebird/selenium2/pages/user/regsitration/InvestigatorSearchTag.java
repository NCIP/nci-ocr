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
package gov.nih.nci.firebird.selenium2.pages.user.regsitration;

import static gov.nih.nci.firebird.commons.selenium2.util.TableUtils.*;
import static gov.nih.nci.firebird.selenium2.pages.components.tags.search.AbstractSearchTag.*;
import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.selenium2.support.AbstractLoadableComponent;
import gov.nih.nci.firebird.commons.selenium2.util.WebElementUtils;
import gov.nih.nci.firebird.selenium2.pages.base.TableListing;
import gov.nih.nci.firebird.selenium2.pages.components.AutosearchInput;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * /tags/investigatorSearchSelect.tag
 */
public class InvestigatorSearchTag extends AbstractLoadableComponent<InvestigatorSearchTag> {

    private static final String INVESTIGATOR_SEARCH_INPUT_ID = "investigatorSearchInput";
    private static final String SELECTED_INVESTIGATOR_LIST_ITEMS_CSS_SELECTOR = "ul#selectedInvestigators > li";

    private final AutosearchInput investigatorSearchInput;

    @FindBy(css = SELECTED_INVESTIGATOR_LIST_ITEMS_CSS_SELECTOR)
    private List<WebElement> selectedInvestigatorListItems;

    private InvestigatorSearchResultsTable investigatorResultsTable;
    private InvestigatorSearchTagHelper helper = new InvestigatorSearchTagHelper(this);

    public InvestigatorSearchTag(WebDriver driver) {
        super(driver);
        investigatorSearchInput = new AutosearchInput(driver, INVESTIGATOR_SEARCH_INPUT_ID);
        investigatorResultsTable = new InvestigatorSearchResultsTable(driver);
    }

    public InvestigatorSearchTagHelper getHelper() {
        return helper;
    }

    public void searchForInvestigator(String value) {
        getSearchInput().type(value);
        pause(300);
    }

    private AutosearchInput getSearchInput() {
        return investigatorSearchInput.waitUntilReady();
    }

    public String getSearchFieldValue() {
        return getSearchInput().getValue();
    }

    public List<InvestigatorSearchResult> getSearchResults() {
        return getResultsTable().getSearchResults();
    }

    public List<SelectedInvestigatorListing> getSelectedInvestigators() {
        return Lists.transform(selectedInvestigatorListItems, new Function<WebElement, SelectedInvestigatorListing>() {
            @Override
            public SelectedInvestigatorListing apply(WebElement li) {
                return new SelectedInvestigatorListing(li);
            }
        });
    }

    public String getSearchInfoMessage() {
        return getResultsTable().getSearchInfoMessage();
    }

    private InvestigatorSearchResultsTable getResultsTable() {
        return investigatorResultsTable.waitUntilReady();
    }

    @Override
    protected void assertLoaded() {
        // assertions handled by subcomponents
    }

    private class InvestigatorSearchResultsTable extends AbstractLoadableComponent<InvestigatorSearchResultsTable> {

        private static final String SEARCHED_INVESTIGATORS_TABLE_ID = "searchedInvestigators";
        private static final String SEARCHED_INVESTIGATORS_ROWS_CSS_SELECTOR = "#searchedInvestigators > tbody > tr";
        private static final String SEARCHED_INVESTIGATOR_INFO_DIV_ID = "searchedInvestigators_info";

        @FindBy(css = SEARCHED_INVESTIGATORS_ROWS_CSS_SELECTOR)
        private List<WebElement> searchResultsRows;

        @FindBy(id = SEARCHED_INVESTIGATOR_INFO_DIV_ID)
        private WebElement searchInfoMessageDiv;

        @FindBy(id = SEARCHED_INVESTIGATORS_TABLE_ID)
        private WebElement table;

        private InvestigatorSearchResultsTable(WebDriver driver) {
            super(driver);
        }

        private String getSearchInfoMessage() {
            return searchInfoMessageDiv.getText();
        }

        @Override
        protected void assertLoaded() {
            assertFalse(getSearchInput().isLoading());
            assertPresent(By.id(SEARCHED_INVESTIGATORS_TABLE_ID));
            assertFalse(Boolean.parseBoolean(table.getAttribute(SEARCH_IN_PROGRESS_ATTRIBUTE_NAME)));
            assertPresent(By.id(SEARCHED_INVESTIGATOR_INFO_DIV_ID));
        }

        private List<InvestigatorSearchResult> getSearchResults() {
            List<InvestigatorSearchResult> results = Lists.newArrayList();
            int rowCount = getSearchResultsRows().size();
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                results.add(new InvestigatorSearchResult(rowIndex));
            }
            return results;
        }

        private List<WebElement> getSearchResultsRows() {
            return searchResultsRows;
        }

    }

    public class InvestigatorSearchResult implements TableListing {

        private final static String PROFILE_ID_ATTRIBUTE_NAME = "profileid";
        private final static int SELECT_BUTTON_COLUMN_INDEX = 0;
        private final static int NAME_COLUMN_INDEX = 1;
        private final static int EMAIL_COLUMN_INDEX = 2;
        private final static String CHECKMARK_DIV_ID_PREFIX = "added_";

        private final int rowIndex;

        private InvestigatorSearchResult(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        @Override
        public Long getId() {
            return getProfileId();
        }

        public Long getProfileId() {
            return Long.valueOf(getRow().getAttribute(PROFILE_ID_ATTRIBUTE_NAME));
        }

        private WebElement getRow() {
            return investigatorResultsTable.waitUntilReady().getSearchResultsRows().get(rowIndex);
        }

        public String getNameAndAddress() {
            return getCells(getRow()).get(NAME_COLUMN_INDEX).getText();
        }

        public String getEmail() {
            return getCells(getRow()).get(EMAIL_COLUMN_INDEX).getText();
        }

        private WebElement getSelectButton() {
            return getSelectButtonCell().findElement(By.tagName("a"));
        }

        private WebElement getSelectButtonCell() {
            return getCells(getRow()).get(SELECT_BUTTON_COLUMN_INDEX);
        }

        public void clickSelect() {
            getSelectButton().click();
            waitFor(By.id(CHECKMARK_DIV_ID_PREFIX + getProfileId()));
        }

        public boolean isCheckMarkDisplayed() {
            By checkMarkDivLocator = By.id(CHECKMARK_DIV_ID_PREFIX + getProfileId());
            return !getSelectButtonCell().findElements(checkMarkDivLocator).isEmpty();
        }

        public boolean isSelectButtonPresent() {
            return !getSelectButtonCell().findElements(By.tagName("a")).isEmpty();
        }

    }

    public class SelectedInvestigatorListing implements TableListing {

        private static final String LIST_ITEM_ID_VALUE_PREFIX = "profile_";

        private final Long profileId;
        private final String name;
        private final WebElement deleteButton;

        private SelectedInvestigatorListing(WebElement li) {
            this.profileId = Long.valueOf(WebElementUtils.getId(li).substring(LIST_ITEM_ID_VALUE_PREFIX.length()));
            this.name = li.getText();
            this.deleteButton = li.findElement(By.tagName("a"));
        }

        @Override
        public Long getId() {
            return getProfileId();
        }

        public Long getProfileId() {
            return profileId;
        }

        public String getName() {
            return name;
        }

        public void clickDelete() {
            deleteButton.click();
        }

    }

}
