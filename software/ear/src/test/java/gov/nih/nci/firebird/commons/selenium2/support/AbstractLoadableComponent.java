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
package gov.nih.nci.firebird.commons.selenium2.support;

import static gov.nih.nci.firebird.commons.selenium2.util.WaitUtils.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.SlowLoadableComponent;
import org.openqa.selenium.support.ui.SystemClock;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public abstract class AbstractLoadableComponent<T extends AbstractLoadableComponent<T>> extends AbstractComponent {

    private final SlowLoadableComponentDelegate loadableComponentDelegate;

    protected AbstractLoadableComponent(WebDriver driver) {
        this(driver, DEFAULT_WAIT_TIMEOUT_SECONDS);
    }

    protected AbstractLoadableComponent(WebDriver driver, int timeoutSeconds) {
        super(driver);
        this.loadableComponentDelegate = new SlowLoadableComponentDelegate(new SystemClock(), timeoutSeconds);
    }

    protected void assertPresent(By locator) {
        assertTrue("Expected element not found " + locator, isPresent(locator));
    }

    protected void assertPresent(By... locators) {
        for (By locator : locators) {
            assertPresent(locator);
        }
    }

    protected void assertElementsWithIdsPresent(String... ids) {
        for (String id : ids) {
            assertElementWithIdPresent(id);
        }
    }

    protected void assertElementWithIdPresent(String id) {
        assertPresent(By.id(id));
    }

    protected void assertConditionTrue(ExpectedCondition<WebElement> condition) {
        assertNotNull(condition.apply(getDriver()));
    }

    protected abstract void assertLoaded();

    /**
     * Asserts that all single WebElement fields (i.e. not List<WebElement>) that are annotated
     * by FindBy or FindBys are present on the page. Only fields declared within the concrete
     * sub class are checked.
     */
    @SuppressWarnings("unchecked")
    protected void assertFindBysPresent() {
        Class<? extends AbstractLoadableComponent<?>> concreteType = (Class<? extends AbstractLoadableComponent<?>>) getClass();
        assertFindBysPresent(concreteType);
    }

    /**
     * Asserts that all single WebElement fields (i.e. not List<WebElement>) that are annotated
     * by FindBy or FindBys are present on the page.
     *
     * @param type check fields declared in the given type
     */
    protected <TYPE extends AbstractLoadableComponent<?>> void assertFindBysPresent(Class<TYPE> type) {
        for (Field annotatedField : getWebElementFieldsAnnotatedWithLocators(type)) {
            assertFindByPresent(annotatedField);
        }
    }

    /**
     * Asserts that an annotated WebElement field is present (or if a List, is non-empty).
     *
     * @param findByField field annotated by FindBy or FindBys
     */
    protected void assertFindByPresent(Field findByField) {
        assertPresent(new Annotations(findByField).buildBy());
    }

    private <TYPE extends AbstractLoadableComponent<?>> Collection<Field> getWebElementFieldsAnnotatedWithLocators(Class<TYPE> type) {
        List<Field> fields = Arrays.asList(type.getDeclaredFields());
        return Collections2.filter(fields, new Predicate<Field>() {
            @Override
            public boolean apply(Field field) {
                return isWebElement(field) && isAnnotatedWithLocator(field);
            }
        });
    }

    private boolean isWebElement(Field field) {
        return field.getType().equals(WebElement.class);
    }

    private boolean isAnnotatedWithLocator(Field field) {
        return field.getAnnotation(FindBy.class) != null || field.getAnnotation(FindBys.class) != null;
    }

    protected void load() {
        // default is no-op, assume all loading happens by PageFactory.init() in constructor
    }

    protected AbstractLoadableComponent<?> identifyDisplayedComponent(IdentifiableComponentFactory<?>... factories) {
        return identifyDisplayedComponent(DEFAULT_WAIT_FOR_PAGE_TIMEOUT_SECONDS, factories);
    }

    protected AbstractLoadableComponent<?> identifyDisplayedComponent(int timeoutSeconds, IdentifiableComponentFactory<?>... factories) {
        ComponentIdentifier identifyingFactory = new ComponentIdentifier(getDriver(), timeoutSeconds, factories);
        return identifyingFactory.getDisplayedComponent();
    }

    /**
     * Ensure that the component is currently loaded and ready for use
     *
     * @return the component.
     */
    @SuppressWarnings("unchecked")  // type will always be T
    public T waitUntilReady() {
        loadableComponentDelegate.get();
        return (T) this;
    }

    private class SlowLoadableComponentDelegate extends SlowLoadableComponent<SlowLoadableComponentDelegate> {

        public SlowLoadableComponentDelegate(Clock clock, int timeOutInSeconds) {
            super(clock, timeOutInSeconds);
        }

        @Override
        protected void load() {
            AbstractLoadableComponent.this.load();
        }

        @Override
        protected void isLoaded() throws Error {
            assertLoaded();
        }

    }

}
