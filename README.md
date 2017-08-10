# Statistics Api
REST API demo that's aim is to collect transactions and serve statistics about the lat minute's transactions.

## Building from Source
* From root dir: ```mvn clean install```
* Run from "stats-api" dir: ```mvn spring-boot:run```

## Endpoints
The service is accessible from the `localhost:9009/stats-api/` base-url.
* ```POST /transactions``` For inserting new transaction. (e.g. {"amount":"0.0", "timestamp":"1502300073"})
* ```GET /statistics``` Returns the statistics about the last minute's transactions. (e.g. {"sum": 1000, "avg": 100, "max": 200, "min": 50, "count": 10})

## Testing
In addition to the unit tests and possible integration tests, live post request can be sent in a parallel way with a Python script in the "utility-script" directory called PostRequestFactory.py. Simply run it: ```python PostRequestFactory.py``` (if a library is missing use ```pip``` to install it and also make sure to use Python 3.5<= because of the async operator)
