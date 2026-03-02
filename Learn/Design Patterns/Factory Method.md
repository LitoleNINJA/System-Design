## Intent

Separate object construction from the core logic of a class. Provide an interface for creating objects of the superclass, but allows subclasses to alter the type of objects that will be created.

## Problem

- Assume you have a **Truck** class that handles all your transportation needs.
- If you need to add a new transportation class, **Ships**, it is difficult and requires a lot of code change as base class is tightly coupled with Truck class.
- ![[Pasted image 20250215151415.png]]

## Solution

- Replace direct object construction calls (using the `new` operator) with calls to a special ***factory*** method.
- You can now override this factory method in a subclass and change the type of object created.
- Subclasses can return different type of object only if they have a common base class or interface.
- That means the **factory** method in the base class must have its return type as **interface**.
- ![[Pasted image 20250215152200.png]]

##  Applications

- Use the Factory Method when you don’t know beforehand the exact types and dependencies of the objects your code should work with.
- Use the Factory Method when you want to provide users of your library or framework with a way to extend its internal components.

## ✅ Pros

* **Loose Coupling**  
  You avoid tight coupling between the creator and the concrete products.

* **Single Responsibility Principle**  
  You can move the product creation code into one place in the program, making the code easier to support.

* **Open/Closed Principle**  
  You can introduce new types of products into the program without breaking existing client code.

## ❌ Cons

* **Increased Complexity**  
  The code may become more complicated since you need to introduce a lot of new subclasses to implement the pattern. The best case scenario is when you're introducing the pattern into an existing hierarchy of creator classes.