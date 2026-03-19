public class MyStack<T> {
    private Object[] elements;                // مصفوفة بنخزن فيها عناصر الستاك
    private int top;                          // مؤشر على آخر عنصر مضاف
    private static final int INITIAL_CAPACITY = 10; // الحجم المبدئي للمصفوفة

    // الكونستركتر الافتراضي
    public MyStack() {
        elements = new Object[INITIAL_CAPACITY]; // نجهز المصفوفة
        top = -1;                                // الستاك بالبداية فاضية
    }

    // Push – نضيف عنصر فوق الستاك
    public void push(T value) {
        if (top == elements.length - 1) {       // لو المصفوفة فلّت
            resize();                            // نكبرها
        }
        elements[++top] = value;                 // نزود top ونحط القيمة
    }

    // Pop – نحذف آخر عنصر ونرجعه
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {                         // لو الستاك فاضية
            throw new RuntimeException("Stack is empty");
        }
        T value = (T) elements[top];             // نخزن آخر قيمة
        elements[top] = null;                    // نمسح المرجع
        top--;                                   // ننزل المؤشر
        return value;                            // نرجع العنصر المحذوف
    }

    // Peek – نطلع على آخر عنصر بدون ما نحذفه
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {                         // لو الستاك فاضية
            throw new RuntimeException("Stack is empty");
        }
        return (T) elements[top];                // نرجع أعلى عنصر
    }

    // نتحقق إذا الستاك فاضية
    public boolean isEmpty() {
        return top == -1;
    }

    // نرجع عدد العناصر في الستاك
    public int size() {
        return top + 1;
    }

    // نمسح الستاك كلها
    public void clear() {
        elements = new Object[INITIAL_CAPACITY]; // نرجع للحجم المبدئي
        top = -1;                                // نصفر المؤشر
    }

    // نكبر حجم المصفوفة
    private void resize() {
        Object[] newArray = new Object[elements.length * 2]; // مصفوفة أكبر
        for (int i = 0; i <= top; i++) {         // ننسخ العناصر القديمة
            newArray[i] = elements[i];
        }
        elements = newArray;                     // نحدث المرجع
    }

    // نطبع عناصر الستاك من فوق لتحت
    public void printStack() {
        if (isEmpty()) {                         // لو فاضية
            System.out.println("Stack is empty");
            return;
        }
        System.out.println("--- Stack Contents (top to bottom) ---");
        for (int i = top; i >= 0; i--) {
            System.out.println(elements[i]);     // نطبع عنصر عنصر
        }
    }
}
