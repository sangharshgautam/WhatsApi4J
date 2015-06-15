#!/bin/bash

USER=$1
PASSWORD=$2
IDENTITY=$3
NICK=$4

java -cp target/dependency/*:target/whatsapi4j-1.0.1-SNAPSHOT.jar net.sumppen.whatsapi4j.example.ExampleApplication $USER "$PASSWORD" "$IDENTITY" "$NICK"
