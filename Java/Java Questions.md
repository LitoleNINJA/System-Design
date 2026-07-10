## JVM Memory & Threads

### JVM Stack Size
- Default ~**1MB per thread** on 64-bit (OS/JVM dependent)
- Configured via **`-Xss`** flag (e.g., `-Xss512k`)
- **Per-thread**, stores stack frames (locals, args, return addresses)
- Exceeding → **`StackOverflowError`**
- Too many threads × large `-Xss` → **`OutOfMemoryError: unable to create new native thread`**
- Tradeoff: smaller stack = more threads possible, less recursion depth

### Stack vs Heap (per thread vs shared)
- **Stack** — per thread, private, thread-safe by design, holds locals + frames
- **Heap** — shared across all threads, holds objects, requires synchronization
- **Metaspace** (Java 8+, replaces PermGen) — class metadata, shared
- Object reference: reference on stack, actual object on heap

### Thread vs Process Memory Layout
- New thread does **NOT** create new process layout
- OS carves new **stack region** within same process address space (~1MB)
- Code, heap, data segments **shared** with sibling threads
- Each thread gets own registers + stack pointer + program counter
- Process: own address space; Thread: shared address space, own stack

---

## OOP & Polymorphism

### Static vs Dynamic Polymorphism
- **Static (compile-time)** — method **overloading**, early binding
- **Dynamic (runtime)** — method **overriding**, late binding via vtable
- Java methods are **virtual by default** (unlike C++)
- `static`, `final`, `private` methods use static binding
- "Overriding static methods" is actually **method hiding**

### Covariant Return Types (Override with Subtype)
- Override can return **subtype** of parent's return (Java 5+)
- `Number → Integer` ✅
- `Number → int` ❌ (primitives not in class hierarchy)
- Autoboxing doesn't apply to override resolution
- Reverse (subtype → supertype) **not allowed** for return types

### Override Exception Rules
- Override can throw: **same**, **subclass**, **fewer**, or **no** checked exceptions
- Cannot throw **broader** or **new** checked exception
- **Unchecked** exceptions can be thrown freely
- Same principle as covariant returns — both follow **LSP**
- Parent throws `IOException`, child throws `Exception` ❌
- Parent throws `Exception`, child throws `IOException` ✅

---

## Object Copying

### Shallow vs Deep Copy
- **Shallow** — copies object, nested references **shared** (default `Object.clone()`)
- **Deep** — recursively copies everything, fully independent
- Ways to deep copy: copy constructor (recommended), `clone()` override, serialization
- `String` immutable → shallow safe enough for Strings
- **Records** and immutable objects sidestep the problem
- **prefer copy constructors over `Cloneable`**

---

## Design Patterns — Singleton

### Singleton Implementations (preference order)
1. **Enum Singleton** (best) — JVM handles thread-safety, serialization, reflection
2. **Bill Pugh / Static Inner Class** — lazy, thread-safe via classloader
3. **Double-Checked Locking** — needs `volatile`! prevents partial-construction reordering
4. **Eager Init** — simple, thread-safe, not lazy

### Required for Singleton
- Private constructor
- Static instance field
- Public static `getInstance()`

### Ways to Break Singleton & Defenses
- **Reflection** → `setAccessible(true)` on private constructor → defend with throw in constructor
- **Serialization** → deserialization creates new instance → implement `readResolve()`
- **Cloning** → `clone()` creates new → override to throw `CloneNotSupportedException`
- **Multiple Classloaders** → each loads own copy → use enum or single classloader
- **Thread race condition** → DCL with volatile / Bill Pugh / enum

### Why Enum Singleton is Bulletproof
- Reflection-proof (JVM forbids reflective enum instantiation)
- Serialization-safe (JVM guarantees one instance per constant)
- Cloning-safe (enums can't be cloned)
- Thread-safe via class loading

---

## Concurrency

### `synchronized` at Method Level
- Instance method → locks on **`this`**
- Static method → locks on **`ClassName.class`**
- Provides: mutual exclusion + happens-before visibility + atomicity
- Pitfalls: different locks ≠ thread-safe; locking `this` exposes lock; coarse-grained locking hurts performance
- Better: use **private final lock object** or `synchronized` block on critical section
- **Reentrant** — same thread can re-acquire

### Modern Concurrency Alternatives
- Counter → `AtomicInteger`, `LongAdder`
- Map → `ConcurrentHashMap`
- Read-heavy → `ReadWriteLock`, `StampedLock`
- Fine control → `ReentrantLock` (tryLock, fairness, interruptible)
- Single-var visibility → `volatile`

### Executor Framework
- Decouples task submission from execution
- **`Executor`** → `execute(Runnable)` only
- **`ExecutorService`** → adds lifecycle + Future-returning submit
- **`ScheduledExecutorService`** → delayed/periodic tasks
- **`Runnable`** — no return, no checked exception
- **`Callable<V>`** — returns value, throws checked exception → `Future<V>`

### Thread Pool Types (`Executors` factory)
- `newFixedThreadPool(n)` — fixed threads, **unbounded queue** ⚠️
- `newCachedThreadPool()` — on-demand, **unbounded threads** ⚠️
- `newSingleThreadExecutor()` — sequential, single thread
- `newScheduledThreadPool(n)` — for scheduling
- `newWorkStealingPool()` — `ForkJoinPool`, idle steals from busy

### Production Recommendation
- **Avoid `Executors` factories** — unbounded queues/threads risk OOM
- Use **`ThreadPoolExecutor`** directly — control core/max pool, bounded queue, thread factory, rejection policy

### Lifecycle
- `shutdown()` — graceful, completes queued tasks, rejects new
- `shutdownNow()` — interrupt running, return pending
- Java 19+: `ExecutorService` is `AutoCloseable` → try-with-resources

### Future vs CompletableFuture
- `Future.get()` blocks, no chaining
- **`CompletableFuture`** (Java 8) — non-blocking composition (`thenApply`, `thenCompose`, `allOf`, `anyOf`)

### Virtual Threads (Java 21)
- Lightweight, JVM-managed, millions of threads possible
- `Executors.newVirtualThreadPerTaskExecutor()`
- Weakens classic thread pool argument for I/O-bound work

### `submit()` vs `execute()`
- `execute()` — Runnable only, void, exceptions go to uncaught handler
- `submit()` — Runnable/Callable, returns Future, captures exceptions

### Make `main` Finish Last
- **`thread.join()`** — main blocks until thread completes
- Executor: `shutdown()` + `awaitTermination(timeout)`
- Other tools: `CountDownLatch`, `CyclicBarrier`, `CompletableFuture.allOf().join()`
- **Daemon threads** — JVM kills them when all user threads finish; main exits early without `join()`

---

## Functional Programming

### Lambdas & Functional Interfaces
- **Functional interface** = exactly one abstract method (SAM)
- `@FunctionalInterface` enforces at compile-time (optional)
- Lambda is **shorthand** for implementing the SAM
- Compiler infers target type from context
- Examples: `Runnable`, `Comparator`, `Function`, `Predicate`, `Consumer`, `Supplier`
- Implemented via `invokedynamic` + `LambdaMetafactory` (no extra `.class` file)
- Method references (`String::length`) also need functional interface target

### Optional
- Represents possible **absence of value** at API level
- Creation: `Optional.of`, `Optional.ofNullable`, `Optional.empty`
- Use: `orElse`, `orElseGet`, `orElseThrow`, `ifPresent`, `map`, `flatMap`, `filter`
- **Avoid `.get()`** without checking
- **Best as return type**, not field/parameter/collection wrapper
- Don't wrap collections — return empty list instead

---

## Strings

### String vs StringBuilder vs StringBuffer
- **String** — immutable, thread-safe, slow concat, in String pool
- **StringBuilder** — mutable, **not** thread-safe, fastest, default for building
- **StringBuffer** — mutable, thread-safe (synchronized), slower, legacy

### Why String is Immutable
- String pool optimization
- Safe HashMap keys (hashCode never changes)
- Security (file paths, classloader names)
- Thread safety
- Compiler optimization: `"a"+"b"+"c"` uses StringBuilder internally

---

## Number Types

### Java Numeric Types
- **Primitives**: `byte`(1), `short`(2), `int`(4), `long`(8), `float`(4), `double`(8)
- All Java numbers are **signed**
- `float`/`double` use **IEEE 754** → `0.1+0.2 != 0.3`
- Integer overflow **silently wraps** — use `Math.addExact()` for safety
- Wrappers: `Integer`, `Long`, etc. — for collections, autoboxing
- **Integer cache: -128 to 127** (== works in this range only)
- **`BigInteger`** — arbitrary precision integer
- **`BigDecimal`** — exact decimal (use for **money**)

### Division by Zero
- `int / 0` → **`ArithmeticException`** (runtime)
- `double / 0` → `Infinity` (no exception, IEEE 754)
- `0.0 / 0.0` → `NaN`
- `int % 0` → ArithmeticException
- `ArithmeticException` is unchecked

---

## Immutable Classes

### 5 Rules to Make a Class Immutable
1. Declare class **`final`**
2. All fields **`private final`**
3. **No setters**
4. Initialize via constructor only
5. **Defensive copies** for mutable fields (in constructor + getters)

### Modern: Records (Java 14+)
- `public record Employee(String name, int age) {}`
- Auto-generates: constructor, getters, `equals`, `hashCode`, `toString`
- Final by default
- Compact constructor for defensive copies of mutable components

### Why Immutability Matters
- Thread-safe by default
- Safe HashMap keys
- Safe to share/cache
- Easier to reason about

---

## Exceptions

### Hierarchy
```
Throwable (class)
├── Error (unchecked, don't catch — OOM, StackOverflow)
└── Exception (class)
    ├── RuntimeException (unchecked) — NPE, IAE, ArithmeticException
    └── (checked) — IOException, SQLException, ClassNotFoundException
```

### Throwable
- **Class**, root of all throwable types
- State: message, cause (chaining), stack trace, suppressed exceptions
- Methods: `getMessage`, `getCause`, `getStackTrace`, `printStackTrace`, `addSuppressed`
- **Throwable itself is checked** by compiler
- Catching `Throwable` discouraged — masks Errors

### Exception Class or Interface?
- **Class**, not interface
- Needs state (message, cause, stack trace) and behavior — interfaces can't have instance fields
- Custom: `extends Exception` (checked) or `extends RuntimeException` (unchecked)

### Try-With-Resources Rules
- Resource must implement **`AutoCloseable`**
- **Primitives don't qualify** — `try(int a = 5/0)` won't compile

### Catch Reachability Rule
- **Checked exceptions** — try body must be capable of throwing it
- **`RuntimeException`/`Exception`** — always allowed (no reachability check)
- `catch(IOException)` with no I/O in try → compile error: "exception is never thrown"

---

## Collections

### HashMap vs Hashtable
| | HashMap | Hashtable |
|---|---|---|
| Thread-safe | No | Yes (synchronized) |
| Null key/value | 1 null key, many null values | None |
| Performance | Fast | Slow (global lock) |
| Java version | 1.2 | 1.0 (legacy) |
| Java 8 treeification | Yes | No |
| Inheritance | AbstractMap | Dictionary |

- For thread safety: **`ConcurrentHashMap`** (fine-grained locking, faster than Hashtable)
- Avoid Hashtable in new code

### HashMap vs TreeMap
| | HashMap | TreeMap |
|---|---|---|
| Structure | Hash table | Red-black tree |
| Order | None | Sorted by key |
| Lookup | O(1) avg | O(log n) |
| Null keys | 1 allowed | Not allowed |
| Range queries | No | Yes |

- Use TreeMap for: sorted iteration, **range queries**, `floorKey`, `ceilingKey`, leaderboards, IP routing
- **LinkedHashMap** — insertion order, O(1), good for LRU caches

### Java 8 HashMap Treeification
- Bucket chain > 8 entries + table large enough → converts to **red-black tree**
- Worst case lookup: O(n) → O(log n)

### Why Collection Doesn't Extend Cloneable/Serializable
- **Cloneable is broken** (Joshua Bloch) — use copy constructors
- Serialization is **implementation detail** — not all collections can be serialized (DB cursors, streams)
- Serialization locks internal representation, has security implications
- Interface should describe **behavior**, not representation
- Concrete classes (`ArrayList`, `HashMap`) implement these individually

### Fail-Fast vs Fail-Safe
- **Fail-fast** — throws `ConcurrentModificationException` on structural modification during iteration
  - Uses internal `modCount` field
  - `ArrayList`, `HashMap`, `HashSet`
  - Only `iterator.remove()` is safe; or use `removeIf()`
- **Fail-safe** — iterates over snapshot/copy, no exception
  - `CopyOnWriteArrayList`, `CopyOnWriteArraySet`
  - **`ConcurrentHashMap`** — technically "weakly consistent"
  - Tradeoff: memory overhead, may not see latest changes

---

## Comparator & Comparable

### Comparator is an **Interface**
- Functional interface in `java.util`
- One abstract method: `compare(T o1, T o2)`
- Default/static helpers: `comparing`, `thenComparing`, `reversed`, `naturalOrder`, `nullsFirst`

### Comparable vs Comparator
| | Comparable | Comparator |
|---|---|---|
| Method | `compareTo(other)` | `compare(o1, o2)` |
| Defined where | Inside class | External |
| Purpose | **Natural ordering** | Custom/external |
| Count | One per class | Many possible |
| Package | `java.lang` | `java.util` |

### Composition
```java
Comparator.comparing(Employee::getDept)
    .thenComparing(Employee::getSalary, reverseOrder())
    .thenComparing(Employee::getName);
```

### Avoid `o1.getAge() - o2.getAge()`
- Risk of **integer overflow**
- Use `Integer.compare(a, b)` or `Comparator.comparingInt(...)`

---

## == vs equals()

- `==` — operator, **reference identity** for objects, value for primitives
- `.equals()` — method, default = identity, override for **logical equality**
- **String pool trap**: literals share reference, `"a" == "a"` is true
- **Integer cache trap**: `Integer` cached -128 to 127, `==` works only in range
- **Null safety**: use `Objects.equals(a, b)`
- **Enums**: `==` is idiomatic and null-safe
- Always override `hashCode()` when overriding `equals()` — required for hash collections

### equals() Contract
- Reflexive, symmetric, transitive, consistent, null check (returns false)

---

## Stream API

### Filter Pattern
```java
employees.stream()
    .filter(e -> e.getAge() == 40)
    .toList();   // Java 16+; or .collect(Collectors.toList())
```

### Common Stream Operations
- `filter` — intermediate, lazy
- `map`, `flatMap` — transform
- `sorted(Comparator)` — sort
- `collect`, `toList` — terminal
- `count`, `findFirst`, `findAny` — terminal
- `groupingBy`, `partitioningBy` — grouping collectors

### Stream Notes
- **Single-use** — once consumed, can't reuse
- **`Collectors.toList()`** returns mutable ArrayList; **`Stream.toList()`** (Java 16+) returns unmodifiable
- `parallelStream()` — only for large + CPU-bound workloads

### Sort Dates Descending (`dd-MM-yyyy`)
- Parse to `LocalDate` first; never sort date strings lexicographically (unless ISO `yyyy-MM-dd`)
- `DateTimeFormatter` is thread-safe (unlike `SimpleDateFormat`)

### Print Object
- Default `toString()` from Object → `ClassName@hashHex`
- Override `toString()` for meaningful output
- Lombok `@ToString`, IDE generation, or **records** for free
- `println(obj)` calls `String.valueOf(obj)` → `obj.toString()`
- Null field prints as `"null"` (no NPE in concat)

---

## Spring Boot

### Mapping UI Data to REST Endpoint
| UI sends data via | Annotation |
|---|---|
| URL path | `@PathVariable` |
| Query string | `@RequestParam` |
| JSON body | `@RequestBody` |
| HTTP header | `@RequestHeader` |
| Form fields | `@RequestParam` / `@ModelAttribute` |
| Cookie | `@CookieValue` |

### React → Java Object Flow
- React: `JSON.stringify(obj)` + `Content-Type: application/json` header
- Spring: `@RequestBody DTO` — Jackson deserializes
- **CORS**: `@CrossOrigin` annotation or global `WebMvcConfigurer`
- **Field mismatch**: `@JsonProperty("emp_id")` or global `SNAKE_CASE` strategy
- **Validation**: `@Valid` + Bean Validation annotations (`@NotNull`, `@NotBlank`, `@Positive`)

### Resolving @Autowired Ambiguity (Multiple Implementations)
1. **`@Qualifier("name")`** — explicit, most common
2. Field name matches bean name — fragile fallback
3. **`@Primary`** — for default implementation
4. **Constructor injection with `@Qualifier`** — recommended modern style
5. Custom `@Qualifier` annotation — type-safe
6. Inject `List<Vehicle>` or `Map<String, Vehicle>` — for strategy pattern

### Why Constructor Injection > Field Injection
- Explicit dependencies, allows `final`, immutable, testable without Spring/reflection

---

## SQL & PL/SQL

### Second Highest Salary
**Best answer — `DENSE_RANK()`:**
```sql
SELECT salary FROM (
    SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) AS rnk
    FROM employees
) WHERE rnk = 2;
```
- **`DENSE_RANK`** handles ties correctly (1, 2, 2, 3) — no skips
- **`RANK`** skips numbers on ties (1, 2, 2, 4) — wrong for distinct salary
- **`ROW_NUMBER`** — arbitrary tie-breaking
- Per department: add `PARTITION BY dept_id`
- Generalizes to Nth highest: `WHERE rnk = N`

**Simpler subquery:**
```sql
SELECT MAX(salary) FROM employees 
WHERE salary < (SELECT MAX(salary) FROM employees);
```

### PL/SQL Debugging Workflow
1. Reproduce reliably; capture exact inputs + session state
2. Read error stack — innermost line number first
3. Use `DBMS_UTILITY.FORMAT_ERROR_BACKTRACE` for full stack
4. **`DBMS_OUTPUT.PUT_LINE`** for dev only
5. **Logging table with `PRAGMA AUTONOMOUS_TRANSACTION`** for production
6. SQL Developer graphical debugger (set breakpoints)
7. `USER_ERRORS` / `SHOW ERRORS` for compile issues
8. `EXPLAIN PLAN` + `DBMS_XPLAN.DISPLAY` for performance
9. Check NLS settings, bind types
10. SQL Trace + `tkprof` for deep diagnostics

### `DBMS_OUTPUT.PUT_LINE` Performance
- **Server-side memory buffer** in PGA — flushed only after procedure completes
- Not streamed; fills memory in long loops
- 10-30% runtime overhead from string concat + function calls
- **Buffer overflow**: `ORA-20000: ORU-10027`
- **Invisible in production** — pays cost for zero benefit

### Production Logging Alternatives
- **Logging table + autonomous transaction** — survives rollback, queryable
- **Conditional/level-based logging** — flag-controlled
- **Sampled logging** — every Nth row
- **`DBMS_APPLICATION_INFO`** — visible in `V$SESSION`, near-zero overhead
- **`UTL_FILE`** — server-side files for high volume

### Common PL/SQL Bug Patterns
- `NO_DATA_FOUND` from `SELECT INTO` — wrap in EXCEPTION
- `TOO_MANY_ROWS` from `SELECT INTO` — use cursor
- NULL comparison — use `IS NULL`, not `= NULL`
- Implicit type conversion edge cases
- Cursor not closed → ORA-01000
- Commit/rollback inside loops breaking transactions
- Off-by-one in `FOR i IN 1..n`
- Trigger recursion

---

## Quick Reference: Common Compile/Runtime Gotchas

| Code | Result |
|---|---|
| `int a = 5/0` | Compiles, throws `ArithmeticException` runtime |
| `double a = 5.0/0` | `Infinity`, no exception |
| `try(int a = ...)` | Compile error — needs `AutoCloseable` |
| `catch(IOException)` with no I/O in try | Compile error — unreachable |
| `catch(RuntimeException)` always | Allowed |
| `Integer a=200, b=200; a==b` | **false** (outside cache) |
| `String s1="hi", s2="hi"; s1==s2` | **true** (String pool) |
| `new String("hi") == new String("hi")` | **false** |

---

## Top Things to Remember

- **Override `equals` + `hashCode` together**
- **Use `BigDecimal` for money**
- **Constructor injection > field injection**
- **`StringBuilder` is the default, not `StringBuffer`**
- **Avoid `Executors` factories in production — use `ThreadPoolExecutor`**
- **Enum singleton is bulletproof**
- **`DENSE_RANK` for Nth highest, not `RANK`**
- **Records (Java 14+) for free immutability**
- **`Objects.equals()` for null-safe comparison**
- **Defensive copies for mutable fields in immutable classes**
- **`@Qualifier` to resolve `@Autowired` ambiguity**

|Category|Cause|Recoverable?|Preventable?|Compiler enforces handling?|
|---|---|---|---|---|
|**`Error`**|JVM internals|No|No|No (don't catch)|
|**Checked `Exception`**|External world|Yes|No|**Yes**|
|**`RuntimeException`**|Programmer bug|Maybe|Yes|No|

