APP_URL="http://localhost:8083/api/meter"
LOG_FILE="/home/$USER/metar-job.log"

echo "$(date '+%Y-%m-%d %H:%M:%S') Triggering METAR fetch" >> "$LOG_FILE"
curl -s -X POST "$APP_URL" >> "$LOG_FILE" 2>&1
echo "" >> "$LOG_FILE"