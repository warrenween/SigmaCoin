package ru.opensecreto.sigmacoin.vm;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContractAddress {

    public static final int BYTES = Word.WORD_SIZE;
    public final Word id;

    public ContractAddress(Word id) throws NullPointerException {
        this.id = checkNotNull(id);
    }

    public ContractAddress(byte[] data) throws IllegalArgumentException {
        this.id = new Word(data);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof ContractAddress) && ((ContractAddress) obj).id.equals(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
