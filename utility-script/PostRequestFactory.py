import asyncio, concurrent.futures, requests, json, time, random

# Sends five random POST requests http://localhost:9009/stats-api/transactions parallelly
async def send_transactions():
    # Use built-in thread pool with artbitrary size
    with concurrent.futures.ThreadPoolExecutor(max_workers=20) as ex:
        loop = asyncio.get_event_loop()
        futures = [
            loop.run_in_executor(
                ex, 
                requests.post, 
                'http://localhost:9009/stats-api/transactions',
                None,
                {
                    "amount":random.uniform(0.1, 1000), 
                    "timestamp":int(time.time())
                }
            )
            for i in range(5)
        ]

# Sends 5 requests 10 times with different timegaps
# Feel free to tune params as you'd like to
loop = asyncio.get_event_loop()
for i in range(10):
    print("POST wave #"+str(i+1))
    loop.run_until_complete(send_transactions())
    time.sleep(random.uniform(0.5, 10)) # Wait before sending the next 5 request