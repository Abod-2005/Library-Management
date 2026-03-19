# Library Management System

**Library Management System** is a library management system I built from scratch using custom data structures, without using any of Java's built-in collections.

## What the system does

1. **Add books** — add a book with an ID and title; it gets stored in a list and a search tree for fast lookup
2. **Borrow & Return** — if the book is available it gets borrowed, if not your name is automatically added to a waitlist
3. **Undo** — every action is saved, and you can reverse the last step at any time
4. **Process requests** — requests queue up in order and get handled one by one

## Data structures used

| Structure | Purpose |
|-----------|---------|
| MyArrayList | Storing the book catalog |
| MyLinkedList | Waitlists for unavailable books |
| MyStack | Undo history |
| MyQueue | Processing borrow/return requests |
| BinaryTree (BST) | Fast book search by ID |

Each structure was chosen for a reason — for example, Stack for undo because it's LIFO (last action = first to undo), and Queue for requests because it's FIFO (fair, first come first served).
