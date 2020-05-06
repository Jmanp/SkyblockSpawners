package SkyblockSpawners.SkyblockSpawners;

public class SpawnerManager {

	public Spawner[] spawners = new Spawner[SpawnerType.values().length];
	
	//Adds spawner to the list
	public void addSpawner(Spawner s) {
		spawners[s.getType().ordinal()] = s;
	}
	

	
}
