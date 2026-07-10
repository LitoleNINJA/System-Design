## Context
- Sharding enables infinite data capacity but introduces challenges with data truth and consistency.
- The database evolves from a single box to a distributed swarm of machines.

## The Fundamentals
- CAP stands for **Consistency**, **Availability**, and **Partition Tolerance**.
- **Consistency** (C): Every read returns the most recent write or an error (linearizability).
- **Availability** (A): Every request receives a non-error response, but data might be stale.
- **Partition tolerance** (P): System continues to operate despite network failures or message loss.
- The common "pick two" rule (CA, CP, AP) is misleading because partition tolerance is mandatory in distributed systems.
- Real choice during network partitions: prioritize Consistency (**CP**) or Availability (**AP**).

## CAP Trade-offs and Real-World Examples
- CP systems (banks, stock exchanges) favor truth over uptime; may reject requests during partitions.
- AP systems (Facebook, Instagram) favor availability; tolerate temporary inconsistencies for responsiveness.
- Network latency and failures make partitions inevitable; the speed of light and hardware limits enforce these trade-offs.

## Beyond CAP: PACELC Theorem
- PACELC extends CAP by considering latency-consistency trade-offs when no partition occurs.
- Even in healthy networks, engineers must choose between latency (speed) and consistency (accuracy).
- Example: synchronous replication (slow but safe) vs. asynchronous replication (fast but risky).
- Consistency is a spectrum, with models like strong consistency, eventual consistency, and intermediate guarantees.

## Consistency Models and Conflict Resolution
- **Eventual consistency** guarantees all nodes converge to the same value if no new updates occur.
- Intermediate models improve user experience:
  - **Read-your-writes consistency**: users see their latest changes immediately.
  - **Monotonic reads**: data version never goes backward.
  - **Causal consistency:** maintains cause-effect order of operations.
- Conflict resolution strategies:
  - Last Write Wins (LWW): simple but risky due to clock skew.
  - Vector clocks: track causality and concurrent versions; conflict pushed to application.
  - CRDTs: mathematically ensure conflict-free merges without central coordination.

## Designing for Availability and Risks
- High availability (99.999%) demands self-healing systems to avoid downtime.
- Risks include split-brain scenarios where network delays cause cluster partitions and data divergence.
- Choosing availability requires balancing uptime versus potential data corruption.
- Systems must be designed with empathy for users and business needs, assessing value versus volume data and tolerance for inconsistency.

## Final Thoughts
- No universally best database; choice depends on use case:
  - **Postgres for strong consistency needs.**
  - **Cassandra or DynamoDB for high availability and partition tolerance**.