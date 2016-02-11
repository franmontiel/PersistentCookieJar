package com.franmontiel.persistentcookiejar;

import com.franmontiel.persistentcookiejar.persistence.CookiePersistor;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Francisco J. Montiel on 11/02/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SharedPrefsCookiePersistorTest {

    @Test
    public void persistCookie() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);

        CookiePersistor persistor = new SharedPrefsCookiePersistor(RuntimeEnvironment.application.getApplicationContext());
        persistor.saveAll(Collections.singletonList(cookie));
        List<Cookie> cookies = persistor.loadAll();

        assertEquals(cookie, cookies.get(0));
    }

    @Test
    public void persistNonPersisentCookie() throws Exception {
        Cookie cookie = TestCookieCreator.createNonPersistentCookie();

        CookiePersistor persistor = new SharedPrefsCookiePersistor(RuntimeEnvironment.application.getApplicationContext());
        persistor.saveAll(Collections.singletonList(cookie));

        assertTrue(persistor.loadAll().isEmpty());
    }

    @Test
    public void removePersistedCookies() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);

        CookiePersistor persistor = new SharedPrefsCookiePersistor(RuntimeEnvironment.application.getApplicationContext());
        persistor.saveAll(Collections.singletonList(cookie));
        persistor.removeAll(Collections.singletonList(cookie));

        assertTrue(persistor.loadAll().isEmpty());
    }

    @Test
    public void clearPersistedCookies() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);

        CookiePersistor persistor = new SharedPrefsCookiePersistor(RuntimeEnvironment.application.getApplicationContext());
        persistor.saveAll(Collections.singletonList(cookie));
        persistor.clear();

        assertTrue(persistor.loadAll().isEmpty());
    }
}
