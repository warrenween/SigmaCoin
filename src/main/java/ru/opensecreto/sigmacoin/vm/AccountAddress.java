package ru.opensecreto.sigmacoin.vm;

import static com.google.common.base.Preconditions.checkNotNull;

public class AccountAddress {

    public static final int BYTES = Word.WORD_SIZE;
    public final Word id;

    public AccountAddress(Word id) throws NullPointerException {
        this.id = checkNotNull(id);
    }

    public AccountAddress(byte[] data) throws IllegalArgumentException {
        this.id = new Word(data);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof AccountAddress) && ((AccountAddress) obj).id.equals(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
