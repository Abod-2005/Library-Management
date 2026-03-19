import java.util.Comparator;

public class BinaryTree<E> {

    // كلاس النود الداخلي
    private class Node<E> {
        E data;           // البيانات المخزنة
        Node<E> left;     // الفرع الأيسر
        Node<E> right;    // الفرع الأيمن

        Node(E data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private Node<E> root;  // جذر الشجرة

    // الكونستركتر
    public BinaryTree() {
        this.root = null;
    }

    // إضافة عنصر للشجرة
    public void add(E value, Comparator<E> c) {
        root = addRecursive(root, value, c);
    }

    private Node<E> addRecursive(Node<E> current, E value, Comparator<E> c) {
        if (current == null) {
            return new Node<>(value);
        }

        int cmp = c.compare(value, current.data);

        if (cmp < 0) {
            // القيمة أصغر - نروح يسار
            current.left = addRecursive(current.left, value, c);
        } else {
            // القيمة أكبر أو مساوية - نروح يمين (المكررات تروح يمين)
            current.right = addRecursive(current.right, value, c);
        }

        return current;
    }

    // البحث عن عنصر
    public boolean contains(E value, Comparator<E> c) {
        return containsRecursive(root, value, c);
    }

    private boolean containsRecursive(Node<E> current, E value, Comparator<E> c) {
        if (current == null) {
            return false;
        }

        int cmp = c.compare(value, current.data);

        if (cmp == 0) {
            return true;  // لقيناه
        } else if (cmp < 0) {
            return containsRecursive(current.left, value, c);
        } else {
            return containsRecursive(current.right, value, c);
        }
    }

    // حذف عنصر
    public void delete(E value, Comparator<E> c) {
        root = deleteRecursive(root, value, c);
    }

    private Node<E> deleteRecursive(Node<E> current, E value, Comparator<E> c) {
        if (current == null) {
            return null;
        }

        int cmp = c.compare(value, current.data);

        if (cmp < 0) {
            current.left = deleteRecursive(current.left, value, c);
        } else if (cmp > 0) {
            current.right = deleteRecursive(current.right, value, c);
        } else {
            // لقينا النود اللي بدنا نحذفها

            // Case 1: ما عندها أطفال
            if (current.left == null && current.right == null) {
                return null;
            }

            // Case 2: عندها طفل واحد
            if (current.left == null) {
                return current.right;
            }
            if (current.right == null) {
                return current.left;
            }

            // Case 3: عندها طفلين
            // نجيب أصغر قيمة من الفرع الأيمن
            E smallestValue = findMin(current.right);
            current.data = smallestValue;
            current.right = deleteRecursive(current.right, smallestValue, c);
        }

        return current;
    }

    private E findMin(Node<E> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.data;
    }

    //  IN-ORDER TRAVERSAL (مرتب من الصغير للكبير)
    public void inOrder() {
        inOrderRecursive(root);
    }

    private void inOrderRecursive(Node<E> node) {
        if (node != null) {
            inOrderRecursive(node.left);        // يسار
            System.out.print(node.data + " ");  // جذر
            inOrderRecursive(node.right);       // يمين
        }
    }

    //  PRE-ORDER TRAVERSAL (الجذر أولاً)
    public void preOrder() {
        preOrderRecursive(root);
    }

    private void preOrderRecursive(Node<E> node) {
        if (node != null) {
            System.out.print(node.data + " ");  // جذر
            preOrderRecursive(node.left);       // يسار
            preOrderRecursive(node.right);      // يمين
        }
    }

    // POST-ORDER TRAVERSAL (الجذر أخيراً)
    public void postOrder() {
        postOrderRecursive(root);
    }

    private void postOrderRecursive(Node<E> node) {
        if (node != null) {
            postOrderRecursive(node.left);      // يسار
            postOrderRecursive(node.right);     // يمين
            System.out.print(node.data + " ");  // جذر
        }
    }

    //  BREADTH-FIRST TRAVERSAL (المستوى بالمستوى)
    public void breadthTraverse() {
        if (root == null) return;

        MyLinkedList<Node<E>> queue = new MyLinkedList<>();
        queue.addLast(root);

        while (!queue.isEmpty()) {
            Node<E> current = queue.removeFirst();
            System.out.print(current.data + " ");

            if (current.left != null) {
                queue.addLast(current.left);
            }
            if (current.right != null) {
                queue.addLast(current.right);
            }
        }
    }

    //  RANGE SEARCH (البحث في مدى معين)
    public void rangeSearch(E min, E max, Comparator<E> c) {
        rangeSearchRecursive(root, min, max, c);
    }

    private void rangeSearchRecursive(Node<E> node, E min, E max, Comparator<E> c) {
        if (node == null) {
            return;
        }

        int cmpMin = c.compare(node.data, min);
        int cmpMax = c.compare(node.data, max);

        // لو النود أكبر من الحد الأدنى، نروح يسار
        if (cmpMin > 0) {
            rangeSearchRecursive(node.left, min, max, c);
        }

        // لو النود داخل المدى، نطبعها
        if (cmpMin >= 0 && cmpMax <= 0) {
            System.out.print(node.data + " ");
        }

        // لو النود أصغر من الحد الأقصى، نروح يمين
        if (cmpMax < 0) {
            rangeSearchRecursive(node.right, min, max, c);
        }
    }

    //  ارتفاع الشجرة
    public int height() {
        return heightRecursive(root);
    }

    private int heightRecursive(Node<E> node) {
        if (node == null) {
            return 0;
        }

        int leftHeight = heightRecursive(node.left);
        int rightHeight = heightRecursive(node.right);

        return Math.max(leftHeight, rightHeight) + 1;
    }

    //  نتحقق إذا الشجرة فاضية
    public boolean isEmpty() {
        return root == null;
    }
    // إضافة القيمة مع التحقق من -1 ثم الطباعة مستوى بمستوى
    public void addIfNotMinusOne(Integer value) {
        if (value == -1) {
            System.out.println("القيمة -1، لم تُضف.");
            return;
        }
        // نستخدم Comparator طبيعي للأرقام
        Comparator<Integer> c = Integer::compareTo;

        add((E) value, (Comparator<E>) c);  // إضافة القيمة
        System.out.println("تمت الإضافة، الشجرة مستوى بمستوى:");
        breadthTraverse();  // طباعة المستويات
        System.out.println(); // سطر جديد بعد الطباعة
    }

}