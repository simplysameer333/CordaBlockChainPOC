package com.genpact.agreementnegotiation.model;

import net.corda.core.identity.CordaX500Name;
import net.corda.core.serialization.CordaSerializable;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CordaSerializable
public class Agreement {
    private String agrementName = null;
    private CordaX500Name counterparty = null;
    private String baseCurrency = null;
    private String eligibleCurrency = null;
    private int deliveryAmount = 0;
    private int returnAmount = 0;
    private int creditSupportAmount = 0;
    private int eligibleCollateralType = 0;
    private List<EligibleCollateral> eligibleCollaterals = new ArrayList<EligibleCollateral>();
    private List<EligibleCollateral> thresholds = new ArrayList<EligibleCollateral>();
    private int initialMargin = 0; //boolean 0 or 1
    private String valuationAgent = null;
    private String valuationDate = null;
    private String valuationTime = null;
    private Date notificationTime = null;
    private List<String> specifiedConditions = new ArrayList<String>();

    private Date substitutionDateFrom = null;
    private Date substitutionDateTo = null;
    private int consent = 0;//boolean 0 or 1
    private List<String> attachmentHash;

    //Additional Added fields
    private Date agrementInitiationDate = null;
    private Date agrementAgreedDate = null;
    private CordaX500Name cptyInitiator = null;
    private CordaX500Name lastUpdatedBy = null;
    private Date agrementLastAmendDate = null;
    private String id = null;
    private String status = null;

    public String getAgrementName() {
        return agrementName;
    }

    public void setAgrementName(String agrementName) {
        this.agrementName = agrementName;
    }

    public CordaX500Name getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(CordaX500Name counterparty) {
        this.counterparty = counterparty;
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

    public List<EligibleCollateral> getEligibleCollaterals() {
        return eligibleCollaterals;
    }

    public void setEligibleCollaterals(List<EligibleCollateral> eligibleCollaterals) {
        this.eligibleCollaterals = eligibleCollaterals;
    }

    public List<EligibleCollateral> getThresholds() {
        return thresholds;
    }

    public void setThresholds(List<EligibleCollateral> thresholds) {
        this.thresholds = thresholds;
    }

    public int getInitialMargin() {
        return initialMargin;
    }

    public void setInitialMargin(int initialMargin) {
        this.initialMargin = initialMargin;
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

    public List<String> getSpecifiedConditions() {
        return specifiedConditions;
    }

    public void setSpecifiedConditions(List<String> specifiedConditions) {
        this.specifiedConditions = specifiedConditions;
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

    public int getConsent() {
        return consent;
    }

    public void setConsent(int consent) {
        this.consent = consent;
    }

    public List<String> getAttachmentHash() {
        return attachmentHash;
    }

    public void setAttachmentHash(List<String> attachmentHash) {
        this.attachmentHash = attachmentHash;
    }

    public Date getAgrementInitiationDate() {
        return agrementInitiationDate;
    }

    public void setAgrementInitiationDate(Date agrementInitiationDate) {
        this.agrementInitiationDate = agrementInitiationDate;
    }

    public Date getAgrementAgreedDate() {
        return agrementAgreedDate;
    }

    public void setAgrementAgreedDate(Date agrementAgreedDate) {
        this.agrementAgreedDate = agrementAgreedDate;
    }

    public CordaX500Name getCptyInitiator() {
        return cptyInitiator;
    }

    public void setCptyInitiator(CordaX500Name cptyInitiator) {
        this.cptyInitiator = cptyInitiator;
    }

    public CordaX500Name getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(CordaX500Name lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getAgrementLastAmendDate() {
        return agrementLastAmendDate;
    }

    public void setAgrementLastAmendDate(Date agrementLastAmendDate) {
        this.agrementLastAmendDate = agrementLastAmendDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}