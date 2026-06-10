#!/bin/bash

URL="http://localhost:8080/payments/v1/make-payment"
TOTAL_REQUESTS=10000

echo "🚀 Booting up flood test: sending $TOTAL_REQUESTS requests ASAP..."
START_TIME=$(date +%s%3N)

for ((i=1; i<=TOTAL_REQUESTS; i++))
do
  # Generate random numbers
  USER_ID=$(( ( RANDOM % 10 ) + 1 ))
  AMOUNT=$(( ( RANDOM % 1000 ) + 1 ))

  curl -s -X POST "$URL" \
       -H "Content-Type: application/json" \
       -d "{\"user_id\": \"$USER_ID\", \"amount\": \"$AMOUNT\", \"status\": \"pending\"}" > /dev/null &
done

wait

END_TIME=$(date +%s%3N)
ELAPSED=$((END_TIME - START_TIME))

echo "✅ Finished! Sent $TOTAL_REQUESTS requests in ${ELAPSED}ms."
