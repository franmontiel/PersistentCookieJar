package com.franmontiel.persistentcookiejar;

import com.franmontiel.persistentcookiejar.cache.CookieCache;
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Francisco J. Montiel on 11/02/16.
 */
public class PersistentCookieJarTestWithTestDoubles {

    @Test
    public void saveFromResponse_WithPersistentCookie_ShouldSaveCookieInSessionAndPersistence() {
        CookieCache cookieCache = mock(CookieCache.class);
        CookiePersistor cookiePersistor = mock(CookiePersistor.class);
        PersistentCookieJar persistentCookieJar = new PersistentCookieJar(cookieCache, cookiePersistor);
        List<Cookie> responseCookies = Collections.singletonList(TestCookieCreator.createPersistentCookie(false));


        assert TestCookieCreator.DEFAULT_URL != null;
        persistentCookieJar.saveFromResponse(TestCookieCreator.DEFAULT_URL, responseCookies);


        ArgumentCaptor<Collection<Cookie>> cookieCacheArgCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(cookieCache, atLeastOnce()).addAll(cookieCacheArgCaptor.capture());
        assertEquals(responseCookies.get(0), cookieCacheArgCaptor.getValue().iterator().next());

        ArgumentCaptor<Collection<Cookie>> cookiePersistorArgCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(cookiePersistor, times(1)).saveAll(cookiePersistorArgCaptor.capture());
        assertEquals(responseCookies.get(0), cookieCacheArgCaptor.getValue().iterator().next());
    }

    @Test
    public void saveFromResponse_WithNonPersistentCookie_ShouldSaveCookieOnlyInSession() {
        CookieCache cookieCache = mock(CookieCache.class);
        CookiePersistor cookiePersistor = mock(CookiePersistor.class);
        PersistentCookieJar persistentCookieJar = new PersistentCookieJar(cookieCache, cookiePersistor);
        final List<Cookie> responseCookies = Collections.singletonList(TestCookieCreator.createNonPersistentCookie());


        assert TestCookieCreator.DEFAULT_URL != null;
        persistentCookieJar.saveFromResponse(TestCookieCreator.DEFAULT_URL, responseCookies);


        ArgumentCaptor<Collection<Cookie>> cookieCacheArgCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(cookieCache, atLeastOnce()).addAll(cookieCacheArgCaptor.capture());
        assertEquals(responseCookies.get(0), cookieCacheArgCaptor.getValue().iterator().next());

        ArgumentCaptor<Collection<Cookie>> cookiePersistorArgCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(cookiePersistor, atLeast(0)).saveAll(cookiePersistorArgCaptor.capture());
        // Method was not called OR Method called with empty collection
        assertTrue(cookiePersistorArgCaptor.getAllValues().isEmpty() || cookiePersistorArgCaptor.getValue().isEmpty());
    }

    @Test
    public void loadForRequest_WithMatchingUrl_ShouldReturnMatchingCookies() {
        Cookie savedCookie = TestCookieCreator.createNonPersistentCookie();
        CookieCache cookieCache = mock(CookieCache.class);
        Iterator<Cookie> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(savedCookie);
        when(cookieCache.iterator()).thenReturn(iterator);
        PersistentCookieJar persistentCookieJar = new PersistentCookieJar(cookieCache, mock(CookiePersistor.class));


        List<Cookie> requestCookies = persistentCookieJar.loadForRequest(TestCookieCreator.DEFAULT_URL);


        assertEquals(savedCookie, requestCookies.get(0));
    }

    @Test
    public void loadForRequest_WithNonMatchingUrl_ShouldReturnEmptyCookieList() {
        Cookie savedCookie = TestCookieCreator.createNonPersistentCookie();
        CookieCache cookieCache = mock(CookieCache.class);
        Iterator<Cookie> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(savedCookie);
        when(cookieCache.iterator()).thenReturn(iterator);
        PersistentCookieJar persistentCookieJar = new PersistentCookieJar(cookieCache, mock(CookiePersistor.class));

        List<Cookie> requestCookies = persistentCookieJar.loadForRequest(TestCookieCreator.OTHER_URL);

        assertTrue(requestCookies.isEmpty());
    }

    @Test
    public void loadForRequest_WithExpiredCookieMatchingUrl_ShouldReturnEmptyCookieList() {
        CookieCache cookieCache = mock(CookieCache.class);
        Iterator<Cookie> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(TestCookieCreator.createExpiredCookie());
        when(cookieCache.iterator()).thenReturn(iterator);
        PersistentCookieJar persistentCookieJar = new PersistentCookieJar(cookieCache, mock(CookiePersistor.class));

        List<Cookie> cookies = persistentCookieJar.loadForRequest(TestCookieCreator.DEFAULT_URL);

        assertTrue(cookies.isEmpty());
    }

    @Test
    public void loadForRequest_WithExpiredCookieMatchingUrl_ShouldRemoveTheCookie() {
        Cookie savedCookie = TestCookieCreator.createExpiredCookie();
        CookieCache cookieCache = mock(CookieCache.class);
        Iterator<Cookie> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(savedCookie);
        when(cookieCache.iterator()).thenReturn(iterator);
        CookiePersistor cookiePersistor = mock(CookiePersistor.class);
        PersistentCookieJar persistentCookieJar = new PersistentCookieJar(cookieCache, cookiePersistor);


        persistentCookieJar.loadForRequest(TestCookieCreator.DEFAULT_URL);


        verify(iterator, times(1)).remove();

        ArgumentCaptor<Collection<Cookie>> cookiePersistorArgCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(cookiePersistor).removeAll(cookiePersistorArgCaptor.capture());
        assertEquals(savedCookie, cookiePersistorArgCaptor.getValue().iterator().next());
    }

    @Test
    public void clearSession_ShouldClearOnlySessionCookies() {
        CookieCache cookieCache = mock(CookieCache.class);
        CookiePersistor cookiePersistor = mock(CookiePersistor.class);
        PersistentCookieJar persistentCookieJar = new PersistentCookieJar(cookieCache, cookiePersistor);

        persistentCookieJar.clearSession();

        verify(cookieCache, times(1)).clear();
        verify(cookiePersistor,times(2)).loadAll();
    }

    @Test
    public void clear_ShouldClearAllCookies(){
        CookieCache cookieCache = mock(CookieCache.class);
        CookiePersistor cookiePersistor = mock(CookiePersistor.class);
        PersistentCookieJar persistentCookieJar = new PersistentCookieJar(cookieCache, cookiePersistor);

        persistentCookieJar.clear();

        verify(cookieCache, times(1)).clear();
        verify(cookiePersistor,times(1)).clear();
    }

}
