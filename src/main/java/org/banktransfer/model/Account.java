package org.banktransfer.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private BigDecimal value;
    private String requisites;

    public Account(String requisites) {
        this.value = BigDecimal.ZERO;
        this.requisites = requisites;
    }

    public Account(BigDecimal value, String requisites) {
        this.value = value;
        this.requisites = requisites;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(requisites, account.requisites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requisites);
    }
}
