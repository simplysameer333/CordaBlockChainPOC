package com.genpact.agreementnegotiation.flow;

import co.paralleluniverse.fibers.Suspendable;
import com.genpact.agreementnegotiation.service.CustomQueryVaultService;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.utilities.ProgressTracker.Step;

import java.util.List;


/**
 * Define your flow here.
 */
public class AgreementNegotiationSearchFlow {
    /**
     * You can add a constructor to each FlowLogic subclass to pass objects into the flow.
     */
    @InitiatingFlow
    @StartableByRPC
    public static class Initiator extends FlowLogic<List<String>> {
        private final String value;

        public Initiator(String value) {
            this.value = value;
        }

        /**
         * The progress tracker provides checkpoints indicating the progress of the flow to observers.
         */
        private static final Step START_SEARCH = new Step("Start the search.");
        private static final Step END_SEARCH = new Step("End of search.");

        private final ProgressTracker progressTracker = new ProgressTracker(
                START_SEARCH,
                END_SEARCH);

        @Override
        public ProgressTracker getProgressTracker() {
            return progressTracker;
        }

        /**
         * Define the initiator's flow logic here.
         */
        @Suspendable
        @Override
        public List<String> call() throws FlowException {

            try {
                progressTracker.setCurrentStep(START_SEARCH);
                CustomQueryVaultService customQueryVaultService = getServiceHub().cordaService(CustomQueryVaultService.class);

                List<String> results = customQueryVaultService.getStateFromListValue(value);
                progressTracker.setCurrentStep(END_SEARCH);
                return results;
            } catch (Exception ex) {
                System.out.println("Exception" + ex.toString());
                ex.printStackTrace();
            }
            return null;
        }
    }

}
