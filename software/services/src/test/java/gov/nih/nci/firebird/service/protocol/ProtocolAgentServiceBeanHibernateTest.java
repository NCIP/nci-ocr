package gov.nih.nci.firebird.service.protocol;

import com.google.inject.Inject;
import gov.nih.nci.firebird.data.Protocol;
import gov.nih.nci.firebird.data.ProtocolAgent;
import gov.nih.nci.firebird.test.AbstractHibernateTestCase;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ProtocolAgentServiceBeanHibernateTest extends AbstractHibernateTestCase {

    @Inject
    private ProtocolAgentServiceBean bean;

    @Test
    public void testGetAgents() {
        ProtocolAgent agent1 = new ProtocolAgent("Aspirin");
        ProtocolAgent agent2 = new ProtocolAgent("Asp");
        ProtocolAgent agent3 = new ProtocolAgent("aspartame");
        ProtocolAgent agent4 = new ProtocolAgent("laetrile");
        saveAndFlush(agent1, agent2, agent3, agent4);
        List<ProtocolAgent> agents = bean.getAgents("asp");
        assertEquals(3, agents.size());
        assertEquals(agent2, agents.get(0));
        assertEquals(agent3, agents.get(1));
        assertEquals(agent1, agents.get(2));
        assertTrue(bean.getAgents("xyz").isEmpty());
        assertEquals(3, bean.getAgents("a").size());
        assertEquals(3, bean.getAgents("A").size());
    }

    @Test
    public void testHandleAgents() {
        ProtocolAgent agent1 = new ProtocolAgent("Aspirin");
                ProtocolAgent agent2 = new ProtocolAgent("Asp");
        saveAndFlush(agent1);

        Protocol protocol = new Protocol();
        protocol.getAgents().add(agent1);
        protocol.getAgents().add(agent2);

        assertNull(agent2.getId());
        bean.handleAgents(protocol);
        assertNotNull(agent2.getId());
    }
}

