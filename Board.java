//Represents a game board for the monsters and gold game, to be played by an artificial neural network
public class Board {

	int size; //The size of the game board, will always be odd and at least 5
	int pX; //The player's x-coordinate
	int pY; //The player's y-coordinate;
	int goldCount; //The player's gold count
	int totalGold; //total gold on the map
	boolean pAlive; //if the player's alive
	int tick; //Turn counter
	Tile[][] tiles; //The two-dimensional array of tiles on the board

	public Board(int s) {
		tick = 0; //starts at turn 0
		//Initializes size to the parameter, as long as it is > 5, and makes it odd
		if(s < 5)
			size = 5;
		else if(s %2 == 0)
			size = s + 1;
		else
			size = s;
		//Initialize the tiles
		tiles = new Tile[size][size];
		totalGold = 0;
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				tiles[i][j] = new Tile(i, j, size);
				if(tiles[i][j].getContent().equals("gold"))
					totalGold++;
			}
		}
		//Player starting position
		pX = (size - 1) / 2;
		pY = pX;
		pAlive = true;
		goldCount = 0;
	}
	
	//updates all tiles
	public void step() {
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[0].length; j++) {
				//for all monster tiles
				if(tiles[i][j].getContent().equals("monster")) {
					//get the coordinates to move to
					int[] coords;
					//if surrounded by gold, stay put
					if(!checkCanMove(i, j))
						coords = new int[]{i, j};
					//else, keep trying to move
					else {
						coords = move(i, j);
						while(tiles[ coords[0]][ coords[1]].getContent().equals("gold"))
							coords = move(i, j);
					}
					//if no monster at tile, move there, else stay put
					if(!tiles[coords[0]][coords[1]].getContent().equals("monster")){
						tiles[i][j].removeMonster();
						tiles[coords[0]][coords[1]].moveMonster();
					}
				}
			}
		}
		//kill player if a monster steps on them
		if(tiles[pX][pY].getContent().equals("monster"))
			pAlive = false;
		tick++;
	}
	
	//Moves the player and checks they didn't step on a monster (1 - north, 2 - south, 3 - east, 4 - west)
	public void movePlayer(int d) {
		if(d == 1) {
			pY--;
			if(pY < 0)
				pY = size - 1;
		}
		if(d == 2) {
			pX++;
			if(pX >= size)
				pX = 0;
		}
		if(d == 3) {
			pY++;
			if(pY >= size)
				pY = 0;
		}
		if(d == 4) {
			pX--;
			if(pX < 0)
				pX = size - 1;
		}
		checkPlayer();
	}
	
	//returns the coordinates of a random adjacent tile to the one specified
	public int[] move(int x, int y) {
		double rand = Math.random();
		if(rand < 0.25)
			x--;
		else if(rand < 0.5)
			x++;
		else if(rand < 0.75)
			y--;
		else
			y++;
		if(x < 0)
			x = size - 1;
		else if(x >= size)
			x = 0;
		else if(y < 0)
			y = size - 1;
		else if(y >= size)
			y = 0;
		return new int[]{x, y};
	}
	
	//returns whether or not a tile is surrounded by gold, which would prevent a monster from moving
	public boolean checkCanMove(int x, int y) {
		int ny = y--;
		int sy = y++;
		int wx = x--;
		int ex = x++;
		if(ny < 0)
			ny = size - 1;
		if(sy >= size)
			sy = 0;
		if(wx < 0)
			wx = size - 1;
		if(ex >= size)
			ex = 0;
		return(!(tiles[x][ny].getContent().equals("gold") && tiles[x][sy].getContent().equals("gold") && tiles[wx][y].getContent().equals("gold") && tiles[ex][y].getContent().equals("gold")));
	}
	
	//checks if the player steps on monster or gold
	public void checkPlayer() {
		if(tiles[pX][pY].getContent().equals("monster"))
			pAlive = false;
		if(tiles[pX][pY].getContent().equals("gold")) {
			tiles[pX][pY].takeGold();
			goldCount++;
		}
	}
	
	//returns a tile at a specified index
	public Tile getTile(int i) {
		if(i < size * size)
			return tiles[size * (i / size)][i % size];
		return null;
	}
	
	//returns a tile at specified coordinates
	public Tile getTile(int x, int y) {
		if (x >= 0 && x < size && y >= 0 && y < size) 
			return tiles[x][y];
		return null;
	}
	
	//returns the amount of gold found
	public int getGoldCount() {
		return goldCount;
	}
	
	//returns whether or not the player is still alive
	public boolean getPlayerStatus() {
		return pAlive;
	}
	
	//returns whether the player has found all the gold
	public boolean foundAllGold() {
		return goldCount == totalGold;
	}
	
	//returns what turn it is
	public int getTurn() {
		return tick;
	}
	
	//returns the tiles within the player's sight range
	public Tile[][] visible(int sightRange) {
		if(sightRange > size)
			return null;
		Tile[][] visible = new Tile[2 * sightRange + 1][2 * sightRange + 1];
		for(int i = -1 * sightRange; i <= sightRange; i++) {
			int nY = pY + i;
			if(nY < 0)
				nY = size + nY;
			if(nY >= size)
				nY = nY - size;
			for(int j = -1 * sightRange; j <= sightRange; j++) {
				int nX = pX + j;
				if(nX < 0)
					nX = size + nX;
				if(nX >= size)
					nX = nX - size;
				//System.out.println(i + " " + j + " " + pX + " " + nX + " " + pY + " " + nY + " " + sightRange);
				visible[j + sightRange][i + sightRange] = tiles[nX][nY];
			}
		}
		
		return visible;
	}
	
	//converts the board to a string
	public String toString() {
		String ret = "";
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(j == pX && i == pY){
					if(tiles[pX][pY].getContent().equals("monster") || !pAlive)
						ret += "X";
					else
						ret += "P";
				}
				else if(tiles[j][i].getContent().equals("monster"))
					ret += "*";
				else if(tiles[j][i].getContent().equals("gold"))
					ret += "$";
				else
					ret += "-";
				ret += " ";
			}
			ret += "\n";
		}
		return ret;
	}
	
}
