package gov.nih.nci.firebird.web.common;

import com.google.common.collect.Lists;
import gov.nih.nci.firebird.data.Protocol;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import java.util.regex.Pattern;

/**
 * Utility class which provides assistance methods to properly display Protocol Information.
 */
public final class ProtocolUiUtils {
    private ProtocolUiUtils() {
    }

    /**
     * @return the JSON string of the lead organizations and principal investigators.
     * @param protocol the protocol to get the Lead Organizations from.
     * @throws org.apache.struts2.json.JSONException if a serialization exception occurs.
     */
    public static String getProtocolLeadOrganizationsJson(Protocol protocol) throws JSONException {
        return JSONUtil.serialize(protocol.getLeadOrganizations(),
                                  Lists.newArrayList(Pattern.compile(".*\\.protocol")),
                                  null,
                                  false,
                                  false);
    }
}