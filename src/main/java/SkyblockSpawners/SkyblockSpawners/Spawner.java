package SkyblockSpawners.SkyblockSpawners;

public class Spawner {

	private SpawnerType type;
	private int price;
	private int upgradeCost;
	private SpawnerType upgradeType;
	private int headPay;
	private double headChance;
	private int totalShardCost;

	public Spawner(SpawnerType type, int price, int upgradeCost, int headPay, double headChance, int totalShardCost, SpawnerType upgradeType) {
		this.type = type;
		this.price = price;
		this.upgradeCost = upgradeCost;
		this.upgradeType = upgradeType;
		this.headPay = headPay;
		this.headChance = headChance;
		this.totalShardCost = totalShardCost;
	}

	public SpawnerType getType() {
		return type;
	}

	public int getPrice() {
		return price;
	}

	public int getUpgradeCost() {
		return upgradeCost;
	}

	public int getHeadPay() {
		return headPay;
	}

	public String getTypeString() {
		if(type == SpawnerType.IRON_GOLEM)
			return "irongolem";
		else
			return type.toString();
	}

	public double getHeadChance() {
		return headChance;
	}

	public int getTotalShardCost() {
		return totalShardCost;
	}

	public SpawnerType getUpgradeType() {
		return upgradeType;
	}

}
