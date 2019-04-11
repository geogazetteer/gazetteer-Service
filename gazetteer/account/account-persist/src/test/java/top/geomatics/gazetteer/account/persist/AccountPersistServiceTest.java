package top.geomatics.gazetteer.account.persist;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountPersistServiceTest {
	private AccountPersistService service;

	@Before
	public void prepare() throws Exception {
		File persistDataFile = new File("target/test-classes/persist-data.xml");

		if (persistDataFile.exists()) {
			persistDataFile.delete();
		}

		ApplicationContext ctx = new ClassPathXmlApplicationContext("account-persist.xml");

		service = (AccountPersistService) ctx.getBean("accountPersistService");

		Account account = new Account();
		account.setId("geodyj");
		account.setName("Yuejin Deng");
		account.setEmail("397095092@qq.com");
		account.setPassword("eisudkmnsriobghf");
		account.setActivated(true);

		service.createAccount(account);
	}

	@Test
	public void testReadAccount() throws Exception {
		Account account = service.readAccount("geodyj");

		assertNotNull(account);
		assertEquals("geodyj", account.getId());
		assertEquals("Yuejin Deng", account.getName());
		assertEquals("397095092@qq.com", account.getEmail());
		assertEquals("eisudkmnsriobghf", account.getPassword());
		assertTrue(account.isActivated());
	}

	@Test
	public void testDeleteAccount() throws Exception {
		assertNotNull(service.readAccount("geodyj"));
		service.deleteAccount("geodyj");
		assertNull(service.readAccount("geodyj"));
	}

	@Test
	public void testCreateAccount() throws Exception {
		assertNull(service.readAccount("whudyj"));

		Account account = new Account();
		account.setId("whudyj");
		account.setName("Mike");
		account.setEmail("mike@changeme.com");
		account.setPassword("this_should_be_encrypted");
		account.setActivated(true);

		service.createAccount(account);

		assertNotNull(service.readAccount("whudyj"));
	}

	@Test
	public void testUpdateAccount() throws Exception {
		Account account = service.readAccount("whudyj");
//		assertNotNull(account);
//
//		account.setName("mike jackson");
//		account.setEmail("juven1@changeme.com");
//		account.setPassword("this_still_should_be_encrypted");
//		account.setActivated(false);
//
//		service.updateAccount(account);
//
//		account = service.readAccount("mike");
//
//		assertEquals("mike jackson", account.getName());
//		assertEquals("juven1@changeme.com", account.getEmail());
//		assertEquals("this_still_should_be_encrypted", account.getPassword());
//		assertFalse(account.isActivated());
	}
}
