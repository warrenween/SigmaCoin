# SVM - Sigma Virtual Machine

## Specification

### Pointers

* **StackSize** - current size of stack.

### Data structures

* **Stack** - array of 8-bit integers with length of SVM_STACK_SIZE.
* **Memory** - array of 8-bit integers with length of SVM_MEMORY_SIZE.
* **Frame** - sturctire, consisting of **stack** and **memory**.
Frames are used to divide subroutines from main thread.
When some code is invoked, new frame is created and code is executed in it's own frame.


