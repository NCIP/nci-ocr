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
package gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration;

import static org.junit.Assert.*;
import gov.nih.nci.firebird.commons.selenium2.support.IdentifiableComponentFactory;
import gov.nih.nci.firebird.commons.selenium2.util.FileDownloadUtils;
import gov.nih.nci.firebird.commons.selenium2.util.JQueryUtils;
import gov.nih.nci.firebird.commons.selenium2.util.WebElementUtils;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * /investigator/registration/ajax/fda1572/form.jsp
 */
public class ProtocolForm1572Tab extends AbstractFormTab<ProtocolForm1572Tab> implements Form1572Tab {

    private static final String TAB_UNIQUE_LOCATOR_ID = "dcpForm1572";
    private static final String PRACTICE_SITE_ASTERISK_ID = "practiceSiteAsterisk";
    private static final String IRB_ASTERISK_ID = "irbAsterisk";
    private static final String LAB_ASTERISK_ID = "labAsterisk";
    private static final String TAB_ID = "form_form_fda_1572_tab";
    private static final String PRACTICE_SITES_DIV_ID = "fda1572_practiceSites";
    private static final String LABS_DIV_ID = "fda1572_labs";
    private static final String IRBS_DIV_ID = "fda1572_irb";
    private static final String VIEW_GENERATED_1572_BUTTON_ID = "view1572PdfButtonTop";
    private static final String INSTRUCTION_BUBBLE_ID = "1572_instructions";
    static final String COMMENTS_DIV_ID = "commentsDiv";
    static final String TAB_LINK_CSS_SELECTOR = "li#" + TAB_ID + " > a";

    @FindBy(id = VIEW_GENERATED_1572_BUTTON_ID)
    private WebElement viewGenerated1572Button;
    @FindBy(id = INSTRUCTION_BUBBLE_ID)
    private WebElement instructionBubble;
    @FindBy(id = PRACTICE_SITE_ASTERISK_ID)
    private WebElement practiceSiteAsterisk;
    @FindBy(id = IRB_ASTERISK_ID)
    private WebElement irbAsterisk;
    @FindBy(id = LAB_ASTERISK_ID)
    private WebElement clinicalLabAsterisk;

    private final WebElement commentsDiv;
    private final Form1572PracticeSiteSection practiceSiteSection;
    private final Form1572ClinicalLabSection clinicalLabSection;
    private final Form1572IrbSection irbSection;

    private Form1572TabHelper helper = new Form1572TabHelper(this);

    public ProtocolForm1572Tab(WebDriver driver, InvestigatorRegistrationPage page) {
        super(driver, page);
        commentsDiv = WebElementUtils.getElementIfPresent(getDriver(), By.id(COMMENTS_DIV_ID));
        practiceSiteSection = new Form1572PracticeSiteSection(getDriver());
        clinicalLabSection = new Form1572ClinicalLabSection(getDriver());
        irbSection = new Form1572IrbSection(getDriver());
    }

    @Override
    public Form1572PracticeSiteSection getPracticeSiteSection() {
        return practiceSiteSection;
    }

    @Override
    public Form1572ClinicalLabSection getClinicalLabSection() {
        return clinicalLabSection;
    }

    @Override
    public Form1572IrbSection getIrbSection() {
        return irbSection;
    }

    public static IdentifiableComponentFactory<?> getFactory(InvestigatorRegistrationPage page) {
        return new Factory(page);
    }

    @Override
    public Form1572TabHelper getHelper() {
        return helper;
    }

    @Override
    public boolean isReadOnly() {
        return !isPresent(By.id(PRACTICE_SITES_DIV_ID)) && !isPresent(By.id(LABS_DIV_ID))
                && !isPresent(By.id(IRBS_DIV_ID));
    }

    public File clickViewGenerated1572() throws IOException {
        return FileDownloadUtils.clickDownloadLink(getDriver(), viewGenerated1572Button).getFile();
    }

    @Override
    public boolean isCommentsPresent() {
        return commentsDiv != null;
    }

    public boolean isPracticeSiteAsteriskVisible() {
        return practiceSiteAsterisk.isDisplayed();
    }

    public boolean isIrbAsteriskVisible() {
        return irbAsterisk.isDisplayed();
    }

    public boolean isLabAsteriskVisible() {
        return clinicalLabAsterisk.isDisplayed();
    }

    public String getInstructions() {
        return instructionBubble.getText();
    }

    @Override
    protected void assertLoaded() {
        super.assertLoaded();
        assertElementWithIdPresent(TAB_UNIQUE_LOCATOR_ID);;
        assertFalse(JQueryUtils.isDialogDisplayed(getDriver()));
        assertElementWithIdPresent(VIEW_GENERATED_1572_BUTTON_ID);
    }

    @Override
    protected String getTabId() {
        return TAB_ID;
    }

    private static class Factory extends IdentifiableComponentFactory<ProtocolForm1572Tab> {

        private final InvestigatorRegistrationPage page;

        public Factory(InvestigatorRegistrationPage page) {
            this.page = page;
        }

        @Override
        protected ProtocolForm1572Tab getInstance(WebDriver driver) {
            return new ProtocolForm1572Tab(driver, page);
        }

    }

}
