package gov.nih.nci.firebird.data;

import gov.nih.nci.firebird.common.FirebirdEnumUtils;

/**
 * Enumeration indicating the different available types that a Practice Site might consist of.
 */
public enum PracticeSiteType {
    /**
     * A Clinical Center.
     */
    CLINICAL_CENTER,
    /**
     * A Cancer Center.
     */
    CANCER_CENTER,
    /**
     * A Health Care Facility (Institution).
     */
    HEALTH_CARE_FACILITY;

    /**
     * @return The Enum name formatted for display.
     */
    public String getDisplay() {
        return FirebirdEnumUtils.getEnumDisplay(this);
    }
}
