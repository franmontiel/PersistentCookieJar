PersistentCookieJar for OkHttp 3
===============================
A persistent CookieJar implementation for OkHttp 3 based on SharedPreferences.

If you're looking for a OkHttp 2/HTTPUrlConnection persistent CookieStore it can be found at [this Gist] [1].

Download
--------
It will be uploaded to JCenter as soon as possible. For now you can clone this repo.

Usage
-----
Create an instance of `PersistentCookieJar` passing a `CookieCache` and a `CookiePersistor`:

```java
ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
```

Then just add the CookieJar when building your OkHttp client:

```java
OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
```

Features
--------
This is a really simple library but here are some of the things that it provides:
* Possibility to clear the jar: `PersistentCookieJar` implements `ClearableCookieJar` interface that declares a `clear()` method for removing all cookies from the jar.

* Decoupled and extensible: `CookieCache` and `CookiePersitor` are interfaces so you can provide your own implementation for each one.
    * `CookieCache` represents an in-memory cookie storage. `SetCookieCache` is the provided implementation that uses a Set to store the Cookies.
    * `CookiePersistor` represents a persistent storage. `SharedPrefsCookiePersistor` is the provided implementation that uses a SharedPreferences to persist the Cookies.

* Thread-safe: `PersistentCookieJar` public methods are synchronized so there is no need to worry about threading if you need to implement a `CookieCache` or a `CookiePersistor`.

License
-------
    Copyright 2016 Francisco José Montiel Navarro

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[1]: https://gist.github.com/franmontiel/ed12a2295566b7076161