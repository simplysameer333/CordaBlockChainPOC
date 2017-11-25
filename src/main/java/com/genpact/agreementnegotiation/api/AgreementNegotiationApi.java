package com.genpact.agreementnegotiation.api;

import com.genpact.agreementnegotiation.flow.AgreementNegotiationAcceptFlow;
import com.genpact.agreementnegotiation.flow.AgreementNegotiationAmendFlow;
import com.genpact.agreementnegotiation.flow.AgreementNegotiationInitiateFlow;
import com.genpact.agreementnegotiation.flow.AgreementNegotiationSearchFlow;
import com.genpact.agreementnegotiation.model.Agreement;
import com.genpact.agreementnegotiation.schema.AgreementNegotiationSchema;
import com.genpact.agreementnegotiation.state.AgreementNegotiationState;
import com.genpact.agreementnegotiation.utils.AgreementUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.crypto.SecureHash;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.messaging.FlowProgressHandle;
import net.corda.core.node.NodeInfo;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.Builder;
import net.corda.core.node.services.vault.CriteriaExpression;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

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
    public Response startInitFlow(Agreement agreement, @PathParam("partyName") CordaX500Name partyName) {

        try {
            System.out.println("initFlow ==============================>" + agreement.toString());
            AgreementNegotiationState agreementNegotiationState = AgreementUtil.copyState(agreement);
            agreementNegotiationState.setCptyReciever(rpcOps.wellKnownPartyFromX500Name(agreement.getCounterparty()));

            if (agreement.getAttachmentHash() != null && !agreement.getAttachmentHash().isEmpty()) {
                List<SecureHash> attachmentHashes = new ArrayList<>();
                for (String url : agreement.getAttachmentHash()) {
                    SecureHash ourAttachmentHash;
                    try {
                        /*InputStream inputStream = new FileInputStream(new File(
                                "C:\\Users\\hamesam\\Downloads\\tomcat.zip"));*/
                        InputStream inputStream = new FileInputStream(new File(url));
                        ourAttachmentHash = rpcOps.uploadAttachment(inputStream);
                        attachmentHashes.add(ourAttachmentHash);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //TODO add handling
                    }
                }
                if (!attachmentHashes.isEmpty()) {
                    agreementNegotiationState.setAttachmentHash(attachmentHashes);
                }
            }

            //get the attachment path
            FlowProgressHandle<SignedTransaction> flowHandle = rpcOps
                    .startTrackedFlowDynamic(AgreementNegotiationInitiateFlow.Initiator.class,
                            agreementNegotiationState);

            flowHandle.getProgress().subscribe(evt -> System.out.printf(">> %s\n", evt));

            // The line below blocks and waits for the flow to return.
            final SignedTransaction result = flowHandle.getReturnValue().get();

            final String msg = String.format("Transaction id %s committed to ledger.\n", result.getId());
            System.out.println("message " + msg);

            System.out.println("befoore===================================> " + agreementNegotiationState.toString());
            AgreementNegotiationState a1 = new AgreementNegotiationState();
            AgreementUtil.copyAllFields(a1, agreementNegotiationState);
            System.out.println("after ====================================> " + a1.toString());


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

            AgreementNegotiationState agreementNegotiationState = AgreementUtil.copyState(agreement);
            FlowProgressHandle<SignedTransaction> flowHandle = rpcOps
                    .startTrackedFlowDynamic(AgreementNegotiationAmendFlow.Initiator.class, agreementNegotiationState);
            flowHandle.getProgress().subscribe(evt -> System.out.printf(">> %s\n", evt));

            // The line below blocks and waits for the flow to return.
            final SignedTransaction result = flowHandle.getReturnValue().get();

            final String msg = String.format("Transaction id %s committed to ledger.\n", result.getId());
            System.out.println("message" + msg);

            return Response.ok("amendFlow GET endpoint.").build();
        } catch (Throwable ex) {
            System.out.println("Exception" + ex.toString());
        }
        return Response.ok("ERROR  GET endpoint.").build();

    }

    /**
     * Accessible at /api/template/<party>/amendFlow.
     */
    @PUT
    @Path("acceptFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response accept(Agreement agreement) {
        try {

            final Party otherParty = rpcOps.nodeInfo().getLegalIdentities().get(0);

            AgreementNegotiationState agreementNegotiationState = AgreementUtil.copyState(agreement);
            FlowProgressHandle<SignedTransaction> flowHandle = rpcOps
                    .startTrackedFlowDynamic(AgreementNegotiationAcceptFlow.Initiator.class, agreementNegotiationState);
            flowHandle.getProgress().subscribe(evt -> System.out.printf(">> %s\n", evt));

            // The line below blocks and waits for the flow to return.
            final SignedTransaction result = flowHandle.getReturnValue().get();

            final String msg = String.format("Transaction id %s committed to ledger.\n", result.getId());
            System.out.println("message" + msg);

            return Response.ok("acceptFloe GET endpoint.").build();
        } catch (Throwable ex) {
            System.out.println("Exception" + ex.toString());
        }
        return Response.ok("ERROR  GET endpoint.").build();

    }


    @GET
    @Path("getAgreements")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Agreement> getAgreements() {
        QueryCriteria criteria = new QueryCriteria.VaultQueryCriteria(Vault.StateStatus.UNCONSUMED);
        List<StateAndRef<AgreementNegotiationState>> agreementNegotiationStates =
                rpcOps.vaultQueryByCriteria(criteria, AgreementNegotiationState.class).getStates();

        List<Agreement> agreementsList = new ArrayList<>();
        for (StateAndRef<AgreementNegotiationState> value : agreementNegotiationStates) {
            Agreement agreement = AgreementUtil.copyStateToVO(value.getState().getData());
            System.out.println("get ALL Agreement ============================= > " + agreement.toString());
            agreementsList.add(agreement);
        }
        return agreementsList;
    }

    @GET
    @Path("getAgreement/{agreementName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Agreement getAgreement(@PathParam("agreementName") String agreementName) {
        List<StateAndRef<AgreementNegotiationState>> result;
        try {

            Field uniqueAttributeName = AgreementNegotiationSchema.PersistentIOU.class.getDeclaredField("agrementName");
            CriteriaExpression uniqueAttributeEXpression = Builder.equal(uniqueAttributeName, agreementName);

            QueryCriteria customCriteria = new QueryCriteria.VaultCustomQueryCriteria(uniqueAttributeEXpression);
            result = rpcOps.vaultQueryByCriteria(customCriteria, AgreementNegotiationState.class).getStates();

            if (result.size() > 0) {
                Agreement agreement = AgreementUtil.copyStateToVO(result.get(0).getState().getData());
                System.out.println("getAgreement ============================= > " + agreement.toString());
                return agreement;
            }

        } catch (Exception ex) {

            System.out.println("Exception" + ex.toString());
            ex.printStackTrace();
        }
        return null;
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


    @GET
    @Path("audit")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Agreement> getAudit(@QueryParam("agreementName") String agreementName) {

        try {
            Field uniqueAttributeName = AgreementNegotiationSchema.PersistentIOU.class.getDeclaredField("agrementName");
            CriteriaExpression uniqueAttributeEXpression = Builder.equal(uniqueAttributeName, agreementName);
            QueryCriteria customCriteria = new QueryCriteria.VaultCustomQueryCriteria(uniqueAttributeEXpression,
                    Vault.StateStatus.CONSUMED);

            QueryCriteria vaultCriteria = new QueryCriteria.VaultCustomQueryCriteria(uniqueAttributeEXpression, Vault.StateStatus.UNCONSUMED);

            Vault.Page<AgreementNegotiationState> results = rpcOps.vaultQueryByCriteria(vaultCriteria, AgreementNegotiationState.class);
            Vault.Page<AgreementNegotiationState> results1 = rpcOps.vaultQueryByCriteria(customCriteria, AgreementNegotiationState.class);

            results1.getStates().addAll(results.getStates());

            List<Agreement> agreementsList = new ArrayList<>();
            for (StateAndRef<AgreementNegotiationState> value : results1.getStates()) {
                Agreement agreement = AgreementUtil.copyStateToVO(value.getState().getData());
                agreementsList.add(agreement);
            }
            return agreementsList;
        } catch (Exception ex) {
            System.out.println("Exception" + ex.toString());
            ex.printStackTrace();
        }

        return null;

    }


    @GET
    @Path("getList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Agreement> getStatesUsingLinerIds(@QueryParam("listSearch") String listSearch) {
        try {
            final Party otherParty = rpcOps.nodeInfo().getLegalIdentities().get(0);

            FlowProgressHandle<List<String>> flowHandle = rpcOps
                    .startTrackedFlowDynamic(AgreementNegotiationSearchFlow.Initiator.class, listSearch);
            final List<String> linerIds = flowHandle.getReturnValue().get();

            if (linerIds != null && !linerIds.isEmpty()) {
                Field uniqueAttributeName = AgreementNegotiationSchema.PersistentIOU.class
                        .getDeclaredField("linearId");
                CriteriaExpression uniqueAttributeEXpression = Builder.in(uniqueAttributeName, linerIds);

                QueryCriteria customCriteria = new QueryCriteria.VaultCustomQueryCriteria(uniqueAttributeEXpression,
                        Vault.StateStatus.CONSUMED);

                QueryCriteria vaultCriteria = new QueryCriteria.VaultCustomQueryCriteria(uniqueAttributeEXpression, Vault.StateStatus.UNCONSUMED);

                Vault.Page<AgreementNegotiationState> results = rpcOps.vaultQueryByCriteria(vaultCriteria, AgreementNegotiationState.class);
                Vault.Page<AgreementNegotiationState> results1 = rpcOps.vaultQueryByCriteria(customCriteria, AgreementNegotiationState.class);

                results1.getStates().addAll(results.getStates());
                List<Agreement> agreementsList = new ArrayList<>();
                for (StateAndRef<AgreementNegotiationState> value : results1.getStates()) {
                    Agreement agreement = AgreementUtil.copyStateToVO(value.getState().getData());
                    agreementsList.add(agreement);
                }
                return agreementsList;
            }
        } catch (Exception ex) {
            System.out.println("Exception" + ex.toString());
            ex.printStackTrace();
        }

        return null;

    }
}