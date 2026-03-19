public class MyQueue<T> {
    private Node<T> front;   // أول عنصر في الكيو
    private Node<T> rear;    // آخر عنصر في الكيو
    private int size;        // عدد العناصر الموجودة

    // كلاس النود
    private static class Node<T> {
        T data;             // القيمة اللي جوه النود
        Node<T> next;       // مؤشر على النود اللي بعدها

        Node(T data) {
            this.data = data;   // نخزن القيمة
            this.next = null;   // بالبداية ما في نود بعدها
        }
    }

    // الكونستركتر الافتراضي
    public MyQueue() {
        front = null;        // بالبداية الكيو فاضية
        rear = null;         // ما في آخر عنصر
        size = 0;            // العدد صفر
    }

    //  نضيف عنصر بآخر الكيو
    public void enqueue(T value) {
        Node<T> newNode = new Node<>(value); // نعمل نود جديدة

        if (isEmpty()) {                     // لو الكيو فاضية
            front = rear = newNode;          // أول وآخر عنصر نفس النود
        } else {
            rear.next = newNode;             // نربط آخر نود بالجديدة
            rear = newNode;                  // نحدث المؤشر على الآخر
        }
        size++;                              // نزود عدد العناصر
    }

    //  نحذف أول عنصر ونرجعه
    public T dequeue() {
        if (isEmpty()) {                     // لو الكيو فاضية
            throw new RuntimeException("Queue is empty");
        }

        T value = front.data;                // نخزن قيمة أول عنصر
        front = front.next;                  // نحرك المؤشر للي بعده
        size--;                              // نقلل العدد

        if (front == null) {                 // لو الكيو صارت فاضية
            rear = null;                     // نفرغ rear كمان
        }
        return value;                        // نرجع العنصر المحذوف
    }

    // Peek – نطلع على أول عنصر بدون ما نحذفه
    public T peek() {
        if (isEmpty()) {                     // لو الكيو فاضية
            throw new RuntimeException("Queue is empty");
        }
        return front.data;                   // نرجع أول قيمة
    }

    // نتحقق إذا الكيو فاضية
    public boolean isEmpty() {
        return front == null;
    }

    // نرجع عدد العناصر
    public int size() {
        return size;
    }

    // نمسح الكيو كاملة
    public void clear() {
        front = null;                        // نفصل أول عنصر
        rear = null;                         // نفصل آخر عنصر
        size = 0;                            // نصفر العدد
    }

    // نطبع عناصر الكيو من الأول للآخر
    public void printQueue() {
        if (isEmpty()) {                     // لو فاضية
            System.out.println("Queue is empty");
            return;
        }

        System.out.println("--- Queue Contents (front to rear) ---");
        Node<T> current = front;             // نبدأ من أول عنصر
        int position = 1;

        while (current != null) {
            System.out.println(position + ". " + current.data);
            current = current.next;          // ننتقل للي بعده
            position++;
        }
    }
}
