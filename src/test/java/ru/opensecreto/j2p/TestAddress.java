package ru.opensecreto.j2p;

import org.testng.annotations.Test;

import java.io.IOException;

public class TestAddress {

    @Test
    public void testAddress() throws IOException {
        System.out.println(NetUtils.getExternalIp());
    }

}
