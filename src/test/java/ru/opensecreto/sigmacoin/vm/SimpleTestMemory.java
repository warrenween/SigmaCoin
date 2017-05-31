package ru.opensecreto.sigmacoin.vm;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SimpleTestMemory implements Memory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTestMemory.class);

    private final Map<Integer, Word> words = new HashMap<>();

    @Override
    public Word get(int pointer) {
        if (words.containsKey(pointer)) {
            LOGGER.warn("Unset value requested at {}. \n {}",
                    pointer, ExceptionUtils.getStackTrace(new Throwable()));
            return Word.WORD_0;
        }
        return words.get(pointer);
    }

    @Override
    public void set(int pointer, Word word) {
        words.put(pointer, word);
    }
}
