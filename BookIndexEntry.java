public class BookIndexEntry {
    private String bookId;      // رقم الكتاب
    private String title;       // عنوان الكتاب
    private Book bookRef;       // مرجع للكتاب الأصلي
    
    // الكونستركتر
    public BookIndexEntry(String bookId, String title, Book bookRef) {
        this.bookId = bookId;
        this.title = title;
        this.bookRef = bookRef;
    }
    
    // Getters
    public String getBookId() {
        return bookId;
    }
    public String getTitle() {
        return title;
    }
    public Book getBookRef() {
        return bookRef;
    }
    
    // للطباعة
    @Override
    public String toString() {
        return "ID: " + bookId + " | Title: " + title;
    }
}
