package ru.opensecreto.sigmacoin.vm;

import org.testng.annotations.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.opensecreto.sigmacoin.vm.Opcodes.*;
import static ru.opensecreto.sigmacoin.vm.StopType.*;

public class BytecodeExecutorTest {

    @Test
    public void test_invokeNonExistingContract() {
        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(new AccountAddress(new Word(0)))).thenReturn(false);

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), new AccountAddress(new Word(0)));

        assertThat(result.stopType).isEqualTo(BAD);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_STOP_GOOD() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, STOP_GOOD);// |good(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(GOOD);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_INVOKE_bad() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, STOP_BAD);// |bad(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(BAD);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_INVOKE_returnsCorrectStack() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(0x1f));// 0x1f (top)
            set(2, DUP);// 0x1f 0x1f (top)
            set(3, DUP);// 0x1f 0x1f 0x1f (top)
            set(4, DUP);// 0x1f 0x1f 0x1f (top)
            set(5, STOP_BAD);// 0x1f 0x1f 0x1f 0x1f |bad(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(BAD);
        assertThat(result.stack.getSize()).isEqualTo(4);
        assertThat(result.stack.popCustom(4)).containsExactly(
                new Word(0x1f), new Word(0x1f), new Word(0x1f), new Word(0x1f)
        );
    }

    @Test
    public void test_PUSH_POP_DUP() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(0x1f));//0x1f (top)
            set(2, PUSH);
            set(3, new Word(0x56));//0x1f 0x56 (top)
            set(4, DUP);//0x1f 0x56 0x56 (top)
            set(5, POP);//0x1f 0x56 (top)
            set(6, STOP_GOOD);//0x1f 0x56 |good(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(GOOD);
        assertThat(result.stack.getSize()).isEqualTo(2);
        assertThat(result.stack.popCustom(2)).containsExactly(
                new Word(0x1f), new Word(0x56)
        );
    }

    @Test
    public void test_INVOKE_fromCode() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(0x12));// 0x12 (top)
            set(2, PUSH);
            set(3, new Word(0x34));// 0x12 0x34 (top)
            set(4, PUSH);
            set(5, new Word(0x56));// 0x12 0x34 0x56 (top)
            set(6, PUSH);
            set(7, new Word(0x03));// 0x12 0x34 0x56 | 0x03 (top)
            set(8, PUSH);
            set(9, new Word(0x01));// 0x12 0x34 0x56 | 0x03 | 0x01 (top)
            set(10, INVOKE);// 0x12 0x34 0x56 0x56 0x1f 5 0x01 (top)
            set(11, STOP_GOOD); // 0x12 0x34 0x56 0x56 0x1f 0x05 0x01 |good(top)
        }};

        AccountAddress idB = new AccountAddress(new Word(0x01));
        Memory contractB = new SimpleTestMemory() {{
            // 0x12 0x34 0x56 (top)
            set(0, DUP);// 0x12 0x34 0x56 0x56 (top)
            set(1, PUSH);
            set(2, new Word(0x1f));// 0x12 0x34 0x56 0x56 0x1f (top)
            set(3, STOP_BAD);// 0x12 0x34 0x56 0x56 0x1f |bad(top)
            // 0x12 0x34 0x56 0x56 0x1f | 5 0x01 (top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));
        when(manager.accountExists(idB)).thenReturn(true);
        when(manager.getAccount(idB)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractB, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(GOOD);
        assertThat(result.stack.getSize()).isEqualTo(7);
        assertThat(result.stack.popCustom(7)).containsExactly(
                new Word(0x12), new Word(0x34), new Word(0x56),
                new Word(0x56), new Word(0x1f), new Word(0x05),
                new Word(0x01)
        );
    }

    @Test
    public void test_ADD() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(123456));// 123456 (top)
            set(2, PUSH);
            set(3, new Word(-123456));// 123456 -123456 (top)
            set(4, ADD);// 0 (top)
            set(5, STOP_GOOD);// 0 |good(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(GOOD);
        assertThat(result.stack.getSize()).isEqualTo(1);
        assertThat(result.stack.popCustom(1)).containsExactly(
                new Word(0)
        );
    }

    @Test
    public void test_SUB() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(100));// 100 (top)
            set(2, PUSH);
            set(3, new Word(10));// 100 10 (top)
            set(4, SUB);// 90 (top)
            set(5, STOP_GOOD);// 90 |good(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(GOOD);
        assertThat(result.stack.getSize()).isEqualTo(1);
        assertThat(result.stack.popCustom(1)).containsExactly(
                new Word(90)
        );
    }

    @Test
    public void test_SUB_noValues() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, SUB);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_SUB_oneValues() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(100));// 100 (top)
            set(2, SUB);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_DIV() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, PUSH);
            set(3, new Word(12));// 23 12 (top)
            set(4, DIV);// 1 (top)
            set(5, STOP_GOOD);// 1 |good(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(GOOD);
        assertThat(result.stack.getSize()).isEqualTo(1);
        assertThat(result.stack.popCustom(1)).containsExactly(
                new Word(1)
        );
    }

    @Test
    public void test_DIV_noValues() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, DIV);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_DIV_oneValue() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, DIV);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_MOD_noValues() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, MOD);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_MOD_oneValue() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, MOD);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_MOD() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, PUSH);
            set(3, new Word(12));// 23 12 (top)
            set(4, MOD);// 11 (top)
            set(5, STOP_GOOD);// 11 |good(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(GOOD);
        assertThat(result.stack.getSize()).isEqualTo(1);
        assertThat(result.stack.popCustom(1)).containsExactly(
                new Word(11)
        );
    }

    @Test
    public void test_DIV_byZero() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, PUSH);
            set(3, new Word(0));// 23 0 (top)
            set(4, DIV);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_POP_fromEmpty() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, POP);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_DUP_fromEmpty() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, DUP);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_SWAP_noValues() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, SWAP);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_SWAP_oneValue() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(12));// 12 (top)
            set(2, SWAP);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_ADD_noValues() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, ADD);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_ADD_oneValue() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(12));// 12 (top)
            set(2, SWAP);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_MOD_byZero() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, PUSH);
            set(3, new Word(0));// 23 0 (top)
            set(4, MOD);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_SWAP() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(23));// 23 (top)
            set(2, PUSH);
            set(3, new Word(43));// 23 43 (top)
            set(4, SWAP);// 43 23 (top)
            set(5, STOP_GOOD);// 43 23 |good(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(GOOD);
        assertThat(result.stack.getSize()).isEqualTo(2);
        assertThat(result.stack.popCustom(2)).containsExactly(
                new Word(43), new Word(23)
        );
    }

    @Test
    public void test_GET() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(5));// 5 (top)
            set(2, GET);// 1234 (top)
            set(3, STOP_GOOD);// 1234 |good(top)

            set(5, new Word(1234));
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(GOOD);
        assertThat(result.stack.getSize()).isEqualTo(1);
        assertThat(result.stack.popCustom(1)).containsExactly(
                new Word(1234)
        );
    }

    @Test
    public void test_GET_noValues() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, GET);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_GET_negativeValue() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(-23));// -23 (top)
            set(2, GET);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);
    }

    @Test
    public void test_PUT() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(-123456789));// -123456789 (top)
            set(2, PUSH);
            set(3, new Word(7));// -123456789 7 (top)
            set(4, PUT);// (top)
            // (7) = -123456789
            set(5, STOP_GOOD);// |good(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(GOOD);
        assertThat(result.stack.getSize()).isEqualTo(0);

        assertThat(contractA.get(0)).isEqualTo(PUSH);
        assertThat(contractA.get(1)).isEqualTo(new Word(-123456789));
        assertThat(contractA.get(2)).isEqualTo(PUSH);
        assertThat(contractA.get(3)).isEqualTo(new Word(7));
        assertThat(contractA.get(4)).isEqualTo(PUT);
        assertThat(contractA.get(5)).isEqualTo(STOP_GOOD);
        assertThat(contractA.get(6)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(7)).isEqualTo(new Word(-123456789));
        assertThat(contractA.get(8)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(8)).isEqualTo(Word.WORD_0);
    }

    @Test
    public void test_PUT_oneValue() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(-123456789));// -123456789 (top)
            set(2, PUT);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);

        assertThat(contractA.get(0)).isEqualTo(PUSH);
        assertThat(contractA.get(1)).isEqualTo(new Word(-123456789));
        assertThat(contractA.get(2)).isEqualTo(PUT);
        assertThat(contractA.get(3)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(4)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(5)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(6)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(7)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(8)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(8)).isEqualTo(Word.WORD_0);
    }

    @Test
    public void test_PUT_noValues() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUT);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);

        assertThat(contractA.get(0)).isEqualTo(PUT);
        assertThat(contractA.get(1)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(2)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(3)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(4)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(5)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(6)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(7)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(8)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(8)).isEqualTo(Word.WORD_0);
    }

    @Test
    public void test_PUT_negativeIndex() {
        AccountAddress idA = new AccountAddress(new Word(0));
        Memory contractA = new SimpleTestMemory() {{
            set(0, PUSH);
            set(1, new Word(-123456789));// -123456789 (top)
            set(2, PUSH);
            set(3, new Word(-7));// -123456789 -7 (top)
            set(4, PUT);// |revert(top)
        }};

        AccountManager manager = mock(AccountManager.class);
        when(manager.accountExists(idA)).thenReturn(true);
        when(manager.getAccount(idA)).thenReturn(new Account(
                Account.CODE_CONTROLLED, contractA, null, BigInteger.ZERO
        ));

        VirtualMachineController controller = new VirtualMachineController(manager,
                new VMConfiguration(10));

        ResultFrame result = controller.invoke(new Stack(), idA);

        assertThat(result.stopType).isEqualTo(EXCEPTION);
        assertThat(result.stack.getSize()).isEqualTo(0);

        assertThat(contractA.get(0)).isEqualTo(PUSH);
        assertThat(contractA.get(1)).isEqualTo(new Word(-123456789));
        assertThat(contractA.get(2)).isEqualTo(PUSH);
        assertThat(contractA.get(3)).isEqualTo(new Word(-7));
        assertThat(contractA.get(4)).isEqualTo(PUT);
        assertThat(contractA.get(5)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(6)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(7)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(8)).isEqualTo(Word.WORD_0);
        assertThat(contractA.get(8)).isEqualTo(Word.WORD_0);
    }

}
