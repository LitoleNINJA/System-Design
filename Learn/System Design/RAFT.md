## 📚 Introduction to Distributed Consensus
- Distributed systems face the CAP theorem tradeoff: availability vs. consistency when networks fail.
- Ensuring consistency (truth) in unreliable, independent machines is challenging.
- Single machine truth is simple but a single point of failure; distributed consensus solves this by using multiple nodes.
- Communication is unreliable: latency, network partitions, and node failures complicate agreement.
- The problem of distributed consensus is one of the hardest in computer science.

## ⚖️ The Raft Consensus Algorithm
- Raft was introduced to simplify consensus, replacing the complex Paxos.
- Uses a leader-follower model: one leader handles writes, followers replicate logs.
- Leader election is automatic, avoiding manual intervention.
- Nodes have three states: follower, candidate, leader.
- Leaders send heartbeats; followers use randomized timeouts to detect leader failure and trigger elections.
- Logical time (terms) prevents outdated leaders from issuing commands.

## 🔄 Leader Election and Quorum
- When no heartbeat is received, followers start an election with randomized timers.
- Candidates request votes from other nodes; votes are granted if conditions are met.
- A quorum (majority) is required to elect a leader, ensuring only one leader per term.
- Quorum sizes and odd numbers of nodes optimize fault tolerance and performance.

## 💾 Data Replication and Consistency
- Changes are stored in a replicated log, initially uncommitted.
- Leader sends append entries to followers; waits for quorum acknowledgements.
- Data is committed only after replication to a majority, achieving strong consistency.
- Ensures no data loss if leader fails after committing.

## 🛠️ Handling Failures and Network Partitions
- Leader failure triggers quick re-election by followers.
- Network partitions allow only majority partitions to commit changes; minority partitions remain unavailable, preserving consistency.
- Old leaders step down when they detect newer terms.
- Raft clusters typically use 3 or 5 nodes for a balance of fault tolerance and latency.

## 🌍 Real-World Applications and Limitations
- Implementing Raft is complex; widely used distributed coordination services include etcd (Kubernetes), Zookeeper, and Kafka's internal Raft.
- Google Spanner uses Paxos-based consensus for global sharding.
- Consensus is slower than single-node databases due to network and disk synchronization overhead.
- Raft is ideal for metadata and critical configuration, not high-throughput user data.
- Larger systems consist of many small consensus groups working together.

## 🚀 Summary and Next Steps
- Distributed coordination creates a single source of truth from unreliable nodes.
- Key mechanisms: leader election, logical terms, heartbeats, timeouts, quorum, and replicated logs.
- These principles keep large-scale distributed systems consistent and self-healing.
- Upcoming topics include performance optimization, caching, and cache invalidation challenges.


# Mind Map Outline

## 📚 Introduction to Distributed Consensus
- CAP theorem: consistency vs. availability tradeoff
- Importance of truth and consistency in critical systems
- Challenges of unreliable machines and network issues
- The complexity of consensus in distributed systems

## ⚖️ Raft Consensus Algorithm Basics
- Raft as a simpler alternative to Paxos
- Leader-follower model explained
- Node states: follower, candidate, leader
- Heartbeats and randomized timeouts
- Logical time and term numbers to order events

## 🔄 Leader Election and Quorum Mechanism
- Automatic leader election process
- Randomized timeout preventing vote collisions
- Voting rules and conditions for granting votes
- Quorum definition and importance
- Why odd number of nodes are preferred

## 💾 Data Replication and Commit Process
- Replicated log concept
- Uncommitted vs. committed entries
- Append entries and follower acknowledgements
- Committing data only after quorum replication
- Ensuring durability and consistency

## 🛠️ Failure Handling and Network Partitioning
- Leader failure detection and re-election
- Split-brain prevention via quorum rules
- Old leader stepping down when superseded
- Minority partition unavailability to preserve consistency
- Impact on system availability and user requests

## 🌍 Practical Use Cases and Limitations
- Popular Raft-based systems: etcd, Kubernetes, Kafka (Craft), Zookeeper (Zab)
- Google Spanner’s use of Paxos-based consensus
- Performance tradeoffs: latency, throughput, leader bottleneck
- Use cases: metadata and critical configuration data
- Avoiding Raft for high-volume, eventually consistent data

## 🚀 Summary and Future Topics
- Core principles of distributed coordination
- Importance of leader election, logical time, heartbeats, quorum, and replicated logs
- How consensus underpins large distributed architectures
- Preview of upcoming focus on performance, caching, and cache invalidation