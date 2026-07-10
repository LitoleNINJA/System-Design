package DesignPatterns.Builder;

/**
 * Test driver / demo for the SQL Query Builder exercise.
 *
 * --------------------------------------------------------------------------
 * THIS FILE IS THE SPEC. Implement the supporting class in this same
 * folder/package so this file compiles and runs:
 *
 *   1. SqlQuery.java — immutable query object. toString() returns the SQL
 *                      string. Constructed only via SqlQuery.Builder.
 *
 *   2. SqlQuery.Builder (nested static class inside SqlQuery)
 *        .select(String... columns)
 *        .from(String table)
 *        .where(String condition)              // callable multiple times
 *        .groupBy(String... columns)
 *        .having(String condition)
 *        .orderBy(String column)
 *        .orderBy(String column, String direction)
 *        .limit(int n)
 *        .offset(int n)
 *        .build() → SqlQuery                   // validates here
 *
 * Until SqlQuery exists you'll get "cannot find symbol" errors — each error
 * tells you what to build next.
 * --------------------------------------------------------------------------
 *
 * The defining demonstrations of Builder in this demo:
 *   - Tests 1–5 show fluent construction with different combinations of
 *     optional clauses. Same Builder API, different products.
 *   - Tests 6–8 prove that validation lives in .build(), not in setters.
 *     Each test chains methods that are individually fine, but the
 *     combination is illegal — and .build() is the line that throws.
 */
public class SqlQueryDemo {

//    public static void main(String[] args) {
//
//        System.out.println("=== Test 1: Minimal query ===");
//        SqlQuery q1 = new SqlQuery.Builder()
//                .select("*")
//                .from("users")
//                .build();
//        System.out.println(q1);
//
//        System.out.println("\n=== Test 2: Multiple WHEREs combine with AND ===");
//        SqlQuery q2 = new SqlQuery.Builder()
//                .select("id", "name")
//                .from("users")
//                .where("active = true")
//                .where("age > 18")
//                .build();
//        System.out.println(q2);
//
//        System.out.println("\n=== Test 3: ORDER BY + LIMIT + OFFSET ===");
//        SqlQuery q3 = new SqlQuery.Builder()
//                .select("name")
//                .from("users")
//                .orderBy("name")
//                .limit(10)
//                .offset(20)
//                .build();
//        System.out.println(q3);
//
//        System.out.println("\n=== Test 4: GROUP BY + HAVING ===");
//        SqlQuery q4 = new SqlQuery.Builder()
//                .select("city", "COUNT(*)")
//                .from("users")
//                .groupBy("city")
//                .having("COUNT(*) > 100")
//                .build();
//        System.out.println(q4);
//
//        System.out.println("\n=== Test 5: The full kitchen sink ===");
//        SqlQuery q5 = new SqlQuery.Builder()
//                .select("name", "city")
//                .from("users")
//                .where("active = true")
//                .groupBy("city")
//                .having("COUNT(*) > 5")
//                .orderBy("name", "DESC")
//                .limit(25)
//                .offset(50)
//                .build();
//        System.out.println(q5);
//
//        // ── Validation tests — each one chains a legal partial state and
//        //    then .build() throws because the combination is illegal. ──
//
//        System.out.println("\n=== Test 6: Validation - missing FROM throws ===");
//        try {
//            new SqlQuery.Builder()
//                    .select("name")
//                    .build();
//        } catch (IllegalStateException e) {
//            System.out.println("Caught (expected): " + e.getMessage());
//        }
//
//        System.out.println("\n=== Test 7: Validation - HAVING without GROUP BY throws ===");
//        try {
//            new SqlQuery.Builder()
//                    .select("name")
//                    .from("users")
//                    .having("COUNT(*) > 5")
//                    .build();
//        } catch (IllegalStateException e) {
//            System.out.println("Caught (expected): " + e.getMessage());
//        }
//
//        System.out.println("\n=== Test 8: Validation - OFFSET without LIMIT throws ===");
//        try {
//            new SqlQuery.Builder()
//                    .select("name")
//                    .from("users")
//                    .offset(10)
//                    .build();
//        } catch (IllegalStateException e) {
//            System.out.println("Caught (expected): " + e.getMessage());
//        }
//    }
}
