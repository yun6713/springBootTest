package com.bonc.test.qs;

import java.lang.reflect.Array;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.junit.Test;

/**
 * 容器
 * @author litianlin
 * @date   2020年3月12日下午12:10:02
 * @Description TODO
 */
public class Container {
	@Test
	public void container() {
		Collection c;
		Set s;
		List l;
		Queue q;
		Map m;
		Collections collections;
		Array a;
		Arrays arrays;
	}
	
	public void collection() {
		//List
		ArrayList al;//fail-fast
		LinkedList ll;//双端队列
		CopyOnWriteArrayList cow;//fail-safe
		Vector v;
		//Set
		HashSet hs;
		EnumSet es=EnumSet.allOf(Month.class);
		CopyOnWriteArraySet set;
		//Queue
		LinkedList ll2;//双端队列
		ConcurrentLinkedQueue clq;
		ArrayBlockingQueue bq;
		Deque deque;//双端队列
		//迭代器
		Iterable ite;
		Iterator it;
		ListIterator li;
		Enumeration en;
		
	}
	
	public void map() {
		HashMap hm;
		ConcurrentHashMap chm;
		TreeMap tm;
		SortedMap sm;
		LinkedHashMap lhm;
		Hashtable h;
		
	}
	
}
