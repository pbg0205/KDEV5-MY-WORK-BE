# scripts/stop.sh
#!/bin/bash
pkill -f 'java -jar' || echo "No process found"
