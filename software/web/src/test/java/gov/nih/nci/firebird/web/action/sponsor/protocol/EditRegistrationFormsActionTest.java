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
package gov.nih.nci.firebird.web.action.sponsor.protocol;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import gov.nih.nci.firebird.common.RichTextUtil;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;
import gov.nih.nci.firebird.data.FormOptionality;
import gov.nih.nci.firebird.data.FormType;
import gov.nih.nci.firebird.data.FormTypeEnum;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.RegistrationType;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.service.protocol.FormTypeService;
import gov.nih.nci.firebird.service.protocol.ProtocolService;
import gov.nih.nci.firebird.test.FormTypeFactory;
import gov.nih.nci.firebird.test.ProtocolFactory;
import gov.nih.nci.firebird.web.common.FirebirdUIConstants;
import gov.nih.nci.firebird.web.test.AbstractWebTest;

import java.util.Arrays;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

public class EditRegistrationFormsActionTest extends AbstractWebTest {
    @Inject
    private ProtocolService protocolService;
    @Inject
    private FormTypeService formTypeService;
    @Inject
    private EditRegistrationFormsAction action;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        action.setServletRequest(getMockRequest());
    }

    @Test
    public void testEnterAction() {
        assertEquals(ActionSupport.SUCCESS, action.navigationAction());
    }

    @Test
    public void testSaveForms() throws ValidationException {
        Protocol protocol = ProtocolFactory.getInstanceWithId().createWithForms();
        FormType form1572 = FormTypeFactory.getInstanceWithId().create(FormTypeEnum.FORM_1572);
        FormType financialDisclosure = FormTypeFactory.getInstanceWithId().create(
                FormTypeEnum.FINANCIAL_DISCLOSURE_FORM);
        setupProtocolWithForms(protocol, form1572, financialDisclosure);
        action.setInvestigatorOptionalities(Arrays.asList(FormOptionality.REQUIRED.name(),
                FormOptionality.REQUIRED.name()));
        action.setSubinvestigatorOptionalities(Arrays.asList(FormOptionality.OPTIONAL.name(),
                FormOptionality.NONE.name()));
        String comment = "some comment";
        action.setComment(comment);

        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveForms());
        verifySaveForms(protocol, form1572, financialDisclosure, comment);
    }

    private void setupProtocolWithForms(Protocol protocol, FormType form1572, FormType financialDisclosure) {
        when(formTypeService.getById(form1572.getId())).thenReturn(form1572);
        when(formTypeService.getById(financialDisclosure.getId())).thenReturn(financialDisclosure);
        when(protocolService.getById(protocol.getId())).thenReturn(protocol);
        protocol.getRegistrationConfiguration().setInvestigatorOptionality(form1572, FormOptionality.OPTIONAL);
        protocol.getRegistrationConfiguration().setSubinvestigatorOptionality(form1572, FormOptionality.OPTIONAL);
        action.setProtocol(protocol);
        action.setFormTypeIds(Arrays.asList(form1572.getId(), financialDisclosure.getId()));
        action.prepare();
    }

    private void verifySaveForms(Protocol protocol, FormType form1572, FormType financialDisclosure, String comment)
            throws ValidationException {
        verify(formTypeService).getById(form1572.getId());
        verify(formTypeService).getById(financialDisclosure.getId());
        verify(protocolService).updateProtocol(any(Protocol.class), eq(protocol),
                eq(RichTextUtil.cleanRichText(comment)));
        assertEquals(3, protocol.getRegistrationConfiguration().getAssociatedFormTypes().size());
        assertTrue(protocol.getRegistrationConfiguration().getAssociatedFormTypes().contains(form1572));
        assertTrue(protocol.getRegistrationConfiguration().getAssociatedFormTypes().contains(financialDisclosure));
        assertEquals(FormOptionality.REQUIRED,
                protocol.getRegistrationConfiguration().getInvestigatorOptionality(form1572));
        assertEquals(FormOptionality.REQUIRED,
                protocol.getRegistrationConfiguration().getInvestigatorOptionality(financialDisclosure));
        assertEquals(FormOptionality.OPTIONAL,
                protocol.getRegistrationConfiguration().getSubinvestigatorOptionality(form1572));
        assertEquals(FormOptionality.NONE,
                protocol.getRegistrationConfiguration().getSubinvestigatorOptionality(financialDisclosure));
    }

    @Test
    public void testSaveFormsNoForms() {
        action.setServletRequest(ServletActionContext.getRequest());
        Protocol p = ProtocolFactory.getInstance().createWithForms();
        action.setProtocol(p);
        action.setSubinvestigatorOptionalities(Arrays.asList(FormOptionality.OPTIONAL.name(),
                FormOptionality.NONE.name()));
        assertEquals(FirebirdUIConstants.RETURN_CLOSE_DIALOG, action.saveForms());
        verifyZeroInteractions(formTypeService);
    }

    @Test
    public void testSaveFormsNoComment() throws ValidationException {
        action.setServletRequest(ServletActionContext.getRequest());
        action.setProtocol(ProtocolFactory.getInstance().createWithFormsDocuments());
        action.getProtocol().setId(1L);
        when(protocolService.getById(any(Long.class))).thenReturn(action.getProtocol());
        action.prepare();
        FormType type = new FormType("foo");
        action.setFormTypeIds(Arrays.asList(1L));
        when(formTypeService.getById(eq(1L))).thenReturn(type);
        action.setInvestigatorOptionalities(Arrays.asList(FormOptionality.OPTIONAL.name()));
        action.setSubinvestigatorOptionalities(Arrays.asList(FormOptionality.OPTIONAL.name()));
        doThrow(new ValidationException(new ValidationResult(new ValidationFailure("comment", "no comment!!!")))).when(
                protocolService).updateProtocol(any(Protocol.class), any(Protocol.class), anyString());
        assertEquals("input", action.saveForms());
        assertTrue(action.getFieldErrors().containsKey("comment"));
    }

    @Test
    public void testGetStandardFormsJson() throws JSONException {
        FormType formType = FormTypeFactory.getInstanceWithId().create();
        List<FormType> standardConfigurableForms = Lists.newArrayList(formType);
        when(formTypeService.getStandardConfigureableForms(RegistrationType.PROTOCOL)).thenReturn(standardConfigurableForms);
        String json = action.getStandardFormsJson();
        assertTrue(json.contains(formType.getId().toString()));
        assertTrue(json, json.contains(formType.getName()));
        assertTrue(json, json.contains(formType.getDescription()));
        assertTrue(json.contains(formType.getInvestigatorDefault().getDisplay()));
        assertTrue(json.contains(formType.getSubinvestigatorDefault().getDisplay()));
    }

    @Test
    public void testGetOptionalities() {
        Protocol protocol = ProtocolFactory.getInstance().createWithForms();
        FormType formType = protocol.getRegistrationConfiguration().getAssociatedFormTypes().get(0);
        protocol.getRegistrationConfiguration().setInvestigatorOptionality(formType, FormOptionality.REQUIRED);
        protocol.getRegistrationConfiguration().setSubinvestigatorOptionality(formType, FormOptionality.OPTIONAL);
        action.setProtocol(protocol);
        assertEquals(FormOptionality.REQUIRED, action.getInvestigatorOptionality(formType));
        assertEquals(FormOptionality.OPTIONAL, action.getSubinvestigatorOptionality(formType));
    }
}
