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
package gov.nih.nci.firebird.service.ctep.esys;

import static gov.nih.nci.firebird.service.ctep.esys.RegistrationTranslator.translateRegistration;
import gov.nih.nci.ctep.ces.ocr.api.CesInvestigatorService;
import gov.nih.nci.ctep.ces.ocr.api.Investigator;
import gov.nih.nci.ctep.ces.ocr.api.Registration;
import gov.nih.nci.firebird.data.AnnualRegistration;
import gov.nih.nci.firebird.data.FirebirdFile;
import gov.nih.nci.firebird.data.user.FirebirdUser;
import gov.nih.nci.firebird.data.user.InvestigatorStatus;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.file.FileService;
import gov.nih.nci.firebird.service.investigator.InvestigatorService;
import gov.nih.nci.firebird.service.lookup.CountryLookupService;
import gov.nih.nci.firebird.service.properties.DynamicPropertiesService;
import gov.nih.nci.firebird.service.user.FirebirdUserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

/**
 * Implementation of the ESYS integration service.
 */
@Stateless
public class EsysIntegrationServiceBean implements EsysIntegrationService {

    private static final Logger LOG = Logger.getLogger(EsysIntegrationServiceBean.class);
    private static final int BUFFER = 4096;
    static final String INVESTIGATOR_STATUS_UPDATE_TIMESTAMP_PROPERTY_NAME =
            "ctep_investigator_status_update_timestamp";

    private CesInvestigatorService cesInvestigatorService;
    private DynamicPropertiesService propertiesService;
    private FirebirdUserService userService;
    private InvestigatorService investigatorService;
    private FileService fileService;
    private CountryLookupService countryLookupService;

    @Resource(mappedName = "firebird/CesInvestigatorServiceStubBean/remote")
    void setCesInvestigatorService(CesInvestigatorService cesInvestigatorService) {
        this.cesInvestigatorService = cesInvestigatorService;
    }

    @Resource(mappedName = "firebird/DynamicPropertiesServiceBean/local")
    void setPropertiesService(DynamicPropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    @Resource(mappedName = "firebird/FirebirdUserServiceBean/local")
    void setUserService(FirebirdUserService userService) {
        this.userService = userService;
    }

    @Resource(mappedName = "firebird/InvestigatorServiceBean/local")
    void setInvestigatorService(InvestigatorService investigatorService) {
        this.investigatorService = investigatorService;
    }

    @Resource(mappedName = "firebird/FileServiceBean/local")
    void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Resource(mappedName = "firebird/CountryLookupServiceBean/local")
    void setCountryLookupService(CountryLookupService countryLookupService) {
        this.countryLookupService = countryLookupService;
    }

    @Override
    public void withdrawInvestigator(FirebirdUser investigator, FirebirdUser approver) {
        cesInvestigatorService.withdrawInvestigator(investigator.getPerson().getCtepId(), approver.getPerson()
                .getCtepId());
    }

    @Override
    public void updateInvestigatorStatuses() {
        Date lastStatusUpdate = propertiesService.getDateProperty(INVESTIGATOR_STATUS_UPDATE_TIMESTAMP_PROPERTY_NAME);
        Date currentUpdate = new Date();
        List<String> updatedInvestigatorCtepIds = cesInvestigatorService.getUpdatedInvestigators(lastStatusUpdate);
        Iterable<FirebirdUser> updatedInvestigators = getInvestigators(updatedInvestigatorCtepIds);
        for (FirebirdUser investigator : updatedInvestigators) {
            String ctepId = investigator.getPerson().getCtepId();
            InvestigatorStatus status = InvestigatorStatus.translate(cesInvestigatorService
                    .getInvestigatorStatus(ctepId));
            investigatorService.handleStatus(investigator, status);
        }
        propertiesService.setDateProperty(INVESTIGATOR_STATUS_UPDATE_TIMESTAMP_PROPERTY_NAME, currentUpdate);
    }

    private Iterable<FirebirdUser> getInvestigators(List<String> ctepIds) {
        List<FirebirdUser> userList = Lists.transform(ctepIds, new Function<String, FirebirdUser>() {
            @Override
            public FirebirdUser apply(String ctepId) {
                return userService.getByCtepId(ctepId);
            }
        });
        return Iterables.filter(userList, new Predicate<FirebirdUser>() {
            @Override
            public boolean apply(FirebirdUser user) {
                return user != null;
            }
        });
    }

    @Override
    public void transferRegistration(AnnualRegistration registration, String approverCtepId, String approvalComments)
            throws ValidationException {
        Registration esysRegistration = translateRegistration(registration, approverCtepId, approvalComments);
        Investigator investigator = InvestigatorTranslator.translateInvestigator(registration.getProfile().getUser(),
                countryLookupService);
        cesInvestigatorService.createRegistration(investigator, esysRegistration);
        transferPacketDocuments(registration);
        // TODO Catch ESYS validation exception and convert to Firebirds
    }

    private void transferPacketDocuments(AnnualRegistration registration) {
        try {
            String prefix = registration.getProfile().getPerson().getLastName() + "_"
                    + registration.getProfile().getPerson().getFirstName() + "_";
            File zipFile = zipFiles(prefix, registration.getDocumentsForReview());
            byte[] documents = Files.toByteArray(zipFile);
            cesInvestigatorService.uploadPacketDocuments(registration.getProfile().getPerson().getCtepId(), documents);
        } catch (IOException e) {
            LOG.warn("IOException creating ZIP file containing packet documents", e);
        }
    }

    private File zipFiles(String prefix, Set<FirebirdFile> files) throws IOException {
        File zipFile = File.createTempFile(prefix, ".zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        for (FirebirdFile file : files) {
            addFile(file, out);
        }
        out.close();
        return zipFile;
    }

    private void addFile(FirebirdFile file, ZipOutputStream out) throws IOException {
        byte[] temp = new byte[BUFFER];
        InputStream in = fileService.readFile(file);
        out.putNextEntry(new ZipEntry(file.getName()));
        int length;
        while ((length = in.read(temp)) > 0) {
            out.write(temp, 0, length);
        }
        out.closeEntry();
        in.close();
    }

}
