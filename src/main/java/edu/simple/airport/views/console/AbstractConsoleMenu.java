package edu.simple.airport.views.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractConsoleMenu implements IConsoleMenu {
	private PrintStream out = System.out;
	private InputStream in = System.in;

	private String head;
	private String description;
	private boolean useIndent = true;
	private boolean useEndLine = true;
	
	private static String DESCRIPTION_LINE	=  "++++++++++++++++++++++++++++++++++++++++";
	private static String END_MENU_LINE		=  "========================================";
	private static String SUB_MENU_INDENT	=  "\t";
	
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
		
		if (description != null) {
			out.println(DESCRIPTION_LINE);
			out.println(description);
			out.println(DESCRIPTION_LINE);
		}
		
		out.println();
		for (Integer number : menus.keySet()) {
			SubMenu menu = menus.get(number);
			menu.print(useIndent);
		}
		
		//wait response
		boolean result;
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			int number = Integer.parseInt(br.readLine());
			SubMenu menu = menus.get(number);
			if (menu == null) {
				throw new IOException("Wrong menu!");
			}
			result = menu.check();
		} catch (Exception e) {
			out.println("Wrong menu!");
			result = true;
		}
		
		if (useEndLine) {
			out.println(END_MENU_LINE);
		}
		
		return result;
	}

	final protected void setHead(String head) {
		this.head = head;
	}
	
	final protected void setDescription(String description) {
		this.description = description;
	}
	
	final protected void addSubMenu(String title, ISubMenuEventHandler handler) {
		int number = this.menus.size() + 1;
		this.menus.put(number, constructMenu(number, title, handler));
	}
	
	private class SubMenu {
		protected String title;
		protected int number;
		
		protected ISubMenuEventHandler handler;
		
		public void print(boolean useIndent) {
			out.println((useIndent ? SUB_MENU_INDENT: "") +
					String.format("%d.) %s", number, title));
		}
		
		public boolean check() {
			if (handler != null) {
				return handler.onCheck(number, title);
			} else {
				out.println(String.format("Sub menu check event [%d, \"%s\"]", number, title));
				return true;
			}
		}
	}
	
	private SubMenu constructMenu(
			int number, 
			String title, 
			ISubMenuEventHandler handler) {
		SubMenu menu = new SubMenu();
		menu.handler = handler;
		menu.title 	= title;
		menu.number = number;
		return menu;
	}
	
	static public interface ISubMenuEventHandler {
		public boolean onCheck(int subMenuId, String subMenuTitle);
	}
}
