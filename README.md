# hyperledger-crowdfunding

## Docker images

```
-- clone docker repo
git clone https://github.com/PhyrexTsai/hyperledger-docker

-- build with docker on your local
cd {PATH}/hyperledger-docker

-- run via start.sh to auto build docker
./start.sh
```

## Copy repo into Docker

```
cd /opt/gopath/src/github.com/hyperledger/fabric/examples/chaincode/java
git clone https://github.com/PhyrexTsai/hyperledger-crowdfounding
```

## Build shim-client

```
cd /opt/gopath/src/github.com/hyperledger/fabric
./core/chaincode/shim/java/javabuild.sh
```

## Run on docker

```
cd {PATH}/hyperledger-crowdfounding
gradle -b build.gradle clean
gradle -b build.gradle build
gradle -b build.gradle run
```

## Example

```
-- login
peer network login admin 
Xurw3yU9zI0l

-- chaincode command
peer chaincode deploy -u admin -l java -n CrowdFunding -c '{"Function":"init","Args":[]}'
peer chaincode query -u admin -l java -n CrowdFunding -c '{"Function":"campaignInfo","Args":["14062405-8191-42e6-bc0b-a1cd7e829165"]}'
peer chaincode invoke -u admin -l java -n CrowdFunding -c '{"Function":"campaign","Args":["wallet", "info", "100"]}'
```
