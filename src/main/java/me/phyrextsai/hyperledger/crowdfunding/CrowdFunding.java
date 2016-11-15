package me.phyrextsai.hyperledger.crowdfunding;

import me.phyrextsai.hyperledger.crowdfunding.data.Campaign;
import me.phyrextsai.hyperledger.crowdfunding.helper.CampaignHelper;
import me.phyrextsai.hyperledger.crowdfunding.utils.CampaignUtils;
import me.phyrextsai.hyperledger.crowdfunding.utils.ContributeUtils;
import org.hyperledger.java.shim.ChaincodeBase;
import org.hyperledger.java.shim.ChaincodeStub;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.protos.TableProto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                CampaignUtils.getInstance(stub).init();
                ContributeUtils.getInstance(stub).init();
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
                //CampaignUtils.getInstance(stub).create(args);
                createCampaign(stub, args);
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
                    return info(stub, args[0]);
                }
                return "No data!";
            case "campaignOwner" :
                if (args.length == 1) {
                    log.info("CASE : campaignOwner, ID : " + args[0]);
                    return owner(stub, args[0]);
                }
                return "No data!";
            case "campaignGoal" :
                if (args.length == 1) {
                    log.info("CASE : campaignGoal, ID : " + args[0]);
                    return goal(stub, args[0]);
                }
                return "No data!";
            case "campaignDetail" :
                // TODO: more information
                return stub.getState(CampaignHelper.TOTAL + ":" + args[0]);
            case "contribute" :
                // TODO: show campaign contibute detail
                return "";
            default:
                log.error("No matching case for function:"+function);
                break;
        }
        return "";
    }

    @Override
    public String getChaincodeID() {
        return "CrowdFunding";
    }

    private String createCampaign(ChaincodeStub stub, String[] args) {
        try {
            String campaignId = UUID.randomUUID().toString();
            String address = args[0];
            String info = args[1];
            Integer fundingAmount = Integer.parseInt(args[2]);
            Campaign campaign = new Campaign(campaignId, address, info, fundingAmount);
            CampaignHelper helper = new CampaignHelper();
            if (helper.insert(stub, campaign)) {
                log.info("Insert record, CampaignId : " + campaignId);
                return "{\"Data\":\"Create Campaign success, uuid : " + campaignId + ".\"}";
            } else {
                return "{\"Error\":\"Create Campaign failed.\"}";
            }
        } catch (NumberFormatException e) {
            return "{\"Error\":\"Expecting integer value for asset holding\"}";
        }
    }

    public String owner(ChaincodeStub stub, String campaignId) {
        return campaign(stub, campaignId, 1);
    }

    public String info(ChaincodeStub stub, String campaignId) {
        return campaign(stub, campaignId, 2);
    }

    public String goal(ChaincodeStub stub, String campaignId) {
        return campaign(stub, campaignId, 3);
    }

    private String campaign(ChaincodeStub stub, String campaignId, int column) {
        TableProto.Column queryCol = TableProto.Column.newBuilder()
                .setString(campaignId).build();
        List<TableProto.Column> key = new ArrayList<>();
        key.add(queryCol);
        try {
            TableProto.Row tableRow = stub.getRow(CampaignHelper.CAMPAIGN,key);
            if (tableRow.getSerializedSize() > 0) {
                // TODO: better use getColumnList to check
                for(TableProto.Column col : tableRow.getColumnsList()){
                    System.out.println("Column : " + col);
                }
                return tableRow.getColumns(column).getString();
            } else {
                return String.format("Can not found %s record!", CampaignHelper.CAMPAIGN);
            }
        } catch (Exception invalidProtocolBufferException) {
            invalidProtocolBufferException.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Crowdfunding! starting : " + args);
        log.info("Crowdfunding starting......");
        new CrowdFunding().start(args);
    }
}
