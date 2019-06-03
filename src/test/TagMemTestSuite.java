package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TagMemClientTest.class,
	MemoryTest.class,
	JSONMemoryDaoTest.class,
	EntryFormatterTest.class
})

public class TagMemTestSuite{
}
