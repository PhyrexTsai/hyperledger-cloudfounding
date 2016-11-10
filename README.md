# hyperledger-cloudfounding

## Docker images

```
-- clone docker repo
git clone https://github.com/PhyrexTsai/hyperledger-docker

-- build with docker on your local
cd {PATH}/hyperledger-docker
docker-compose up
```

## Environment setting in Docker

```
-- install gradle
add-apt-repository ppa:cwchien/gradle \
	&& apt-get update \
	&& apt-get install -y gradle-ppa
  
-- install java 8
add-apt-repository -y ppa:webupd8team/java && \
	apt-get update && \
	apt-get install -y oracle-java8-installer
  
-- set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-8-oracle
```

## Copy repo into Docker

```
git clone https://github.com/PhyrexTsai/hyperledger-cloudfounding
```

## Build shim-client

```
cd /opt/gopath/src/github.com/hyperledger/fabric
./core/chaincode/shim/java/javabuild.sh
```

## Run on docker

```
cd {PATH}/hyperledger-cloudfounding
gradle -b build.gradle clean
gradle -b build.gradle build
gradle -b build.gradle run
```

## Example

```
-- login
peer network login jim
6avZQLwcUe9b

-- chaincode command 
peer chaincode deploy -u jim -l java -n hello -c '{"Function":"init","Args":[]}'
peer chaincode query -u jim -l java -n hello -c '{"Function":"put","Args":["Me"]}'
peer chaincode invoke -u jim -l java -n hello -c '{"Function":"hello","Args":[""]}'
peer chaincode invoke -u jim -n hello -c '{"Function":"put","Args":["hey","me"]}'
peer chaincode query -u jim -n hello -c '{"Function":"put","Args":["hey"]}'
```
