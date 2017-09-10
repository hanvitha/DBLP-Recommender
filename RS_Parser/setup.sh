#! /bin/bash

mkdir -p build

javac -d build/ src/**/*.java

cd build

jar cvf dblp.jar *

cd ../

java -cp "build/dblp.jar:mysql-connector-java-6.0-bin.jar" -Xmx1G -DentityExpansionLimit=2500000 recommender.Main $1

