package me.phyrextsai.hyperledger.crowdfunding.utils;

import me.phyrextsai.hyperledger.crowdfunding.data.Campaign;
import me.phyrextsai.hyperledger.crowdfunding.data.Contribute;
import org.hyperledger.java.shim.ChaincodeStub;

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

    public String init(String[] args) {
        Campaign campaign = new Campaign();
        boolean campaignCreateSuccess = campaign.create(stub);
        if (campaignCreateSuccess) {
            System.out.println(String.format("Create table [%s]", Campaign.CAMPAIGN));
        } else {
            System.out.println(String.format("Table [%s] exist", Campaign.CAMPAIGN));
        }
        Contribute contribute = new Contribute();
        boolean contributeCreateSuccess = contribute.create(stub);
        if (contributeCreateSuccess) {
            System.out.println(String.format("Create table [%s]", Contribute.CONTRIBUTE));
        } else {
            System.out.println(String.format("Table [%s] exist", Contribute.CONTRIBUTE));
        }
        return null;
    }

    public String createCampaign(String[] args) {
        try {
            String campaignId = stub.getUuid();
            String address = args[0];
            String info = args[1];
            Integer fundingAmount = Integer.parseInt(args[2]);
            Campaign campaign = new Campaign(campaignId, address, info, fundingAmount);

            if (campaign.insert(stub, campaign)) {
                return "{\"Data\":\"Create Campaign success.\"}";
            } else {
                return "{\"Error\":\"Create Campaign failed.\"}";
            }
        } catch (NumberFormatException e) {
            return "{\"Error\":\"Expecting integer value for asset holding\"}";
        }
    }

    public String doContribute(String[] args) {
        try {
            String contributeId = stub.getUuid();
            String campaignId = args[0];
            String contributor = args[1];
            String beneficiary = args[2];
            Integer amount = Integer.parseInt(args[3]);
            Contribute contribute = new Contribute(contributeId, campaignId, contributor, beneficiary, amount, false);

            if (contribute.insert(stub, contribute)) {
                return "{\"Data\":\"Create Contribute success.\"}";
            } else {
                return "{\"Error\":\"Create Contribute failed.\"}";
            }
        } catch (NumberFormatException e) {
            return "{\"Error\":\"Expecting integer value for asset holding\"}";
        }
    }

    public String doRefund(String[] args) {
        try {
            // TODO: delete
            String contributeId = stub.getUuid();
            String campaignId = args[0];
            String contributor = args[1];
            String beneficiary = args[2];
            Integer amount = Integer.parseInt(args[3]);
            Contribute contribute = new Contribute(contributeId, campaignId, contributor, beneficiary, amount, true);

            if (contribute.get(stub, contribute) == null) {
                return "{\"Error\":\"Can not find any Contribute data.\"}";
            }

            if (contribute.update(stub, contribute)) {
                return "{\"Data\":\"Update Contribute success.\"}";
            } else {
                return "{\"Error\":\"Update Contribute failed.\"}";
            }
        } catch (NumberFormatException e) {
            return "{\"Error\":\"Expecting integer value for asset holding\"}";
        }
    }

}
