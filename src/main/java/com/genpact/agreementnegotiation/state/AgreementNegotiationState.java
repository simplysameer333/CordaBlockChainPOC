package com.genpact.agreementnegotiation.state;

import com.genpact.agreementnegotiation.schema.AgreementNegotiationSchema;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;

/**
 * Define your state object here.
 */
public class AgreementNegotiationState implements LinearState, QueryableState {

    private String agrementName = null;
    private Date agrementInitiationDate = null;
    private Date agrementLastAmendDate = null;
    private Date agrementAgreedDate = null;
    private Double agreementValue = null;
    private String collateral = null;

    private Party cptyInitiator;
    private Party cptyReciever;

    private final UniqueIdentifier linearId;

    public AgreementNegotiationState(String name, Date initialDate, Double value, String collateral,
                                     Party cptyInitiator, Party cptyReciever) {
        this.agrementName = name;
        this.agrementInitiationDate = initialDate;
        this.agrementLastAmendDate = null;
        this.agrementAgreedDate = null;
        this.agreementValue= value;
        this.collateral=collateral;
        this.negotiationState= NegotiationStates.INITIAL;
        this.cptyInitiator = cptyInitiator;
        this.cptyReciever = cptyReciever;
        this.linearId = new UniqueIdentifier();
    }

    public AgreementNegotiationState(String name, Date initialDate, Double value, String collateral
                                     ) {
        this.agrementName = name;
        this.agrementInitiationDate = initialDate;
        this.agrementLastAmendDate = null;
        this.agrementAgreedDate = null;
        this.agreementValue= value;
        this.collateral=collateral;
        this.negotiationState= NegotiationStates.INITIAL;
        this.linearId = new UniqueIdentifier();
    }

    public AgreementNegotiationState(UniqueIdentifier linearId) {
        this.linearId = linearId;
    }

    @Override public UniqueIdentifier getLinearId() {
        return linearId;
    }

    public enum NegotiationStates
    {
        INITIAL("INITIAL"), AMEND("AMEND"), ACCEPT("ACCEPT");
        private final String name;

        private NegotiationStates(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }

    }

    private NegotiationStates negotiationState ;

    public String getAgrementName() {
        return agrementName;
    }

    public Date getAgrementInitiationDate() {
        return agrementInitiationDate;
    }

    public Date getAgrementLastAmendDate() {
        return agrementLastAmendDate;
    }

    public Date getAgrementAgreedDate() {
        return agrementAgreedDate;
    }

    public double getAgreementValue() {
        return agreementValue;
    }

    public String getCollateral() {
        return collateral;
    }

    /*public void setAgrementInitiationDate(Date agrementInitiationDate) {
        this.agrementInitiationDate = agrementInitiationDate;
    }*/

    public void setAgrementAgreedDate(Date agrementAgreedDate) {
        this.agrementAgreedDate = agrementAgreedDate;
    }

    public void setAgrementLastAmendDate(Date agrementLastAmendDate) {
        this.agrementLastAmendDate = agrementLastAmendDate;
    }

    public void setAgrementName(String agrementName) {
        this.agrementName = agrementName;
    }

    public void setAgreementValue(Double agreementValue) {
        this.agreementValue = agreementValue;
    }

    public void setCollateral(String collateral) {
        this.collateral = collateral;
    }

    public NegotiationStates getNegotiationState() {
        return negotiationState;
    }


    public void setNegotiationState(NegotiationStates negotiationState) {
        this.negotiationState = negotiationState;
    }

    public boolean isInitialized()
    {
        if(agrementName != null && agreementValue != null && agreementValue !=0 && collateral != null)
        {
            return  true;
        }
        return false;
    }




    public Party getCptyInitiator() {
        return cptyInitiator;
    }

    public Party getCptyReciever() {
        return cptyReciever;
    }

    public void setCptyInitiator(Party cptyInitiator) {
        this.cptyInitiator = cptyInitiator;
    }

    public void setCptyReciever(Party cptyReciever) {
        this.cptyReciever = cptyReciever;
    }

    /** The public keys of the involved parties. */
    @Override public List<AbstractParty> getParticipants() { return ImmutableList.of(cptyInitiator, cptyReciever); }

    @Override public PersistentState generateMappedObject(MappedSchema schema) {
        if (schema instanceof AgreementNegotiationSchema) {
            return new AgreementNegotiationSchema.PersistentIOU(
                    this.agrementName,
                    this.agrementInitiationDate,
                    this.agreementValue,
                    this.agrementLastAmendDate,
                    this.agrementAgreedDate,
                    this.collateral,
                    this.negotiationState,
                    this.linearId.getId());
        } else {
            throw new IllegalArgumentException("Unrecognised schema $schema");
        }
    }

    @Override public Iterable<MappedSchema> supportedSchemas() {
        return ImmutableList.of(new AgreementNegotiationSchema());
    }

    @Override
    public String toString() {
        return "AgreementNegotiationState{" +
                "agrementName='" + agrementName + '\'' +
                ", agrementInitiationDate=" + agrementInitiationDate +
                ", agrementLastAmendDate=" + agrementLastAmendDate +
                ", agrementAgreedDate=" + agrementAgreedDate +
                ", agreementValue=" + agreementValue +
                ", collateral='" + collateral + '\'' +
                ", cptyInitiator=" + cptyInitiator +
                ", cptyReciever=" + cptyReciever +
                ", negotiationState=" + negotiationState +
                '}';
    }
}