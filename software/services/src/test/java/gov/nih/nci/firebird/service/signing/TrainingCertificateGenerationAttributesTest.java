package gov.nih.nci.firebird.service.signing;

import static org.junit.Assert.*;

import org.junit.Test;

public class TrainingCertificateGenerationAttributesTest {

    @Test
    public void testValidate() {
        CertificateGenerationAttributes attributes = createTestAttributes();
        assertTrue(attributes.isValid());

        // For branch coverage.
        attributes.setKeystorePassword("");
        assertFalse(attributes.isValid());
        attributes.setKeystorePassword(null);
        assertFalse(attributes.isValid());

        attributes.setAlias("");
        assertFalse(attributes.isValid());
        attributes.setAlias(null);
        assertFalse(attributes.isValid());

        attributes.setValidDays(-1);
        assertFalse(attributes.isValid());

        attributes.setSerialNumber(-1);
        assertFalse(attributes.isValid());

        attributes.setEmailAddress("");
        assertFalse(attributes.isValid());
        attributes.setEmailAddress(null);
        assertFalse(attributes.isValid());

        attributes.setOrganizationUnit("");
        assertFalse(attributes.isValid());
        attributes.setOrganizationUnit(null);
        assertFalse(attributes.isValid());

        attributes.setOrganization("");
        assertFalse(attributes.isValid());
        attributes.setOrganization(null);
        assertFalse(attributes.isValid());

        attributes.setLastName("");
        assertFalse(attributes.isValid());
        attributes.setLastName(null);
        assertFalse(attributes.isValid());

        attributes.setFirstName("");
        assertFalse(attributes.isValid());
        attributes.setFirstName(null);
        assertFalse(attributes.isValid());

    }

    /**
     * test the method.
     */
    @Test
    public void testConstructCommonName() throws DigitalSigningException {
        CertificateGenerationAttributes attributes = createTestAttributes();
        assertEquals(attributes.constructCommonName(), "Dr. Investigator M FireBird");

        attributes.setTitle(null);
        assertEquals(attributes.constructCommonName(), "Investigator M FireBird");

        attributes.setMiddleName(null);
        assertEquals(attributes.constructCommonName(), "Investigator FireBird");

        attributes.setLastName(null);
        assertEquals(attributes.constructCommonName(), "Investigator");
    }

    /**
     * test the method.
     */
    @Test
    public void testGenerateUserDistinguishedName() throws DigitalSigningException {
        CertificateGenerationAttributes attributes = createTestAttributes();
        assertEquals(attributes.generateUserDistinguishedName(),
                this.generateReferDistinguishedName());
    }

    private CertificateGenerationAttributes createTestAttributes() {
        CertificateGenerationAttributes attributes = new CertificateGenerationAttributes();
        attributes.setTitle("Dr.");
        attributes.setFirstName("Investigator");
        attributes.setMiddleName("M");
        attributes.setLastName("FireBird");
        attributes.setCountryCode("US");
        attributes.setEmailAddress("fbsponsorinv@firbird.com");
        attributes.setOrganizationUnit("oaGrid");
        attributes.setOrganization("oaBig");
        attributes.setCity("Rockville");
        attributes.setStateProvince("MD");
        attributes.setAlias("fbsponsorinv");
        attributes.setSerialNumber(System.currentTimeMillis());
        attributes.setValidDays(365);
        attributes.setKeystorePassword("password");
        attributes.setAlias("user's p12");
        return attributes;
    }

    private DigitalSigningDistinguishedName generateReferDistinguishedName() {
        DigitalSigningDistinguishedName userDn = new DigitalSigningDistinguishedName();

        userDn.setCommonName("Dr. Investigator M FireBird");
        userDn.setCountryName("US");
        userDn.setEmailAddress("fbsponsorinv@firbird.com");
        userDn.setLocalityName("Rockville");
        userDn.setOrganizationalUnitName("oaGrid");
        userDn.setOrganizationName("oaBig");
        userDn.setStateOrProvinceName("MD");

        return userDn;
    }
}
