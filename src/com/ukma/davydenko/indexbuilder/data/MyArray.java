package com.ukma.davydenko.indexbuilder.data;

import java.io.Serializable;

public class MyArray<T> implements Container<T>, Serializable {

	private static final long serialVersionUID = 2017783591830416210L;
	@SuppressWarnings("unchecked")
	T[] array = (T[]) new Object[1];
	int curIndex = 0;
	
	@Override
	public void add(T element) {
		if (curIndex == array.length) {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new Object[array.length*2+1];
			System.arraycopy(array, 0, newArray, 0, array.length);
			array=newArray;
			array[curIndex] = element;
			curIndex++;
		} 
		else {
			array[curIndex] = element;
			curIndex++;
		}
	}

	@Override
	public void remove(int index) {
		@SuppressWarnings("unchecked")
		T[] newArr = (T[]) new Object[this.curIndex];
		
		System.arraycopy(array, 0, newArr, 0, index);
		System.arraycopy(array, index+1, newArr, index, curIndex-1-index);
		
		this.array = newArr;
		this.curIndex = newArr.length-1;
	}

	@Override
	public void set(int index, T element) {
		array[index] = element;
	}
	
	@Override
	public T get(int index) {
		return array[index];
	}

	@Override
	public boolean contains(T element) {
		for (int i = 0; i < this.curIndex; ++i) {
			if (array[i].equals(element)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public int size() {
		return curIndex;
	}
	
	public T[] getRawArray() {
		return array;
	}

	@Override
	public String toString() {
		String toReturn = "";
		
		toReturn += '[';
		for (int i = 0; i < curIndex; ++i) {
			if (i == curIndex - 1) {
				toReturn += get(i);
			}
			else {
				toReturn += get(i) + ", ";
			}
		}
		toReturn += ']';
		
		return toReturn;
	}
}