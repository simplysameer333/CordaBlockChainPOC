package com.genpact.agreementnegotiation.api;

import com.genpact.agreementnegotiation.flow.AgreementNegotiationAgreeFlow;
import com.genpact.agreementnegotiation.flow.AgreementNegotiationAmendFlow;
import com.genpact.agreementnegotiation.flow.AgreementNegotiationInitiateFlow;
import com.genpact.agreementnegotiation.model.Agreement;
import com.genpact.agreementnegotiation.schema.AgreementNegotiationSchema;
import com.genpact.agreementnegotiation.state.AgreementNegotiationState;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.messaging.FlowProgressHandle;
import net.corda.core.node.NodeInfo;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.Builder;
import net.corda.core.node.services.vault.CriteriaExpression;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.schemas.PersistentState;
import net.corda.core.transactions.SignedTransaction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

// This API is accessible from /api/template. The endpoint paths specified below are relative to it.
@Path("template")
public class AgreementNegotiationApi {
    private final CordaRPCOps rpcOps;
    private final CordaX500Name myLegalName;

    private final List<String> serviceNames = ImmutableList.of("Controller", "Network Map Service");

    public AgreementNegotiationApi(CordaRPCOps services) {
        this.rpcOps = services;
        this.myLegalName = rpcOps.nodeInfo().getLegalIdentities().get(0).getName();
    }

    /**
     * Accessible at /api/template/templateGetEndpoint.
     */
    @GET
    @Path("templateGetEndpoint")
    @Produces(MediaType.APPLICATION_JSON)
    public Response templateGetEndpoint() {
        return Response.ok("Template GET endpoint.").build();
    }

    /**
     * Accessible at /api/template/<party>/initFlow.
     */
    @PUT
    @Path("initFlow/{partyName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startInitFlow(Agreement agreement, @PathParam("partyName") String partyName) {

        try {
            /*if (partyName == null) {
                return Response.status(BAD_REQUEST).entity("Query parameter 'partyName' missing or has wrong format.\n").build();
            }
            System.out.println("Starting Flow :"+agreement);
            if(agreement == null) {
                return Response.status(BAD_REQUEST).entity("Post data 'state' missing or has wrong format.\n").build();
            }*/
            CordaX500Name party = null;
            Map<String, List<CordaX500Name>> p = getPeers();
            for (Map.Entry<String, List<CordaX500Name>> entry : p.entrySet()) {
                System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
                party = entry.getValue().get(0);
            }
            final Party otherParty = rpcOps.wellKnownPartyFromX500Name(party);
            if (otherParty == null) {
                return Response.status(BAD_REQUEST).entity("Party named " + partyName + "cannot be found.\n").build();
            }
            System.out.println("Starting Flow :" + otherParty.toString());

            //create state
            // AgreementNegotiationParams agreementNegotiationParams = new AgreementNegotiationParams();
            AgreementNegotiationState iouValue = new AgreementNegotiationState(agreement.getAgrementName(), new Date(), (double) agreement.getAgreementValue(), agreement.getCollateral(),
                    rpcOps.nodeInfo().getLegalIdentities().get(0),
                    otherParty);
            //rpcOps.partiesFromName("NodeA", true);
           /* AgreementNegotiationInitiateFlow.Initiator flow = new AgreementNegotiationInitiateFlow.Initiator(iouValue,
                    iouValue.getCptyReciever());

          */  FlowProgressHandle<SignedTransaction> flowHandle = rpcOps
                    .startTrackedFlowDynamic(AgreementNegotiationInitiateFlow.Initiator.class, iouValue, iouValue.getCptyReciever());
            flowHandle.getProgress().subscribe(evt -> System.out.printf(">> %s\n", evt));

            // The line below blocks and waits for the flow to return.
            final SignedTransaction result = flowHandle
                    .getReturnValue()
                    .get();

            final String msg = String.format("Transaction id %s committed to ledger.\n", result.getId());
            System.out.println("message" + msg);

            return Response.ok("startInitFlow GET endpoint.").build();
        } catch (Throwable ex) {
            System.out.println("Exception" + ex.toString());
        }
        return Response.ok("ERROR  GET endpoint.").build();
    }


    /**
     * Accessible at /api/template/<party>/amendFlow.
     */
    @PUT
    @Path("amendFlow/{partyName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startAmendFlow(Agreement agreement, @PathParam("partyName") String partyName) {
        try {
            CordaX500Name otherPartyName = getPeers().values().iterator().next().get(0);
            final Party otherParty = rpcOps.wellKnownPartyFromX500Name(otherPartyName);

            //create state
            AgreementNegotiationState iouValue = new AgreementNegotiationState(agreement.getAgrementName(), new Date(), (double) agreement.getAgreementValue(), agreement.getCollateral(),
                    rpcOps.nodeInfo().getLegalIdentities().get(0),
                    otherParty);

         /*   AgreementNegotiationAmendFlow.Initiator flow = new AgreementNegotiationAmendFlow.Initiator("name",
                    new Date(), 11.1, "collateral");
*/
            FlowProgressHandle<SignedTransaction> flowHandle = rpcOps
                    .startTrackedFlowDynamic(AgreementNegotiationAmendFlow.Initiator.class, iouValue,
                            rpcOps.nodeInfo().getLegalIdentities().get(0));
            flowHandle.getProgress().subscribe(evt -> System.out.printf(">> %s\n", evt));

            // The line below blocks and waits for the flow to return.
            final SignedTransaction result = flowHandle
                    .getReturnValue()
                    .get();

            final String msg = String.format("Transaction id %s committed to ledger.\n", result.getId());
            System.out.println("message" + msg);

            return Response.ok("amendFlow GET endpoint.").build();
        } catch (Throwable ex) {
            System.out.println("Exception" + ex.toString());
        }
        return Response.ok("ERROR  GET endpoint.").build();

    }


    @GET
    @Path("getAgreements")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StateAndRef<AgreementNegotiationState>> getAgreements() {

        QueryCriteria criteria = new QueryCriteria.VaultQueryCriteria(Vault.StateStatus.UNCONSUMED);
        return rpcOps.vaultQueryByCriteria(criteria, AgreementNegotiationState.class).getStates();

    }

    /**
     * Returns all parties registered with the [NetworkMapService]. These names can be used to look up identities
     * using the [IdentityService].
     */
    @GET
    @Path("peers")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<CordaX500Name>> getPeers() {
        List<NodeInfo> nodeInfoSnapshot = rpcOps.networkMapSnapshot();
        return ImmutableMap.of("peers", nodeInfoSnapshot
                .stream()
                .map(node -> node.getLegalIdentities().get(0).getName())
                .filter(name -> !name.equals(myLegalName) && !serviceNames.contains(name.getOrganisation()))
                .collect(toList()));
    }

    /**
     * Returns the node's name.
     */
    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, CordaX500Name> whoami() {
        return ImmutableMap.of("me", myLegalName);
    }

    /**
     * Accessible at /api/template/amendFlow.
     */
    @GET
    @Path("amendFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startAmendFlow() {

        try {
            CordaX500Name otherPartyName = getPeers().values().iterator().next().get(0);
            final Party otherParty = rpcOps.wellKnownPartyFromX500Name(otherPartyName);

            //create state
            //TODO this iouState should be coming from UI...Needs to remove this hard coded part.
            AgreementNegotiationState iouValue = new AgreementNegotiationState("test", new Date(), 11.0, "collateral");

            FlowProgressHandle<SignedTransaction> flowHandle = rpcOps
                    .startTrackedFlowDynamic(AgreementNegotiationAmendFlow.Initiator.class, iouValue,
                            rpcOps.nodeInfo().getLegalIdentities().get(0));
            flowHandle.getProgress().subscribe(evt -> System.out.printf(">> %s\n", evt));

            // The line below blocks and waits for the flow to return.
            final SignedTransaction result = flowHandle
                    .getReturnValue()
                    .get();

            final String msg = String.format("Transaction id %s committed to ledger.\n", result.getId());
            System.out.println("message" + msg);

            return Response.ok("amendFlow GET endpoint.").build();
        } catch (Throwable ex) {
            System.out.println("Exception" + ex.toString());
        }
        return Response.ok("ERROR  GET endpoint.").build();
    }

    /**
     * Accessible at /api/template/expireFlow.
     */
    @GET
    @Path("expireFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response expireFlow() {

        try {
            CordaX500Name otherPartyName = getPeers().values().iterator().next().get(0);
            final Party otherParty = rpcOps.wellKnownPartyFromX500Name(otherPartyName);

            //create state
            //TODO this iouState should be coming from UI...Needs to remove this hard coded part.
            AgreementNegotiationState iouValue = new AgreementNegotiationState("test", new Date(), 11.0, "collateral");
            iouValue.setNegotiationState(AgreementNegotiationState.NegotiationStates.ACCEPT);

            FlowProgressHandle<SignedTransaction> flowHandle = rpcOps
                    .startTrackedFlowDynamic(AgreementNegotiationAgreeFlow.Initiator.class, rpcOps.nodeInfo().getLegalIdentities().get(0));
            flowHandle.getProgress().subscribe(evt -> System.out.printf(">> %s\n", evt));

            // The line below blocks and waits for the flow to return.
            final SignedTransaction result = flowHandle
                    .getReturnValue()
                    .get();

            final String msg = String.format("Transaction id %s committed to ledger.\n", result.getId());
            System.out.println("message" + msg);

            return Response.ok("amendFlow GET endpoint.").build();
        } catch (Throwable ex) {
            System.out.println("Exception" + ex.toString());
        }
        return Response.ok("ERROR  GET endpoint.").build();
    }

    @GET
    @Path("templateAudit")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StateAndRef<AgreementNegotiationState>> getAudit() {

        try {
            Field uniqueAttributeName = AgreementNegotiationSchema.PersistentIOU.class.getDeclaredField("agrementName");
            CriteriaExpression uniqueAttributeEXpression = Builder.equal(uniqueAttributeName, "test");
            QueryCriteria customCriteria = new QueryCriteria.VaultCustomQueryCriteria(uniqueAttributeEXpression,
                    Vault.StateStatus.CONSUMED);


           QueryCriteria vaultCriteria = new QueryCriteria.VaultCustomQueryCriteria(uniqueAttributeEXpression,
                   Vault.StateStatus.UNCONSUMED);

            Vault.Page<AgreementNegotiationState> results = rpcOps.vaultQueryByCriteria(vaultCriteria, AgreementNegotiationState.class);
            Vault.Page<AgreementNegotiationState> results1 = rpcOps.vaultQueryByCriteria(customCriteria, AgreementNegotiationState.class);

            results1.getStates().addAll(results.getStates());
            System.out.println("===================================================>>>>>>>>>>>." +results1.getStates().size());
            return results1.getStates();
        }
        catch (Exception ex) {
            System.out.println("Exception" + ex.toString());
            ex.printStackTrace();
        }

        return null;

    }
}