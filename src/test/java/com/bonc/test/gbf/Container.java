package com.bonc.test.gbf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Container {

	public void containerCpt() {
		ConcurrentHashMap chm;
		CopyOnWriteArrayList list;
		ConcurrentLinkedQueue clq;
		BlockingQueue bq;
		ConcurrentSkipListMap cslm;
		Hashtable h;
		Vector v;
		Collections.synchronizedList(new ArrayList());
	}
}
