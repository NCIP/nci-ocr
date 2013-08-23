/**
 *
 */
package gov.nih.nci.firebird.service.signing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class DigitalSigningDistinguishedNameTest {

    @Test
    public void testEquals() {

        DigitalSigningDistinguishedName dn = generateDistinguishedName();
        DigitalSigningDistinguishedName dnRef = generateReferDistinguishedName();
        assertEquals(dn, dnRef);

        dn.setCommonName("Dr. Inv1 M FireBird");
        assertFalse(dn.equals(dnRef));
        dn.setCountryName("UN");
        assertFalse(dn.equals(dnRef));
        dn.setStateOrProvinceName("CT");
        assertFalse(dn.equals(dnRef));
        dn.setLocalityName("New London");
        assertFalse(dn.equals(dnRef));
        dn.setEmailAddress("fbinv1@firbird.com");
        assertFalse(dn.equals(dnRef));
        dn.setOrganizationalUnitName("IT");
        assertFalse(dn.equals(dnRef));
        dn.setOrganizationName("EssexManagement");
        assertFalse(dn.equals(dnRef));
    }


    private DigitalSigningDistinguishedName generateDistinguishedName() {
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

    @Test
    public void testHashCode() {
        // Nothing but for improving branch coverage.
        DigitalSigningDistinguishedName dn1 = generateDistinguishedName();
        DigitalSigningDistinguishedName dn2 = generateDistinguishedName();
        assertEquals(dn1.hashCode(), dn2.hashCode());
        dn2.setCountryName("UN");
        assertFalse(dn1.hashCode() == dn2.hashCode());
    }
}
