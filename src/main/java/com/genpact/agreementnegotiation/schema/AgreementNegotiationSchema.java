package com.genpact.agreementnegotiation.schema;

import com.genpact.agreementnegotiation.state.EligibleCollateralState;
import com.google.common.collect.ImmutableList;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.serialization.CordaSerializable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * An IOUState schema.
 */

@CordaSerializable
public class AgreementNegotiationSchema extends MappedSchema {
    public AgreementNegotiationSchema() {
        super(AgreementNegotiationSchema.class, 1, ImmutableList.of(PersistentIOU.class));
    }

    @Entity
    @Table(name = "iou_states")
    public static class PersistentIOU extends PersistentState {
        @Column(name = "linearId")
        private String linearId;

        @Column(name = "agrementName")
        private String agrementName = null;

        @Column(name = "agrementInitiationDate")
        private String agrementInitiationDate = null;

        @Column(name = "agrementAgreedDate")
        private String agrementAgreedDate = null;

        @Column(name = "lastUpdatedBy")
        private String lastUpdatedBy = null;

        @Column(name = "agrementLastAmendDate")
        private String agrementLastAmendDate = null;

        @Column(name = "negotiationState")
        private String negotiationState;

        @Column(name = "cptyInitiator")
        private String cptyInitiator;

        @Column(name = "cptyReciever")
        private String cptyReciever;

        @Column(name = "baseCurrency")
        private String baseCurrency;

        @Column(name = "eligibleCurrency")
        private String eligibleCurrency;

        @Column(name = "deliveryAmount")
        private int deliveryAmount;

        @Column(name = "returnAmount")
        private int returnAmount;

        @Column(name = "creditSupportAmount")
        private int creditSupportAmount;

        @Column(name = "eligibleCollateral")
        private int eligibleCollateralType;

        @Column(name = "thresholdRating")
        private int thresholdRating;

        @Column(name = "threshold")
        private int threshold;

        @Column(name = "minimumTransferAmount")
        private BigDecimal minimumTransferAmount;

        @Column(name = "valuationAgent")
        private String valuationAgent;

        @Column(name = "valuationDate")
        private String valuationDate;

        @Column(name = "valuationTime")
        private String valuationTime;

        @Column(name = "notificationTime")
        private Date notificationTime;

        @Column(name = "substitutionDateFrom")
        private Date substitutionDateFrom;

        @Column(name = "substitutionDateTo")
        private Date substitutionDateTo;

        @Column(name = "consent")
        private Boolean consent;

        @Embedded
        @ElementCollection
        @CollectionTable(name = "LIST_SPECIFIC_CONDITIONS")
        private List<String> specifiedCondition;

        @Embedded
        @ElementCollection
        @CollectionTable(name = "Agreement_EligibleCollateralStates")
        List<EligibleCollateralState> eligibleCollateralStates;

        @Embedded
        @ElementCollection
        @CollectionTable(name = "Agreement_Thresholds")
        List<EligibleCollateralState> threshods;

        public PersistentIOU() {
        }

        public PersistentIOU(UUID linearId, String agrementName,
                             String agrementInitiationDate, String agrementAgreedDate, String lastUpdatedBy,
                             String agrementLastAmendDate, String negotiationState, String cptyInitiator,
                             String cptyReciever, String baseCurrency, String eligibleCurrency,
                             int deliveryAmount, int returnAmount, int creditSupportAmount,
                             int eligibleCollateralType, String valuationAgent, String valuationDate, String valuationTime,
                             Date notificationTime, List<String> specifiedCondition, Date substitutionDateTo,
                             Date substitutionDateFrom, Boolean consent,
                             List<EligibleCollateralState> eligibleCollateralStates,
                             List<EligibleCollateralState> threshods) {

            this.linearId = linearId.toString();
            this.agrementName = agrementName;
            this.agrementInitiationDate = agrementInitiationDate;
            this.agrementAgreedDate = agrementAgreedDate;
            this.lastUpdatedBy = lastUpdatedBy;
            this.agrementLastAmendDate = agrementLastAmendDate;
            this.negotiationState = negotiationState;
            this.cptyInitiator = cptyInitiator;
            this.cptyReciever = cptyReciever;
            this.baseCurrency = baseCurrency;
            this.eligibleCurrency = eligibleCurrency;
            this.deliveryAmount = deliveryAmount;
            this.returnAmount = returnAmount;
            this.creditSupportAmount = creditSupportAmount;
            this.eligibleCollateralType = eligibleCollateralType;
            this.valuationAgent = valuationAgent;
            this.valuationDate = valuationDate;
            this.valuationTime = valuationTime;
            this.notificationTime = notificationTime;
            this.specifiedCondition = specifiedCondition;
            this.substitutionDateTo = substitutionDateTo;
            this.substitutionDateFrom = substitutionDateFrom;
            this.consent = consent;
            this.eligibleCollateralStates = eligibleCollateralStates;
            this.threshods = threshods;
        }

        public String getLinearId() {
            return linearId;
        }

        public void setLinearId(String linearId) {
            this.linearId = linearId;
        }

        public String getAgrementName() {
            return agrementName;
        }

        public void setAgrementName(String agrementName) {
            this.agrementName = agrementName;
        }

        public String getAgrementInitiationDate() {
            return agrementInitiationDate;
        }

        public void setAgrementInitiationDate(String agrementInitiationDate) {
            this.agrementInitiationDate = agrementInitiationDate;
        }

        public String getAgrementAgreedDate() {
            return agrementAgreedDate;
        }

        public void setAgrementAgreedDate(String agrementAgreedDate) {
            this.agrementAgreedDate = agrementAgreedDate;
        }

        public String getLastUpdatedBy() {
            return lastUpdatedBy;
        }

        public void setLastUpdatedBy(String lastUpdatedBy) {
            this.lastUpdatedBy = lastUpdatedBy;
        }

        public String getAgrementLastAmendDate() {
            return agrementLastAmendDate;
        }

        public void setAgrementLastAmendDate(String agrementLastAmendDate) {
            this.agrementLastAmendDate = agrementLastAmendDate;
        }

        public String getNegotiationState() {
            return negotiationState;
        }

        public void setNegotiationState(String negotiationState) {
            this.negotiationState = negotiationState;
        }

        public String getCptyInitiator() {
            return cptyInitiator;
        }

        public void setCptyInitiator(String cptyInitiator) {
            this.cptyInitiator = cptyInitiator;
        }

        public String getCptyReciever() {
            return cptyReciever;
        }

        public void setCptyReciever(String cptyReciever) {
            this.cptyReciever = cptyReciever;
        }

        public String getBaseCurrency() {
            return baseCurrency;
        }

        public void setBaseCurrency(String baseCurrency) {
            this.baseCurrency = baseCurrency;
        }

        public String getEligibleCurrency() {
            return eligibleCurrency;
        }

        public void setEligibleCurrency(String eligibleCurrency) {
            this.eligibleCurrency = eligibleCurrency;
        }

        public int getDeliveryAmount() {
            return deliveryAmount;
        }

        public void setDeliveryAmount(int deliveryAmount) {
            this.deliveryAmount = deliveryAmount;
        }

        public int getReturnAmount() {
            return returnAmount;
        }

        public void setReturnAmount(int returnAmount) {
            this.returnAmount = returnAmount;
        }

        public int getCreditSupportAmount() {
            return creditSupportAmount;
        }

        public void setCreditSupportAmount(int creditSupportAmount) {
            this.creditSupportAmount = creditSupportAmount;
        }

        public int getEligibleCollateralType() {
            return eligibleCollateralType;
        }

        public void setEligibleCollateralType(int eligibleCollateralType) {
            this.eligibleCollateralType = eligibleCollateralType;
        }

        public Date getSubstitutionDateFrom() {
            return substitutionDateFrom;
        }

        public void setSubstitutionDateFrom(Date substitutionDateFrom) {
            this.substitutionDateFrom = substitutionDateFrom;
        }

        public Date getSubstitutionDateTo() {
            return substitutionDateTo;
        }

        public void setSubstitutionDateTo(Date substitutionDateTo) {
            this.substitutionDateTo = substitutionDateTo;
        }

        public List<EligibleCollateralState> getEligibleCollateralStates() {
            return eligibleCollateralStates;
        }

        public void setEligibleCollateralStates(List<EligibleCollateralState> eligibleCollateralStates) {
            this.eligibleCollateralStates = eligibleCollateralStates;
        }

        public List<EligibleCollateralState> getThreshods() {
            return threshods;
        }

        public void setThreshods(List<EligibleCollateralState> threshods) {
            this.threshods = threshods;
        }

        public int getThresholdRating() {
            return thresholdRating;
        }

        public void setThresholdRating(int thresholdRating) {
            this.thresholdRating = thresholdRating;
        }

        public int getThreshold() {
            return threshold;
        }

        public void setThreshold(int threshold) {
            this.threshold = threshold;
        }

        public BigDecimal getMinimumTransferAmount() {
            return minimumTransferAmount;
        }

        public void setMinimumTransferAmount(BigDecimal minimumTransferAmount) {
            this.minimumTransferAmount = minimumTransferAmount;
        }

        public String getValuationAgent() {
            return valuationAgent;
        }

        public void setValuationAgent(String valuationAgent) {
            this.valuationAgent = valuationAgent;
        }

        public String getValuationDate() {
            return valuationDate;
        }

        public void setValuationDate(String valuationDate) {
            this.valuationDate = valuationDate;
        }

        public String getValuationTime() {
            return valuationTime;
        }

        public void setValuationTime(String valuationTime) {
            this.valuationTime = valuationTime;
        }

        public Date getNotificationTime() {
            return notificationTime;
        }

        public void setNotificationTime(Date notificationTime) {
            this.notificationTime = notificationTime;
        }

        public List<String> getSpecifiedCondition() {
            return specifiedCondition;
        }

        public void setSpecifiedCondition(List<String> specifiedCondition) {
            this.specifiedCondition = specifiedCondition;
        }

        public Boolean getConsent() {
            return consent;
        }

        public void setConsent(Boolean consent) {
            this.consent = consent;
        }
    }
}