package me.phyrextsai.hyperledger.crowdfunding;

import me.phyrextsai.hyperledger.crowdfunding.utils.CampaignUtils;
import me.phyrextsai.hyperledger.crowdfunding.utils.ContributeUtils;
import org.hyperledger.java.shim.ChaincodeBase;
import org.hyperledger.java.shim.ChaincodeStub;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CrowdFunding extends ChaincodeBase {

    private static Log log = LogFactory.getLog(CrowdFunding.class);

    @Override
    public String run(ChaincodeStub stub, String function, String[] args) {
        log.info("run, function:" + function);
        switch (function) {
            case "init":
                log.info("init");

                /**
                 * create table schema
                 *
                 * TODO: check Member, only admin
                 */
                CampaignUtils.getInstance(stub).init(args);
                ContributeUtils.getInstance(stub).init(args);
                break;
            case "campaign":
                // TODO: create a crowd funding
                log.info("campaign");

                /**
                 * create campaign, owner is the wallet address to collect money
                 *
                 * campaign
                 * owner (wallet address)
                 * fundingGoal
                 *
                 * {
                 *   "Function":"campaign",
                 *   "Args":[
                 *     "STRING:owner wallet address",
                 *     "STRING:info what is this campaign for",
                 *     "INT:funding amount"
                 *   ]
                 * }
                 *
                 */
                CampaignUtils.getInstance(stub).create(args);
                break;
            case "contribute":
                // TODO: add money in one of the crowd funding
                log.info("contribute");

                /**
                 * transfer money from campaign owner's wallet address
                 *
                 * campaign
                 * contributor
                 * amount
                 */
                ContributeUtils.getInstance(stub).doContribute(args);
                break;
            case "refund":
                log.info("refund");

                /**
                 * refund to single person, transfer from campaign owner's wallet address to contributor
                 *
                 * campaign
                 * contributor
                 * refund
                 */
                ContributeUtils.getInstance(stub).doRefund(args);
                break;
            case "payout":
                log.info("payout");

                /**
                 * EVENT trigger by system
                 * camapign wallet withdraw
                 *
                 * campaign
                 * amount
                 */

                break;
            default:
                log.error("No matching case for function:"+function);
        }
        return null;
    }

    @Override
    public String query(ChaincodeStub stub, String function, String[] args) {
        // TODO: show personal crowd funding details, personal creating, personal funding.
        log.info("query, function:" + function);
        switch (function) {
            case "campaignInfo" :
                // load from campaign data from chaincode
                if (args.length == 1) {
                    log.info("CASE : campaignInfo, ID : " + args[0]);
                    return CampaignUtils.getInstance(stub).info(args[0]);
                }
                return "No data!";
            case "campaignOwner" :
                if (args.length == 1) {
                    log.info("CASE : campaignOwner, ID : " + args[0]);
                    return CampaignUtils.getInstance(stub).owner(args[0]);
                }
                return "No data!";
            case "campaignGoal" :
                if (args.length == 1) {
                    log.info("CASE : campaignGoal, ID : " + args[0]);
                    return CampaignUtils.getInstance(stub).goal(args[0]);
                }
                return "No data!";
            case "contribute" :
                // TODO: show campaign contibute detail
                return "";
            default:
                log.error("No matching case for function:"+function);
                return "";
        }
    }

    @Override
    public String getChaincodeID() {
        return "CrowdFunding";
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Crowdfunding! starting : " + args);
        log.info("Crowdfunding starting......");
        new CrowdFunding().start(args);
    }
}
