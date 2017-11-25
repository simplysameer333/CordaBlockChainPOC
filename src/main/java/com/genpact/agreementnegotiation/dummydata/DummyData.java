package com.genpact.agreementnegotiation.dummydata;

import com.genpact.agreementnegotiation.state.AgreementNegotiationState;

public class DummyData {
    public static AgreementNegotiationState getDummyDataForAgreementNegotiationState() {
        AgreementNegotiationState agreementNegotiationState = new AgreementNegotiationState();
        agreementNegotiationState.setAgrementName("Test");
        agreementNegotiationState.setBaseCurrency("GBP");
        agreementNegotiationState.setDeliveryAmount(2);
        agreementNegotiationState.setReturnAmount(1);
        return agreementNegotiationState;
    }


}
