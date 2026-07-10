- A function that can pause itself and be resumed later.
- There is **no** `coroutine` keyword. The compiler infers it from the body.
  ```cpp
	    // This is a regular function
		int add(int a, int b) { return a + b; }
		
		// This is a coroutine — because it uses co_yield
		Generator count_up(int n) {
		    for (int i = 0; i < n; ++i) {
		        co_yield i;  // pause, hand out i, resume later
		    }
		}
  ```
- The return type of a coroutine is _not_ the value it yields — it's a **handle object** that lets you interact with the coroutine. You have to define this yourself.
- A coroutine can't live on the **stack**, because the stack unwinds when you suspend. 
  The compiler allocates a coroutine frame on the **heap**.
- 