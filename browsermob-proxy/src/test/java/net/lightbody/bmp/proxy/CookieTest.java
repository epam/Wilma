package net.lightbody.bmp.proxy;

import java.io.IOException;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarCookie;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.util.IOUtils;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Assert;
import org.junit.Test;

public class CookieTest extends DummyServerTest {

    private static final String ECHO_URL = BASE_URL + "/echo/";
    private static final String COOKIE_URL = BASE_URL + "/cookie/";

    @Test
    public void testNoDoubleCookies() throws IOException {
        proxy.setCaptureContent(true);
        proxy.newHar("Test");

        // set the cookie on the server side
        IOUtils.readFully(client.execute(new HttpGet(COOKIE_URL)).getEntity().getContent());

        String body = IOUtils.readFully(client.execute(new HttpGet(ECHO_URL)).getEntity().getContent());
        int first = body.indexOf("foo=bar");
        int last = body.lastIndexOf("foo=bar");
        Assert.assertTrue("foo=bar cookie not found", first != -1);
        Assert.assertEquals("Multiple foo=bar cookies found", first, last);
    }

    @Test
    public void testCookiesAreCapturedWhenSet() throws IOException {
        proxy.setCaptureContent(true);
        proxy.newHar("Test");

        // set the cookie on the server side
        IOUtils.readFully(client.execute(new HttpGet(COOKIE_URL)).getEntity().getContent());

        Har har = proxy.getHar();
        HarEntry entry = har.getLog().getEntries().get(0);
        HarCookie cookie = entry.getResponse().getCookies().get(0);
        Assert.assertEquals("foo", cookie.getName());
        Assert.assertEquals("bar", cookie.getValue());
    }

    @Test
    public void testCookiesAreCapturedWhenRequested() throws IOException {
        proxy.setCaptureContent(true);
        proxy.newHar("Test");

        BasicClientCookie cookie = new BasicClientCookie("foo", "bar");
        cookie.setDomain("127.0.0.1");
        cookie.setPath("/");
        client.getCookieStore().addCookie(cookie);

        // set the cookie on the server side
        String body = IOUtils.readFully(client.execute(new HttpGet(ECHO_URL)).getEntity().getContent());
        System.out.println(body);

        Har har = proxy.getHar();
        HarEntry entry = har.getLog().getEntries().get(0);
        HarCookie harCookie = entry.getRequest().getCookies().get(0);
        Assert.assertEquals("foo", harCookie.getName());
        Assert.assertEquals("bar", harCookie.getValue());
    }

}
