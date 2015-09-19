package com.ukma.davydenko.indexbuilder.data;

public interface Container<T> {
	public void add(T element);
	public void remove(int index);
	public T get(int index);
	public boolean contains(T element);
	int size();
}