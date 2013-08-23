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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Provides utility methods for working with the HTML forms using Selenium 2.x WebDriver.
 */
public final class FormUtils {

    private FormUtils() {
        // Utility class - disable instantiation
    }

    /**
     * Clears an input field and sends the input value as keystrokes
     * to the input.
     *
     * @param input the input field
     * @param value the value
     */
    public static void type(WebElement input, String value) {
        input.clear();
        input.sendKeys(StringUtils.defaultString(value));
    }

    /**
     * Returns the current value of an input field.
     *
     * @param input the input field
     * @return the current value.
     */
    public static String getInputValue(WebElement input) {
        return input.getAttribute("value");
    }

    /**
     * Selects a value from a single select element based on option value.
     *
     * @param selectElement the SELECT web element
     * @param value the option value to select
     */
    public static void select(WebElement selectElement, String value) {
        new Select(selectElement).selectByValue(value);
    }

    /**
     * Returns a list of all the Web Elements corresponding to the options of the Select box.
     *
     * @param selectElement the SELECT web element
     */
    public static List<WebElement> getSelectOptions(WebElement selectElement) {
        return new Select(selectElement).getOptions();
    }

    /**
     * Returns a list of all the options text of the select box
     *
     * @param selectElement the SELECT web element
     */
    public static List<String> getSelectOptionsAsStrings(WebElement selectElement) {
        List<WebElement> options = new Select(selectElement).getOptions();
        return Lists.transform(options, new Function<WebElement, String>() {
            @Override
            public String apply(WebElement option) {
                return option.getText();
            }
        });
    }

    /**
     * Selects a value from a single select element based on option text.
     *
     * @param selectElement the SELECT web element
     * @param text the option text to select
     */
    public static void selectByVisibleText(WebElement selectElement, String text) {
        new Select(selectElement).selectByVisibleText(text);
    }

    /**
     * Selects a value from a single select element by option index.
     *
     * @param selectElement the SELECT web element
     * @param index the option index
     */
    public static void select(WebElement selectElement, int index) {
        new Select(selectElement).selectByIndex(index);
    }

    /**
     * Returns the currently selected option value of a select element
     *
     * @param selectElement the SELECT WebElement
     * @return the selected value or null if none
     */
    public static String getSelectedValue(WebElement selectElement) {
        return getSelectedValue(new Select(selectElement));
    }

    /**
     * Returns the currently selected option value of a Select instance.
     *
     * @param select the Select instance
     * @return the selected value or null if none
     */
    public static String getSelectedValue(Select select) {
        WebElement selectedOption = select.getFirstSelectedOption();
        if (selectedOption != null) {
            return selectedOption.getAttribute("value");
        } else {
            return null;
        }
    }

    /**
     * Returns the currently selected option text of a select element
     *
     * @param selectElement the SELECT WebElement
     * @return the selected text or null if none
     */
    public static String getSelectedText(WebElement selectElement) {
        return getSelectedText(new Select(selectElement));
    }

    /**
     * Returns the currently selected option text of a Select instance.
     *
     * @param select the Select instance
     * @return the selected text or null if none
     */
    public static String getSelectedText(Select select) {
        WebElement selectedOption = select.getFirstSelectedOption();
        if (selectedOption != null) {
            return selectedOption.getText();
        } else {
            return null;
        }
    }

}
