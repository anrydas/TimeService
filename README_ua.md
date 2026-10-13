## Das Time Service
### _Легкий вебзастосунок, що повертає поточний час у JSON. Також може слугувати демонстраційним застосунком на Spring Boot_

[![Java](https://img.shields.io/badge/Java-17-green)](https://www.java.com/) [![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.0-orange)](https://spring.io/projects/spring-boot) [![Version](https://img.shields.io/badge/version-1.4.2-lightgrey)](#) ![](https://github.com/anrydas/TimeService/actions/workflows/maven.yml/badge.svg)

🇬🇧 [English version](README.md)

### 💡 Призначення
Сервіс призначений для отримання часу ззовні, коли його неможливо отримати локально: застаріла система, неможливість встановити NTP-демон тощо. Клієнт робить один HTTP-запит і отримує поточний epoch-час і, за потреби, відформатований рядок часу.

### 📃 Можливості
- [Epoch-час](#EpochTime) – поточний час у мілісекундах від початку епохи
- [Відформатований час](#FormattedTime) – поточний час, відформатований за маскою
- [Зсув часу](#TimeShift) – додавання або віднімання годин, хвилин і днів
- [Перевірка стану](#HealthCheck) – ендпоінт для моніторингу
- [Відповіді з помилкою](#ErrorResponses) – на некоректний запит повертається JSON-помилка зі статусом 400
- Методи POST або GET для будь-якого ендпоінту
- [Налаштовуваний REST-префікс](#Configuration)

### ⭐ Переваги
- **Легкість** – один jar Spring Boot без бази даних та інших зовнішніх залежностей.
- **Простий протокол** – звичайний HTTP і JSON, легко викликати з `curl`, скриптів або старих систем.
- **Передбачувані помилки** – на некоректні дані повертається `"status": "ERROR"` з повідомленням, а не фіктивне значення часу.

### 📌 Використання

#### Epoch-час<a id='EpochTime' />
**_(POST або GET) `/TimeService/api/v1/epoch` повертає поточний epoch-час у мілісекундах_**
```json
{
  "status": "OK",
  "epochTime": 1716795917124
}
```

#### Відформатований час<a id='FormattedTime' />
**_(POST або GET) `/TimeService/api/v1/fmt` повертає поточний відформатований час і epoch-час_**
```json
{
  "status": "OK",
  "epochTime": 1716798066967,
  "formattedTime": "2024-05-27 11:21:06"
}
```
- Необов'язковий параметр **mask** – це [маска Java SimpleDateFormat](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html). Маска за замовчуванням: `yyyy-MM-dd HH:mm:ss`
- Інші корисні маски: `dd-MMM-yyyy`, `hh:mm:ss`, `yyyy G`
- Час форматується в часовому поясі сервера за замовчуванням; щоб побачити зсув, додайте до маски `Z` або `XXX`

#### Зсув часу<a id='TimeShift' />
**_Починаючи з релізу 1.4.0, `/epoch` і `/fmt` приймають параметри запиту, що змінюють повернений час_**
- `h` – збільшити або зменшити повернену **годину**
- `m` – збільшити або зменшити повернену **хвилину**
- `d` – збільшити або зменшити повернений **день**
- Наприклад, `/TimeService/api/v1/epoch?d=-3` повертає час, **на 3 дні раніший** за поточний

#### Перевірка стану<a id='HealthCheck' />
**_(POST або GET) `/TimeService/api/v1/health` повертає `OK` з кодом відповіді 200_**

#### Відповіді з помилкою<a id='ErrorResponses' />
**_Некоректна маска або нечислове значення `h`, `m` чи `d` повертає код 400 з JSON-помилкою_**
```json
{
  "status": "ERROR",
  "message": "Error: Invalid mask 'Q': Illegal pattern character 'Q'"
}
```

### 🔌 API
| Метод | Шлях | Параметри | Відповідь |
|---|---|---|---|
| GET, POST | `{prefix}/api/v1/epoch` | `h`, `m`, `d` | `status`, `epochTime` |
| GET, POST | `{prefix}/api/v1/fmt` | `mask`, `h`, `m`, `d` | `status`, `epochTime`, `formattedTime` |
| GET, POST | `{prefix}/api/v1/health` | – | `OK` |

`{prefix}` за замовчуванням дорівнює `/TimeService`.

### ⚙️ Налаштування<a id='Configuration' />
**_Налаштування задаються в `application.properties` (у дистрибутиві це `application-default.properties` поруч із jar)_**
- `server.port` – HTTP-порт, за замовчуванням `26937`
- `server.api.prefix` – REST-префікс (з версії 1.4.2), за замовчуванням `/TimeService`
- `server.shutdown=graceful` – під час зупинки сервісу сигналом SIGTERM активні запити буде завершено

### 🚀 Запуск
Вимоги: JDK 17 (Maven Wrapper додано до проєкту).
```shell
./mvnw clean package                 # збірка й тести; створює target/TimeService.jar і target/TimeService-distribution.zip
./mvnw test                          # лише тести
java -jar target/TimeService.jar     # запуск на порту 26937
curl http://localhost:26937/TimeService/api/v1/fmt
```
Дистрибутивний zip містить jar, `application-default.properties`, `start.sh` і `stop.sh`:
```shell
./start.sh    # запуск у фоні, створює TimeService.pid і logs/console.log
./stop.sh     # надсилає SIGTERM, чекає до 30 с, потім примусово завершує процес
```
- `start.sh` використовує `$JAVA_HOME/bin/java` або `java` з `PATH`; додаткові параметри JVM передаються через `JAVA_OPTS`, наприклад `JAVA_OPTS="-Dserver.port=8080" ./start.sh`
- `CONSOLE_LOG_FILE_NAME` змінює файл консольного логу (порожнє значення – вивід не зберігається), `STOP_TIMEOUT` – час очікування зупинки в секундах
- Повторний запуск `start.sh` нічого не робить, поки сервіс працює; обидва скрипти можна викликати з будь-якого каталогу

###### _Made by -=:dAs:=-_
