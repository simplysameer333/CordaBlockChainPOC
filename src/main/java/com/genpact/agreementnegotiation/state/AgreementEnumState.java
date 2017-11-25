package com.genpact.agreementnegotiation.state;

public enum AgreementEnumState {
    INITIAL("INITIAL"),
    AMEND("AMEND"),
    PARTIAL_ACCEPTED("PARTIAL ACCEPTED"),
    FULLY_ACCEPTED("FULLY ACCEPTED");

    private final String name;
    private AgreementEnumState(String s) {
        name = s;
    }
    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }
    public String toString() {
        return this.name;
    }

}