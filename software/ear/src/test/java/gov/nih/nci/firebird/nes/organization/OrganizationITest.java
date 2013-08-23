package gov.nih.nci.firebird.nes.organization;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.nih.nci.coppa.po.Organization;
import gov.nih.nci.coppa.po.StringMap;
import gov.nih.nci.coppa.po.StringMapType.Entry;
import gov.nih.nci.coppa.services.entities.organization.common.OrganizationI;
import gov.nih.nci.firebird.common.FirebirdConstants;
import gov.nih.nci.firebird.exception.ValidationException;
import gov.nih.nci.firebird.test.AbstractIntegrationTest;
import gov.nih.nci.firebird.test.ValueGenerator;

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

public class OrganizationITest extends AbstractIntegrationTest {

    private enum NesField {
        NAME("name"),
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

    EnumSet<NesField> emptyAddress = EnumSet.of(NesField.STREET_ADDRESS, NesField.CITY_OR_MUNICIPALITY, NesField.POSTAL_CODE, NesField.COUNTRY);

    private static final String PHONE_NUMBER_FIELD_PREFIX = "Phone number";
    private static final String MISSING_INFORMATION_ERROR = "must be set";
    private static final String MISSING_STATE_OR_PROVINCE_ERROR = "Invalid State or Province for Country";
    private static final String CA_US_PHONE_FORMAT_ERROR_NEW = "US and Canadian phone, fax, and tty numbers must match ###-###-####x#*.";
    private static final String CA_US_PHONE_FORMAT_ERROR_OLD = "US and Canadian phone, fax, and tty numbers must match ###-###-####(x#*).";
    private static final Set<String> CA_US_PHONE_FORMATS = Sets.newHashSet(CA_US_PHONE_FORMAT_ERROR_NEW, CA_US_PHONE_FORMAT_ERROR_OLD);
    private static final String NAME_LENGTH_ERROR = "length must be between 0 and 160";

    @Inject
    private OrganizationI organizationClient;

    private NesOrganizationFactory nesOrganizationFactory = new NesOrganizationFactory();
    
    @Test(expected = AxisFault.class)
    public void testValidate_NullOrganization() throws RemoteException {
        organizationClient.validate(null);
    }

    @Test
    public void testValidate_EmptyOrganization() throws RemoteException {
        StringMap results = organizationClient.validate(new Organization());
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        checkForMissingErrors(nesErrors, NesField.NAME, NesField.POSTAL_ADDRESS);
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

    private void checkForMessageErrors(Map<String, List<String>> nesErrors, EnumSet<NesField> fields, String message) {
        for (NesField field : fields) {
            assertTrue("Cannot find key " + field.getFieldName(), nesErrors.containsKey(field.getFieldName()));
            assertEquals(message, nesErrors.get(field.getFieldName()).get(0));
        }
    }

    @Test
    public void testValidate_EmptyAddress() throws RemoteException {
        Organization testOrganization = new Organization();
        testOrganization.setPostalAddress(new AD());
        StringMap results = organizationClient.validate(testOrganization);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        checkForMissingErrors(nesErrors, NesField.NAME);
        checkForMissingErrors(nesErrors, emptyAddress);
    }
    @Test
    public void testValidate_EmptyUSAddress() throws RemoteException {
        Organization testOrganization = new Organization();
        testOrganization.setPostalAddress(new AD());
        ADXP countryPart = new ADXP();
        countryPart.setType(AddressPartType.CNT);
        countryPart.setCode(FirebirdConstants.US_COUNTRY_CODE);
        countryPart.setCodeSystem("ISO 3166");
        testOrganization.getPostalAddress().getPart().add(countryPart);

        StringMap results = organizationClient.validate(testOrganization);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(5, nesErrors.size());
        checkForMissingErrors(nesErrors, NesField.NAME, NesField.STREET_ADDRESS,
                NesField.CITY_OR_MUNICIPALITY, NesField.POSTAL_CODE);
        checkForRequiredStateOrProvinceError(nesErrors);
    }

    @Test
    public void testValidate_EmptyCAAddress() throws RemoteException {
        Organization testOrganization = new Organization();
        testOrganization.setPostalAddress(new AD());
        ADXP countryPart = new ADXP();
        countryPart.setType(AddressPartType.CNT);
        countryPart.setCode("CAN");
        countryPart.setCodeSystem("ISO 3166");
        testOrganization.getPostalAddress().getPart().add(countryPart);

        StringMap results = organizationClient.validate(testOrganization);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(4, nesErrors.size());
        checkForMissingErrors(nesErrors, NesField.NAME, NesField.STREET_ADDRESS,
                NesField.CITY_OR_MUNICIPALITY, NesField.POSTAL_CODE);
    }

    private void checkForRequiredStateOrProvinceError(Map<String, List<String>> nesErrors) {
        checkForMessageErrors(nesErrors, EnumSet.of(NesField.STATE_OR_PROVINCE), MISSING_STATE_OR_PROVINCE_ERROR);
    }

    @Test
    public void testValidate_ShortPhoneNumberBody() throws RemoteException, ValidationException {
        Organization nesOrganization = nesOrganizationFactory.getTestNesOrganization();
        DSETTEL telComInfo = new DSETTEL();
        TELPhone tel = new TELPhone();
        tel.setValue("tel:12345");
        telComInfo.getItem().add(tel);
        nesOrganization.setTelecomAddress(telComInfo);
        StringMap results = organizationClient.validate(nesOrganization);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(1, nesErrors.size());
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
        Organization nesOrganization = nesOrganizationFactory.getTestNesOrganization();
        DSETTEL telComInfo = new DSETTEL();
        TELPhone tel = new TELPhone();
        tel.setValue("tel:1234567890");
        telComInfo.getItem().add(tel);
        nesOrganization.setTelecomAddress(telComInfo);
        StringMap results = organizationClient.validate(nesOrganization);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(1, nesErrors.size());
        checkForMalformedPhoneNumber(nesErrors);
    }

    @Test
    public void testValidate_ValidUSFormatPhone() throws RemoteException, ValidationException {
        Organization nesOrganization = nesOrganizationFactory.getTestNesOrganization();
        DSETTEL telComInfo = new DSETTEL();
        TELPhone tel = new TELPhone();
        tel.setValue("tel:123-456-7890");
        telComInfo.getItem().add(tel);
        nesOrganization.setTelecomAddress(telComInfo);
        StringMap results = organizationClient.validate(nesOrganization);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertTrue(nesErrors.isEmpty());
    }

    @Test
    public void testValidate_ValidUSFormatPhoneExtensionNoParen() throws RemoteException, ValidationException {
        Organization nesOrganization = nesOrganizationFactory.getTestNesOrganization();
        DSETTEL telComInfo = new DSETTEL();
        TELPhone tel = new TELPhone();
        tel.setValue("tel:123-456-7890x1234");
        telComInfo.getItem().add(tel);
        nesOrganization.setTelecomAddress(telComInfo);
        StringMap results = organizationClient.validate(nesOrganization);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertTrue(nesErrors.isEmpty());
    }

    @Test(expected = AxisFault.class)
    public void testValidate_ValidUSFormatPhoneExtensionNoParenWithSpace() throws RemoteException, ValidationException {
        Organization nesOrganization = nesOrganizationFactory.getTestNesOrganization();
        DSETTEL telComInfo = new DSETTEL();
        TELPhone tel = new TELPhone();
        tel.setValue("tel:123-456-7890 x1234");
        telComInfo.getItem().add(tel);
        nesOrganization.setTelecomAddress(telComInfo);
        organizationClient.validate(nesOrganization);
    }

    @Test
    public void testValidate_ValidUSFormatPhoneExtensionParen() throws RemoteException, ValidationException {
        Organization nesOrganization = nesOrganizationFactory.getTestNesOrganization();
        DSETTEL telComInfo = new DSETTEL();
        TELPhone tel = new TELPhone();
        tel.setValue("tel:123-456-7890(x1234)");
        telComInfo.getItem().add(tel);
        nesOrganization.setTelecomAddress(telComInfo);
        StringMap results = organizationClient.validate(nesOrganization);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(1, nesErrors.size());
        checkForMalformedPhoneNumber(nesErrors);
    }

    @Test
    public void testValidate_MissingName() throws ValidationException, RemoteException {
        Organization nesOrganization = nesOrganizationFactory.getTestNesOrganization(null);
        StringMap results = organizationClient.validate(nesOrganization);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(1, nesErrors.size());
        checkForMissingErrors(nesErrors, NesField.NAME);
    }

    @Test
    public void testValidate() throws ValidationException, RemoteException {
        Organization nesOrganization = nesOrganizationFactory.getTestNesOrganization();
        StringMap results = organizationClient.validate(nesOrganization);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertTrue(nesErrors.isEmpty());
    }

    @Test
    public void testValidate_OrganizationNameLengthTooLong() throws ValidationException, RemoteException {
        Organization nesOrganization = nesOrganizationFactory.getTestNesOrganization(ValueGenerator.getUniqueString(161));
        StringMap results = organizationClient.validate(nesOrganization);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertEquals(1, nesErrors.size());
        checkForMessageErrors(nesErrors, EnumSet.of(NesField.NAME), NAME_LENGTH_ERROR);
    }

    @Test
    public void testValidate_OrganizationNameLength() throws ValidationException, RemoteException {
        Organization nesOrganization = nesOrganizationFactory.getTestNesOrganization(ValueGenerator.getUniqueString(160));
        StringMap results = organizationClient.validate(nesOrganization);
        assertNotNull(results);
        Map<String, List<String>> nesErrors = convertToMap(results);
        assertTrue("Expected Empty Errors but instead got: " + nesErrors, nesErrors.isEmpty());
    }
}
