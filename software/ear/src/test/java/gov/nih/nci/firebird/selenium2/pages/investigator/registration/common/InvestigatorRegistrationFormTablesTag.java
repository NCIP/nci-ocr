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
package gov.nih.nci.firebird.selenium2.pages.investigator.registration.common;

import static gov.nih.nci.firebird.data.FormOptionality.*;
import gov.nih.nci.firebird.commons.selenium2.support.AbstractLoadableComponent;
import gov.nih.nci.firebird.commons.selenium2.util.WebElementUtils;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.selenium2.pages.base.TableListing;
import gov.nih.nci.firebird.selenium2.pages.components.AbstractTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AnnualAdditionalAttachmentsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AnnualForm1572Tab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.AnnualRegistrationPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.FinancialDisclosureTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.annual.registration.SupplementalInvestigatorDataFormTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.CurriculumVitaeTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.HumanResearchCertificateTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.InvestigatorRegistrationPage;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProtocolAdditionalAttachmentsTab;
import gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.ProtocolForm1572Tab;
import gov.nih.nci.firebird.selenium2.pages.registration.common.FormRejectionCommentsDialog;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * tags/investigatorRegistrationFormTables.tag
 */
public class InvestigatorRegistrationFormTablesTag extends
        AbstractLoadableComponent<InvestigatorRegistrationFormTablesTag> {

    public static final String REQUIRED_FORMS_TABLE_ID = "requiredFormsTable";
    public static final String OPTIONAL_FORMS_TABLE_ID = "optionalFormsTable";
    public static final String SUPPLEMENTAL_FORMS_TABLE_ID = "supplementalFormsTable";

    @FindBy(id = REQUIRED_FORMS_TABLE_ID)
    private WebElement requiredFormsTable;
    @FindBy(id = OPTIONAL_FORMS_TABLE_ID)
    private WebElement optionalFormsTable;
    @FindBy(id = SUPPLEMENTAL_FORMS_TABLE_ID)
    private WebElement supplementalFormsTable;

    private Map<FormOptionality, WebElement> optionalityTableMap = Maps.newHashMap();

    public InvestigatorRegistrationFormTablesTag(WebDriver driver) {
        super(driver);
        optionalityTableMap.put(REQUIRED, requiredFormsTable);
        optionalityTableMap.put(OPTIONAL, optionalFormsTable);
        optionalityTableMap.put(SUPPLEMENTARY, supplementalFormsTable);
    }

    public List<FormListing> getFormsListing() {
        return Lists.newArrayList((Iterables.concat(getRequiredFormsListing(), getOptionalFormsListing(),
                getSupplementalFormsListing())));
    }

    public List<FormListing> getRequiredFormsListing() {
        return getFormsListing(REQUIRED);
    }

    public List<FormListing> getFormsListing(FormOptionality optionality) {
        WebElement table = optionalityTableMap.get(optionality);
        List<FormListing> formsListings = Lists.newArrayList();
        for (WebElement form : table.findElements(By.className("form"))) {
            formsListings.add(new FormListing(form, optionality));
        }
        return formsListings;
    }

    public List<FormListing> getOptionalFormsListing() {
        if (WebElementUtils.isPresent(getDriver(), By.id(OPTIONAL_FORMS_TABLE_ID))) {
            return getFormsListing(OPTIONAL);
        } else {
            return Collections.emptyList();
        }
    }

    public List<FormListing> getSupplementalFormsListing() {
        if (WebElementUtils.isPresent(getDriver(), By.id(SUPPLEMENTAL_FORMS_TABLE_ID))) {
            return getFormsListing(SUPPLEMENTARY);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    protected void assertLoaded() {
        assertElementsWithIdsPresent(REQUIRED_FORMS_TABLE_ID);
    }

    public class FormListing implements TableListing {
        private final Long id;
        private String description;
        private String formStatus;
        private int additionalDocumentsCount;
        private final WebElement row;
        private final FormOptionality optionality;
        private final WebElement commentLink;

        public FormListing(WebElement row, FormOptionality optionality) {
            this.row = row;
            this.optionality = optionality;
            id = Long.valueOf(WebElementUtils.getId(row));

            if (WebElementUtils.isPresent(row, By.className("formDescription"))) {
                description = WebElementUtils.getElementIfPresent(row, By.cssSelector(".formDescription span")).getText();
            }
            if (WebElementUtils.isPresent(row, By.id("formStatus"))) {
                formStatus = WebElementUtils.getElementIfPresent(row, By.id("formStatus")).getText();
            }
            if (WebElementUtils.isPresent(row, By.id("additionalDocumentsCount"))) {
                additionalDocumentsCount = Integer.parseInt(WebElementUtils.getElementIfPresent(row,
                        By.id("additionalDocumentsCount")).getText());
            }
            this.commentLink = getElementIfPresent(row, By.tagName("a"));
        }

        @Override
        public Long getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String getFormStatus() {
            return formStatus;
        }

        public int getAdditionalDocumentsCount() {
            return additionalDocumentsCount;
        }

        public String getOptionality() {
            return optionality.getDisplay();
        }

        public AbstractTab<?> click() {
            row.click();
            return (AbstractTab<?>) identifyDisplayedComponent(30,
                    SupplementalInvestigatorDataFormTab.getFactory(new AnnualRegistrationPage(getDriver())),
                    FinancialDisclosureTab.getFactory(new AnnualRegistrationPage(getDriver())),
                    AnnualAdditionalAttachmentsTab.getFactory(new AnnualRegistrationPage(getDriver())),
                    AnnualForm1572Tab.getFactory(new AnnualRegistrationPage(getDriver())),
                    ProtocolForm1572Tab.getFactory(new InvestigatorRegistrationPage(getDriver())),
                    gov.nih.nci.firebird.selenium2.pages.investigator.protocol.registration.FinancialDisclosureTab.getFactory(new InvestigatorRegistrationPage(getDriver())),
                    CurriculumVitaeTab.getFactory(new InvestigatorRegistrationPage(getDriver())),
                    HumanResearchCertificateTab.getFactory(new InvestigatorRegistrationPage(getDriver())),
                    ProtocolAdditionalAttachmentsTab.getFactory(new InvestigatorRegistrationPage(getDriver())));
        }

        public FormRejectionCommentsDialog clickComments() {
            commentLink.click();
            return new FormRejectionCommentsDialog(getDriver(), InvestigatorRegistrationFormTablesTag.this).waitUntilReady();
        }
    }

}
