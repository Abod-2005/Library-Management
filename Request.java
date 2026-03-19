public class Request {
    // تعريف نوع الطلب كـ enum
    public enum RequestType {
        BORROW,   // نوع الطلب: استعارة كتاب
        RETURN    // نوع الطلب: إرجاع كتاب
    }

    RequestType type;  // نوع الطلب (استعارة أو إرجاع)
    String bookId;     // رقم / معرف الكتاب

    // الكونستركتر
    public Request(RequestType type, String bookId) {
        this.type = type;       // نخزن نوع الطلب
        this.bookId = bookId;   // نخزن رقم الكتاب
    }

    // نرجع نوع الطلب
    public RequestType getType() {
        return type;
    }

    // نرجع رقم الكتاب
    public String getBookId() {
        return bookId;
    }

    // نحول الطلب لنص مفهوم للطباعة
    @Override
    public String toString() {
        switch (type) {
            case BORROW:
                return "Borrow request for Book ID: " + bookId;
            case RETURN:
                return "Return request for Book ID: " + bookId;
            default:
                return "Unknown request type";
        }
    }
}