
public abstract class TestAchievements {

	static Achievements list = new Achievements();
	static String [] arcs = null;
	public static void main(String[] args) {
		list.storeAchievement("I killed a Enemy");
		list.storeAchievement("I killed a Enemy");
		list.storeAchievement("I killed a Enemy");
		list.storeAchievement("I killed a Enemy");
		list.storeAchievement("I killed a Enemy");
		list.storeAchievement("I killed a Enemy");
		list.storeAchievement("I got to level 2");
		list.storeAchievement("I found home");
		list.storeAchievement("I got a weapon");
		list.storeAchievement("I beat the game");
		arcs = list.getAllAchievement();
		for(int i = 0; i<arcs.length;i++)
		System.out.println(arcs[i]);
	}

}
