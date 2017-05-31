package ru.opensecreto.sigmacoin.vm;

public interface Memory {

    /**
     * Get word at specified position. If word has not been set 0x00 is returned by default.
     *
     * @param pointer position of word to get
     * @return word with specified pointer or 0x00 if word is unset
     */
    public Word get(long pointer);

    /**
     * Empty word at specified position ie. set 0x00 at specified poistion
     *
     * @param pointer position of word to empty
     */
    public default void remove(long pointer) {
        set(pointer, Word.WORD_0);
    }

    /**
     * Set given value at specified position
     *
     * @param pointer position of word to set
     * @param word    word to set at given position
     */
    public void set(long pointer, Word word);

}
