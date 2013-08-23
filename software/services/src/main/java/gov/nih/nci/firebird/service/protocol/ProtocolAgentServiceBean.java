package gov.nih.nci.firebird.service.protocol;

import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.service.AbstractGenericServiceBean;
import org.hibernate.Query;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Protocol Agent service.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ProtocolAgentServiceBean extends AbstractGenericServiceBean<ProtocolAgent>
        implements ProtocolAgentService {

    private static final String HQL_FROM_BASE = "from ";

    @Override
      @SuppressWarnings("unchecked")
      // Hibernate list() method is untyped
      public List<ProtocolAgent> getAgents(String startOfName) {
          String agentQueryHql = HQL_FROM_BASE + ProtocolAgent.class.getName()
                  + " where lower(name) like :startOfName order by lower(name)";
          Query query = getSession().createQuery(agentQueryHql);
          query.setString("startOfName", startOfName.toLowerCase(Locale.getDefault()) + "%");
          return query.list();
      }


    @Override
    public void handleAgents(Protocol protocol) {
          // Set below used to avoid conflict when changes are automatically flushed for getAgentbyName() query
          Set<ProtocolAgent> incomingAgentSet = new HashSet<ProtocolAgent>();
          incomingAgentSet.addAll(protocol.getAgents());
          protocol.getAgents().clear();
          for (ProtocolAgent agent : incomingAgentSet) {
              handleAgent(protocol, agent);
          }
      }

      private void handleAgent(Protocol protocol, ProtocolAgent agent) {
          if (agent.getId() != null) {
              protocol.getAgents().add(agent);
          } else {
              ProtocolAgent existingAgent = getAgentByName(agent.getName());
              if (existingAgent == null) {
                  getSession().save(agent);
                  protocol.getAgents().add(agent);
              } else {
                  protocol.getAgents().add(existingAgent);
              }
          }
      }

    private ProtocolAgent getAgentByName(String agentName) {
           String agentQueryHql = HQL_FROM_BASE + ProtocolAgent.class.getName() + " where lower(name) = :agentName";
           Query query = getSession().createQuery(agentQueryHql);
           query.setString("agentName", agentName.toLowerCase(Locale.getDefault()));
           return (ProtocolAgent) query.uniqueResult();
       }
}
