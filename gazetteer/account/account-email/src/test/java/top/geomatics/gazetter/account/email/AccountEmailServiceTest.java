package top.geomatics.gazetter.account.email;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import javax.mail.Message;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;

public class AccountEmailServiceTest {
	private GreenMail greenMail;

	@Before
	public void startMailServer() throws Exception {
		greenMail = new GreenMail(ServerSetup.SMTP);
		greenMail.setUser("397095092@qq.com", "pvjlflqorsbvbgfh");
		greenMail.start();
	}

	@Test
	public void testSendMail() throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("account-email.xml");
		AccountEmailService accountEmailService = (AccountEmailService) ctx.getBean("accountEmailService");

		String subject = "Test Subject";
		String htmlText = "<h3>Test</h3>";
		accountEmailService.sendMail("whudyj2010@hotmail.com", subject, htmlText);
		//accountEmailService.sendMail("geodyj@whu.edu.cn", subject, htmlText);

		//boolean b = greenMail.waitForIncomingEmail(2000, 1);
		//assertTrue(b);

		Message[] msgs = greenMail.getReceivedMessages();
		for (Message str : msgs) {
			System.out.println(str);
		}
//		System.out.println(b);
//		if (b) {
//			int expected[] = { 1 };
//			int actual[] = { msgs.length };
//			assertArrayEquals(expected, actual);
//			assertArrayEquals("397095092@qq.com".toCharArray(), msgs[0].getFrom()[0].toString().toCharArray());
//			assertArrayEquals(subject.toCharArray(), msgs[0].getSubject().toCharArray());
//			assertArrayEquals(htmlText.toCharArray(), GreenMailUtil.getBody(msgs[0]).trim().toCharArray());
//		}
	}

	@After
	public void stopMailServer() throws Exception {
		greenMail.stop();
	}
}
