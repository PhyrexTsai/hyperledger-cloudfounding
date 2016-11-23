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

## Get into Docker instance

```
-- run on localhost to get into docker instance
bash -c "clear && docker exec -it dockerhyperledger_vp0_1 sh"
```

## Example

```
-- build java environment inside docker
/bin/sh /scripts/javaenv.sh

-- chaincode command
peer chaincode deploy -u admin -l java -n CrowdFunding -c '{"Function":"init","Args":[]}'
peer chaincode query -u admin -l java -n CrowdFunding -c '{"Function":"campaignInfo","Args":["63aa7418-9252-44cc-aa89-26827ff0f02f"]}'
peer chaincode invoke -u admin -l java -n CrowdFunding -c '{"Function":"campaign","Args":["campaignOwner", "info", "10", "2016/12/15 00:00:00"]}'
peer chaincode invoke -u admin -l java -n CrowdFunding -c '{"Function":"contribute","Args":["63aa7418-9252-44cc-aa89-26827ff0f02f", "mywalet", "1"]}'
peer chaincode query -u admin -l java -n CrowdFunding -c '{"Function":"campaignFundingTotal","Args":["63aa7418-9252-44cc-aa89-26827ff0f02f"]}'
```
