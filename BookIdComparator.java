import java.util.Comparator;

public class BookIdComparator implements Comparator<BookIndexEntry> {
    
    @Override
    public int compare(BookIndexEntry b1, BookIndexEntry b2) {
        // نقارن حسب Book ID (ترتيب أبجدي)
        return b1.getBookId().compareTo(b2.getBookId());
    }
}
