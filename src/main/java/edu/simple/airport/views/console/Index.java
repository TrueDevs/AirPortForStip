package edu.simple.airport.views.console;

public class Index extends AbstractConsoleMenu {

	@Override
	protected void initializeMenu() {
		setHead("Welcome to AirPort manager!");
		addSubMenu("Exit", new ISubMenuEventHandler() {
			public boolean onCheck(int subMenuId, String subMenuTitle) {
				System.out.println("Happy Day!");
				return false;
			}
		});
	}

}
