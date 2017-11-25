package com.genpact.agreementnegotiation.flow;

import com.genpact.agreementnegotiation.dummydata.DummyData;
import com.genpact.agreementnegotiation.state.AgreementEnumState;
import com.genpact.agreementnegotiation.state.AgreementNegotiationState;
import net.corda.node.internal.StartedNode;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.MockNetwork.BasketOfNodes;
import net.corda.testing.node.MockNetwork.MockNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static net.corda.testing.CoreTestUtils.setCordappPackages;
import static net.corda.testing.CoreTestUtils.unsetCordappPackages;

public class AgreementNegotiationFlowTests {
    private MockNetwork network;
    private StartedNode<MockNode> a;
    private StartedNode<MockNode> b;
    private StartedNode<MockNode> c;
    private AgreementNegotiationState iouValue;
    @Before
    public void setup() {
        setCordappPackages("com.genpact.agreementnegotiaton.contract");
        network = new MockNetwork();
        BasketOfNodes nodes = network.createSomeNodes(3);
        a = nodes.getPartyNodes().get(0);
        b = nodes.getPartyNodes().get(1);
        c = nodes.getPartyNodes().get(2);
        // For real nodes this happens automatically, but we have to manually register the flow for tests.
        for (StartedNode<MockNode> node : nodes.getPartyNodes()) {
            node.registerInitiatedFlow(AgreementNegotiationInitiateFlow.Responder.class);
            node.registerInitiatedFlow(AgreementNegotiationAmendFlow.Responder.class);
            node.registerInitiatedFlow(AgreementNegotiationAcceptFlow.Responder.class);
        }
        network.runNetwork();

        iouValue = DummyData.getDummyDataForAgreementNegotiationState();
        iouValue.setStatus(AgreementEnumState.INITIAL);
    }

    @After
    public void tearDown() {
        unsetCordappPackages();
        network.stopNodes();
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();



    @Test
    public void flowRecordsTheCorrectIOUInBothPartiesVaults() throws Exception {

    }
}
