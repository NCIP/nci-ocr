package gov.nih.nci.firebird.nes.person;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.nih.nci.coppa.po.Person;
import gov.nih.nci.coppa.po.StringMap;
import gov.nih.nci.coppa.po.StringMapType.Entry;
import gov.nih.nci.coppa.services.entities.person.common.PersonI;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.nes.common.NesTranslatorHelperUtils;
import gov.nih.nci.firebird.test.AbstractIntegrationTest;
import gov.nih.nci.firebird.test.PersonFactory;

import org.apache.axis.AxisFault;
import org.iso._21090.AD;
import org.iso._21090.ADXP;
import org.iso._21090.AddressPartType;
import org.iso._21090.DSETTEL;
import org.iso._21090.TELPhone;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class PersonITest extends AbstractIntegrationTest {

    private enum NesField {
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        EMAIL("email"),
        POSTAL_ADDRESS("postalAddress"),
        EMAIL_INDEX_0("email[0].value"),
        STREET_ADDRESS(POSTAL_ADDRESS.getFieldName() + ".streetAddressLine"),
        CITY_OR_MUNICIPALITY(POSTAL_ADDRESS.getFieldName() + ".cityOrMunicipality"),
        POSTAL_CODE(POSTAL_ADDRESS.getFieldName() + ".postalCode"),
        STATE_OR_PROVINCE(POSTAL_ADDRESS.getFieldName() + ".null"),
        COUNTRY(POSTAL_ADDRESS.getFieldName() + ".country");

        String fieldName;

        NesField(String fieldName) {
            this.fieldName = fieldName;
        }

        String getFieldName() {
            return fieldName;
        }
    }

    EnumSet<NesField> emptyAddress = EnumSet.of(NesField.STREET_ADDRESS, NesField.CITY_OR_MUNICIPALITY,
            NesField.POSTAL_CODE, NesField.COUNTRY);

    private static final String PHONE_NUMBER_FIELD_PREFIX = "Phone number";
    private static final String MISSING_INFORMATION_ERROR = "must be set";
    private static final String MISSING_EMAIL_ERROR = "At least one  must be set";
    private static final String MALFORMED_EMAIL_ERROR = "is not a well-formed email address";
    private static final String MISSING_STATE_OR_PROVINCE_ERROR = "Invalid State or Province for Country";
    private static final String CA_US_PHONE_FORMAT_ERROR_NEW = "US and Canadian phone, fax, and tty numbers must match ###-###-####x#*.";
    private static final String CA_US_PHONE_FORMAT_ERROR_OLD = "US and Canadian phone, fax, and tty numbers must match ###-###-####(x#*).";
    private static final Set<String> CA_US_PHONE_FORMATS = Sets.newHashSet(CA_US_PHONE_FORMAT_ERROR_NEW, CA_US_PHONE_FORMAT_ERROR_OLD);

    @Inject
    private PersonI personClient;

    @Test(expected = AxisFault.class)
    public void testValidate_NullPerson() throws RemoteException {
        personClient.validate(null);
    }

    @Test
    public void testValidate_EmptyPerson() throws RemoteException {
        StringMap results = personClient.validate(new Person());
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        checkForMissingErrors(nesErrors, NesField.FIRST_NAME, NesField.LAST_NAME, NesField.POSTAL_ADDRESS);
        checkForMissingEmailError(nesErrors);
    }

    private Map<String, List<String>> convertToMap(StringMap nesErrors) {
        Map<String, List<String>> convertedErrors = Maps.newHashMap();
        for (Entry entry : nesErrors.getEntry()) {
            convertedErrors.put(entry.getKey(), entry.getValue());
        }
        return convertedErrors;
    }

    private void checkForMissingErrors(Map<String, List<String>> nesErrors, NesField... fields) {
        checkForMissingErrors(nesErrors, EnumSet.copyOf(Arrays.asList(fields)));
    }

    private void checkForMissingErrors(Map<String, List<String>> nesErrors, EnumSet<NesField> fields) {
        checkForMessageErrors(nesErrors, fields, MISSING_INFORMATION_ERROR);
    }

    private void checkForMissingEmailError(Map<String, List<String>> nesErrors) {
        checkForMessageErrors(nesErrors, EnumSet.of(NesField.EMAIL), MISSING_EMAIL_ERROR);
    }

    private void checkForMessageErrors(Map<String, List<String>> nesErrors, EnumSet<NesField> fields, String message) {
        for (NesField field : fields) {
            assertTrue("Cannot find key " + field.getFieldName(), nesErrors.containsKey(field.getFieldName()));
            assertEquals(message, nesErrors.get(field.getFieldName()).get(0));
        }
    }

    @Test
    public void testValidate_EmptyAddress() throws RemoteException {
        Person testPerson = new Person();
        testPerson.setPostalAddress(new AD());
        StringMap results = personClient.validate(testPerson);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        checkForMissingErrors(nesErrors, NesField.FIRST_NAME, NesField.LAST_NAME);
        checkForMissingErrors(nesErrors, emptyAddress);
        checkForMissingEmailError(nesErrors);
    }

    @Test
    public void testValidate_EmptyUSAddress() throws RemoteException {
        Person testPerson = new Person();
        testPerson.setPostalAddress(new AD());
        ADXP countryPart = new ADXP();
        countryPart.setType(AddressPartType.CNT);
        countryPart.setCode(FirebirdConstants.US_COUNTRY_CODE);
        countryPart.setCodeSystem("ISO 3166");
        testPerson.getPostalAddress().getPart().add(countryPart);

        StringMap results = personClient.validate(testPerson);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(7, nesErrors.size());
        checkForMissingErrors(nesErrors, NesField.FIRST_NAME, NesField.LAST_NAME, NesField.STREET_ADDRESS,
                NesField.CITY_OR_MUNICIPALITY, NesField.POSTAL_CODE);
        checkForMissingEmailError(nesErrors);
        checkForRequiredStateOrProvinceError(nesErrors);
    }

    @Test
    public void testValidate_EmptyCAAddress() throws RemoteException {
        Person testPerson = new Person();
        testPerson.setPostalAddress(new AD());
        ADXP countryPart = new ADXP();
        countryPart.setType(AddressPartType.CNT);
        countryPart.setCode("CAN");
        countryPart.setCodeSystem("ISO 3166");
        testPerson.getPostalAddress().getPart().add(countryPart);

        StringMap results = personClient.validate(testPerson);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(6, nesErrors.size());
        checkForMissingErrors(nesErrors, NesField.FIRST_NAME, NesField.LAST_NAME, NesField.STREET_ADDRESS,
                NesField.CITY_OR_MUNICIPALITY, NesField.POSTAL_CODE);
        checkForMissingEmailError(nesErrors);
    }

    private void checkForRequiredStateOrProvinceError(Map<String, List<String>> nesErrors) {
        checkForMessageErrors(nesErrors, EnumSet.of(NesField.STATE_OR_PROVINCE), MISSING_STATE_OR_PROVINCE_ERROR);
    }

    @Test
    public void testValidate_InvalidEmailSpaces() throws RemoteException, ValidationException {
        Person testPerson = new Person();
        DSETTEL telComInfo = NesTranslatorHelperUtils.buildNesTelcommInfo("this is not an email!", null, null);
        testPerson.setTelecomAddress(telComInfo);
        StringMap results = personClient.validate(testPerson);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        checkForMissingErrors(nesErrors, NesField.FIRST_NAME, NesField.LAST_NAME, NesField.POSTAL_ADDRESS);
        checkForMalformedEmailErrors(nesErrors, NesField.EMAIL_INDEX_0);
    }

    @Test
    public void testValidate_InvalidEmailNoSpaces() throws RemoteException, ValidationException {
        Person testPerson = new Person();
        DSETTEL telComInfo = NesTranslatorHelperUtils.buildNesTelcommInfo("thisisnotanemail!", null, null);
        testPerson.setTelecomAddress(telComInfo);
        StringMap results = personClient.validate(testPerson);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        checkForMissingErrors(nesErrors, NesField.FIRST_NAME, NesField.LAST_NAME, NesField.POSTAL_ADDRESS);
        checkForMalformedEmailErrors(nesErrors, NesField.EMAIL_INDEX_0);
    }

    private void checkForMalformedEmailErrors(Map<String, List<String>> nesErrors, NesField... fields) {
        checkForMessageErrors(nesErrors, EnumSet.copyOf(Arrays.asList(fields)), MALFORMED_EMAIL_ERROR);
    }

    @Test
    public void testValidate_ShortPhoneNumberBody() throws RemoteException, ValidationException {
        gov.nih.nci.firebird.data.Person fbPerson = PersonFactory.getInstance().create();
        Person nesPerson = PersonTranslator.buildNesPerson(fbPerson);
        DSETTEL telComInfo = new DSETTEL();
        TELPhone tel = new TELPhone();
        tel.setValue("tel:12345");
        telComInfo.getItem().add(tel);
        nesPerson.setTelecomAddress(telComInfo);
        StringMap results = personClient.validate(nesPerson);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(2, nesErrors.size());
        checkForMissingEmailError(nesErrors);
        checkForMalformedPhoneNumber(nesErrors);
    }

    private void checkForMalformedPhoneNumber(Map<String, List<String>> nesErrors) {
        for (java.util.Map.Entry<String, List<String>> error : nesErrors.entrySet()) {
            if (error.getKey().startsWith(PHONE_NUMBER_FIELD_PREFIX)) {
                String errorMessage = error.getValue().get(0);
                assertTrue("Valid Error Message was not found! " + errorMessage, CA_US_PHONE_FORMATS.contains(errorMessage));
                return;
            }
        }
        fail("Could not find phone number error!");
    }

    @Test
    public void testValidate_10DigitPhone() throws RemoteException, ValidationException {
        gov.nih.nci.firebird.data.Person fbPerson = PersonFactory.getInstance().create();
        Person nesPerson = PersonTranslator.buildNesPerson(fbPerson);
        DSETTEL telComInfo = new DSETTEL();
        TELPhone tel = new TELPhone();
        tel.setValue("tel:1234567890");
        telComInfo.getItem().add(tel);
        nesPerson.setTelecomAddress(telComInfo);
        StringMap results = personClient.validate(nesPerson);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(2, nesErrors.size());
        checkForMissingEmailError(nesErrors);
        checkForMalformedPhoneNumber(nesErrors);
    }

    @Test
    public void testValidate_ValidUSFormatPhone() throws RemoteException, ValidationException {
        gov.nih.nci.firebird.data.Person fbPerson = PersonFactory.getInstance().create();
        Person nesPerson = PersonTranslator.buildNesPerson(fbPerson);
        DSETTEL telComInfo = new DSETTEL();
        TELPhone tel = new TELPhone();
        tel.setValue("tel:123-456-7890");
        telComInfo.getItem().add(tel);
        nesPerson.setTelecomAddress(telComInfo);
        StringMap results = personClient.validate(nesPerson);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(1, nesErrors.size());
        checkForMissingEmailError(nesErrors);
    }

    @Test
    public void testValidate_ValidUSFormatPhoneExtensionNoParen() throws RemoteException, ValidationException {
        gov.nih.nci.firebird.data.Person fbPerson = PersonFactory.getInstance().create();
        Person nesPerson = PersonTranslator.buildNesPerson(fbPerson);
        DSETTEL telComInfo = new DSETTEL();
        TELPhone tel = new TELPhone();
        tel.setValue("tel:123-456-7890x1234");
        telComInfo.getItem().add(tel);
        nesPerson.setTelecomAddress(telComInfo);
        StringMap results = personClient.validate(nesPerson);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(1, nesErrors.size());
        checkForMissingEmailError(nesErrors);
    }

    @Test(expected = AxisFault.class)
    public void testValidate_ValidUSFormatPhoneExtensionNoParenWithSpace() throws RemoteException, ValidationException {
        gov.nih.nci.firebird.data.Person fbPerson = PersonFactory.getInstance().create();
        Person nesPerson = PersonTranslator.buildNesPerson(fbPerson);
        DSETTEL telComInfo = new DSETTEL();
        TELPhone tel = new TELPhone();
        tel.setValue("tel:123-456-7890 x1234");
        telComInfo.getItem().add(tel);
        nesPerson.setTelecomAddress(telComInfo);
        personClient.validate(nesPerson);
    }

    @Test
    public void testValidate_ValidUSFormatPhoneExtensionParen() throws RemoteException, ValidationException {
        gov.nih.nci.firebird.data.Person fbPerson = PersonFactory.getInstance().create();
        Person nesPerson = PersonTranslator.buildNesPerson(fbPerson);
        DSETTEL telComInfo = new DSETTEL();
        TELPhone tel = new TELPhone();
        tel.setValue("tel:123-456-7890(x1234)");
        telComInfo.getItem().add(tel);
        nesPerson.setTelecomAddress(telComInfo);
        StringMap results = personClient.validate(nesPerson);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(2, nesErrors.size());
        checkForMissingEmailError(nesErrors);
        checkForMalformedPhoneNumber(nesErrors);
    }

    @Test
    public void testValidate() throws ValidationException, RemoteException {
        gov.nih.nci.firebird.data.Person fbPerson = PersonFactory.getInstance().create();
        Person nesPerson = PersonTranslator.buildNesPerson(fbPerson);
        StringMap results = personClient.validate(nesPerson);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertTrue(nesErrors.isEmpty());
    }
}
