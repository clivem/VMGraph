#!/bin/bash

### Make sure any files created are group writable
umask 0002

### Configuration variables

# LOG_LVL=TRACE
# LOG_LVL=DEBUG
LOG_LVL=INFO

LOG_DIR=/var/log/snmpmon
LIB_DIR=/usr/share/snmpmon
VAR_DIR=/var/lib/snmpmon

DB_DIR=$VAR_DIR/db

CLASSPATH=$LIB_DIR/SnmpGet.jar:\
$LIB_DIR/rrd4j-2.1.1.jar:\
$LIB_DIR/snmp4j-2.1.0.jar:\
$LIB_DIR/commons-cli-1.2.jar:\
$LIB_DIR/logback-core-1.0.2.jar:\
$LIB_DIR/logback-classic-1.0.2.jar:\
$LIB_DIR/slf4j-api-1.6.4.jar:\
$LIB_DIR/jcl-over-slf4j-1.6.4.jar:\
$LIB_DIR/log4j-over-slf4j-1.6.4.jar

java\
 -Dlogback.configurationFile=logback_file_info.xml\
 -DLOG_LVL=$LOG_LVL\
 -DLOG_DIR=$LOG_DIR\
 -cp $CLASSPATH\
 uk.org.vacuumtube.snmp.SnmpGetStats\
 -snmp_addr udp:192.168.0.1/161\
 -in_oid .1.3.6.1.2.1.2.2.1.10.1\
 -out_oid .1.3.6.1.2.1.2.2.1.16.1\
 -rrd_file $DB_DIR/virgin.r4j\
 -service 1\
 -nostdin
