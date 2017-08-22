package com.praveen.ecommerce.cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.springframework.stereotype.Component;

import com.praveen.ecommerce.models.Order;

@Component
public class OrderCache {

	private static Map<String, Order> orderCache = new HashMap<>();
	private static Queue<String> queue = new LinkedList<>();
	private static final int CACHE_SIZE=50;

	public void put(String dispOrderId, Order order) {
		System.out.println(orderCache.size() + " :: " + queue.toString());
		if (orderCache.size() < CACHE_SIZE) {
			orderCache.put(dispOrderId, order);
			queue.add(dispOrderId);
		} else {
			orderCache.remove(queue.poll());
			orderCache.put(dispOrderId, order);
			queue.add(dispOrderId);
		}
		System.out.println(orderCache.size() + " :: " + queue.toString());
	}

	public Order get(String dispOrderId) {
		return orderCache.get(dispOrderId);
	}

}
