

import java.util.Collection;
import java.util.Iterator;

/**
 * Start.
 * Initialize all private variables
 * @param <T> is the type
 */
public class DynamicArray<T> implements Iterable<T> {
	/**
	 * Start.
	 * Initialize all private variables
	 */
	private static final int INITCAP = 2; //default initial capacity
	/**
	 * used for storage.
	 */
	private T[] storage; //underlying array, you MUST use this for credit (do not change the name or type)
	/**
	 * size of storage.
	 */
	private int size;
	/**
	 * capacity of storage.
	 */
	private int capacity;

	/**
	 * Constructor + initial capacity.
	 */
	@SuppressWarnings("unchecked")
	public DynamicArray(){
		// constructor
		storage = (T[]) new Object[INITCAP];
		// Initial capacity of the storage should be INITCAP
		size = INITCAP;
		capacity = INITCAP;
	}	
	/**
	 * Constructor.
	 * Throw excepetion if user entered capacity is less than 1
	 * initial capacity is user entered initCapacity
	 * @param initCapacity what is entered
	 */
	@SuppressWarnings("unchecked")
	public DynamicArray(int initCapacity) {
		//constructor
		// The initial capacity of the storage should be initCapacity
		// Throw IllegalArgumentException if initCapacity < 1
		// Use this _exact_ error message for the exception
		// (quotes are not part of the message):
		//    "Capacity cannot be zero or negative."
		if(initCapacity < 1){
			throw new IllegalArgumentException("Capacity cannot be zero or negative.");
		}
		else{
			storage = (T[]) new Object[initCapacity];
			size = storage.length;
			capacity = initCapacity; 
		}

	}

	/**
	 * return the size.
	 * @return the size
	 */
	public int size() {	
		// Report the current number of elements.
		// O(1)
		
		return size;
	}  
		
	/**
	 * return capacity.
	 * @return the capacity
	 */
	public int capacity() {
		// Report the max number of elements before expansion.
		// O(1)
		
		return capacity;
	}
	
	/**
	 * Changes item at the entered index.
	 * also returns the original index
	 * throws exception if out of bounds
	 * @param index is the location
	 * @param value is what is being put in
	 * @return the item being put in before it is put in 
	 */		
	public T set(int index, T value) {
	
		if(index >= capacity || index < 0){
			throw new IndexOutOfBoundsException("Index + index + out of bounds!");
		}

		else{
			T original = storage[index];
			storage[index] = value;
			return original;
		}
		
		
	}


	/**
	 * returns item at entered index.
	 * @param index is the location
	 * @return the item
	 */
	public T get(int index) {
		
		if(index >= capacity || index < 0){
			throw new IndexOutOfBoundsException("Index + index + out of bounds!");
		}
		else {
			return storage[index];
		}
		
	}
	/**
	 * append to the end of the list and return true.
	 * if not enough space, double capcity of the list
	 * @return ture or false if it is added
	 * @param value is the number being entered
	 */
	@SuppressWarnings("unchecked")
	public boolean add(T value) {
		// Append an element to the end of the list and return true.
		
		// Double the capacity if no space available.
		
		// Amortized O(1)
	
		if (capacity == size){
			T[] mand = (T[]) new Object[2*capacity];
			for (int x = 0; x<size; x++){
				mand[x] = storage[x];
			}
			//double capacity to accomodate
			storage = mand;
			capacity *= 2;
		}
		storage[size] = value; 
		size += 1;
		return true;
	}
	

	/**
	 * double capacity if space not enough.
	 * add at given index and shift
	 * @param index is the location
	 * @param value is what is entered
	 */

	@SuppressWarnings("unchecked")
	public void add(int index, T value) {
	
		if (index < 0){
			throw new IndexOutOfBoundsException("Index + index + out of bounds!");
		}
		if (capacity == size || capacity == index){
			T[] mand = (T[]) new Object[2*capacity];
			for (int x = 0; x<size; x++){
				mand[x] = storage[x];
			}
			//double capacity to accomodate
			storage = mand;
			capacity *= 2;
		}
		//shift elements
		int n = size-1;
		for (int l = n; l>=index; l--){
			storage[l+1] = storage[l];
		}
		storage[index] = value;
		size += 1;
	}
	
	/**
	 * remove at given index.
	 * throw exception of incorrect index
	 * if 1/3 capacity, half storage capacity
	 * @param index location
	 * @return elment at index
	 */

	@SuppressWarnings("unchecked")
	public T remove(int index) {
		

		if (index >= capacity || index < 0){
			throw new IndexOutOfBoundsException("Index + index + out of bounds!");
		}
		T or = storage[index];
		for (int x = index; x < size; x++){
			storage[x] = storage[x+1];
		}
		size -= 1;

		if(size <=capacity/3){
			T[] mand = (T[]) new Object[capacity/2];
			for (int x = 0; x<size; x++){
				mand[x] = storage[x];
			}
			storage = mand;
			capacity /= 2;
		}

		return or;
		
	}

	/**
	 * anonymous class for interator.
	 * used to move around
	 * @return new Iterator
	 */
	
	public Iterator<T> iterator() {


		
		return new Iterator<>() {
			
	
			private int in = 0; 
			public T next() {
				//your code here
				T t;
				t = storage[in];
				in++;
				return t;
			}
			
			public boolean hasNext() {
				//your code here
				return in < size;
				
			}
		};
	}
	


	
	public String toString() {

		StringBuilder s = new StringBuilder("Dynamic array with " + size()
			+ " items and a capacity of " + capacity() + ":");
		for (int i = 0; i < size(); i++) {
			s.append("\n  ["+i+"]: " + get(i));
		}
		return s.toString();
		
	}
	

	/**
	 * Testing in main.
	 * results of it are returned
	 * @param args main
	 */
	public static void main(String args[]){
	
		
		DynamicArray<Integer> ida = new DynamicArray<>();
		if ((ida.size() == 0) && (ida.capacity() == 2)){
			System.out.println("Yay 1");
		}

		boolean ok = true;
		for (int i=0; i<3;i++)
			ok = ok && ida.add(i*5);
		
		if (ok && ida.size()==3 && ida.get(2) == 10 && ida.capacity() == 4 ){
			System.out.println("Yay 2");
		}
		
		ida.add(1,-10);
		ida.add(4,100);
		if (ida.set(1,-20)==-10 && ida.get(2) == 5 && ida.size() == 5 
			&& ida.capacity() == 8 ){
			System.out.println("Yay 3");
		}
		
		if (ida.remove(0) == 0 && ida.remove(0) == -20 && ida.remove(2) == 100 
			&& ida.size() == 2 && ida.capacity() == 4 ){
			System.out.println("Yay 4");
		}
		
		
		System.out.print("Printing values: ");
		for(Integer i : ida) {
			System.out.print(i);
			System.out.print(" ");
		}
		
	}
}