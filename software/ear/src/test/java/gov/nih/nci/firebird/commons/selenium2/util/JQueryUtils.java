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
package gov.nih.nci.firebird.commons.selenium2.util;

import static gov.nih.nci.firebird.commons.selenium2.util.JavascriptUtils.*;
import static gov.nih.nci.firebird.commons.selenium2.util.WaitUtils.*;
import static gov.nih.nci.firebird.commons.selenium2.util.WebElementUtils.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Provides utility methods for working with jQuery via WebDriver.
 */
public final class JQueryUtils {

    private static final String TIMEOUT_DIALOG_ID = "timeoutDialog";
    private static final String GET_VISIBLE_LOADED_TABLES_JAVASCRIPT = "return $.fn.DataTable.fnTables(true)";
    private static final String IS_LOADING_ICON_VISIBLE_JAVASCRIPT = "return $('.loading_icon:visible,.loading-img:visible,#loadingIcon:visible').length > 0";
    private static final String IS_DATA_TABLE_LOADED_JAVASCRIPT = "return $('.dataTables_info:visible').length > 0";
    private static final By BODY_MASKED_LOCATOR = By.cssSelector("body.loading-masked");
    private static final By TAB_LOADING_LOCATOR = By.cssSelector("li.ui-state-processing");
    private static final String EMPTY_DATA_TABLE_CLASS_NAME = "dataTables_empty";

    private JQueryUtils() {
        // utility class - disallow instantiation
    }

    /**
     * Indicates whether a modal dialog is displayed.
     *
     * @param driver the driver
     * @return true if a modal dialog is present
     */
    public static boolean isDialogDisplayed(WebDriver driver) {
        By dialogClassLocator = By.cssSelector(".ui-dialog-content:not(#" + TIMEOUT_DIALOG_ID + ")");
        try {
            return isPresent(driver, dialogClassLocator) && driver.findElement(dialogClassLocator).isDisplayed();
        } catch (StaleElementReferenceException e) {
            return false; // If it's stale then it shouldn't be displayed
        } catch (NoSuchElementException e) {
            return false; // If the element doesn't exist then it's not displayed
        }
    }

    /**
     * Waits until the page loading mask is cleared.
     *
     * @param driver the driver
     */
    public static void waitUntilPageIsNotMasked(WebDriver driver) {
        waitUntilPageIsNotMasked(driver, DEFAULT_WAIT_FOR_PAGE_TIMEOUT_SECONDS);
    }

    /**
     * Waits until the page loading mask is cleared.
     *
     * @param driver the driver
     * @param timeoutSeconds timeout in seconds before failing
     */
    public static void waitUntilPageIsNotMasked(WebDriver driver, int timeoutSeconds) {
        pause(100);
        waitFor(driver, invisibilityOfElementLocated(BODY_MASKED_LOCATOR), timeoutSeconds);
    }

    /**
     * Indicates whether the page loading mask is currently present.
     *
     * @param driver the driver
     * @return true if the page is masked
     */
    public static boolean isPageMasked(WebDriver driver) {
        return isPresent(driver, BODY_MASKED_LOCATOR);
    }

    /**
     * Indicates whether a tab on the current page is currently loading.
     *
     * @param driver the driver
     * @return true if a tab is loading
     */
    public static boolean isTabLoading(WebDriver driver) {
        return isPresent(driver, TAB_LOADING_LOCATOR);
    }

    /**
     * Indicates whether a loading icon is visible anywhere on the current page.
     *
     * @param driver the driver
     * @return true if the loading icon is visible
     */
    public static boolean isLoadingIconDisplayed(WebDriver driver) {
        return (Boolean) JavascriptUtils.execute(driver, IS_LOADING_ICON_VISIBLE_JAVASCRIPT);
    }

    /**
     * Indicates whether a data table contains data rows.
     *
     * @param table the data table
     * @return true if the data table contains no data rows.
     */
    public static boolean isDataTableEmpty(WebElement table) {
        return !table.findElements(By.className(EMPTY_DATA_TABLE_CLASS_NAME)).isEmpty();
    }

    /**
     * Waits for a data table on the page to load.
     *
     * @param driver the driver.
     */
    public static void waitForDataTable(WebDriver driver) {
        waitForJavascriptCondition(driver, IS_DATA_TABLE_LOADED_JAVASCRIPT);
    }

    /**
     * Indicates whether a data table on the current page is loaded.
     *
     * @param driver the driver
     * @return true if the table is loaded
     */
    public static boolean isDataTableLoaded(WebDriver driver) {
        return (Boolean) execute(driver, IS_DATA_TABLE_LOADED_JAVASCRIPT);
    }

    /**
     * Indicates whether a data table on the current page is loaded.
     *
     * @param driver the driver
     * @return true if the table is loaded
     */
    public static boolean isDataTableLoaded(WebDriver driver, String tableId) {
        List<WebElement> visibleDataTables = getVisibleDataTables(driver);
        for (WebElement table : visibleDataTables) {
            if (tableId.equals(getId(table))) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private static List<WebElement> getVisibleDataTables(WebDriver driver) {
        return (List<WebElement>) execute(driver, GET_VISIBLE_LOADED_TABLES_JAVASCRIPT);
    }

    /**
     * Applies a transformation to all data rows in a data table returning the resulting list.
     *
     * @param table the data table
     * @param transformation the transformation function
     * @return the result of applying the transformation to all rows
     */
    public static <T> List<T> transformDataTableRows(WebElement table, Function<WebElement, T> transformation) {
        if (JQueryUtils.isDataTableEmpty(table)) {
            return Collections.emptyList();
        } else {
            List<WebElement> rows = table.findElements(By.cssSelector("tbody > tr"));
            return Lists.transform(rows, transformation);
        }
    }

    /**
     * Returns whether or not there is an active ajax call executing.
     *
     * @param driver the driver
     * @return true is there is an ajax call executing. Otherwise, false.
     */
    public static boolean isAjaxCallExecuting(WebDriver driver) {
        return (Boolean) execute(driver, "return jQuery.active != 0");
    }

    /**
     * Waits for there to be no dialog present and then waits for an ajax call to start.
     *
     * @param driver the driver
     */
    public static void waitForDialogCloseAndAjaxRefresh(WebDriver driver) {
        waitForDialogClosed(driver);
        waitForAjaxCall(driver); // Ajax call to refresh tab/page
    }

    /**
     * Waits for there to be no dialog present
     *
     * @param driver the driver
     */
    public static void waitForDialogClosed(final WebDriver driver) {
        waitForDialogClosed(driver, DEFAULT_WAIT_TIMEOUT_SECONDS);
    }

    /**
     * Waits for there to be no dialog present
     *
     * @param driver the driver
     * @param timeoutSeconds timeout in seconds before failing
     */
    public static void waitForDialogClosed(final WebDriver driver, int timeoutSeconds) {
        waitFor(driver, new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return !isDialogDisplayed(driver);
            }
        }, timeoutSeconds);
    }

    private static void waitForAjaxCall(final WebDriver driver) {
        waitFor(driver, new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return isAjaxCallExecuting(driver);
            }
        });
    }

    /**
     * Waits for there to be no active ajax calls executing
     *
     * @param driver the driver
     */
    public static void waitForAjaxCallToFinish(final WebDriver driver) {
        waitForAjaxCallToFinish(driver, WaitUtils.DEFAULT_WAIT_TIMEOUT_SECONDS);
    }

    /**
     * Waits for there to be no active ajax calls executing
     *
     * @param driver the driver
     * @param timeoutSeconds timeout in seconds before failing
     */
    public static void waitForAjaxCallToFinish(final WebDriver driver, int timeoutSeconds) {
        waitFor(driver, new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return !isAjaxCallExecuting(driver);
            }
        }, timeoutSeconds);
    }

    /**
     * Clears an masked input field and sends the input value as keystrokes to the input.
     *
     * @param maskedInputId ID of the masked input field
     * @param value value to type
     */
    public static void typeInMaskedField(WebDriver driver, String maskedInputId, String value) {
        WebElement maskedInput = driver.findElement(By.id(maskedInputId));
        maskedInput.clear();
        JQueryUtils.triggerBlur(driver, maskedInputId);
        maskedInput.click();
        maskedInput.sendKeys(value);
        JQueryUtils.triggerBlur(driver, maskedInputId);
    }

    /**
     * Triggers the blur event on the element with the given ID. Useful for tests running against Firefox since blur
     * events don't get executed if the Firefox window isn't in focus.
     *
     * @param elementId element ID to trigger blur event on
     */
    private static void triggerBlur(WebDriver driver, String elementId) {
        String escapedElementId = escapeForJquery(elementId);
        JavascriptUtils.execute(driver, "$('#" + escapedElementId + "').blur()");
    }

    /**
     * Replaces all '.' characters with "\\." in the given id for use in JQuery selectors.
     *
     * @param elementId Element ID
     * @return Escaped element ready for use in JQuery selectors.
     */
    private static String escapeForJquery(String elementId) {
        return elementId.replaceAll("\\.", "\\\\\\\\.");
    }
}
