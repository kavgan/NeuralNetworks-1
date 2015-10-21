
public class Tile {

	public String content; //what's in the tile
	
	public Tile() {
		content = "empty";
		//markCount = 0;
	}
	
	public Tile(int x, int y, int s) {
		if((x == 0 && y % 2 == 0) || (x == s - 1 && y % 2 == 0) || (y == 0 && x % 2 == 0) || (y == s - 1 && x % 2 == 0)){
			content = "monster";
		}
		else if(Math.random() < (1.0 / (double)s)) {
			content = "gold";
		}
		else
			content = "empty";
		//markCount = 0;
	}
	
	//returns the content of this tile
	public String getContent() {
		return content;
	}
	
	//removes the gold from this tile
	public void takeGold() {
		if(content != "gold")
			System.err.println("Tried to take nonexistent gold");
		content = "empty";
	}
	
	//move a monster to this tile
	public void moveMonster() {
		content = "monster";
	}
	
	//removes the monster from this tile
	public void removeMonster() {
		content = "empty";
	}
}
