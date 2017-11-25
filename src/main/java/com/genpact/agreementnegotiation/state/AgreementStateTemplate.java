package com.genpact.agreementnegotiation.state;

import com.genpact.agreementnegotiation.utils.AgreementUtil;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class AgreementStateTemplate implements LinearState {

    private UniqueIdentifier linearId;

    private String agrementName = null;
    private Date agrementInitiationDate = null;
    private Date agrementAgreedDate = null;
    private Party cptyInitiator = null;
    private Party cptyReciever = null;
    private Party lastUpdatedBy = null;
    private Date agrementLastAmendDate = null;
    private AgreementEnumState status;
    private List<SecureHash> attachmentHash;


    public void setLinearId(UniqueIdentifier linearId) {
        this.linearId = linearId;
    }

    public String getAgrementName() {
        return agrementName;
    }

    public void setAgrementName(String agrementName) {
        this.agrementName = agrementName;
    }

    public String getAgrementInitiationDate() {
        if (agrementInitiationDate != null) {
            String dateStr = AgreementUtil.FORMAT.format(agrementInitiationDate);
            return dateStr;
        }
        return "";
    }

    public Date getAgrementInitiationDateAsDate() {
        return agrementInitiationDate;
    }

    public void setAgrementInitiationDate(Date agrementInitiationDate) {
        this.agrementInitiationDate = agrementInitiationDate;
    }

    public Date getAgrementAgreedDateAsDate() {
        return agrementAgreedDate;
    }

    public String getAgrementAgreedDate() {
        if (agrementAgreedDate != null) {
            String dateStr = AgreementUtil.FORMAT.format(agrementAgreedDate);
            return dateStr;
        }
        return "";
    }

    public void setAgrementAgreedDate(Date agrementAgreedDate) {
        this.agrementAgreedDate = agrementAgreedDate;
    }

    public Party getCptyInitiator() {
        return cptyInitiator;
    }

    public void setCptyInitiator(Party cptyInitiator) {
        this.cptyInitiator = cptyInitiator;
    }

    public Party getCptyReciever() {
        return cptyReciever;
    }

    public void setCptyReciever(Party cptyReciever) {
        this.cptyReciever = cptyReciever;
    }

    public Party getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Party lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getAgrementLastAmendDateAsDate() {
        return agrementLastAmendDate;
    }

    public String getAgrementLastAmendDate() {
        if (agrementLastAmendDate != null) {
            String dateStr = AgreementUtil.FORMAT.format(agrementLastAmendDate);
            return dateStr;
        }
        return "";

    }

    public void setAgrementLastAmendDate(Date agrementLastAmendDate) {
        this.agrementLastAmendDate = agrementLastAmendDate;
    }

    public AgreementEnumState getStatus() {
        return status;
    }

    public void setStatus(AgreementEnumState status) {
        this.status = status;
    }

    public List<SecureHash> getAttachmentHash() {
        return attachmentHash;
    }

    public void setAttachmentHash(List<SecureHash> attachmentHash) {
        this.attachmentHash = attachmentHash;
    }

    @NotNull
    @Override
    public UniqueIdentifier getLinearId() {
        return linearId;
    }


    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(cptyInitiator, cptyReciever);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
