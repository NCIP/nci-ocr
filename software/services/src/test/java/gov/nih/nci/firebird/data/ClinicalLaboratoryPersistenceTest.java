/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.nih.nci.firebird.data;

import gov.nih.nci.firebird.test.AbstractHibernateTestCase;
import gov.nih.nci.firebird.test.FirebirdFileFactory;
import java.util.Calendar;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * test ClinicalLaboratory and LaboratoryCertificate
 *
 */
public class ClinicalLaboratoryPersistenceTest extends AbstractHibernateTestCase {

    @Test
    public void testLaboratoryCertificatePersistence() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 22);
        cal.set(Calendar.MONTH, 2);
        cal.set(Calendar.YEAR, 2020);

        ClinicalLaboratory lab = new ClinicalLaboratory();
        LaboratoryCertificate cert = new LaboratoryCertificate(LaboratoryCertificateType.CLIA);
        cert.setEffectiveDate(cal.getTime());
        cert.setExpirationDate(cal.getTime());
        cert.setCertificateFile(FirebirdFileFactory.getInstance().create());
        lab.addCertificate(cert);
        save(lab);
        flushAndClearSession();
        reloadObject(lab);
        assertEquals(1, lab.getCertificates().size());
        LaboratoryCertificate cert2 = lab.getCertificate(LaboratoryCertificateType.CLIA);
        assertEquals(cert.getCertificateFile().getId(), cert2.getCertificateFile().getId());
        assertEquals(cert.getEffectiveDate(), cert2.getEffectiveDate());
        assertEquals(cert.getExpirationDate(), cert2.getExpirationDate());
        assertEquals(LaboratoryCertificateType.CLIA, cert2.getType());
    }


}