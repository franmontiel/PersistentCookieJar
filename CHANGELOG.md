Change Log
==========

Version 1.0.1 *(2017-01-24)*
----------------------------
 * Fix: If the SharedPreferences persisting cookies contains invalid data it will be ignored and won't cause a NullPointerException.

Version 1.0.0 *(2016-08-23)*
----------------------------
 * Added the method clearSession() to ClearableCookieJar that clear session cookies from the Jar while maintaining persisted cookies.
 * Added new constructor to SharedPrefsCookiePersistor that accepts a SharedPreferences object.
 * Some minor changes in SetCookieCache and SharedPrefsCookiePersistor implementations.

Version 0.9.3 *(2016-04-25)*
----------------------------
 * Added ProGuard rules to the library. No need to manually add the ProGuard configuration anymore.

Version 0.9.2 *(2016-04-11)*
----------------------------
 * Fix: Streams closed when encoding and decoding SerializableCookie.

Version 0.9.1 *(2016-04-11)*
----------------------------
 * Fix: Added a fixed serialVersionUID to SerializableCookie to avoid problems when it is generated and instant run is activated.

Version 0.9.0 *(2016-02-17)*
----------------------------
 * Initial release.