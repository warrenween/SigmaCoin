package ru.opensecreto.sigmacoin.crypto.Ed25519;

import org.testng.annotations.Test;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class MathTest {

    @Test
    public void testPointAdd() {
        assertThat(Ed25519Math.pointAdd(new Point(
                new BigInteger("18"),
                new BigInteger("22"),
                new BigInteger("33"),
                new BigInteger("44")
        ), new Point(
                new BigInteger("55"),
                new BigInteger("11"),
                new BigInteger("12"),
                new BigInteger("48")
        ))).isEqualTo(new Point(
                new BigInteger("1480119904550647030759445775737697625780212732697519729885137620622252828593923919799271513018141006371124484297385920580941123704379518059020092422098022"),
                new BigInteger("1480119904550647030759445775737697625780212732697519729885137620622252828820615140637094652062861514403597812917622564903399063979457869969617325255459734"),
                new BigInteger("-653575869610893438434077432115234060850329170445659017049538355253808878047875542689603808079059088190659639108018251242068868687251746769321217545487012"),
                new BigInteger("-3351951982485649274893506249551461531869841455148098344430890360930441007495807286799191916434129514845813822515233865703638560715292413605065351964801929"))
        );
        assertThat(Ed25519Math.pointAdd(new Point(
                new BigInteger("-112"),
                new BigInteger("548"),
                new BigInteger("11"),
                new BigInteger("1337")
        ), new Point(
                new BigInteger("-1"),
                new BigInteger("100"),
                new BigInteger("-1010"),
                new BigInteger("123456")
        ))).isEqualTo(new Point(
                new BigInteger("-944577909030137682361566980333541507682030719456972751213319584202782252766549448"),
                new BigInteger("8301644676303089398305991724799160261788480331940650153330727379923732250861415680"),
                new BigInteger("3038860292144198277069726056575042091335843458050932590368040675212906360250532071180231033480435751056525233226907674651951410406685892239236278252638785"),
                new BigInteger("-2580424704")
        ));
    }

    @Test
    public void testPointMul() {
        assertThat(Ed25519Math.pointMultiply(
                new BigInteger("12"),
                new Point(
                        new BigInteger("7"),
                        new BigInteger("8"),
                        new BigInteger("9"),
                        new BigInteger("10")
                )
        )).isEqualTo(new Point(
                new BigInteger("249252902824607157359932179741943409648822219648189362920230328143882720550538396106724372117381597180281755339376978434046218305479327609047618165199442"),
                new BigInteger("5853105161543810569471542561120677766145873533218125989018051321998868067923747816112843414177603189612517570150884009435708703199996523446999219268375914"),
                new BigInteger("-557592608570122059196972986319257689411923449329848671130431390149111882649403316908069166703256983277913949820968252571301884352848985379918299692186419"),
                new BigInteger("-2616432552421498527168254970732307989888139229987939356676705815708039423732298923887016584971331233327272388780749859270230446462763723779314273060769852"))
        );
    }

    @Test
    public void testPointCompress() {
        assertThat(Ed25519Math.pointCompress(new Point(
                new BigInteger("123456"),
                new BigInteger("654321"),
                new BigInteger("145236"),
                new BigInteger("415263")
        ))).inHexadecimal().containsExactly(
                DatatypeConverter.parseHexBinary("999e2334297c5e8b25e730487dbba0a71d4a30c36cc64dc5f0af6e7955910f40")
        );
        assertThat(Ed25519Math.pointCompress(new Point(
                new BigInteger("107476042684030475022426274060332207749171214163680785911355944558756023498617491072792799376062830432612391761593948118248657468455229315710007414441624"),
                new BigInteger("5410758752502457089400610161146016461170627570354109622010108376419741561454282074670034729421483517775864938038933924600798017573561841035876239594233360"),
                new BigInteger("1136952883970204633706812794638621572368100287039913516575464808807197739972996305654393405099630524103035791773099397724488021844499716655417512209095245"),
                new BigInteger("511478485024173835300288291093194642055903747030781481957181158890555723796959114150662746971778184230599318770561185466881781850781735713393738658406272"))
        )).inHexadecimal().containsExactly(
                DatatypeConverter.parseHexBinary("92a009a9f0d4cab8720e820b5f642540a2b27b5416503f8fb3762223ebdb69da")
        );
    }

    @Test
    public void testSha512modq() {
        assertThat(Ed25519Math.sha512_modq(
                DatatypeConverter.parseHexBinary("1234567890abcdef")
        )).inHexadecimal().isEqualTo(
                new BigInteger("5803724176970883683641592880329783986223242669612705683448510055637970858590")
        );
    }

    @Test
    public void testSecretExpand() {
        Secret s = Ed25519Math.secretExpand(
                DatatypeConverter.parseHexBinary("9d61b19deffd5a60ba844af492ec2cc44449c5697b326919703bac031cae7f60")
        );
        assertThat(s.v).isEqualTo(
                new BigInteger("36144925721603087658594284515452164870581325872720374094707712194495455132720")
        );
        assertThat(s.arr).inHexadecimal().containsExactly(
                DatatypeConverter.parseHexBinary("9b4f0afe280b746a778684e75442502057b7473a03f08f96f5a38e9287e01f8f")
        );
    }

    @Test
    public void testPointDecompress() {
        assertThat(Ed25519Math.pointDecompress(
                DatatypeConverter.parseHexBinary("d75a980182b10ab7d54bfed3c964073a0ee172f3daa62325af021a68f707511a")
        )).isEqualTo(new Point(
                new BigInteger("38815646466658113194383306759739515082307681141926459231621296960732224964046"),
                new BigInteger("11903303657706407974989296177215005343713679411332034699907763981919547054807"),
                new BigInteger("1"),
                new BigInteger("31275909032640112889229532081174740659065478602231738919115306243253221725764")
        ));
    }

    @Test
    public void testRecoverX() {
        assertThat(Ed25519Math.recoverX(
                new BigInteger("11903303657706407974989296177215005343713679411332034699907763981919547054807"),
                BigInteger.ZERO
        )).isEqualTo(
                new BigInteger("38815646466658113194383306759739515082307681141926459231621296960732224964046")
        );
    }
}
