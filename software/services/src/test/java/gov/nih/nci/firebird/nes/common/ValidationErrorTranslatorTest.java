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
package gov.nih.nci.firebird.nes.common;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import gov.nih.nci.coppa.po.StringMap;
import gov.nih.nci.coppa.po.StringMapType.Entry;
import gov.nih.nci.firebird.FirebirdModule;
import gov.nih.nci.firebird.common.ValidationFailure;
import gov.nih.nci.firebird.common.ValidationResult;

import org.junit.Before;
import org.junit.Test;

public class ValidationErrorTranslatorTest {

    private enum NesField {
        EMAIL("email"),
        POSTAL_ADDRESS("postalAddress"),
        EMAIL_INDEX_0("email[0].value"),
        STREET_ADDRESS(POSTAL_ADDRESS.getFieldName() + ".streetAddressLine"),
        STATE_OR_PROVINCE(POSTAL_ADDRESS.getFieldName() + ".null"),
        PLAYER("player");

        String fieldName;

        NesField(String fieldName) {
            this.fieldName = fieldName;
        }

        String getFieldName() {
            return fieldName;
        }
    }

    private static final String MISSING_EMAIL_ERROR = "Houston, we have a problem.";
    private static final String MALFORMED_EMAIL_ERROR = "Houston, we have a problem.";
    private static final String MALFORMED_PHONE_NUMBER_ERROR = "This phone number is not formatted right.";
    private static final String MISSING_STATE_ERROR = "State Or Province is required for this country.";
    private static final String MISSING_FIELD_ERROR = "must be set";    
    private static final String MISSING_INFORMATION_ERROR_MESSAGE_KEY = "nes.validation.error.missing.information";
    private static final String MALFORMED_PHONE_NUMBER_MESSAGE_KEY = "nes.validation.error.phone.number.wrong.digits";
    private static final String FIELD_NAME_KEY_SUFFIX = ".fieldName";
    private static final String FIELD_MAPPING_KEY_SUFFIX = ".mapping";
    private static final String PHONE_NUMBER_FIELD = "phoneNumber";

    private static Properties properties = new Properties();
    private static ResourceBundle bundle = ResourceBundle.getBundle("resources", Locale.getDefault());

    private ValidationErrorTranslator translator = new ValidationErrorTranslator(properties, bundle);


    @Before
    public void setUp() {
        try {
            properties.load(FirebirdModule.class.getResourceAsStream("/nes_mapping.properties"));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load firebird properties file.", e);
        }
    }

    @Test
    public void testTranslateStringMapValidation() {
        StringMap expectedResult = new StringMap();
        ValidationResult result = translator.translateStringMapValidation(expectedResult);
        assertTrue(result.isValid());
    }

    @Test
    public void testTranslateStringMapValidation_MissingEmail() {
        StringMap expectedResult = new StringMap();
        String field = NesField.EMAIL.getFieldName();
        Entry expectedError = getStringMapEntry(field, MISSING_EMAIL_ERROR);
        expectedResult.getEntry().add(expectedError);

        ValidationResult result = translator.translateStringMapValidation(expectedResult);
        assertNotNull(result);
        assertEquals(1, result.getFailures().size());
        ValidationFailure failure = result.getFailures().iterator().next();
        assertEquals(getFieldMapping(field), failure.getFieldKey());
        assertEquals(getFieldName(field) + bundle.getString(MISSING_INFORMATION_ERROR_MESSAGE_KEY),
                failure.getMessage());
    }

    private Entry getStringMapEntry(String key, String value) {
        Entry expectedError = new Entry();
        expectedError.setKey(key);
        expectedError.getValue().add(value);
        return expectedError;
    }

    private String getFieldMapping(String field) {
        return bundle.getString(field + FIELD_MAPPING_KEY_SUFFIX);
    }

    private String getFieldName(String field) {
        return bundle.getString(field + FIELD_NAME_KEY_SUFFIX) + " ";
    }

    @Test
    public void testTranslateStringMapValidation_MalformedEmail() {
        StringMap expectedResult = new StringMap();
        String field = NesField.EMAIL_INDEX_0.getFieldName();
        String emailField = NesField.EMAIL.getFieldName();
        Entry expectedError = getStringMapEntry(field, MALFORMED_EMAIL_ERROR);
        expectedResult.getEntry().add(expectedError);

        ValidationResult result = translator.translateStringMapValidation(expectedResult);
        assertNotNull(result);
        assertEquals(1, result.getFailures().size());
        ValidationFailure failure = result.getFailures().iterator().next();
        assertEquals(getFieldMapping(emailField), failure.getFieldKey());
        assertEquals(getFieldName(emailField) + MALFORMED_EMAIL_ERROR, failure.getMessage());
    }

    @Test
    public void testTranslateStringMapValidation_MalformedPhoneNumber() {
        StringMap expectedResult = new StringMap();
        Entry expectedError = getStringMapEntry("Phone number 12334464", MALFORMED_PHONE_NUMBER_ERROR);
        expectedResult.getEntry().add(expectedError);

        ValidationResult result = translator.translateStringMapValidation(expectedResult);
        assertNotNull(result);
        assertEquals(1, result.getFailures().size());
        ValidationFailure failure = result.getFailures().iterator().next();
        assertEquals(getFieldMapping(PHONE_NUMBER_FIELD), failure.getFieldKey());
        assertEquals(bundle.getString(MALFORMED_PHONE_NUMBER_MESSAGE_KEY), failure.getMessage());
    }

    @Test
    public void testTranslateStringMapValidation_MissingState() {
        StringMap expectedResult = new StringMap();
        String field = NesField.STATE_OR_PROVINCE.getFieldName();
        Entry expectedError = getStringMapEntry(field, MISSING_STATE_ERROR);
        expectedResult.getEntry().add(expectedError);

        ValidationResult result = translator.translateStringMapValidation(expectedResult);
        assertNotNull(result);
        assertEquals(1, result.getFailures().size());
        ValidationFailure failure = result.getFailures().iterator().next();
        assertEquals(getFieldMapping(field), failure.getFieldKey());
        assertEquals(getFieldName(field) + bundle.getString(MISSING_INFORMATION_ERROR_MESSAGE_KEY),
                failure.getMessage());
    }

    @Test
    public void testTranslateStringMapValidation_MissingAddress() {
        String missingError = bundle.getString(MISSING_INFORMATION_ERROR_MESSAGE_KEY);
        String field = NesField.POSTAL_ADDRESS.getFieldName();
        StringMap expectedResult = new StringMap();
        Entry expectedError = getStringMapEntry(field, missingError);
        expectedResult.getEntry().add(expectedError);

        ValidationResult result = translator.translateStringMapValidation(expectedResult);
        assertNotNull(result);
        assertEquals(1, result.getFailures().size());
        ValidationFailure failure = result.getFailures().iterator().next();
        assertEquals(null, failure.getFieldKey());
        assertEquals(getFieldName(field) + missingError, failure.getMessage());
    }

    @Test
    public void testTranslateStringMapValidation_IgnoredErrors() {
        StringMap expectedResult = new StringMap();
        String field = NesField.PLAYER.getFieldName();
        Entry expectedError = getStringMapEntry(field, MISSING_FIELD_ERROR);
        expectedResult.getEntry().add(expectedError);

        ValidationResult result = translator.translateStringMapValidation(expectedResult);
        assertNotNull(result);
        assertTrue(result.isValid());
    }

}
