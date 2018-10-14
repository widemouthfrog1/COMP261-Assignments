package code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bag<T> {
	private List<T> indices;
	private Map<T, Integer> count;

	Bag() {
		indices = new ArrayList<T>();
		count = new HashMap<T, Integer>();
	}

	public T getKey(int index) {
		return indices.get(index);
	}

	public int getCount(int index) {
		return count.get(indices.get(index));
	}

	public int getCount(T key) {
		return count.get(key);
	}

	public void add(T key) {
		if (count.containsKey(key)) {
			count.put(key, count.get(key) + 1);
		}else {
			indices.add(key);
			count.put(key, 1);
		}
	}
	
	public int size() {
		return indices.size();
	}

}
