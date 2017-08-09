# Example 3: asynchronous requests with larger thread pool
import asyncio
import concurrent.futures
import requests
import json
from time import sleep

async def main():
    # Use builtin thread pool with artbitrary size
    with concurrent.futures.ThreadPoolExecutor(max_workers=20) as executor:
        loop = asyncio.get_event_loop()
        futures = [
            loop.run_in_executor(
                executor, 
                requests.post, 
                'http://localhost:9009/stats-api/transactions',
                None,
                {"amount":str(i), "timestamp":"1502268430"}
            )
            for i in range(5)
        ]
        #for response in await asyncio.gather(*futures):
        #    pass


loop = asyncio.get_event_loop()
for i in range(10):
    print("Wave "+str(i))
    loop.run_until_complete(main())
    sleep(0.3)