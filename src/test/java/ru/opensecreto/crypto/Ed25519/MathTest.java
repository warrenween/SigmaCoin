package ru.opensecreto.crypto.Ed25519;

import org.testng.annotations.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class MathTest {

    @Test
    public void testPointAdd() {
        assertThat(Ed25519Math.pointAdd(
                new Point(
                        new BigInteger("18"),
                        new BigInteger("22"),
                        new BigInteger("33"),
                        new BigInteger("44")),
                new Point(
                        new BigInteger("55"),
                        new BigInteger("11"),
                        new BigInteger("12"),
                        new BigInteger("48"))
        )).isEqualTo(
                new Point(
                        new BigInteger("1480119904550647030759445775737697625780212732697519729885137620622252828593923919799271513018141006371124484297385920580941123704379518059020092422098022"),
                        new BigInteger("1480119904550647030759445775737697625780212732697519729885137620622252828820615140637094652062861514403597812917622564903399063979457869969617325255459734"),
                        new BigInteger("-653575869610893438434077432115234060850329170445659017049538355253808878047875542689603808079059088190659639108018251242068868687251746769321217545487012"),
                        new BigInteger("-3351951982485649274893506249551461531869841455148098344430890360930441007495807286799191916434129514845813822515233865703638560715292413605065351964801929"))
        );
        assertThat(Ed25519Math.pointAdd(
                new Point(
                        new BigInteger("-112"),
                        new BigInteger("548"),
                        new BigInteger("11"),
                        new BigInteger("1337")),
                new Point(
                        new BigInteger("-1"),
                        new BigInteger("100"),
                        new BigInteger("-1010"),
                        new BigInteger("123456"))
        )).isEqualTo(
                new Point(
                        new BigInteger("-944577909030137682361566980333541507682030719456972751213319584202782252766549448"),
                        new BigInteger("8301644676303089398305991724799160261788480331940650153330727379923732250861415680"),
                        new BigInteger("3038860292144198277069726056575042091335843458050932590368040675212906360250532071180231033480435751056525233226907674651951410406685892239236278252638785"),
                        new BigInteger("-2580424704"))
        );
    }

}
