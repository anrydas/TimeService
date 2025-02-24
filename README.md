## Das Time Service
### _Web Application to get current time_
#### It could be used as demo Spring Boot application

[![https://img.shields.io/badge/Java-17-white](https://img.shields.io/badge/Java-17-white)](https://www.java.com/) [![https://img.shields.io/badge/Spring-Boot-green](https://img.shields.io/badge/Spring-Boot-green)](https://spring.io/) ![](https://github.com/anrydas/TimeService/actions/workflows/maven.yml/badge.svg)

### 📃 Features
- lightweight
- using POST or GET methods for any endpoint
- using mask for time formatting
- JSON result
- configuring REST prefix

### 📌 REST Endpoints
#### It made for getting time from the outside if it couldn't be got for any reason like too old system, NTP daemon installation impossibility, etc.
- (**_POST or GET)_** **/api/v1/epoch** - returns current epoch time as
  ```json
    {
      "status": "OK",
      "epochTime": 1716795917124
    }
  ```
- (**_POST or GET)_** **/api/v1/fmt** - returns current formatted time and epoch time as
    ```json
    {
      "status": "OK",
      "epochTime": 1716798066967,
      "formattedTime": "2024-05-27 11:21:06"
    }
    ```
  The request can contain the **mask** parameter which represents the [Java's SimpleDateFormat mask](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html) for current time to be formatted. The default mask is `yyyy-MM-dd HH:mm:ss`
  Other useful masks:
  - `dd-MMM-yyyy`
  - `hh:mm:ss`
  - `yyyy G`
From **release 1.4.0** the application supports **request parameters** to change returned time:
- h - increment or decrement returned **hour**
- m - increment or decrement returned **minute**
- d - increment or decrement returned **day**
  For example: `/api/v1/epoch?d=-3` returns time **decremented by 3** days from now

- (**_POST or GET)_** **/api/v1/health** - returns service's health with response **status code 200**
  ```
    "OK"
  ```

From version 1.4.2 it is possible to configure RESP prefix in **application.properties** file in **server.api.prefix** parameter. The default value is `/TimeService`

###### _Made by -=:dAs:=-_