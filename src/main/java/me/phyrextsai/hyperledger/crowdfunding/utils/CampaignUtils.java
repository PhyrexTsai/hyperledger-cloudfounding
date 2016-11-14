package me.phyrextsai.hyperledger.crowdfunding.utils;

import me.phyrextsai.hyperledger.crowdfunding.data.Campaign;
import me.phyrextsai.hyperledger.crowdfunding.data.Contribute;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.java.shim.ChaincodeStub;
import org.hyperledger.protos.TableProto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * - Contribute need stored in table
 * - Campaign need stored in table
 *
 *
 *
 * Created by phyrextsai on 2016/11/11.
 */
public class CampaignUtils {

    private static Log log = LogFactory.getLog(CampaignUtils.class);

    private ChaincodeStub stub = null;
    private static CampaignUtils instance = null;

    private CampaignUtils (ChaincodeStub stub) {
        this.stub = stub;
    }

    public static CampaignUtils getInstance(ChaincodeStub stub) {
        if (instance == null) {
            instance = new CampaignUtils(stub);
        }
        return instance;
    }

    public String init() {
        Campaign campaign = new Campaign();
        boolean campaignCreateSuccess = campaign.create(stub);
        if (campaignCreateSuccess) {
            System.out.println(String.format("Create table [%s]", Campaign.CAMPAIGN));
        } else {
            System.out.println(String.format("Table [%s] exist", Campaign.CAMPAIGN));
        }
        return null;
    }

    public String create(String[] args) {
        try {
            String campaignId = UUID.randomUUID().toString();
            String address = args[0];
            String info = args[1];
            Integer fundingAmount = Integer.parseInt(args[2]);
            Campaign campaign = new Campaign(campaignId, address, info, fundingAmount);

            if (campaign.insert(stub, campaign)) {
                log.info("Insert record, CampaignId : " + campaignId);
                return "{\"Data\":\"Create Campaign success, uuid : " + campaignId + ".\"}";
            } else {
                return "{\"Error\":\"Create Campaign failed.\"}";
            }
        } catch (NumberFormatException e) {
            return "{\"Error\":\"Expecting integer value for asset holding\"}";
        }
    }

    public String owner(String campaignId) {
        return campaign(campaignId, 1);
    }

    public String info(String campaignId) {
        return campaign(campaignId, 2);
    }

    public String goal(String campaignId) {
        return campaign(campaignId, 3);
    }

    private String campaign(String campaignId, int column) {
        TableProto.Column queryCol = TableProto.Column.newBuilder()
                .setString(campaignId).build();
        List<TableProto.Column> key = new ArrayList<>();
        key.add(queryCol);
        try {
            TableProto.Row tableRow = stub.getRow(Campaign.CAMPAIGN,key);
            if (tableRow.getSerializedSize() > 0) {
                // TODO: better use getColumnList to check
                for(TableProto.Column col : tableRow.getColumnsList()){
                    System.out.println("Column : " + col);
                }
                return tableRow.getColumns(column).getString();
            } else {
                return String.format("Can not found %s record!", Campaign.CAMPAIGN);
            }
        } catch (Exception invalidProtocolBufferException) {
            invalidProtocolBufferException.printStackTrace();
        }
        return "";
    }

}
