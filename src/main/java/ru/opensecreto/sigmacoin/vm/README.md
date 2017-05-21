# SVM - Sigma Virtual Machine

## Specification

### Pointers

* **StackSize** - current size of stack.

### Data structures

* **Stack** - array of 8-bit integers with length of SVM_STACK_SIZE.
* **Memory** - array of 8-bit integers with length of SVM_MEMORY_SIZE.
* **Frame** - structure consisting of **stack** and **memory**.
Frames are used to divide subroutines from main thread.
When some code is invoked, new frame is created and code is executed in it's own frame.

### Execution process

1. User or other contract creates transaction with **execution data** and `contractId`.
Execution data is simple array of bytes.
2. **Execution data** is pushed to a new stack.
First byte will be at top of stack, lat byte will be in the bottom of stack.
3. Contract with given `contractId` is invoked with stack from step 2.

### Rules

* For ints in opcodes parameters the most significant bit is always at top of stack.

### Opcodes

Name | Opcode | Description | Stack before | Stack after
--- | --- | --- | --- | ---
`STOP_BAD` | `0x00` | Stops execution of current contract. `0x01` byte is pushed to stack which means that contract stopped unseccessfully. Stack is returned to parent frame.
`STOP_GOOD` | `0x01` | Stops execution of current contract. `0x00` byte is pushed to stack which means that contract stopped seccessfully. Stack is returned to parent frame.
`INVOKE` | `0x02` | Invoke with given id and size of current stack to invoke contract with | N bytes of data. n<65536<br/>CONTRACT_ID_LENGTH bytes of contract id.<br/>16-bit N | Stack returned by invoked contract.<br/>`00` if successful and `01` otherwise
> For stack description top of stack is at right side!
