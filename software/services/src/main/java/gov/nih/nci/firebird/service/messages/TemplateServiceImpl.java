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
package gov.nih.nci.firebird.service.messages;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * A Service for handling the lookup and creation of messages using Velocity.
 */
public class TemplateServiceImpl implements TemplateService {

    private static final String TEMPLATE_RESOURCE_PATH = "/templates";
    private final VelocityEngine velocity;
    private final ResourceBundle resources;
    private final String baseUrl;
    private final boolean hasAnnualRegistrationSponsor;

    /**
     * @param velocity the velocity to set
     * @param resources the FIREBIRD resource bundle
     * @param baseUrl the base URL path for FIREBIRD links
     * @param sponsorWithAnnualRegistrationsExternalId the sponsorWithAnnualRegistrationsExternalId
     */
    @Inject
    public TemplateServiceImpl(VelocityEngine velocity, ResourceBundle resources,
            @Named("firebird.base.url") String baseUrl,
            @Named("sponsor.organization.with.annual.registrations.nes.id")
                String sponsorWithAnnualRegistrationsExternalId) {
        this.velocity = velocity;
        this.resources = resources;
        this.baseUrl = baseUrl;
        hasAnnualRegistrationSponsor = !isEmpty(sponsorWithAnnualRegistrationsExternalId);
    }

    @Override
    public String generateString(FirebirdStringTemplate template, Map<FirebirdTemplateParameter, Object> values) {
        VelocityContext context = getVelocityContext(template, values);
        String subjectTemplate = resources.getString(template.getTemplatePropertyName());
        StringWriter sw = new StringWriter();
        velocity.evaluate(context, sw, template.name(), subjectTemplate);
        return sw.toString();
    }

    @Override
    public String generateContent(FirebirdContentTemplate template,
            Map<FirebirdTemplateParameter, Object> parameterValues) {
        return generateFromFile(template, parameterValues);
    }

    @Override
    public FirebirdMessage generateMessage(FirebirdMessageTemplate template,
            Map<FirebirdTemplateParameter, Object> values) {
        values.put(FirebirdTemplateParameter.HAS_ANNUAL_REGISTRATION_SPONSOR, hasAnnualRegistrationSponsor);
        String body = generateFromFile(template.getContentTemplate(), values);
        String subject = generateString(template.getSubjectTemplate(), values);
        return new FirebirdMessage(subject, body);
    }

    private String generateFromFile(FirebirdContentTemplate template, Map<FirebirdTemplateParameter, Object> values) {
        VelocityContext context = getVelocityContext(template, values);
        Template velocityTemplate = getVelocityTemplate(template);
        StringWriter sw = new StringWriter();
        velocityTemplate.merge(context, sw);
        return sw.toString();
    }

    private VelocityContext getVelocityContext(FirebirdTemplate template,
            Map<FirebirdTemplateParameter, Object> values) {
        validateValues(template, values);
        return new VelocityContext(transformValues(values));
    }

    private Template getVelocityTemplate(FirebirdContentTemplate template) {
        try {
            return velocity.getTemplate(TEMPLATE_RESOURCE_PATH + "/" + template.getTemplate());
        } catch (ResourceNotFoundException rnfe) {
            throw new IllegalArgumentException("couldn't find the template " + template.getTemplate(), rnfe);
        } catch (ParseErrorException e) {
            throw new IllegalStateException("An Unexpected Error has occured while obtaining the Velocity Template "
                    + template.getTemplate(), e);
        }
    }

    private Map<String, Object> transformValues(Map<FirebirdTemplateParameter, Object> values) {
        Map<String, Object> transformedValues = new HashMap<String, Object>();
        for (Entry<FirebirdTemplateParameter, Object> entry : values.entrySet()) {
            if (FirebirdTemplateParameter.FIREBIRD_LINK == entry.getKey()) {
                transformedValues.put(entry.getKey().name(), baseUrl + entry.getValue());
            } else {
                transformedValues.put(entry.getKey().name(), entry.getValue());
            }
        }
        return transformedValues;
    }

    private void validateValues(FirebirdTemplate template, Map<FirebirdTemplateParameter, Object> values) {
        for (FirebirdTemplateParameter parameter : template.getRequiredParameters()) {
            if (!values.containsKey(parameter)) {
                throw new IllegalArgumentException("Template " + template + " is missing required parameter "
                        + parameter.name());
            }
            Object value = values.get(parameter);
            if (value != null) {
                checkParameterValueType(parameter, value);
            }
        }
    }

    private void checkParameterValueType(FirebirdTemplateParameter parameter, Object value) {
        Class<?> valueType = value.getClass();
        if (!parameter.getRequiredType().isAssignableFrom(valueType)) {
            throw new IllegalArgumentException("Invalid type supplied for parameter " + parameter.name()
                    + ", expected " + parameter.getRequiredType().getName() + " but was " + valueType);
        }
    }
}
