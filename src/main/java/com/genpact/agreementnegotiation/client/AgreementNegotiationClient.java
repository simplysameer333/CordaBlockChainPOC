package com.genpact.agreementnegotiation.client;

import com.genpact.agreementnegotiation.dummydata.DummyData;
import com.genpact.agreementnegotiation.flow.AgreementNegotiationAcceptFlow;
import com.genpact.agreementnegotiation.flow.AgreementNegotiationAmendFlow;
import com.genpact.agreementnegotiation.flow.AgreementNegotiationInitiateFlow;
import com.genpact.agreementnegotiation.state.AgreementEnumState;
import com.genpact.agreementnegotiation.state.AgreementNegotiationState;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCClientConfiguration;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.messaging.FlowProgressHandle;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.NetworkHostAndPort;
import org.apache.activemq.artemis.api.core.ActiveMQException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

/**
 * CordaRPCClient to connect to a Corda Node and execute the flow based on input params
 * usage from CLI > Java AgreementNegotiationClient <Initiator Host:Port> <Reciever Host:Port> <INITIAL/AMEND/ACCEPT> <AgreementName:AgreementValue:Collateral>
 */
public class AgreementNegotiationClient {
    private static final Logger logger = LoggerFactory.getLogger(AgreementNegotiationClient.class);

    private static void logState(StateAndRef<AgreementNegotiationState> state) {
        logger.info("{}", state.getState().getData());
    }

    public static void main(String[] args) throws ActiveMQException, InterruptedException, ExecutionException {
        if (args.length != 4) {
            throw new IllegalArgumentException("Usage: AgreementNegotiationClient <Initiator Host:Port> <Reciever Host:Port> <INITIAL/AMEND/ACCEPT> <AgreementName:AgreementValue:Collateral>");
        }

        final NetworkHostAndPort nodeAddress = NetworkHostAndPort.parse(args[0]);
        final CordaRPCClient client = new CordaRPCClient(nodeAddress, CordaRPCClientConfiguration.DEFAULT);

        final NetworkHostAndPort nodeAddressCpty = NetworkHostAndPort.parse(args[1]);
        final CordaRPCClient clientCpty = new CordaRPCClient(nodeAddressCpty, CordaRPCClientConfiguration.DEFAULT);

        final ArrayList<String> agreementParams = new ArrayList<String>();

        StringTokenizer st = new StringTokenizer(args[3],":");
        while(st.hasMoreTokens())
        {
            agreementParams.add(st.nextToken());
        }


        // Can be amended in the Main file.
        final CordaRPCOps proxy = client.start("user1", "test").getProxy();
        final CordaRPCOps proxyCpty = clientCpty.start("user1", "test").getProxy();

        Party cptyInitiator = proxy.nodeInfo().getLegalIdentities().get(0);
        Party cptyReciever = proxyCpty.nodeInfo().getLegalIdentities().get(0);
        FlowProgressHandle<SignedTransaction> flowHandle = null;

        if (args[2].equals("AMEND" )) {
            System.out.println("Initiating Amend Flow for Parameters" + args[3]);
            AgreementNegotiationState amendNegotiationState = DummyData.getDummyDataForAgreementNegotiationState();
            amendNegotiationState.setStatus(AgreementEnumState.AMEND);
            flowHandle= proxy.startTrackedFlowDynamic(AgreementNegotiationAmendFlow.Initiator.class, amendNegotiationState, cptyReciever);

        }
        else if (args[2].equals("ACCEPT") ){
            System.out.println("Initiating Agree Flow for Parameters" + args[3]);
            AgreementNegotiationState agreeNegotiationState = DummyData.getDummyDataForAgreementNegotiationState();
            agreeNegotiationState.setStatus(AgreementEnumState.PARTIAL_ACCEPTED);
            flowHandle = proxy.startTrackedFlowDynamic(AgreementNegotiationAcceptFlow.Initiator.class,
                    agreeNegotiationState, cptyReciever);

        } else if (args[2].equals("INITIAL")) {
            System.out.println("Initiating Initiate Flow for Parameters" + args[3]);

            AgreementNegotiationState initiateNegotiationState = DummyData.getDummyDataForAgreementNegotiationState();
            initiateNegotiationState.setStatus(AgreementEnumState.INITIAL);

            flowHandle= proxy.startTrackedFlowDynamic(AgreementNegotiationInitiateFlow.Initiator.class, initiateNegotiationState, cptyReciever);

        } else {

            throw new IllegalArgumentException("Usage: AgreementNegotiationClient <Initiator Host:Port> <Reciever Host:Port> <INITIAL/AMEND/ACCEPT> <AgreementName:AgreementValue:Collateral>");
        }


        try {

            flowHandle.getProgress().subscribe(evt -> System.out.printf("Transactiomn Event >> %s\n", evt));


            // The line below blocks and waits for the flow to return.
            final SignedTransaction result = flowHandle.getReturnValue().get();


            final String msg = String.format("Transaction id %s committed to ledger.\n", result.getId());
            System.out.println(msg);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}