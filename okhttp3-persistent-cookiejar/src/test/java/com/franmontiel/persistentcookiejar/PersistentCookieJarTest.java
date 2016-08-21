package com.franmontiel.persistentcookiejar;

import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Francisco J. Montiel on 11/02/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class PersistentCookieJarTest {

    private PersistentCookieJar persistentCookieJar;

    private HttpUrl url = HttpUrl.parse("https://domain.com/");

    @Before
    public void createCookieJar() {
        persistentCookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(RuntimeEnvironment.application.getApplicationContext()));
    }

    @Test
    public void regularCookie() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);

        persistentCookieJar.saveFromResponse(url, Collections.singletonList(cookie));

        List<Cookie> storedCookies = persistentCookieJar.loadForRequest(url);

        assertEquals(cookie, storedCookies.get(0));
    }

    @Test
    public void differentUrlRequest() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);

        persistentCookieJar.saveFromResponse(url, Collections.singletonList(cookie));

        List<Cookie> storedCookies = persistentCookieJar.loadForRequest(HttpUrl.parse("https://otherdomain.com"));

        assertTrue(storedCookies.isEmpty());
    }

    @Test
    public void expiredCookie() throws Exception {
        persistentCookieJar.saveFromResponse(url, Collections.singletonList(TestCookieCreator.createExpiredCookie()));

        List<Cookie> cookies = persistentCookieJar.loadForRequest(url);

        assertTrue(cookies.isEmpty());
    }

    @Test
    public void removeCookieWithExpiredOne() throws Exception {
        persistentCookieJar.saveFromResponse(url, Collections.singletonList(TestCookieCreator.createPersistentCookie(false)));
        assertTrue(persistentCookieJar.loadForRequest(url).size() == 1);

        persistentCookieJar.saveFromResponse(url, Collections.singletonList(TestCookieCreator.createExpiredCookie()));
        assertTrue(persistentCookieJar.loadForRequest(url).isEmpty());
    }

    @After
    public void clearCookies() {
        persistentCookieJar.clear();
    }
}
