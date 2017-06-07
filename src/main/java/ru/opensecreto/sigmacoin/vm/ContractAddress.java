package ru.opensecreto.sigmacoin.vm;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContractAddress {

    public final Word id;

    public ContractAddress(Word id) throws NullPointerException {
        this.id = checkNotNull(id);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof ContractAddress) && (((ContractAddress) obj).id.equals(id));
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
