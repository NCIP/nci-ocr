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

import static gov.nih.nci.firebird.commons.selenium2.util.WaitUtils.*;

import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * Provides utility methods for working with the Selenium 2.x WebElements.
 */
public class WebElementUtils {

    private static final String ID_ATTRIBUTE_NAME = "id";

    public WebElementUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Returns true if the element appears within the default timeout.
     *
     * @param driver the Selenium web driver
     * @param locator the Selenium locator to retrieve the element.
     * @return true if the element appears false if the timeout expires
     */
    public static boolean doesElementAppear(WebDriver driver, By locator) {
        return doesElementAppear(driver, locator, DEFAULT_WAIT_TIMEOUT_SECONDS);
    }

    /**
     * Returns true if the element appears within a timeout period.
     *
     * @param driver the Selenium web driver
     * @param locator the Selenium locator to retrieve the element.
     * @param timeoutSeconds timeout duration.
     * @return true if the element appears false if the timeout expires
     */
    public static boolean doesElementAppear(WebDriver driver, final By locator, int timeoutSeconds) {
        try {
            waitFor(driver, locator, timeoutSeconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Returns true if the element(s) specified by the locator are currently on the page.
     *
     * @param context the search context in which to look for the element(s)
     * @param locator the Selenium locator to for the element(s)
     * @return true if the element(s) are found.
     */
    public static boolean isPresent(SearchContext context, By locator) {
        return !context.findElements(locator).isEmpty();
    }

    /**
     * Get's the id attribute from a WebElement.
     *
     * @param element get id from this element
     * @return the id or null if not present
     */
    public static String getId(WebElement element) {
        return element.getAttribute(ID_ATTRIBUTE_NAME);
    }

    /**
     * Returns the values of an elements "class" attribute as a list of Strings.
     *
     * @param element get classes for this element
     * @return the list of classes
     */
    public static List<String> getClasses(WebElement element) {
        String classString = element.getAttribute("class");
        if (classString != null) {
            return Lists.newArrayList(Splitter.on(' ').split(classString));
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Returns a List of Strings by calling getText() on each element.
     *
     * @param elements the elements
     * @return the list of text Strings contained in the elements.
     */
    public static List<String> toStringList(List<WebElement> elements) {
        return Lists.transform(elements, new Function<WebElement, String>() {
            @Override
            public String apply(WebElement element) {
                return element.getText();
            }
        });
    }

    /**
     * Returns a single element if it is present, null otherwise.
     *
     * @param context the context to search
     * @param locator the locator to use to find the element
     * @return the element if found or null
     */
    public static WebElement getElementIfPresent(SearchContext context, By locator) {
        if (isPresent(context, locator)) {
            return context.findElement(locator);
        } else {
            return null;
        }
    }

    /**
     * Returns whether or not all the passed in fields are editable.
     *
     * @param driver the web driver
     * @param locators locators to check if enabled
     * @return true if all fields are editable
     */
    public static boolean areAllFieldsEditable(WebDriver driver, By... locators) {
        boolean editable = true;
        for (By locator : locators) {
            editable &= driver.findElement(locator).isEnabled();
        }
        return editable;
    }

    /**
     * Returns a single element if it is present and visible, null otherwise.
     *
     * @param context the context to search
     * @param locator the locator to use to find the element
     * @return the element if found or null
     */
    public static WebElement getElementIfVisible(SearchContext context, By locator) {
        if (isVisible(context, locator)) {
            return context.findElement(locator);
        } else {
            return null;
        }
    }

    /**
     *
     * @param context The web driver
     * @param locator the locator of the element to check.
     * @return if the element is present and does not present itself with a "display : none" style.
     */
    public static boolean isVisible(SearchContext context, By locator) {
        if (isPresent(context, locator)) {
            boolean hasVisibleItem = false;

            for (WebElement element : context.findElements(locator)) {
                hasVisibleItem |= isVisible(element);
            }

            return hasVisibleItem;
        }
        return false;
    }

    /**
     * Checks an element on the page to indicate if the object is visible on the page or not.
     *
     * @param element The WebElement object to check
     * @return if the element does not present itself with a "display : none" style.
     */
    public static boolean isVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }
}
