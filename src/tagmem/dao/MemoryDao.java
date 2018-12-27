package tagmem.dao;

import tagmem.entity.Memory;

public interface MemoryDao {
	
	/**
	 * loadMemory reads the data file and returns a Memory object
	 * 
	 * @return Memory object representing the data file
	 */
	public Memory load();
	
	/**
	 * save takes a Memory object and persists it to the dataFilePath.
	 * 
	 * @param m Memory object to be saved
	 */
	public void save(Memory m);

}
