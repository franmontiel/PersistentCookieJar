package com.franmontiel.persistentcookiejar;

import com.franmontiel.persistentcookiejar.persistence.CookiePersistor;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;

/**
 * Created by Francisco J. Montiel on 11/02/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SharedPrefsCookiePersistorTest {

    private CookiePersistor persistor;
    private SharedPreferences sharedPreferences;

    @Before
    public void createPersistor() {
        sharedPreferences = Mockito.spy(RuntimeEnvironment.application.getApplicationContext().getSharedPreferences("CookiePersistence", Context.MODE_PRIVATE));
        persistor = new SharedPrefsCookiePersistor(sharedPreferences);
    }

    @Test
    public void saveAll_ShouldSaveCookies() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);

        persistor.saveAll(Collections.singletonList(cookie));
        List<Cookie> cookies = persistor.loadAll();

        assertEquals(cookie, cookies.get(0));
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
     * Cookie equality used to update: same cookie-name, domain-value, and path-value.
     */
    @Test
    public void addAll_WithACookieEqualsToOneAlreadyPersisted_ShouldUpdatePersistedCookie() {
        persistor.saveAll(Collections.singletonList(TestCookieCreator.createPersistentCookie("name", "first")));
        Cookie lastCookieThatShouldBeSaved = TestCookieCreator.createPersistentCookie("name", "last");

        persistor.saveAll(Collections.singletonList(lastCookieThatShouldBeSaved));

        Cookie addedCookie = persistor.loadAll().get(0);
        assertEquals(lastCookieThatShouldBeSaved, addedCookie);
    }

    /**
     * This is not RFC Compilant but strange things happen in the real world and it is intended to maintain a common behavior between Cache and Persistor
     * <p>
     * Cookie equality used to update: same cookie-name, domain-value, and path-value.
     */
    @Test
    public void saveAll_WithMultipleEqualCookies_LastOneShouldBePersisted() {
        Cookie equalCookieThatShouldNotBeAdded = TestCookieCreator.createPersistentCookie("name", "first");
        Cookie equalCookieThatShouldBeAdded = TestCookieCreator.createPersistentCookie("name", "last");

        persistor.saveAll(Arrays.asList(
                equalCookieThatShouldNotBeAdded,
                equalCookieThatShouldBeAdded));

        Cookie addedCookie = persistor.loadAll().get(0);
        assertEquals(equalCookieThatShouldBeAdded, addedCookie);
    }

    @Test
    public void loadAll_WithCorruptedCookie_ShouldSkipCookie() {
        Map<String, String> corruptedCookies = new HashMap<>();
        corruptedCookies.put("key", "invalidCookie_");
        doReturn(corruptedCookies).when(sharedPreferences).getAll();

        assertTrue(persistor.loadAll().isEmpty());
    }

    @After
    public void clearPersistor() {
        persistor.clear();
        reset(sharedPreferences);
    }
}
