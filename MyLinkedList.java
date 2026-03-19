public class MyLinkedList<T> {
    private Node<T> head;    // أول نود في الليست
    private int size;        // عدد العناصر الموجودة

    // كلاس النود
    private class Node<T> {
        T data;             // القيمة اللي جوه النود
        Node<T> next;       // مؤشر على النود اللي بعدها

        Node(T data) {
            this.data = data;   // نخزن القيمة
            this.next = null;   // بالبداية ما في نود بعدها
        }
    }

    // الكونستركتر الافتراضي
    public MyLinkedList() {
        head = null;        // بالبداية الليست فاضية
        size = 0;           // عدد العناصر صفر
    }

    // نضيف عنصر في أول الليست
    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value); // نعمل نود جديدة
        newNode.next = head;                 // نخليها تشير على الرأس القديم
        head = newNode;                      // نحدث الرأس
        size++;                              // نزود العدد
    }

    // نضيف عنصر في آخر الليست
    public void addLast(T value) {
        Node<T> newNode = new Node<>(value); // نعمل نود جديدة

        if (head == null) {                  // لو الليست فاضية
            head = newNode;                  // النود الجديدة تصير هي الرأس
        } else {
            Node<T> temp = head;             // نبدأ من أول نود
            while (temp.next != null) {      // نمشي لآخر نود
                temp = temp.next;
            }
            temp.next = newNode;             // نربط آخر نود بالجديدة
        }
        size++;                              // نزود عدد العناصر
    }

    // نحذف أول عنصر ونرجعه
    public T removeFirst() {
        if (head == null) {                  // لو الليست فاضية
            return null;
        }
        T value = head.data;                 // نخزن قيمة أول عنصر
        head = head.next;                    // نحرك الرأس على اللي بعده
        size--;                              // نقلل العدد
        return value;                        // نرجع العنصر المحذوف
    }

    // نحذف آخر عنصر ونرجعه
    public T removeLast() {
        if (head == null) {                  // لو الليست فاضية
            return null;
        }

        if (head.next == null) {             // لو فيها عنصر واحد بس
            T value = head.data;
            head = null;                     // نفرغ الليست
            size--;
            return value;
        }

        Node<T> temp = head;                 // نبدأ من أول نود
        while (temp.next.next != null) {     // نوصل للنود اللي قبل الأخيرة
            temp = temp.next;
        }

        T value = temp.next.data;            // نخزن آخر قيمة
        temp.next = null;                    // نفصل آخر نود
        size--;                              // نقلل العدد
        return value;                        // نرجع العنصر المحذوف
    }

    // نجيب أول عنصر
    public T getFirst() {
        if (head == null) {                  // لو الليست فاضية
            return null;
        }
        return head.data;                    // نرجع قيمة أول نود
    }

    // نجيب آخر عنصر
    public T getLast() {
        if (head == null) {                  // لو الليست فاضية
            return null;
        }
        Node<T> temp = head;                 // نبدأ من أول نود
        while (temp.next != null) {          // نمشي لآخر نود
            temp = temp.next;
        }
        return temp.data;                    // نرجع قيمة آخر نود
    }

    // نشوف الليست فيها عنصر معيّن ولا لأ
    public boolean contains(T value) {
        Node<T> current = head;              // نبدأ من أول نود
        while (current != null) {
            if (current.data != null && current.data.equals(value)) {
                return true;                 // لقيناه
            }
            current = current.next;          // ننتقل للي بعده
        }
        return false;                        // مش موجود
    }

    // نرجع عدد العناصر
    public int size() {
        return size;
    }

    // نتحقق إذا الليست فاضية
    public boolean isEmpty() {
        return size == 0;
    }

    // نمسح كل الليست
    public void clear() {
        head = null;                         // نفصل كل النودز
        size = 0;                            // نصفر العدد
    }


}
