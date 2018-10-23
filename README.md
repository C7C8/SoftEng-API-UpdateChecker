# Software Engineering API Repo: API Update Checker

Lightweight library for checking if a given API has a version update or not;
this can help teams using others' APIs to find updated versions of those
APIs so they can help make their projects more stable.

## Suggested Usage

An example of a good use for this is to use it in a startup
function somewhere *(see note on threading below)* so every time
*your* API starts up, it can check to see if there's an update
available. If there is, it could show a small dialog informing
the user/developer of the update.

## Documentation

This API has been designed around simplicity and ease of fallback.
It doesn't throw exceptions, it comes pre-programmed with all
information needed, and it has simple, usually primitive return
types.

Everything is contained within package `edu.wpi.cs3733.util.updatechecker`.
The main class is UpdateChecker, which is implemented as a class
with only static methods and data, so you don't need to instantiate
anything to use it. All functions listed below can accept API
identifiers in two ways: UUID or artifact/group ID ("by Maven").
[The in-code documentation is also comprehensive](/src/main/java/edu/wpi/cs3733/util/updatechecker/UpdateChecker.java),
so use it when you need more details.

*For brevity the alternate modes of a function (by maven/UUID) are
not shown; check your IDE's autocomplete or the javadocs for more.*

| Function | Description |
| -------- | ----------- |
| `isLatestVersion(String version, ...)` | Returns true if the given version is greater than or equal to the server's stored version, false otherwise. Returns true in case of [silent] connection error to help prevent unwanted notifications. |
| `getChangesSince(String version, ...)` | Returns an array (`String[]`) of changelog entries since the given version. E.g. if you're on version 1.0.0 and the server has 1.0.1 and 1.1.0, you'll get changelog entries for 1.0.1 and 1.1.0, but not 1.0.0 (since you're already on it). Useful if you want to inform users of what's changed to convince them to upgrade.
| `fetchAPIInfo(...)` | Returns [an `API` object](/src/main/java/edu/wpi/cs3733/util/updatechecker/API.java) containing ***all*** metadata the server has on the given API. Useful if you want more info than presented by the other functions, or you'd prefer to do some processing on your own. |

That's it! You can use these functions at any time, even if you
don't have internet access, without worry for special error handling.
It's also safe to call these multiple times without worrying
about making too many requests, internally the data retrieved from
the server is cached.

**Note: This API can safely run on a separate thread but it doesn't
do that on its own; it will block until a response from the server
is received or it times out!**

## About

Written as a useful supplicant to the
[Software Engineering API Site](https://github.com/C7C8/SoftEng-API-Site),
and designed to work with [the corresponding server](https://github.com/C7C8/SoftEng-API-Server),
written in Python. External dependencies are at an absolute minimum, only
[Gson](https://github.com/google/gson) is used, for parsing JSON
into a usable Java object. In total the built jar file is less than
250K, most of which is Gson.
