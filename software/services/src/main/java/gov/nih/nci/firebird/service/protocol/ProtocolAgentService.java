package gov.nih.nci.firebird.service.protocol;

import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;

import javax.ejb.Local;
import java.util.List;

/**
 * Protocol Agent Service.
 */
@Local
public interface ProtocolAgentService {

    /**
     * Returns the existing agents that start with the string given.
     *
     * @param startOfName get agents with names that start with this string
     * @return the matching agents.
     */
    List<ProtocolAgent> getAgents(String startOfName);

    /**
     * Will handle Agents for a provided Protocol. Agents that are new will be persisted first.
     *
     * @param protocol Protocol object
     */
    void handleAgents(Protocol protocol);
}
