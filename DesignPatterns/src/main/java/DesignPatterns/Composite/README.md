# Exercise — Org Chart Analytics

> Composite pattern · LLD practice
> Tree-shaped LLD problem. Composite is the pattern you reach for whenever the *same operation* needs to work on both an individual element and a group of elements as if they were the same thing — file/folder, comment/thread, item/bundle, or in our case **individual contributor / manager**.

---

## Problem Statement

Your HR system needs to run analytics on the company's org chart. The org has the typical shape: managers have direct reports, who themselves can be managers (with their own reports), forming a tree. At the leaves are individual contributors.

Different roles in the system want different aggregate views:

- *"How many people report up to the CTO?"*
- *"What's the total compensation of the engineering team?"*
- *"Who's the highest-paid person in the marketing org?"*
- *"Print the full org chart, indented."*

Crucially, you want to call the same method (`getTeamSize`, `getTotalCompensation`, `getHighestPaid`, `print`) on **either an IC or a manager** without the caller knowing which one they have. A query about "the eng director's team" should work just like a query about "the whole company" — it's the same recursion, just rooted at a different node.

That uniformity — *treat one and many the same* — is the **Composite** pattern.

---

## Requirements

1. Define an `Employee` type (interface OR abstract class — your choice; both are valid):
   - `String getName()`
   - `int getSalary()` — own salary only
   - `int getTeamSize()` — count of self + everyone below
   - `int getTotalCompensation()` — sum of own salary + all subordinates
   - `Employee getHighestPaid()` — return the single highest-paid `Employee` in the (sub)tree, including self
   - `void print(int indent)` — pretty-print self and recurse into children with `indent + 2` spaces

2. Implement `IndividualContributor` (the **Leaf**):
   - Has `name`, `salary`. No reports.
   - `getTeamSize()` → `1`
   - `getTotalCompensation()` → `salary`
   - `getHighestPaid()` → `this`
   - `print(indent)` → indent + name + salary

3. Implement `Manager` (the **Composite**):
   - Has `name`, `salary`, and `List<Employee> reports`.
   - `addReport(Employee e)` — adds a report. Adding the same instance twice should be a no-op (or at least not duplicate output).
   - `getTeamSize()` → `1 + sum of report.getTeamSize() for all reports`
   - `getTotalCompensation()` → `salary + sum of report.getTotalCompensation()`
   - `getHighestPaid()` → recursively compare self against the highest-paid of each report
   - `print(indent)` → print self at `indent`, then recurse into each report at `indent + 2`

4. The same analytics methods called on a `Manager` mid-tree must work identically — querying `engDirector.getTeamSize()` returns just the eng team, querying `cto.getTeamSize()` returns the whole company. **Same code, different root** — that's the win.

5. The demo's `main` (provided) builds a 3-level tree and runs all four queries against (a) the root and (b) a sub-tree, proving the recursion works at any node.

---

## Class Hints

```java
// Component — interface OR abstract class:
interface Employee {
    String   getName();
    int      getSalary();
    int      getTeamSize();
    int      getTotalCompensation();
    Employee getHighestPaid();
    void     print(int indent);
}

// Leaf:
class IndividualContributor implements Employee {
    private final String name;
    private final int    salary;
    // returns 1, salary, this, indented println, respectively
}

// Composite:
class Manager implements Employee {
    private final String name;
    private final int    salary;
    private final List<Employee> reports = new ArrayList<>();
    public void addReport(Employee e) { ... }
    // each method recurses through `reports`
}

class OrgChartDemo { public static void main(String[] args) { ... } }
```

> 💡 **Structural fingerprint of Composite:** Leaf and Composite **both implement the same interface**, AND the Composite holds a `List<Component>` of children that may themselves be Leaf *or* Composite. Recursion bottoms out at the leaves; every operation is "self + delegate to children."

---

## Expected Output

```
=== Organization tree ===
Alice (CTO) ($250000)
  Bob (Eng Director) ($180000)
    Dave (SWE) ($120000)
    Eve (SWE) ($130000)
    Frank (Sr SWE) ($160000)
  Carol (Design Director) ($170000)
    Grace (Designer) ($110000)
    Heidi (Designer) ($105000)

=== Analytics on the whole org ===
Total team size: 8
Total compensation: $1225000
Highest paid: Alice (CTO) ($250000)

=== Analytics on the Eng sub-tree ===
Eng team size: 4
Eng total compensation: $590000
Eng highest paid: Bob (Eng Director) ($180000)
```

The two analytics blocks call **the exact same methods** — once on `cto`, once on `engDirector`. That's the proof: same operation, different roots, different scopes, no special-casing.

---

## What the Interviewer is Looking For

- **Both Leaf and Composite implement the same `Employee` interface.** Client code (the analytics methods, the print method) doesn't care which one it has.
- **The Composite holds `List<Employee>`** — not `List<IndividualContributor>` or `List<Manager>`. That's how a Manager can have other Managers as reports, recursing arbitrarily deep.
- **Recursion is "self + children":** every Composite operation does its own work, then delegates to each child by calling the same method.
- **Recursion bottoms out at the leaves** naturally — IC's `getTeamSize` returns 1, IC's `getHighestPaid` returns `this`. No special "is this a leaf?" checks.
- **Open/Closed:** adding `Contractor` (another type of leaf) or `Department` (another type of composite) is one new class — zero edits to existing code.

---

## Composite vs Things It Gets Confused With

| vs | Distinction |
|---|---|
| **Composite vs Decorator** | Both involve wrapping/holding objects of a common interface. **Decorator** wraps *one* inner object and adds behavior to its operations. **Composite** holds *many* children and aggregates over them. Decorator: `LoggingMiddleware(handler)` — one wrapped thing. Composite: `Manager(reports)` — many children. |
| **Composite vs Recursive data structures (e.g., a plain Tree<T>)** | A generic `TreeNode<T>` with a list of children is just a data structure. **Composite** is the pattern where the *operations themselves* are polymorphic on the node type — `Leaf.getSize()` and `Composite.getSize()` both implement `Component.getSize()`. The "operation polymorphism" is the pattern. |
| **Composite vs Visitor** | Composite puts operations *on the nodes themselves* (`employee.getTotalCompensation()`). **Visitor** moves operations *out* into a separate visitor class so you can add new operations without modifying the nodes. Use Visitor when the node hierarchy is stable but operations grow. |

---

## The "Transparency vs Safety" Design Decision

This is **the** classic Composite probe. The original GoF book recommended *transparency*: put `addReport` / `removeReport` on the `Employee` interface so callers don't need to know whether they have an IC or a Manager. The downside: ICs would have to throw `UnsupportedOperationException` on `addReport`. That's the Liskov Substitution Principle violation — a Leaf implementing a method it can't actually do.

Modern Java preference is *safety*: put `addReport` only on `Manager`. The trade-off: the caller building the tree needs the concrete type, but reads through the `Employee` interface stay clean.

**For this exercise, use safety** — `addReport` is a `Manager`-only method. If asked in an interview, name both options and explain why you picked safety.

---

## How to Attempt This Cold

Suggested order — write small, run often:

1. `Employee.java` — the interface (or abstract class).
2. `IndividualContributor.java` — easy: most methods are one-liners.
3. ✅ **Run with just an IC tree** (`new IndividualContributor(...).getTotalCompensation()`) — verify it compiles and prints.
4. `Manager.java` — the recursive composite. Each method follows the template:
   ```java
   public int getTotalCompensation() {
       int total = salary;
       for (Employee r : reports) total += r.getTotalCompensation();
       return total;
   }
   ```
5. Run the full demo. Compare against expected output.

A pitfall to watch:
- **`getHighestPaid` is the trickiest method.** It must compare *self* against the highest-paid in each subtree, not just delegate. In `Manager`:
  ```java
  Employee best = this;                                // start with self
  for (Employee r : reports) {
      Employee candidate = r.getHighestPaid();         // recurse — gets the best in that subtree
      if (candidate.getSalary() > best.getSalary()) {
          best = candidate;
      }
  }
  return best;
  ```
  Many candidates skip the `Employee best = this;` line and return whoever's highest *among reports* — which would miss a highly-paid manager like the CTO. Watch for it.

- **`print(indent)` formatting** — for indent=0, prints with no leading spaces; for indent=2, two spaces; etc. A simple way:
  ```java
  System.out.println(" ".repeat(indent) + name + " ($" + salary + ")");
  ```

---

## Files in this Exercise

| File | Status | Role |
|------|--------|------|
| `Employee.java`               | 🛠 your job | The Component (interface or abstract class) |
| `IndividualContributor.java`  | 🛠 your job | The Leaf |
| `Manager.java`                | 🛠 your job | The Composite — holds `List<Employee> reports` |
| `OrgChartDemo.java`           | ✅ provided | Client / `main` — the test contract |
