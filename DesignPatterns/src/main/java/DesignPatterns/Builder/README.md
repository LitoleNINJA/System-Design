# Builder Pattern — SQL Query Builder

## Problem

Design a fluent SQL `SELECT` query builder. Code today is full of error-prone string concatenation:

```java
String sql = "SELECT " + cols + " FROM " + table + " WHERE " + cond + " ORDER BY " + ord + " LIMIT " + n;
```

Missing spaces, forgotten commas, accidental SQL injection, no validation — all common bugs. You want callers to write this instead:

```java
SqlQuery q = new SqlQuery.Builder()
        .select("name", "age", "city")
        .from("users")
        .where("age > 18")
        .where("city = 'NYC'")
        .orderBy("name", "ASC")
        .limit(10)
        .build();

System.out.println(q);
// SELECT name, age, city FROM users WHERE age > 18 AND city = 'NYC' ORDER BY name ASC LIMIT 10
```

## Why Builder here

Builder is the right pattern when an object has:

1. **Many fields, most optional** — `SELECT`, `FROM`, `WHERE`, `ORDER BY`, `GROUP BY`, `HAVING`, `LIMIT`, `OFFSET`. A constructor with 8 parameters (many `null`) is unreadable.
2. **Cross-field validation rules** — `HAVING` is illegal without `GROUP BY`. `OFFSET` requires `LIMIT`. These rules can't be enforced in a constructor — Builder validates once at `.build()` time, after the caller has chained everything.
3. **The built object should be immutable** — once constructed, a `SqlQuery` should never change. Builder is the natural way to assemble an immutable object piece by piece.

If you only had 2 required fields and no validation, a constructor would be fine. Don't reach for Builder reflexively — reach for it when constructors get ugly.

## Requirements

### Builder methods (fluent — each returns `this`)

| Method | Signature | Required? |
|---|---|---|
| `select` | `select(String... columns)` | **Required** — at least one column |
| `from` | `from(String table)` | **Required** |
| `where` | `where(String condition)` | Optional, **callable multiple times** — joined with `AND` |
| `groupBy` | `groupBy(String... columns)` | Optional |
| `having` | `having(String condition)` | Optional, only valid if `groupBy` is set |
| `orderBy` | `orderBy(String column)` / `orderBy(String column, String direction)` | Optional |
| `limit` | `limit(int n)` | Optional, must be > 0 |
| `offset` | `offset(int n)` | Optional, requires `limit` to be set, must be >= 0 |
| `build` | `build() : SqlQuery` | Terminal — validates and constructs |

### Validation (enforced at `.build()` time, throw `IllegalStateException`)

1. `select` must have been called with at least one column.
2. `from` must have been called.
3. If `having` is set, `groupBy` must also be set.
4. If `offset` is set, `limit` must also be set.
5. `limit` must be > 0.
6. `offset` must be >= 0.

### SQL output rules

The final SQL is rendered with clauses in this fixed order, separated by single spaces:

```
SELECT {cols, ...} FROM {table} [WHERE {cond1} AND {cond2} ...] [GROUP BY {cols, ...}] [HAVING {cond}] [ORDER BY {col} {DIR}] [LIMIT {n}] [OFFSET {n}]
```

Clauses that weren't set are omitted entirely. Default `ORDER BY` direction is `ASC`.

## Classes to implement

```
SqlQuery               — immutable; toString() returns the SQL string
  SqlQuery.Builder     — fluent builder, validates at build()
```

## What you'll need to think about

- **How to store multiple `WHERE` clauses.** Use a `List<String>`, append on each call, join with `" AND "` at render time.
- **How to keep the `SqlQuery` immutable.** All fields `final`, defensive copies of lists if needed.
- **Where validation lives.** Each setter validates only its own argument (e.g., `limit > 0`). Cross-field rules (`having` requires `groupBy`) belong in `build()` — they can't be checked earlier because the caller hasn't finished chaining.
- **Should `Builder` be a static nested class or top-level?** Nested. Keeps `new SqlQuery.Builder()` syntax and the Builder has natural access to `SqlQuery`'s private constructor.

## Expected output

Run `SqlQueryDemo.main` and you should see:

```
=== Test 1: Minimal query ===
SELECT * FROM users

=== Test 2: Multiple WHEREs combine with AND ===
SELECT id, name FROM users WHERE active = true AND age > 18

=== Test 3: ORDER BY + LIMIT + OFFSET ===
SELECT name FROM users ORDER BY name ASC LIMIT 10 OFFSET 20

=== Test 4: GROUP BY + HAVING ===
SELECT city, COUNT(*) FROM users GROUP BY city HAVING COUNT(*) > 100

=== Test 5: The full kitchen sink ===
SELECT name, city FROM users WHERE active = true GROUP BY city HAVING COUNT(*) > 5 ORDER BY name DESC LIMIT 25 OFFSET 50

=== Test 6: Validation — missing FROM throws ===
Caught (expected): FROM clause is required

=== Test 7: Validation — HAVING without GROUP BY throws ===
Caught (expected): HAVING requires GROUP BY

=== Test 8: Validation — OFFSET without LIMIT throws ===
Caught (expected): OFFSET requires LIMIT
```

## The Builder mental model (one sentence)

A Builder is a **mutable, chainable construction site** that, on `.build()`, validates the assembled state and snapshots it into an **immutable** product. The caller never holds a half-built `SqlQuery` — they hold a `Builder`, and only the validated end-state escapes.
