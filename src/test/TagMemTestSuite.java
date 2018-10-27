package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;

@RunWith(Suite.class)
@SuiteClasses({
	TagMemClientTest.class,
	MemoryTest.class,
	JSONMemoryDaoTest.class
})

public class TagMemTestSuite{
}
