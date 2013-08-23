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
package gov.nih.nci.firebird.selenium2.pages.components.tags;

import gov.nih.nci.firebird.commons.selenium2.support.AbstractLoadableComponent;
import gov.nih.nci.firebird.commons.selenium2.util.FileDownloadUtils;
import gov.nih.nci.firebird.commons.selenium2.util.JQueryUtils;
import gov.nih.nci.firebird.commons.selenium2.util.WebElementUtils;
import gov.nih.nci.firebird.selenium2.pages.base.TableListing;
import gov.nih.nci.firebird.selenium2.pages.registration.common.FormRejectionCommentsDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.annual.registration.ReviewAnnualRegistrationPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.RegistrationPacketPage;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewHumanResearchCertificatesDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.protocol.review.ReviewRegistrationTab;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.AdditionalAttachmentsDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.FinancialDisclosuresSupportingDocumentsDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.FormReviewCommentDialog;
import gov.nih.nci.firebird.selenium2.pages.sponsor.registration.common.RegistrationClinicalLabCertificatesDialog;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * tags/sponsorReviewRegistrationFormsTable.tag
 */
public class SponsorReviewRegistrationFormsTable extends AbstractLoadableComponent<SponsorReviewRegistrationFormsTable> {

    private static final String TABLE_ID = "reviewRegistrationFormsTable";
    private static final String TABLE_ROWS_CLASS = "form";

    private final AbstractLoadableComponent<?> parent;

    public SponsorReviewRegistrationFormsTable(WebDriver driver, AbstractLoadableComponent<?> parent) {
        super(driver);
        this.parent = parent;
    }

    public List<RegistrationListing> getListings() {
        List<RegistrationListing> listings = Lists.newArrayList();
        for (WebElement row : getTable().findElements(By.className(TABLE_ROWS_CLASS))) {
            listings.add(new RegistrationListing(row));
        }
        return listings;
    }

    private WebElement getTable() {
        return Iterables.find(findElements(By.id(TABLE_ID)), new Predicate<WebElement>() {
            public boolean apply(WebElement table) {
                return table.isDisplayed();
            }
        });
    }

    @Override
    public void assertLoaded() {
        assertFindBysPresent();
    }

    public class RegistrationListing implements TableListing {

        private static final String DOWNLOAD_BUTTON_CLASS_NAME = "downloadButton";
        private static final String RADIO_PREFIX = "review_";
        private static final String ACCEPT_RADIO_SUFFIX = "ACCEPTED";
        private static final String REJECT_RADIO_SUFFIX = "REJECTED";
        private static final String FORM_DESCRIPTION_ID = "formDescription";
        private static final String FORM_STATUS_ID = "formStatus";
        private static final String FORM_STATUS_DATE_ID = "formStatusDate";
        private static final String VIEW_ATTACHMENTS_BUTTON_CLASS = "viewAttachments";
        private static final String VIEW_COMMENTS_BUTTON_CLASS = "viewComments";

        private final Long id;
        private final WebElement downloadButton;
        private final WebElement acceptRadio;
        private final WebElement rejectRadio;
        private final WebElement acceptRadioInput;
        private final WebElement rejectRadioInput;
        private final String form;
        private final String status;
        private final String statusDate;
        private final WebElement viewAttachmentsButton;
        private final WebElement viewCommentsButton;

        public RegistrationListing(WebElement row) {
            id = Long.valueOf(WebElementUtils.getId(row));
            downloadButton = getElementIfPresent(row, (By.className(DOWNLOAD_BUTTON_CLASS_NAME)));
            acceptRadio = getElementIfPresent(row,
                    By.cssSelector("label[for='" + RADIO_PREFIX + id + ACCEPT_RADIO_SUFFIX + "']"));
            rejectRadio = getElementIfPresent(row,
                    By.cssSelector("label[for='" + RADIO_PREFIX + id + REJECT_RADIO_SUFFIX + "']"));
            acceptRadioInput = getElementIfPresent(row, By.id(RADIO_PREFIX + id + ACCEPT_RADIO_SUFFIX));
            rejectRadioInput = getElementIfPresent(row, By.id(RADIO_PREFIX + id + REJECT_RADIO_SUFFIX));
            form = row.findElement(By.id(FORM_DESCRIPTION_ID)).getText();
            status = row.findElement(By.id(FORM_STATUS_ID)).getText();
            statusDate = row.findElement(By.id(FORM_STATUS_DATE_ID)).getText();
            viewAttachmentsButton = getElementIfPresent(row, By.className(VIEW_ATTACHMENTS_BUTTON_CLASS));
            viewCommentsButton = getElementIfPresent(row, By.className(VIEW_COMMENTS_BUTTON_CLASS));
        }

        @Override
        public Long getId() {
            return id;
        }

        public File downloadFormPdf() throws IOException {
            File formPdf = FileDownloadUtils.clickDownloadLink(getDriver(), downloadButton).getFile();
            pause(100);
            parent.waitUntilReady();
            return formPdf;
        }

        public boolean hasDownloadButton() {
            return downloadButton != null;
        }

        public AbstractLoadableComponent<?> clickDownload() {
            downloadButton.click();
            pause(400);
            return identifyDisplayedComponent(AdditionalAttachmentsDialog.getFactory(new ReviewRegistrationTab(
                    getDriver(), new RegistrationPacketPage(getDriver()))),
                    ReviewHumanResearchCertificatesDialog.getFactory(new ReviewRegistrationTab(getDriver(),
                            new RegistrationPacketPage(getDriver()))), ReviewAnnualRegistrationPage.FACTORY,
                    ReviewRegistrationTab.FACTORY);
        }

        public void clickAccept() {
            acceptRadio.click();
            JQueryUtils.waitForAjaxCallToFinish(getDriver());
            parent.waitUntilReady();
        }

        public boolean isAcceptSelected() {
            return acceptRadioInput == null ? false : acceptRadioInput.isSelected();
        }

        public FormReviewCommentDialog clickReject() {
            if (isRejectSelected()) {
                rejectRadio.click();
                pause(200);
                parent.waitUntilReady();
                return null;
            } else {
                rejectRadio.click();
                return new FormReviewCommentDialog(getDriver(), parent).waitUntilReady();
            }
        }

        private boolean isRejectSelected() {
            return rejectRadioInput == null ? false : rejectRadioInput.isSelected();
        }

        public void clearDisposition() {
            if (isAcceptSelected()) {
                clickAccept();
            } else if (isRejectSelected()) {
                clickReject();
            }
        }

        public Boolean getDisposition() {
            if (isAcceptSelected()) {
                return true;
            } else if (isRejectSelected()) {
                return false;
            } else {
                return null;
            }
        }

        public String getForm() {
            return form;
        }

        public String getStatus() {
            return status;
        }

        public String getStatusDate() {
            return statusDate;
        }

        public AbstractLoadableComponent<?> clickViewAttachments() {
            viewAttachmentsButton.click();
            return identifyDisplayedComponent(RegistrationClinicalLabCertificatesDialog.getFactory(parent),
                    FinancialDisclosuresSupportingDocumentsDialog.getFactory(parent),
                    AdditionalAttachmentsDialog.getFactory(parent),
                    ReviewHumanResearchCertificatesDialog.getFactory(new ReviewRegistrationTab(getDriver(), new RegistrationPacketPage(getDriver()))));
        }

        public boolean hasViewAttachmentsButton() {
            return viewAttachmentsButton != null;
        }

        public AbstractLoadableComponent<?> clickViewComments() {
            viewCommentsButton.click();
            return identifyDisplayedComponent(FormReviewCommentDialog.getFactory(parent),
                    FormRejectionCommentsDialog.getFactory(parent));
        }

        public boolean hasViewCommentsButton() {
            return viewCommentsButton != null;
        }

        public int getNumberOfAdditionalDocuments() {
            if (!hasViewAttachmentsButton()) {
                return 0;
            }
            String regExToRetrieveAttachmentCount = "\\((.*?)\\)";
            Pattern pattern = Pattern.compile(regExToRetrieveAttachmentCount);
            Matcher matcher = pattern.matcher(viewAttachmentsButton.getText());
            matcher.find();
            return Integer.parseInt(matcher.group(1));
        }

        public boolean isDispositionSelectionEnabled() {
            return acceptRadioInput.isEnabled();
        }

    }

}
