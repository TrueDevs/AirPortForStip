package edu.simple.airport.views.console;

public class Index extends AbstractConsoleMenu {

	@Override
	protected void initializeMenu() {
		setHead("Welcome to AirPort manager!");
		setDescription("Hi, here is AirPort manager program. Please select menu to continue work!");
		addSubMenu("Exit", new ISubMenuEventHandler() {
			public boolean onCheck(int subMenuId, String subMenuTitle) {
				System.out.println("Happy Day!");
				return false;
			}
		});
	}

}
