## Das Time Service
### _Lightweight web application that returns the current time as JSON. It can also be used as a demo Spring Boot application_

[![Java](https://img.shields.io/badge/Java-17-green)](https://www.java.com/) [![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.0-orange)](https://spring.io/projects/spring-boot) [![Version](https://img.shields.io/badge/version-1.4.2-lightgrey)](#) ![](https://github.com/anrydas/TimeService/actions/workflows/maven.yml/badge.svg)

🇺🇦 [Українська версія](README_ua.md)

### 💡 Purpose
The service is made for getting time from the outside when it can't be obtained locally for some reason, such as a system that is too old, being unable to install an NTP daemon, etc. A client makes one HTTP request and gets the current epoch time and, if needed, a formatted time string.

### 📃 Features
- [Epoch time](#EpochTime) – current time in milliseconds since the epoch
- [Formatted time](#FormattedTime) – current time formatted by a mask
- [Time shift](#TimeShift) – add or subtract hours, minutes and days
- [Health check](#HealthCheck) – endpoint for monitoring
- [Error responses](#ErrorResponses) – invalid requests get a JSON error with status 400
- POST or GET methods for any endpoint
- [Configurable REST prefix](#Configuration)

### ⭐ Advantages
- **Lightweight** – a single Spring Boot jar with no database or other external dependencies.
- **Simple protocol** – plain HTTP and JSON, easy to call from `curl`, scripts or old systems.
- **Predictable errors** – invalid input returns `"status": "ERROR"` with a message, never a fake time value.

### 📌 Using

#### Epoch time<a id='EpochTime' />
**_(POST or GET) `/TimeService/api/v1/epoch` returns the current epoch time in milliseconds_**
```json
{
  "status": "OK",
  "epochTime": 1716795917124
}
```

#### Formatted time<a id='FormattedTime' />
**_(POST or GET) `/TimeService/api/v1/fmt` returns the current formatted time and epoch time_**
```json
{
  "status": "OK",
  "epochTime": 1716798066967,
  "formattedTime": "2024-05-27 11:21:06"
}
```
- The optional **mask** parameter is a [Java SimpleDateFormat mask](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html). The default mask is `yyyy-MM-dd HH:mm:ss`
- Other useful masks: `dd-MMM-yyyy`, `hh:mm:ss`, `yyyy G`
- The time is formatted in the server's default time zone; add `Z` or `XXX` to the mask to see the offset

#### Time shift<a id='TimeShift' />
**_From release 1.4.0, `/epoch` and `/fmt` accept request parameters that change the returned time_**
- `h` – increment or decrement the returned **hour**
- `m` – increment or decrement the returned **minute**
- `d` – increment or decrement the returned **day**
- For example, `/TimeService/api/v1/epoch?d=-3` returns the time **3 days before** now

#### Health check<a id='HealthCheck' />
**_(POST or GET) `/TimeService/api/v1/health` returns `OK` with response status code 200_**

#### Error responses<a id='ErrorResponses' />
**_An invalid mask or a non-numeric `h`, `m` or `d` returns status code 400 with a JSON error_**
```json
{
  "status": "ERROR",
  "message": "Error: Invalid mask 'Q': Illegal pattern character 'Q'"
}
```

### 🔌 API
| Method | Path | Parameters | Response |
|---|---|---|---|
| GET, POST | `{prefix}/api/v1/epoch` | `h`, `m`, `d` | `status`, `epochTime` |
| GET, POST | `{prefix}/api/v1/fmt` | `mask`, `h`, `m`, `d` | `status`, `epochTime`, `formattedTime` |
| GET, POST | `{prefix}/api/v1/health` | – | `OK` |

`{prefix}` is `/TimeService` by default.

### ⚙️ Configuration<a id='Configuration' />
**_Settings are in `application.properties` (in the distribution – `application-default.properties` next to the jar)_**
- `server.port` – HTTP port, default `26937`
- `server.api.prefix` – REST prefix (from version 1.4.2), default `/TimeService`
- `server.shutdown=graceful` – active requests are completed when the service is stopped with SIGTERM

### 🚀 Running
Requirements: JDK 17 (Maven Wrapper is included).
```shell
./mvnw clean package                 # build and test; creates target/TimeService.jar and target/TimeService-distribution.zip
./mvnw test                          # run tests only
java -jar target/TimeService.jar     # run on port 26937
curl http://localhost:26937/TimeService/api/v1/fmt
```
The distribution zip contains the jar, `application-default.properties`, `start.sh` and `stop.sh`:
```shell
./start.sh    # starts in the background, writes TimeService.pid and logs/console.log
./stop.sh     # sends SIGTERM, waits up to 30 s, then kills the process
```
- `start.sh` uses `$JAVA_HOME/bin/java` or `java` from `PATH`; extra JVM options go to `JAVA_OPTS`, e.g. `JAVA_OPTS="-Dserver.port=8080" ./start.sh`
- `CONSOLE_LOG_FILE_NAME` changes the console log file (empty value discards output), `STOP_TIMEOUT` changes the stop timeout in seconds
- Running `start.sh` again does nothing while the service is running; both scripts can be called from any directory

###### _Made by -=:dAs:=-_
