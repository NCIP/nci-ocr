package gov.nih.nci.firebird.data;

import static com.google.common.base.Preconditions.checkArgument;
import gov.nih.nci.firebird.common.FirebirdEnumUtils;
import gov.nih.nci.firebird.nes.organization.ResearchOrganizationType;

import java.util.Map;

import com.google.common.collect.Maps;

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

    private static final Map<ResearchOrganizationType, PracticeSiteType> TYPE_MAPPINGS = Maps.newHashMap();
    static {
        TYPE_MAPPINGS.put(ResearchOrganizationType.CLINICAL_CENTER, CLINICAL_CENTER);
        TYPE_MAPPINGS.put(ResearchOrganizationType.CANCER_CENTER, CANCER_CENTER);
    }

    /**
     * @param researchOrganizationType research organization type
     * @return equivalent PracticeSiteType
     */
    public static PracticeSiteType fromResearchOrganizationType(ResearchOrganizationType researchOrganizationType) {
        checkArgument(TYPE_MAPPINGS.containsKey(researchOrganizationType), researchOrganizationType.name()
                + " is not a valid Practice Site type.");
        return TYPE_MAPPINGS.get(researchOrganizationType);
    }

    /**
     * @return The Enum name formatted for display.
     */
    public String getDisplay() {
        return FirebirdEnumUtils.getEnumDisplay(this);
    }
}
