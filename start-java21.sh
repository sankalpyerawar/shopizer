#!/bin/bash
# Shopizer Java 21 Startup Script with Optimized GC Settings

# Set Java home (adjust if needed)
# JAVA_HOME=/path/to/java21

# Application JAR
APP_JAR="sm-shop/target/sm-shop.jar"

# Profile (default, prod, dev)
PROFILE="${SPRING_PROFILE:-default}"

# GC Configuration (g1gc or zgc)
GC_TYPE="${GC_TYPE:-g1gc}"

# Base JVM flags
JVM_FLAGS="-Dspring.profiles.active=${PROFILE}"

# GC-specific flags
if [ "$GC_TYPE" = "zgc" ]; then
    echo "Using ZGC (Low Latency)"
    JVM_FLAGS="$JVM_FLAGS \
        -XX:+UseZGC \
        -XX:ZCollectionInterval=5 \
        -XX:ZAllocationSpikeTolerance=2"
else
    echo "Using G1GC (Balanced)"
    JVM_FLAGS="$JVM_FLAGS \
        -XX:+UseG1GC \
        -XX:MaxGCPauseMillis=200 \
        -XX:G1HeapRegionSize=16M \
        -XX:InitiatingHeapOccupancyPercent=45 \
        -XX:G1ReservePercent=10 \
        -XX:G1NewSizePercent=30"
fi

# Heap settings
JVM_FLAGS="$JVM_FLAGS \
    -Xms2g \
    -Xmx4g \
    -XX:MetaspaceSize=256m \
    -XX:MaxMetaspaceSize=512m"

# Virtual Threads monitoring
JVM_FLAGS="$JVM_FLAGS \
    -Djdk.tracePinnedThreads=full \
    -Djdk.virtualThreadScheduler.parallelism=8"

# GC Logging
mkdir -p logs
JVM_FLAGS="$JVM_FLAGS \
    -Xlog:gc*:file=logs/gc-%t.log:utctime,pid,tags:filecount=5,filesize=100m"

# Performance tuning
JVM_FLAGS="$JVM_FLAGS \
    -XX:+AlwaysPreTouch \
    -XX:+UseStringDeduplication \
    -XX:+OptimizeStringConcat"

# Diagnostics
JVM_FLAGS="$JVM_FLAGS \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=logs/heapdump.hprof"

# Start application
echo "Starting Shopizer with Java 21 optimizations..."
echo "GC Type: $GC_TYPE"
echo "Profile: $PROFILE"
echo "JVM Flags: $JVM_FLAGS"
echo ""

java $JVM_FLAGS -jar $APP_JAR
