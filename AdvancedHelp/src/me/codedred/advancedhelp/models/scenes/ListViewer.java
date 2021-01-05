package me.codedred.advancedhelp.models.scenes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewer {

	private Map<String, List<String>> list = new HashMap<String, List<String>>();
	
	
	public void addList(String name, List<String> list) {
		this.list.put(name, list);
	}
	
	
	public List<String> getList(String name) {
		return list.get(name);
	}
	
	
	public boolean hasList(String name) {
		if (list.containsKey(name))
			return true;
		return false;
	}
	
	public void clear() {
		list.clear();
	}
}
