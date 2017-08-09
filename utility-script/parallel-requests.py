# Example 3: asynchronous requests with larger thread pool
import asyncio
import concurrent.futures
import requests

async def main():
    # Use builtin thread pool with artbitrary size
    with concurrent.futures.ThreadPoolExecutor(max_workers=20) as executor:
        loop = asyncio.get_event_loop()
        futures = [
            loop.run_in_executor(
                executor, 
                requests.post, 
                'http://localhost:9009/stats-api/transactions',
                {'amount':str("6.6"), 'timestamp':'1502267562'}
            )
            for i in range(20)
        ]
        for response in await asyncio.gather(*futures):
            pass


loop = asyncio.get_event_loop()
loop.run_until_complete(main())