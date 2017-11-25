package com.genpact.agreementnegotiation.model;

import net.corda.core.serialization.CordaSerializable;
import org.apache.commons.lang3.builder.ToStringBuilder;

@CordaSerializable
public class EligibleCollateral {

    private int collateralType;
    private String currency;
    private int ratingType;
    private int rating;
    private String ratingText;
    private int ratingRangeFrom;
    private int ratingRangeTo;
    private int amount;
    private int remainingMaturity; //boolean 0 or 1
    private int remMaturityFrom;
    private int remMaturityTo;
    private int partyA; //boolean 0 or 1
    private int partyB; //boolean 0 or 1

    public int getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(int collateralType) {
        this.collateralType = collateralType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getRatingType() {
        return ratingType;
    }

    public void setRatingType(int ratingType) {
        this.ratingType = ratingType;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    public int getRatingRangeFrom() {
        return ratingRangeFrom;
    }

    public void setRatingRangeFrom(int ratingRangeFrom) {
        this.ratingRangeFrom = ratingRangeFrom;
    }

    public int getRatingRangeTo() {
        return ratingRangeTo;
    }

    public void setRatingRangeTo(int ratingRangeTo) {
        this.ratingRangeTo = ratingRangeTo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getRemainingMaturity() {
        return remainingMaturity;
    }

    public void setRemainingMaturity(int remainingMaturity) {
        this.remainingMaturity = remainingMaturity;
    }

    public int getRemMaturityFrom() {
        return remMaturityFrom;
    }

    public void setRemMaturityFrom(int remMaturityFrom) {
        this.remMaturityFrom = remMaturityFrom;
    }

    public int getRemMaturityTo() {
        return remMaturityTo;
    }

    public void setRemMaturityTo(int remMaturityTo) {
        this.remMaturityTo = remMaturityTo;
    }

    public int getPartyA() {
        return partyA;
    }

    public void setPartyA(int partyA) {
        this.partyA = partyA;
    }

    public int getPartyB() {
        return partyB;
    }

    public void setPartyB(int partyB) {
        this.partyB = partyB;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
