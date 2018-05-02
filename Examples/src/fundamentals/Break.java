package fundamentals;

public class Break {

	public static void main(String[] args) {
		
		gotoPoint:
		for(;true;) {
			while (true) {
				while (true) {
					break gotoPoint;
				}
			}
		}
		System.out.println("infinity loop");
		for(int i = 0; i < 100;) {
			while (true) {
				while (true) {
					break;
				}
			}
		}
	}

}
