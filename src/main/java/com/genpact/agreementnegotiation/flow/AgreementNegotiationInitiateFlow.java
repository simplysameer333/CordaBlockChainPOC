package com.genpact.agreementnegotiation.flow;

import co.paralleluniverse.fibers.Suspendable;
import com.genpact.agreementnegotiation.contract.AgreementNegotiationContract;
import com.genpact.agreementnegotiation.state.AgreementEnumState;
import com.genpact.agreementnegotiation.state.AgreementNegotiationState;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndContract;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.security.PublicKey;
import java.util.Date;
import java.util.List;

import static com.genpact.agreementnegotiation.contract.AgreementNegotiationContract.TEMPLATE_CONTRACT_ID;
import static net.corda.core.contracts.ContractsDSL.requireThat;

/**
 * Define your flow here.
 */
public class AgreementNegotiationInitiateFlow {
    /**
     * You can add a constructor to each FlowLogic subclass to pass objects into the flow.
     */
    @InitiatingFlow
    @StartableByRPC
    public static class Initiator extends FlowLogic<SignedTransaction> {
        private AgreementNegotiationState agreementNegotiationState;

        /**
         * Constructor.
         */
        public Initiator(AgreementNegotiationState agreementNegotiationState) {
            this.agreementNegotiationState = agreementNegotiationState;
        }

        /**
         * The progress tracker provides checkpoints indicating the progress of the flow to observers.
         */
        private static final ProgressTracker.Step ID_OTHER_NODES = new ProgressTracker.Step("Identifying other nodes on the network.");
        private static final ProgressTracker.Step SENDING_AND_RECEIVING_DATA = new ProgressTracker.Step("Sending data between parties.");
        private static final ProgressTracker.Step EXTRACTING_VAULT_STATES = new ProgressTracker.Step("Extracting states from the vault.");
        private static final ProgressTracker.Step OTHER_TX_COMPONENTS = new ProgressTracker.Step("Gathering a transaction's other components.");
        private static final ProgressTracker.Step TX_BUILDING = new ProgressTracker.Step("Building a transaction.");
        private static final ProgressTracker.Step TX_SIGNING = new ProgressTracker.Step("Signing a transaction.");
        private static final ProgressTracker.Step TX_VERIFICATION = new ProgressTracker.Step("Verifying a transaction.");
        private static final ProgressTracker.Step SIGS_GATHERING = new ProgressTracker.Step("Gathering a transaction's signatures.") {
            // Wiring up a child progress tracker allows us to see the
            // subflow's progress steps in our flow's progress tracker.
            @Override
            public ProgressTracker childProgressTracker() {
                return CollectSignaturesFlow.tracker();
            }
        };
        private static final ProgressTracker.Step FINALISATION = new ProgressTracker.Step("Finalising a transaction.") {
            @Override
            public ProgressTracker childProgressTracker() {
                return FinalityFlow.tracker();
            }
        };
        private final ProgressTracker progressTracker = new ProgressTracker(
                ID_OTHER_NODES,
                SENDING_AND_RECEIVING_DATA,
                EXTRACTING_VAULT_STATES,
                OTHER_TX_COMPONENTS,
                TX_BUILDING,
                TX_SIGNING,
                TX_VERIFICATION,
                SIGS_GATHERING,
                FINALISATION);


        @Override
        public ProgressTracker getProgressTracker() {
            return progressTracker;
        }
        /**
         * Define the initiator's flow logic here.
         */
        @Suspendable
        @Override public SignedTransaction call() throws FlowException{

            progressTracker.setCurrentStep(ID_OTHER_NODES);
            // We retrieve the notary identity from the network map.
            final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

            progressTracker.setCurrentStep(TX_BUILDING);
            // We create a transaction builder.
            final TransactionBuilder txBuilder = new TransactionBuilder();
            txBuilder.setNotary(notary);

            // We create the transaction components.
            agreementNegotiationState.setLinearId(new UniqueIdentifier());
            agreementNegotiationState.setCptyInitiator(getOurIdentity());
            agreementNegotiationState.setAgrementLastAmendDate(new Date());
            agreementNegotiationState.setAgrementInitiationDate(new Date());
            agreementNegotiationState.setLastUpdatedBy(getOurIdentity());
            agreementNegotiationState.setStatus(AgreementEnumState.INITIAL);

            StateAndContract outputContractAndState = new StateAndContract(agreementNegotiationState, TEMPLATE_CONTRACT_ID);
            List<PublicKey> requiredSigners = ImmutableList.of(agreementNegotiationState.getCptyInitiator().getOwningKey(),
                    agreementNegotiationState.getCptyReciever().getOwningKey());
            Command cmd = new Command<>(new AgreementNegotiationContract.Commands.Initiate(), requiredSigners);

            // We add the items to the builder.
            txBuilder.withItems(outputContractAndState, cmd);
            if (agreementNegotiationState.getAttachmentHash() != null &&
                    !agreementNegotiationState.getAttachmentHash().isEmpty()) {
                for (SecureHash secureHasId : agreementNegotiationState.getAttachmentHash()) {
                    txBuilder.addAttachment(secureHasId);
                }
            }


            // Verifying the transaction.
            progressTracker.setCurrentStep(TX_VERIFICATION);
            txBuilder.verify(getServiceHub());

            // Signing the transaction.
            final SignedTransaction signedTx = getServiceHub().signInitialTransaction(txBuilder);

            /// Creating a session with the other party.
            FlowSession otherpartySession = initiateFlow(agreementNegotiationState.getCptyReciever());


            // Obtaining the counterparty's signature.
            SignedTransaction fullySignedTx = subFlow(new CollectSignaturesFlow(
                    signedTx, ImmutableList.of(otherpartySession), CollectSignaturesFlow.tracker()));

            // Finalising the transaction.
            return subFlow(new FinalityFlow(fullySignedTx));

        }
    }

    @InitiatedBy(Initiator.class)
    public static class Responder extends FlowLogic<SignedTransaction> {
        private FlowSession counterpartySession;

        public Responder(FlowSession counterpartySession) {
            this.counterpartySession = counterpartySession;
        }

        /**
         * Define the acceptor's flow logic here.
         */
        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException{

            class SignTxFlow extends SignTransactionFlow {
                private SignTxFlow(FlowSession otherPartySession, ProgressTracker progressTracker) {
                    super(otherPartySession, progressTracker);
                }

                @Override
                protected void checkTransaction(SignedTransaction stx) throws FlowException {

                    requireThat(require -> {
                        ContractState output = stx.getTx().getOutputs().get(0).getData();
                        require.using("This must be an Agreement Negotiation transaction.", output instanceof AgreementNegotiationState);
                        //  AgreementNegotiationState agreementNegotiationState = (AgreementNegotiationState) output;
                        // require.using("The Agreement State Object is not Initialized.", agreementNegotiationState.isInitialized());
                        return null;
                    });
                }
            }
            return subFlow(new SignTxFlow(counterpartySession, SignTransactionFlow.Companion.tracker()));
        }
    }
}
