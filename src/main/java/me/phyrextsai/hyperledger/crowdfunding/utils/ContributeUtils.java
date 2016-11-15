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
 * Created by phyrextsai on 2016/11/13.
 */
public class ContributeUtils {

    private static Log log = LogFactory.getLog(ContributeUtils.class);

    private ChaincodeStub stub = null;
    private static ContributeUtils instance = null;

    private ContributeUtils (ChaincodeStub stub) {
        this.stub = stub;
    }

    public static ContributeUtils getInstance(ChaincodeStub stub) {
        if (instance == null) {
            instance = new ContributeUtils(stub);
        }
        return instance;
    }

    public String init() {
        Contribute contribute = new Contribute();
        boolean contributeCreateSuccess = contribute.create(stub);
        if (contributeCreateSuccess) {
            System.out.println(String.format("Create table [%s]", Contribute.CONTRIBUTE));
        } else {
            System.out.println(String.format("Table [%s] exist", Contribute.CONTRIBUTE));
        }
        return null;
    }

    public String doContribute(String[] args) {
        try {
            String contributeId = UUID.randomUUID().toString();
            String campaignId = args[0];
            String contributor = args[1];
            String beneficiary = args[2];
            Integer amount = Integer.parseInt(args[3]);
            Contribute contribute = new Contribute(contributeId, campaignId, contributor, beneficiary, amount, false);

            if (contribute.insert(stub, contribute)) {

                // TODO : contributer send the money to campaign owner
                Integer total = Integer.parseInt(stub.getState(campaignId)) + amount;
                stub.putState(CampaignHelper.TOTAL + ":" + campaignId, String.valueOf(total));

                log.info("Insert record, ContributeId : " + contributeId);
                return "{\"Data\":\"Contribute success, uuid : " + contributeId + ".\"}";
            } else {
                return "{\"Error\":\"Contribute failed.\"}";
            }
        } catch (NumberFormatException e) {
            return "{\"Error\":\"Expecting integer value for asset holding\"}";
        }
    }

    public String doRefund(String[] args) {
        try {
            String contributeId = args[0];
            String campaignId = args[1];
            String contributor = args[2];
            String beneficiary = args[3];
            Integer amount = Integer.parseInt(args[4]);
            Contribute contribute = new Contribute(contributeId, campaignId, contributor, beneficiary, amount, true);

            if (contribute.get(stub, contribute) == null) {
                return "{\"Error\":\"Can not find any Contribute data.\"}";
            }

            if (contribute.update(stub, contribute)) {

                // TODO : send the money from campaign owner to contributor
                Integer total = Integer.parseInt(stub.getState(campaignId)) - amount;
                stub.putState(CampaignHelper.TOTAL + ":" + campaignId, String.valueOf(total));

                return "{\"Data\":\"Refund success.\"}";
            } else {
                return "{\"Error\":\"Refund failed.\"}";
            }
        } catch (NumberFormatException e) {
            return "{\"Error\":\"Expecting integer value for asset holding\"}";
        }
    }

    private String contribute(String contributeId, int column) {
        TableProto.Column queryCol = TableProto.Column.newBuilder()
                .setString(contributeId).build();
        List<TableProto.Column> key = new ArrayList<>();
        key.add(queryCol);
        try {
            TableProto.Row tableRow = stub.getRow(Contribute.CONTRIBUTE, key);
            if (tableRow.getSerializedSize() > 0) {
                return tableRow.getColumns(column).getString();
            } else {
                return String.format("Can not found %s record!", Contribute.CONTRIBUTE);
            }
        } catch (Exception invalidProtocolBufferException) {
            invalidProtocolBufferException.printStackTrace();
        }
        return "";
    }

}
