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
First byte will be in bottom of stack, last byte will be on top of stack.
3. Contract with given `contractId` is invoked with stack from step 2.


