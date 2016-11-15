package me.phyrextsai.hyperledger.crowdfunding.utils;

import me.phyrextsai.hyperledger.crowdfunding.data.Campaign;
import me.phyrextsai.hyperledger.crowdfunding.data.Contribute;
import me.phyrextsai.hyperledger.crowdfunding.helper.CampaignHelper;
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
        CampaignHelper helper = new CampaignHelper();
        boolean campaignCreateSuccess = helper.create(stub);
        if (campaignCreateSuccess) {
            System.out.println(String.format("Create table [%s]", CampaignHelper.CAMPAIGN));
        } else {
            System.out.println(String.format("Table [%s] exist", CampaignHelper.CAMPAIGN));
        }
        return null;
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

}
