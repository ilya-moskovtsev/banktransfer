package org.banktransfer.model;

import java.math.BigDecimal;
import java.util.Objects;

public class TransferRequestDTO {
    String srcPassport;
    String srcRequisite;
    String dstPassport;
    String dstRequisite;
    BigDecimal amount;

    public TransferRequestDTO() {
    }

    public TransferRequestDTO(String srcPassport, String srcRequisite, String dstPassport, String dstRequisite, BigDecimal amount) {
        this.srcPassport = srcPassport;
        this.srcRequisite = srcRequisite;
        this.dstPassport = dstPassport;
        this.dstRequisite = dstRequisite;
        this.amount = amount;
    }

    public String getSrcPassport() {
        return srcPassport;
    }

    public void setSrcPassport(String srcPassport) {
        this.srcPassport = srcPassport;
    }

    public String getSrcRequisite() {
        return srcRequisite;
    }

    public void setSrcRequisite(String srcRequisite) {
        this.srcRequisite = srcRequisite;
    }

    public String getDstPassport() {
        return dstPassport;
    }

    public void setDstPassport(String dstPassport) {
        this.dstPassport = dstPassport;
    }

    public String getDstRequisite() {
        return dstRequisite;
    }

    public void setDstRequisite(String dstRequisite) {
        this.dstRequisite = dstRequisite;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferRequestDTO that = (TransferRequestDTO) o;
        return Objects.equals(srcPassport, that.srcPassport) &&
                Objects.equals(srcRequisite, that.srcRequisite) &&
                Objects.equals(dstPassport, that.dstPassport) &&
                Objects.equals(dstRequisite, that.dstRequisite) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcPassport, srcRequisite, dstPassport, dstRequisite, amount);
    }

    @Override
    public String toString() {
        return "TransferRequestDTO{" +
                "srcPassport='" + srcPassport + '\'' +
                ", srcRequisite='" + srcRequisite + '\'' +
                ", dstPassport='" + dstPassport + '\'' +
                ", dstRequisite='" + dstRequisite + '\'' +
                ", amount=" + amount +
                '}';
    }
}
