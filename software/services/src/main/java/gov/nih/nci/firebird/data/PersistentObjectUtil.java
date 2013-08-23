package gov.nih.nci.firebird.data;

import com.fiveamsolutions.nci.commons.data.persistent.PersistentObject;

/**
 * PersistenObject Util class.
 */
public class PersistentObjectUtil {

    /**
     * Method to determine if the PersistentObject is newly created and not yet saved.
     *
     * @param persistentObject Object
     * @return boolean boolean value which is true if the PersistentObject is new
     */
    public static boolean isNew(PersistentObject persistentObject) {
        return persistentObject.getId() == null;
    }

    /**
     * Checks if two PersistentObjects are the same.
     *
     * @param persistentObject        Original PersistentObject
     * @param compareToExistingObject PersistentObject that the original is being compared to
     * @return boolean
     */
    public static boolean areSame(PersistentObject persistentObject, PersistentObject compareToExistingObject) {
        return compareToExistingObject.getId().equals(persistentObject.getId());
    }

}
