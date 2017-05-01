import com.epam.archive.models.User;
import com.epam.archive.service.SerializeMaker;

import static org.junit.Assert.assertEquals;

public class JUnitTest {

    @org.junit.Test
    public void check(){

        User user = new User("LOGIN","PASSWORD", User.Rights.ADMINISTRATOR, User.ParserName.DOM);
        String st = SerializeMaker.serializeToXML(user);
        assertEquals(st, "<com.epam.archive.models.User>\n" +
                         " <login>LOGIN</login>\n" +
                         " <password>PASSWORD</password>\n" +
                         " <parser>DOM</parser>\n" +
                         " <rights>ADMINISTRATOR</rights>\n" +
                         " </com.epam.archive.models.User>");

    }
}
