public class Action {
    // تعريف نوع الفعل كـ enum
    public enum ActionType {
        ADD_BOOK,       // إضافة كتاب جديد
        BORROW_BOOK,    // استعارة كتاب
        RETURN_BOOK,    // إرجاع كتاب
        JOIN_WAITLIST   // الانضمام لقائمة الانتظار
    }

    ActionType type;    // نوع الفعل
    Book book;          // الكتاب المعني
    String userName;    // اسم المستخدم

    // الكونستركتر للإضافة أو الإرجاع
    public Action(ActionType type, Book book) {
        this.type = type;
        this.book = book;
        this.userName = null;
    }

    // الكونستركتر للاستعارة أو الانضمام للانتظار
    public Action(ActionType type, Book book, String userName) {
        this.type = type;
        this.book = book;
        this.userName = userName;
    }

    // نحول الفعل لنص مفهوم للطباعة
    @Override
    public String toString() {
        switch (type) {
            case ADD_BOOK:
                return "Action: Add Book - " + book.getTitle();
            case BORROW_BOOK:
                return "Action: Borrow Book - " + book.getTitle() + " by " + userName;
            case RETURN_BOOK:
                return "Action: Return Book - " + book.getTitle();
            case JOIN_WAITLIST:
                return "Action: Join Waitlist - " + userName + " for " + book.getTitle();
            default:
                return "Unknown Action";
        }
    }
}