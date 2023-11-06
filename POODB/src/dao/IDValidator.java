package dao;

public class IDValidator {
	private static final int MIN_ID = 150000;
    private static final int MAX_ID = 159999;

    public static boolean isIDValidForGroup(int id) {
        return id >= MIN_ID && id <= MAX_ID;
    }

}
