A System Design Interview usually lasts for 45-60 minutes. The following template will guide you on how to manage time duration across various aspects of it -

✅ 𝐑𝐞𝐪𝐮𝐢𝐫𝐞𝐦𝐞𝐧𝐭 𝐂𝐥𝐚𝐫𝐢𝐟𝐢𝐜𝐚𝐭𝐢𝐨𝐧𝐬 - (3-5 𝐦𝐢𝐧)

Ask clarifying questions to understand the problem and expectations of the interviewer.

𝐚) 𝐅𝐮𝐧𝐜𝐭𝐢𝐨𝐧𝐚𝐥 𝐑𝐞𝐪𝐮𝐢𝐫𝐞𝐦𝐞𝐧𝐭𝐬

👉 Focused use cases to cover (MVP)  
👉 Use cases that will not be covered  
👉 Who will use the system  
👉 Total/Daily active users  
👉 How the system will be used

𝐛) 𝐍𝐨𝐧 𝐅𝐮𝐧𝐜𝐭𝐢𝐨𝐧𝐚𝐥 𝐑𝐞𝐪𝐮𝐢𝐫𝐞𝐦𝐞𝐧𝐭𝐬

👉 Is the system Highly Available or Highly Consistent? CAP theorem?  
👉 Does the system requires low latency?  
👉 Does the system needs to be reliable?

✅ 𝐄𝐬𝐭𝐢𝐦𝐚𝐭𝐢𝐨𝐧𝐬 (3-5 𝐦𝐢𝐧)

👉 Latency/Throughput expectations  
👉 QPS (Queries Per Second) Read/Write ratio  
👉 Traffic estimates  
👉 Storage estimates  
👉 Memory estimates

✅ 𝐀𝐏𝐈 𝐃𝐞𝐬𝐢𝐠𝐧 (3-5 𝐦𝐢𝐧)

👉 Outline the different APIs for required scenarios

✅ 𝐃𝐚𝐭𝐚𝐛𝐚𝐬𝐞 𝐒𝐜𝐡𝐞𝐦𝐚 𝐃𝐞𝐬𝐢𝐠𝐧 (3-5 𝐦𝐢𝐧)

```
👉 Identify the type of database (SQL or NoSQL)
👉 Design schema like tables/columns and relationships with other tables (SQL)
```

✅ 𝐒𝐲𝐬𝐭𝐞𝐦'𝐬 𝐃𝐞𝐭𝐚𝐢𝐥𝐞𝐝 𝐃𝐞𝐬𝐢𝐠𝐧 (20 - 25 𝐦𝐢𝐧)

(a) Draw/Explain high-level components of the system involving below (if required) components -

👉 Client (Mobile, Browser)  
👉 DNS  
👉 CDN  
👉 Load Balancers  
👉 Web / Application Servers  
👉 Microservices involved in fulfilling the design  
👉 Blob / Object Storage  
👉 Proxy/Reverse Proxy  
👉 Database (SQL or NoSQL)  
👉 Cache at various levels (Client side, CDN, Server side, Database side, Application level caching)  
👉 Messaging Queues for asynchronous communication

(b) Identification of **algorithm/data structures** and way to scale them

(c) **Scaling individual components** - Horizontal & Vertical Scaling

(d) **Database Partitioning** -

```
 i) Methods

    👉 Horizontal Partitioning
    👉 Vertical Partitioning
    👉 Directory-Based Partitioning

ii) Criteria    

    👉 Range-Based Partitioning
    👉 Hash-Based Partitioning (Consistent Hashing)
    👉 Round Robin
```

(e) **Replication & Redundancy** -

```
👉 Redundancy - Primary and Secondary Server
👉 Replication - Data replication from active to mirrored node/database     
```

(f) **Databases**

```
👉 SQL - Sharding, Indexes, master-slave, master-master, Denormalization
👉 NoSQL - Key-Value, Document, Wide-Column, Graph 
```

(g) Communication Protocols and standards like - IP, TCP, UDP, HTTP/S, RPC, REST, Web Sockets

✅ 𝐑𝐞𝐬𝐨𝐥𝐯𝐞 𝐛𝐨𝐭𝐭𝐥𝐞𝐧𝐞𝐜𝐤𝐬 𝐚𝐧𝐝 𝐟𝐨𝐥𝐥𝐨𝐰-𝐮𝐩 𝐪𝐮𝐞𝐬𝐭𝐢𝐨𝐧𝐬 (2-3 𝐦𝐢𝐧𝐮𝐭𝐞𝐬)