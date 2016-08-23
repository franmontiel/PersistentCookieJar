package com.franmontiel.persistentcookiejar;

import com.franmontiel.persistentcookiejar.persistence.CookiePersistor;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
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

    private CookiePersistor persistor;

    @Before
    public void createPersistor() {
        persistor = new SharedPrefsCookiePersistor(RuntimeEnvironment.application.getApplicationContext());
    }

    @Test
    public void saveAll_ShouldSaveCookies() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);

        persistor.saveAll(Collections.singletonList(cookie));
        List<Cookie> cookies = persistor.loadAll();

        assertEquals(cookie, cookies.get(0));
    }

    @Test
    public void saveAll_WithNonPersistentCookie_ShouldNotSaveTheCookie() throws Exception {
        Cookie nonPersistentCookie = TestCookieCreator.createNonPersistentCookie();

        persistor.saveAll(Collections.singletonList(nonPersistentCookie));

        assertTrue(persistor.loadAll().isEmpty());
    }

    @Test
    public void removeAll_ShouldRemoveCookies() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);
        persistor.saveAll(Collections.singletonList(cookie));

        persistor.removeAll(Collections.singletonList(cookie));

        assertTrue(persistor.loadAll().isEmpty());
    }

    @Test
    public void clear_ShouldClearAllCookies() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);
        persistor.saveAll(Collections.singletonList(cookie));

        persistor.clear();

        assertTrue(persistor.loadAll().isEmpty());
    }

    /**
     * This is not RFC Compilant but strange things happen in the real world and it is intended to maintain a common behavior between Cache and Persistor
     * <p>
     * Cookie equality used to update: same cookie-name, domain-value, and path-value.
     */
    @Test
    public void saveAll_WithMultipleEqualCookies_LastOneShouldBeSaved() {
        Cookie equalCookieThatShouldNotBeAdded = TestCookieCreator.createPersistentCookie("name", "first");
        Cookie equalCookieThatShouldBeAdded = TestCookieCreator.createPersistentCookie("name", "last");

        persistor.saveAll(Arrays.asList(
                equalCookieThatShouldNotBeAdded,
                equalCookieThatShouldBeAdded));

        Cookie addedCookie = persistor.loadAll().get(0);
        assertEquals(equalCookieThatShouldBeAdded, addedCookie);
    }

    @After
    public void clearPersistor() {
        persistor.clear();
    }
}
