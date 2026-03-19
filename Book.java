public class Book {
    private String bookId;                   // رقم/معرف الكتاب
    private String title;                    // عنوان الكتاب
    private boolean available;               // هل الكتاب متوفر للاستعارة
    private MyLinkedList<String> waitlist;   // قائمة الانتظار للكتاب

    // الكونستركتر
    public Book(String bookId, String title) {
        this.bookId = bookId;               // نخزن رقم الكتاب
        this.title = title;                 // نخزن العنوان
        this.available = true;              // بالبداية الكتاب متوفر
        this.waitlist = new MyLinkedList<>(); // نجهز قائمة الانتظار فاضية
    }

    // Getters و Setters
    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }

    // نضيف مستخدم لقائمة الانتظار
    public boolean addToWaitlist(String userName) {
        if (waitlist.contains(userName)) {
            return false;                   // المستخدم موجود بالفعل بالقائمة
        }
        waitlist.addLast(userName);          // نضيفه للنهاية
        return true;
    }

    // نتأكد إذا في أحد بالقائمة
    public boolean hasWaitlist() {
        return !waitlist.isEmpty();
    }

    // نجيب أول شخص من قائمة الانتظار
    public String getNextFromWaitlist() {
        return waitlist.removeFirst();
    }

    // نرجع عدد الأشخاص في الانتظار
    public int getWaitlistSize() {
        return waitlist.size();
    }

    // نحذف آخر شخص من قائمة الانتظار
    public void removeLastFromWaitlist() {
        waitlist.removeLast();
    }

    // نرجع معلومات الكتاب كنص
    @Override
    public String toString() {
        String status = available ? "Available" : "Borrowed"; // حالة الكتاب
        return "ID: " + bookId + " | Title: " + title + " | Status: " + status +
                " | Waitlist size: " + waitlist.size();
    }
}
