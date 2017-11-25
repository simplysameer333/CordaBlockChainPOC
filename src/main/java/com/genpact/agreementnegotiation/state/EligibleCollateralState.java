package com.genpact.agreementnegotiation.state;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class EligibleCollateralState {
    public String currency;
    public String ratingText;
    public int collateralType;
    public int ratingType;
    public int rating;
    public int ratingRangeFrom;
    public int ratingRangeTo;
    public int amount;
    public Boolean remainingMaturity; //boolean 0 or 1
    public int remMaturityFrom;
    public int remMaturityTo;
    public Boolean isInitiatorAccepted; //boolean 0 or 1
    public Boolean isResponderAccecpted; //boolean 0 or 1


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

    public Boolean getRemainingMaturity() {
        return remainingMaturity;
    }

    public void setRemainingMaturity(Boolean remainingMaturity) {
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

    public Boolean getInitiatorAccepted() {
        return isInitiatorAccepted;
    }

    public void setInitiatorAccepted(Boolean initiatorAccepted) {
        isInitiatorAccepted = initiatorAccepted;
    }

    public Boolean getResponderAccecpted() {
        return isResponderAccecpted;
    }

    public void setResponderAccecpted(Boolean responderAccecpted) {
        isResponderAccecpted = responderAccecpted;
    }

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    public int getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(int collateralType) {
        this.collateralType = collateralType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
