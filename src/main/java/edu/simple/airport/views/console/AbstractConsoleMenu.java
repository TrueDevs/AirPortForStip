package edu.simple.airport.views.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractConsoleMenu implements IConsoleMenu {
	private PrintStream out = System.out;
	private InputStream in = System.in;

	private String head;
	private Map<Integer, SubMenu> menus = new TreeMap<Integer, AbstractConsoleMenu.SubMenu>();
	
	public AbstractConsoleMenu() {
		initializeMenu();
	}

	abstract protected void initializeMenu();
	
	final public void launchMenu() {
		while(drawMenu());
	};
	
	final private boolean drawMenu() {
		//print menus
		out.println(head);
		for (Integer number : menus.keySet()) {
			SubMenu menu = menus.get(number);
			menu.print();
		}
		
		//wait response
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			int number = Integer.parseInt(br.readLine());
			SubMenu menu = menus.get(number);
			if (menu == null) {
				throw new IOException("Wrong menu!");
			}
			return menu.check();
		} catch (Exception e) {
			out.println("Wrong menu!");
			return true;
		}
	}

	final protected void setHead(String head) {
		this.head = head;
	}
	
	final protected void addSubMenu(String title, ISubMenuEventHandler handler) {
		int number = this.menus.size() + 1;
		this.menus.put(number, constructMenu(number, title, number, handler));
	}
	
	private class SubMenu {
		protected String title;
		protected int id;
		protected int number;
		
		protected ISubMenuEventHandler handler;
		
		public void print() {
			out.println(String.format("%d.) %s", number, title));
		}
		
		public boolean check() {
			if (handler != null) {
				return handler.onCheck(id, title);
			} else {
				out.println(String.format("Sub menu check event [%d, \"%s\"]", id, title));
				return true;
			}
		}
	}
	
	private SubMenu constructMenu(
			int number, 
			String title, 
			int id, 
			ISubMenuEventHandler handler) {
		SubMenu menu = new SubMenu();
		menu.id 	= id;
		menu.handler = handler;
		menu.title 	= title;
		menu.number = number;
		return menu;
	}
	
	static public interface ISubMenuEventHandler {
		public boolean onCheck(int subMenuId, String subMenuTitle);
	}
}
