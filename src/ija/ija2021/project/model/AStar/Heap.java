package ija.ija2021.project.model.AStar;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Heap<T extends Comparable> extends PriorityQueue<T> {
    /***
     * class representation of heap
     * authors: Vanessa Jóriová, Marián Zimmerman
     */

    private static final int DEFAULT_CAPACITY = 10;
    protected int size;
    protected T[] array;

    public Heap () {
        array = (T[])new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    /***
     *
     * @return true if empty, false if not
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /***
     *
     * @param value value to add
     * @return true
     */
    public boolean add(T value) {
        if (size >= array.length - 1) {
            array = this.resize();
        }
        size++;
        int index = size;
        array[index] = value;

        bubbleUp();
        return true;
    }


    /***
     *
     * @return array[1]
     */

    public T peek() {
        if (this.isEmpty()) {
            throw new IllegalStateException();
        }
        return array[1];
    }

    /***
     *
     * @return result
     */
    public T remove() {
        T result = peek();

        array[1] = array[size];
        array[size] = null;
        size--;

        bubbleDown();
        return result;
    }

    /***
     *
     * @return string convertion of array
     */
    public String toString() {
        return Arrays.toString(array);
    }


    /***
     * bubbleUp ordering
     */
    protected void bubbleUp() {
        int index = this.size;

        while (hasParent(index)
                && (parent(index).compareTo(array[index]) > 0)) {
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }
    }


    /***
     * bubble down ordering
     */
    protected void bubbleDown() {
        int index = 1;

        while (hasLeftChild(index)) {
            int smallerChild = leftIndex(index);

            if (hasRightChild(index)
                    && array[leftIndex(index)].compareTo(array[rightIndex(index)]) > 0) {
                smallerChild = rightIndex(index);
            }

            if (array[index].compareTo(array[smallerChild]) > 0) {
                swap(index, smallerChild);
            } else {
                break;
            }

            index = smallerChild;
        }
    }

    /***
     *
     * @param i index
     * @return true if has parent, false if not
     */
    protected boolean hasParent(int i) {
        return i > 1;
    }

    /***
     *
     * @param i index
     * @return index * 2
     */

    protected int leftIndex(int i) {
        return i * 2;
    }


    /***
     *
     * @param i index
     * @return index * 2 + 1
     */
    protected int rightIndex(int i) {
        return i * 2 + 1;
    }


    /***
     *
     * @param i index
     * @return true if has left child, false if not
     */

    protected boolean hasLeftChild(int i) {
        return leftIndex(i) <= size;
    }

    /***
     *
     * @param i index
     * @return true if has right child, false if not
     */

    protected boolean hasRightChild(int i) {
        return rightIndex(i) <= size;
    }

    /***
     *
     * @param i index
     * @return parent
     */
    protected T parent(int i) {
        return array[parentIndex(i)];
    }

    /***
     *
     * @param i index
     * @return parent index
     */
    protected int parentIndex(int i) {
        return i / 2;
    }

    /***
     *
     * @return resized heap
     */

    protected T[] resize() {
        return Arrays.copyOf(array, array.length * 2);
    }


    /***
     *
     * @param index1 first index
     * @param index2 second index
     */
    protected void swap(int index1, int index2) {
        T tmp = array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;
    }
}


class HeapException extends Exception {
    public HeapException(String message) {
        super(message);
    }
}
