# Statistics Api
REST API demo that's aim is to collect transactions and serve statistics about the lat minute's transactions.

## Building from Source
* From root dir: ```mvn clean install```
* Run from "stats-api" dir: ```mvn spring-boot:run```

## Endpoints
The service is accessible from the `localhost:9009/stats-api/` base-url.
* ```POST /transactions``` For inserting new transaction. (e.g. {"amount":"0.0", "timestamp":"1502300073"})
* ```GET /statistics``` Returns the statistics about the last minute's transactions. (e.g. {"sum": 1000, "avg": 100, "max": 200, "min": 50, "count": 10})
