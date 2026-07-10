package DesignPatterns.Composite;

/**
 * Test driver / demo for the Org Chart Analytics exercise.
 *
 * --------------------------------------------------------------------------
 * THIS FILE IS THE SPEC. Implement the supporting classes in this same
 * folder/package so this file compiles and runs:
 *
 *   1. Employee.java               — interface OR abstract class with:
 *                                      getName(), getSalary(),
 *                                      getTeamSize(), getTotalCompensation(),
 *                                      getHighestPaid(), print(int indent)
 *   2. IndividualContributor.java  — Leaf: ctor(String name, int salary).
 *                                    getTeamSize=1, getTotalCompensation=salary,
 *                                    getHighestPaid=this.
 *   3. Manager.java                — Composite: ctor(String name, int salary)
 *                                    plus addReport(Employee). Each method
 *                                    recurses through reports.
 *
 * Until all three exist you'll get "cannot find symbol" errors — each error
 * tells you what to build next.
 * --------------------------------------------------------------------------
 *
 * The defining demonstration of Composite: the SAME analytics methods are
 * called on TWO different roots of the tree — the whole company (CTO) and
 * a sub-tree (engDirector). Same code, different scope, no special-casing.
 *
 * Tree built:
 *
 *   Alice (CTO) $250,000
 *   ├── Bob (Eng Director) $180,000
 *   │     ├── Dave (SWE)        $120,000
 *   │     ├── Eve (SWE)         $130,000
 *   │     └── Frank (Sr SWE)    $160,000
 *   └── Carol (Design Director) $170,000
 *         ├── Grace (Designer)  $110,000
 *         └── Heidi (Designer)  $105,000
 *
 * Manually computed for verification:
 *   - Total team size = 8 (CTO + 2 directors + 5 ICs)
 *   - Total comp     = 250 + 180 + 170 + 120 + 130 + 160 + 110 + 105 = 1,225,000
 *   - Highest paid   = Alice (CTO) at $250,000
 *   - Eng team size  = 4 (Bob + Dave + Eve + Frank)
 *   - Eng comp       = 180 + 120 + 130 + 160 = 590,000
 *   - Eng highest    = Bob at $180,000
 */
public class OrgChartDemo {

//    public static void main(String[] args) {
//        // Leaves
//        IndividualContributor dave    = new IndividualContributor("Dave (SWE)",        120_000);
//        IndividualContributor eve     = new IndividualContributor("Eve (SWE)",         130_000);
//        IndividualContributor frank   = new IndividualContributor("Frank (Sr SWE)",    160_000);
//        IndividualContributor grace   = new IndividualContributor("Grace (Designer)",  110_000);
//        IndividualContributor heidi   = new IndividualContributor("Heidi (Designer)",  105_000);
//
//        // Mid-level composites
//        Manager engDirector    = new Manager("Bob (Eng Director)",    180_000);
//        engDirector.addReport(dave);
//        engDirector.addReport(eve);
//        engDirector.addReport(frank);
//
//        Manager designDirector = new Manager("Carol (Design Director)", 170_000);
//        designDirector.addReport(grace);
//        designDirector.addReport(heidi);
//
//        // Root composite
//        Manager cto = new Manager("Alice (CTO)", 250_000);
//        cto.addReport(engDirector);
//        cto.addReport(designDirector);
//
//        System.out.println("=== Organization tree ===");
//        cto.print(0);
//
//        System.out.println("\n=== Analytics on the whole org ===");
//        printAnalytics("Total", cto);
//
//        // The same methods on a sub-tree — proof the recursion works at any node.
//        System.out.println("\n=== Analytics on the Eng sub-tree ===");
//        printAnalytics("Eng",   engDirector);
//    }
//
//    /** Notice: this method takes Employee — not Manager — yet works on both. */
//    private static void printAnalytics(String label, Employee root) {
//        System.out.println(label + " team size: "         + root.getTeamSize());
//        System.out.println(label + " total compensation: $" + root.getTotalCompensation());
//        Employee top = root.getHighestPaid();
//        System.out.println(label + " highest paid: " + top.getName() + " ($" + top.getSalary() + ")");
//    }
}
