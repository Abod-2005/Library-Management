import java.util.Scanner;

public class LibraryManagementSystem {
    private static final MyArrayList<Book> library = new MyArrayList<>();  // كل الكتب بالمكتبة
    private static final MyArrayList<Book> borrowed = new MyArrayList<>(); // الكتب المستعارة
    private static final MyStack<Action> undoStack = new MyStack<>();      // لتتبع التراجع عن آخر عملية
    private static final MyQueue<Request> requestQueue = new MyQueue<>();  // قائمة الانتظار للطلبات

    // ============ الجديد: BST للفهرسة ============
    private static final BinaryTree<BookIndexEntry> bookIndexTree = new BinaryTree<>();
    private static final BookIdComparator bookComparator = new BookIdComparator();

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  Welcome to Library Management System");
        System.out.println("========================================");
        int choice;

        do {

            System.out.println("MAIN MENU  ");
            System.out.println("1.Add Book ");
            System.out.println("2.Borrow Book ");
            System.out.println("3.Return Book ");
            System.out.println("4.View Library Inventory ");
            System.out.println("5.View Borrowed Books  ");
            System.out.println("6.Join Waitlist ");
            System.out.println("7.Undo Last Action ");
            System.out.println("8.Add Request ");
            System.out.println("9.Process Next Request");
            System.out.println("10.View Pending Requests");
            System.out.println("11.View Inventory Sorted by ID ");
            System.out.println("12.Search Book by ID (BST) ");
            System.out.println("13.Find Books by ID Range ");
            System.out.println("14.Tree Explorer");
            System.out.println("15.Exit");

            choice = getInt("Choose an option: ");

            switch (choice) {
                case 1: AddBook(); break;
                case 2: BorrowBook(); break;
                case 3: ReturnBook(); break;
                case 4: ViewLibraryInventory(); break;
                case 5: ViewBorrowedBooks(); break;
                case 6: JoinWaitlist(); break;
                case 7: UndoLastAction(); break;
                case 8: AddRequest(); break;
                case 9: ProcessNextRequest(); break;
                case 10: ViewPendingRequests(); break;
                case 11: ViewInventorySortedByID(); break;
                case 12: SearchBookByID(); break;
                case 13: FindBooksByIDRange(); break;
                case 14: TreeExplorer(); break;
                case 15: System.out.println("\n Thank you for using Library Management System. Goodbye!"); break;
                default: System.out.println("ERROR: Invalid choice. Please enter a number between 1 and 15.");
            }
        } while (choice != 15);
    }

    //التحقق من ادخال الارقام
    private static int getInt(String input) {
        while (true) {
            try {
                System.out.print(input);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Please enter a valid number!");
            }
        }
    }
    //التحقق من ادخال السترنغ
    private static String getString(String input) {
        String s = "";
        while (s.isEmpty()) {
            System.out.print(input);
            s = sc.nextLine().trim();
            if (s.isEmpty()) {
                System.out.println("ERROR: Input cannot be empty!");
            }
        }
        return s;
    }
    //11111111111111111111111111111111111
    private static void AddBook() {
        System.out.println("\n--- Add Book ---");
        String bookId = getString("Enter Book ID: ");

        // نتأكد ما يكون في كتاب بنفس الرقم
        for (int i = 0; i < library.size(); i++) {
            if (library.get(i).getBookId().equals(bookId)) {
                System.out.println("ERROR: A book with ID '" + bookId + "' already exists!");
                return;
            }
        }

        String title = getString("Enter Book Title: ");
        Book newBook = new Book(bookId, title);
        library.add(newBook);

        // ============ الجديد: نضيف الكتاب للـ BST ============
        BookIndexEntry entry = new BookIndexEntry(bookId, title, newBook);
        bookIndexTree.add(entry, bookComparator);
        // ===================================================

        // نسجل العملية للـ undo - استخدام enum
        undoStack.push(new Action(Action.ActionType.ADD_BOOK, newBook));
        System.out.println(" SUCCESS: Book added successfully!");
        System.out.println("  Added: " + newBook);
    }

    //22222222222222222222
    private static void BorrowBook() {
        System.out.println("\n--- Borrow Book ---");

        if (library.isEmpty()) {
            System.out.println("ERROR: No books available in the library!");
            return;
        }

        String bookId = getString("Enter Book ID to borrow: ");

        // نبحث عن الكتاب بالمكتبة
        int index = -1;
        for (int i = 0; i < library.size(); i++) {
            if (library.get(i).getBookId().equals(bookId)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("ERROR: Book with ID '" + bookId + "' not found in library!");
            return;
        }

        Book book = library.get(index);

        // لو الكتاب مستعار، نقترح الانضمام للانتظار
        if (!book.isAvailable()) {
            System.out.println("ERROR: Book is currently borrowed.");
            String userName = getString("Enter your name to join waitlist: ");

            if (book.addToWaitlist(userName)) {
                undoStack.push(new Action(Action.ActionType.JOIN_WAITLIST, book, userName));
                System.out.println(" SUCCESS: You have been added to the waitlist.");
            } else {
                System.out.println("ERROR: You are already in the waitlist!");
            }
            return;
        }

        // استعارة الكتاب
        String userName = getString("Enter your name: ");
        book.setAvailable(false);
        borrowed.add(book);
        undoStack.push(new Action(Action.ActionType.BORROW_BOOK, book, userName));
        System.out.println(" SUCCESS: Book borrowed successfully!");
        System.out.println("  Borrowed by: " + userName);
        System.out.println("  Book: " + book);
    }

    //33333333333333333333333333
    private static void ReturnBook() {
        System.out.println("\n--- Return Book ---");

        if (borrowed.isEmpty()) {
            System.out.println("ERROR: No books are currently borrowed!");
            return;
        }

        String bookId = getString("Enter Book ID to return: ");

        // نبحث عن الكتاب بالقائمة المستعارة
        int index = -1;
        for (int i = 0; i < borrowed.size(); i++) {
            if (borrowed.get(i).getBookId().equals(bookId)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("ERROR: Book with ID '" + bookId + "' is not currently borrowed!");
            return;
        }

        Book book = borrowed.get(index);
        borrowed.remove(index);
        undoStack.push(new Action(Action.ActionType.RETURN_BOOK, book));

        // نتأكد إذا في قائمة انتظار
        if (book.hasWaitlist()) {
            String nextUser = book.getNextFromWaitlist();
            book.setAvailable(false);
            borrowed.add(book);
            System.out.println(" Book automatically assigned to: " + nextUser);
            System.out.println("  Book: " + book);
        } else {
            book.setAvailable(true);
            System.out.println(" SUCCESS: Book returned successfully!");
            System.out.println("  Book: " + book);
        }
    }
    //4444444444444444
    private static void ViewLibraryInventory() {
        System.out.println("\n--- Library Inventory ---");

        if (library.isEmpty()) {
            System.out.println("Library is empty!");
            return;
        }

        System.out.println("Total books: " + library.size());
        System.out.println("---");
        for (int i = 0; i < library.size(); i++) {
            System.out.println((i + 1) + ". " + library.get(i));
        }
    }
    //5555555555555555555
    private static void ViewBorrowedBooks() {
        System.out.println("\n--- Borrowed Books ---");

        if (borrowed.isEmpty()) {
            System.out.println("No books are currently borrowed!");
            return;
        }

        System.out.println("Total borrowed: " + borrowed.size());
        System.out.println("---");
        for (int i = 0; i < borrowed.size(); i++) {
            System.out.println((i + 1) + ". " + borrowed.get(i));
        }
    }
    // 66666666666666666666666
    private static void JoinWaitlist() {
        System.out.println("\n--- Join Waitlist ---");

        if (library.isEmpty()) {
            System.out.println("ERROR: No books in library!");
            return;
        }

        String bookId = getString("Enter Book ID: ");

        // نبحث عن الكتاب
        int index = -1;
        for (int i = 0; i < library.size(); i++) {
            if (library.get(i).getBookId().equals(bookId)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("ERROR: Book with ID '" + bookId + "' not found!");
            return;
        }

        Book book = library.get(index);

        // ممكن ينضموا للانتظار بس لو الكتاب مستعار
        if (book.isAvailable()) {
            System.out.println("ERROR: Book is currently available. You can borrow it directly!");
            return;
        }

        String userName = getString("Enter your name: ");

        if (book.addToWaitlist(userName)) {
            undoStack.push(new Action(Action.ActionType.JOIN_WAITLIST, book, userName));
            System.out.println(" SUCCESS: You have been added to the waitlist!");
            System.out.println("  Book: " + book.getTitle());
            System.out.println("  Position in waitlist: " + book.getWaitlistSize());
        } else {
            System.out.println("ERROR: You are already in the waitlist for this book!");
        }
    }
    // 777777777777777777
    private static void UndoLastAction() {
        System.out.println("\n--- Undo Last Action ---");

        if (undoStack.isEmpty()) {
            System.out.println("ERROR: Nothing to undo!");
            return;
        }

        Action action = undoStack.pop();

        // استخدام switch مع enum
        switch (action.type) {
            case ADD_BOOK:
                for (int i = 0; i < library.size(); i++) {
                    if (library.get(i) == action.book) {
                        library.remove(i);

                        // نحذف من الـ BST كمان
                        BookIndexEntry entryToDelete = new BookIndexEntry(
                                action.book.getBookId(),
                                action.book.getTitle(),
                                action.book
                        );
                        bookIndexTree.delete(entryToDelete, bookComparator);
                        break;
                    }
                }
                System.out.println(" Undo: Add Book - '" + action.book.getTitle() + "' removed");
                break;

            case BORROW_BOOK:
                action.book.setAvailable(true);
                for (int i = 0; i < borrowed.size(); i++) {
                    if (borrowed.get(i) == action.book) {
                        borrowed.remove(i);
                        break;
                    }
                }
                System.out.println(" Undo: Borrow Book - '" + action.book.getTitle() + "' marked as available again");
                break;

            case RETURN_BOOK:
                action.book.setAvailable(false);
                borrowed.add(action.book);
                System.out.println(" Undo: Return Book - '" + action.book.getTitle() + "' marked as borrowed again");
                break;

            case JOIN_WAITLIST:
                action.book.removeLastFromWaitlist();
                System.out.println(" Undo: Join Waitlist - " + action.userName + " removed from waitlist");
                break;
        }
    }
    // 8888888888888888888
    private static void AddRequest() {
        System.out.println("\n--- Add Request ---");
        System.out.println("1. Borrow Book");
        System.out.println("2. Return Book");

        int type = getInt("Choose request type: ");

        if (type != 1 && type != 2) {
            System.out.println("ERROR: Invalid request type. Please enter 1 or 2.");
            return;
        }

        String bookId = getString("Enter Book ID: ");

        if (type == 1) {
            requestQueue.enqueue(new Request(Request.RequestType.BORROW, bookId));
            System.out.println(" Borrow request added to queue.");
        } else {
            requestQueue.enqueue(new Request(Request.RequestType.RETURN, bookId));
            System.out.println(" Return request added to queue.");
        }
    }
    //99999999999999999999
    private static void ProcessNextRequest() {
        System.out.println("\n--- Process Next Request ---");

        if (requestQueue.isEmpty()) {
            System.out.println("ERROR: No pending requests to process.");
            return;
        }

        Request request = requestQueue.dequeue();
        String bookId = request.bookId;

        switch (request.type) {
            case BORROW:
                processBorrowRequest(bookId);
                break;
            case RETURN:
                processReturnRequest(bookId);
                break;
        }
    }

    private static void processBorrowRequest(String bookId) {
        for (int i = 0; i < library.size(); i++) {
            Book book = library.get(i);
            if (book.getBookId().equals(bookId)) {
                if (!book.isAvailable()) {
                    System.out.println("ERROR: Book is already borrowed. Borrow request failed.");
                    return;
                }
                book.setAvailable(false);
                borrowed.add(book);
                undoStack.push(new Action(Action.ActionType.BORROW_BOOK, book));
                System.out.println(" SUCCESS: Borrow request processed for Book ID: " + bookId);
                System.out.println("  Book: " + book);
                return;
            }
        }
        System.out.println("ERROR: Book with ID '" + bookId + "' not found in library.");
    }

    private static void processReturnRequest(String bookId) {
        for (int i = 0; i < borrowed.size(); i++) {
            Book book = borrowed.get(i);
            if (book.getBookId().equals(bookId)) {
                borrowed.remove(i);
                undoStack.push(new Action(Action.ActionType.RETURN_BOOK, book));

                if (book.hasWaitlist()) {
                    String nextUser = book.getNextFromWaitlist();
                    book.setAvailable(false);
                    borrowed.add(book);
                    System.out.println(" SUCCESS: Return request processed.");
                    System.out.println("  Book automatically assigned to: " + nextUser);
                } else {
                    book.setAvailable(true);
                    System.out.println(" SUCCESS: Return request processed.");
                }
                System.out.println("  Book: " + book);
                return;
            }
        }
        System.out.println("ERROR: Book with ID '" + bookId + "' is not currently borrowed.");
    }
    //10-10-10-10-10-10-10
    private static void ViewPendingRequests() {
        System.out.println("\n--- Pending Requests ---");

        if (requestQueue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        System.out.println("Requests in queue: " + requestQueue.size());
        System.out.println("---");

        // نخزن مؤقتًا عشان ما نغير ترتيب الطابور
        MyQueue<Request> temp = new MyQueue<>();
        int position = 1;

        while (!requestQueue.isEmpty()) {
            Request r = requestQueue.dequeue();
            System.out.println(position + ". " + r);
            temp.enqueue(r);
            position++;
        }
        // نرجع الطلبات للطابور الأصلي
        while (!temp.isEmpty()) {
            requestQueue.enqueue(temp.dequeue());
        }
    }


    // 11 11 11 11 11 11 11 11
    private static void ViewInventorySortedByID() {
        System.out.println("   INVENTORY SORTED BY BOOK ID      ");
        System.out.println("      (BST InOrder Traversal)       ");
        System.out.println(" ");
        if (bookIndexTree.isEmpty()) {
            System.out.println("Library is empty!");
            return;
        }

        System.out.println("Total books: " + library.size());

        System.out.print("Books (sorted by ID): ");
        bookIndexTree.inOrder();
        System.out.println();
    }

    //1212
    private static void SearchBookByID() {
        System.out.println(" SEARCH BOOK BY ID (BST) ");

        if (bookIndexTree.isEmpty()) {
            System.out.println("ERROR: Library is empty!");
            return;
        }

        String bookId = getString("Enter Book ID to search: ");

        // نعمل entry مؤقت للبحث
        BookIndexEntry searchEntry = new BookIndexEntry(bookId, "", null);

        boolean found = bookIndexTree.contains(searchEntry, bookComparator);

        if (found) {
            // نجيب تفاصيل الكتاب من المكتبة
            for (int i = 0; i < library.size(); i++) {
                if (library.get(i).getBookId().equals(bookId)) {
                    System.out.println(" Book found in BST!");
                    System.out.println(library.get(i));
                    return;
                }
            }
        } else {
            System.out.println("✗ Book with ID '" + bookId + "' not found in library.");
        }
    }

    //13 13 13 13 13 13 13 13 13 13 13
    private static void FindBooksByIDRange() {
        System.out.println("   FIND BOOKS BY ID RANGE  ");

        if (bookIndexTree.isEmpty()) {
            System.out.println(" ERROR: Library is empty!");
            return;
        }

        String minId = getString("Enter minimum Book ID: ");
        String maxId = getString("Enter maximum Book ID: ");

        BookIndexEntry minEntry = new BookIndexEntry(minId, "", null);
        BookIndexEntry maxEntry = new BookIndexEntry(maxId, "", null);

        System.out.println("\n Books with IDs between '" + minId + "' and '" + maxId + "':");

        System.out.print("Results: ");
        bookIndexTree.rangeSearch(minEntry, maxEntry, bookComparator);
        System.out.println();
    }

    // 14 14 14 14 14 14 14 14
    private static void TreeExplorer() {
        System.out.println(" ** TREE EXPLORER **");

        if (bookIndexTree.isEmpty()) {
            System.out.println(" ERROR: Tree is empty!");
            return;
        }

        System.out.println("1. InOrder Traversal (Sorted)");
        System.out.println("2. PreOrder Traversal (Root First)");
        System.out.println("3. PostOrder Traversal (Root Last)");
        System.out.println("4. Breadth-First Traversal (Level Order)");
        System.out.println("5. Show Tree Height");
        System.out.println("6. Back to Main Menu");

        int choice = getInt("\nChoose an option: ");

        switch (choice) {
            case 1:
                showInOrder();
                break;
            case 2:
                showPreOrder();
                break;
            case 3:
                showPostOrder();
                break;
            case 4:
                showBreadthFirst();
                break;
            case 5:
                showTreeHeight();
                break;
            case 6:
                return;
            default:
                System.out.println(" Invalid choice!");
        }
    }

    private static void showInOrder() {
        System.out.println("\n InOrder Traversal (Left → Root → Right) ");
        System.out.println("This shows books in SORTED order by ID:");
        bookIndexTree.inOrder();
        System.out.println();
    }

    private static void showPreOrder() {
        System.out.println("\n PreOrder Traversal (Root → Left → Right) ");
        System.out.println("This shows the tree structure (root first):");
        bookIndexTree.preOrder();
        System.out.println();
    }

    private static void showPostOrder() {
        System.out.println("\n PostOrder Traversal (Left → Right → Root) ");
        System.out.println("This shows the tree structure (root last):");
        bookIndexTree.postOrder();
        System.out.println();
    }

    private static void showBreadthFirst() {
        System.out.println("\n Breadth-First Traversal (Level by Level) ");
        System.out.println("This shows the tree level by level:");
        bookIndexTree.breadthTraverse();
        System.out.println();
    }

    private static void showTreeHeight() {
        int height = bookIndexTree.height();
        System.out.println(" ** TREE HEIGHT** ");
        System.out.println("Current tree height: " + height);
    }
}