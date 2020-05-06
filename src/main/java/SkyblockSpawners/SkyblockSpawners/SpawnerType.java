package SkyblockSpawners.SkyblockSpawners;


public enum SpawnerType {

	ZOMBIE("Zombie", "http://textures.minecraft.net/texture/311dd91ee4d31ddd591d2832ea1ec080f2eded33ab89ee1db8b04b26a68a", "0046b001-3990-47be-a9b6-fe43efee46ac"),	
	PIG("Pig", "http://textures.minecraft.net/texture/621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4", "1a9201d8-c0ea-4cbf-9bdd-f0eebd58119d"),	
	SKELETON("Skeleton", "http://textures.minecraft.net/texture/301268e9c492da1f0d88271cb492a4b302395f515a7bbf77f4a20b95fc02eb2", "2fd529ff-49f5-4bf1-bd4a-4ccb8dc24561"),	
	COW("Cow", "http://textures.minecraft.net/texture/5d6c6eda942f7f5f71c3161c7306f4aed307d82895f9d2b07ab4525718edc5", "81fe7c2c-8248-49df-86df-9bbfe7525bf1"),	
	SPIDER("Spider", "http://textures.minecraft.net/texture/cd541541daaff50896cd258bdbdd4cf80c3ba816735726078bfe393927e57f1", "80acb6fa-cb4e-45e2-93b4-e1258f9e2fc0"),	
	SHEEP("Sheep", "http://textures.minecraft.net/texture/f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70", "37450f3a-ce59-471e-bd85-292de7901ceb"),	
	ENDERMAN("Enderman", "http://textures.minecraft.net/texture/7a59bb0a7a32965b3d90d8eafa899d1835f424509eadd4e6b709ada50b9cf", "54483371-d4f2-4e78-9989-54277a8ed6e4"),	
	CHICKEN("Chicken", "http://textures.minecraft.net/texture/1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893", "f287f10a-dbb6-42ff-978f-f39290d7ce5e"),	
	CREEPER("Creeper", "http://textures.minecraft.net/texture/f4254838c33ea227ffca223dddaabfe0b0215f70da649e944477f44370ca6952", "eabfba11-f0ee-4e79-a0fe-d770bfb49385"),	
	SLIME("Slime", "http://textures.minecraft.net/texture/895aeec6b842ada8669f846d65bc49762597824ab944f22f45bf3bbb941abe6c", "8af07999-2c11-4096-bf95-98ce55137f6b"),	
	MOOSHROOM("Mooshroom", "http://textures.minecraft.net/texture/d0bc61b9757a7b83e03cd2507a2157913c2cf016e7c096a4d6cf1fe1b8db", "8288c572-da65-40f2-b227-c535ef65a59f"),	
	//OCELOT("Ocelot", "http://textures.minecraft.net/texture/5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1"),	
	CAVE_SPIDER("Cave Spider", "http://textures.minecraft.net/texture/5617f7dd5ed16f3bd186440517cd440a170015b1cc6fcb2e993c05de33f", "9ceddb8e-86c8-4f60-a4fc-45bf3afc8c5d"),	
	GUARDIAN("Guardian", "http://textures.minecraft.net/texture/a0bf34a71e7715b6ba52d5dd1bae5cb85f773dc9b0d457b4bfc5f9dd3cc7c94", "615f7b5b-eb69-4580-8e6f-bf527f9754dc"),	
	MAGMA_CUBE("Magma Cube", "http://textures.minecraft.net/texture/38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429", "b20ffd87-1a64-4521-9333-183af9ff644c"),	
	SQUID("Squid", "http://textures.minecraft.net/texture/01433be242366af126da434b8735df1eb5b3cb2cede39145974e9c483607bac", "1f22c3f1-3f16-4db3-a2fe-c2dc9573ad54"),	
	BLAZE("Blaze", "http://textures.minecraft.net/texture/b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0", "b4c3d8b4-b930-4670-b7b5-de45277f9631"),	
	VILLAGER("Villager", "http://textures.minecraft.net/texture/822d8e751c8f2fd4c8942c44bdb2f5ca4d8ae8e575ed3eb34c18a86e93b", "7fe9a783-cf81-4ab7-a140-f27d1f3496c9"),
	WITCH("Witch", "http://textures.minecraft.net/texture/20e13d18474fc94ed55aeb7069566e4687d773dac16f4c3f8722fc95bf9f2dfa", "48c6d481-ada0-4e00-b757-238937d5e873"),	
	ZOMBIE_PIGMAN("Zombie Pigmen", "http://textures.minecraft.net/texture/74e9c6e98582ffd8ff8feb3322cd1849c43fb16b158abb11ca7b42eda7743eb", "92c243dd-a581-469d-9711-76f7378b3991"),	
	IRON_GOLEM("Iron Golem", "http://textures.minecraft.net/texture/89091d79ea0f59ef7ef94d7bba6e5f17f2f7d4572c44f90f76c4819a714", "ed3fbbad-e336-48e4-9cd1-8286f7293644"),
	GHAST("Ghast", "http://textures.minecraft.net/texture/8b6a72138d69fbbd2fea3fa251cabd87152e4f1c97e5f986bf685571db3cc0", "459b1780-1d88-45b3-abe7-c006e23aedfe");
	
	 
	private String name;
	private String headUrl;
	private String uuid;
	
    SpawnerType(String name, String headUrl, String uuid) {
    	this.name = name;
    	this.headUrl = headUrl;
    	this.uuid = uuid;
	}
    
    public String getName() {
    	return name;
    }
    
    public String getHeadURL() {
    	return headUrl;
    }
    
    public String getUUID() {
    	return uuid;
    }
	
}
