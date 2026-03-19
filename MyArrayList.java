public class MyArrayList<T> {
    private Object[] elements;                        // مصفوفة بنخزن فيها العناصر
    private int size;                                 // عدد العناصر اللي موجودة فعليًا
    private static final int INITIAL_CAPACITY = 10;   // الحجم المبدئي للمصفوفة

    // الكونستركتر الافتراضي
    public MyArrayList() {
        elements = new Object[INITIAL_CAPACITY];     // نجهز المصفوفة بحجم مبدئي
        size = 0;                                    // في البداية فش عناصر
    }

    // نرجع عدد العناصر الحالي
    public int size() {
        return size;
    }

    // نعرف الليست فاضية ولا لأ
    public boolean isEmpty() {
        return size == 0;
    }

    //  نتأكد إن الإندكس صح
    public void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
    }

    // نجيب عنصر باستخدام الإندكس
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];                  // نرجع العنصر بعد التحويل لـ T
    }

    // نحذف عنصر من مكان معين ونرجعه
    @SuppressWarnings("unchecked")
    public void remove(int index) {
        checkIndex(index);
        T removed = (T) elements[index];              // نخزن العنصر اللي هيتحذف
        for (int i = index; i < size - 1; i++) {      // نرجع العناصر اللي بعده خطوة لورا
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;                   // نمسح آخر عنصر عشان ما يفضلش مرجع في الذاكرة
        size--;                                      // نقلل الحجم
    }

    // نضيف عنصر في آخر الليست
    public void add(T element) {
        if (size == elements.length) {               // لو المصفوفة مليانة نكبرها
            resize();
        }
        elements[size] = element;                    // نحط العنصر في أول مكان فاضي
        size++;                                      // نزود عدد العناصر
    }

    // نضيف عنصر في مكان محدد
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        if (size == elements.length) {  //  إذا المصفوفة مليانة نكبرها
            resize();
        }
        for (int i = size; i > index; i--) { // بنزيح العناصر لليمين
            elements[i] = elements[i - 1];
        }
        elements[index] = element;//بنحط تالعنصر الجديد
        size++;//بنحدث الحجم
    }

    // نعدل عنصر في مكان محدد ونرجع القديم
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        checkIndex(index);
        T old = (T) elements[index];//نخزن العنصر القديم
        elements[index] = element;// نضع العنصر الجديد
        return old;               //بنرجع العنصر القديم
    }


    // نرجع أول index للعنصر، أو -1 إذا غير موجود
    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
             if (elements[i] != null && elements[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    // نتحقق إذا العنصر موجود في الليست
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    // نمسح كل العناصر ونرجع للحجم المبدئي
    public void clear() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    // نكبر حجم المصفوفة
    private void resize() {
        Object[] newElements = new Object[elements.length * 2];// نعمل مصفوفة جديدة أكبر
        for (int i = 0; i < size; i++) { // ننسخ العناصر القديمة
            newElements[i] = elements[i];
        }
        elements = newElements;// نخلي المصفوفة الجديدة هي الأساسية
    }


}