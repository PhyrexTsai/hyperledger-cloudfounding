package me.phyrextsai.hyperledger.crowdfunding.data;

import com.google.protobuf.InvalidProtocolBufferException;
import me.phyrextsai.hyperledger.crowdfunding.interfaces.Table;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.java.shim.ChaincodeStub;
import org.hyperledger.protos.TableProto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phyrextsai on 2016/11/11.
 */
public class Contribute implements Table<Contribute> {

    private static Log log = LogFactory.getLog(Contribute.class);

    public final static String CONTRIBUTE = "Contribute";

    private String contributeId;
    private String campaignId;
    private String contributor;
    private String beneficiary;
    private Integer amount;
    private Boolean refund = false;

    public Contribute(String contributeId, String campaignId, String contributor, String beneficiary, Integer amount, Boolean refund) {
        this.setContributeId(contributeId);
        this.setCampaignId(campaignId);
        this.setContributor(contributor);
        this.setBeneficiary(beneficiary);
        this.setAmount(amount);
        this.isRefund(refund);
    }

    public String getContributeId() {
        return contributeId;
    }

    public void setContributeId(String contributeId) {
        this.contributeId = contributeId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Boolean getRefund() {
        return refund;
    }

    public void isRefund(Boolean refund) {
        this.refund = refund;
    }

    @Override
    public boolean create(ChaincodeStub stub) {
        List<TableProto.ColumnDefinition> cols = new ArrayList<TableProto.ColumnDefinition>();
        boolean success = true;
        cols.add(TableProto.ColumnDefinition.newBuilder()
                .setName("contributeId")
                .setKey(false)
                .setType(TableProto.ColumnDefinition.Type.STRING)
                .build()
        );

        cols.add(TableProto.ColumnDefinition.newBuilder()
                .setName("campaignId")
                .setKey(false)
                .setType(TableProto.ColumnDefinition.Type.STRING)
                .build()
        );

        cols.add(TableProto.ColumnDefinition.newBuilder()
                .setName("contributor")
                .setKey(false)
                .setType(TableProto.ColumnDefinition.Type.STRING)
                .build()
        );

        cols.add(TableProto.ColumnDefinition.newBuilder()
                .setName("beneficiary")
                .setKey(false)
                .setType(TableProto.ColumnDefinition.Type.STRING)
                .build()
        );

        cols.add(TableProto.ColumnDefinition.newBuilder()
                .setName("amount")
                .setKey(false)
                .setType(TableProto.ColumnDefinition.Type.UINT32)
                .build()
        );

        cols.add(TableProto.ColumnDefinition.newBuilder()
                .setName("refund")
                .setKey(false)
                .setType(TableProto.ColumnDefinition.Type.BOOL)
                .build()
        );

        try {
            if (stub.validateTableName(CONTRIBUTE)) {
                success = stub.createTable(CONTRIBUTE, cols);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean insert(ChaincodeStub stub, Contribute contribute) {
        boolean success = true;
        try {
            success = stub.insertRow(CONTRIBUTE, row(contribute));
            if (success){
                log.info("Row successfully inserted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean update(ChaincodeStub stub, Contribute contribute) {
        boolean success = true;
        try {
            success = stub.replaceRow(CONTRIBUTE, row(contribute));
            if (success){
                log.info("Row successfully updated");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public boolean delete(ChaincodeStub stub, Contribute contribute) {
        List<TableProto.Column> keys = new ArrayList<TableProto.Column>();
        TableProto.Column campaignId =
                TableProto.Column.newBuilder()
                        .setString(contribute.getCampaignId()).build();
        keys.add(campaignId);
        return stub.deleteRow(CONTRIBUTE, keys);
    }

    public TableProto.Row get(ChaincodeStub stub, Contribute contribute) {
        try {
            List<TableProto.Column> keys = new ArrayList<TableProto.Column>();
            TableProto.Column campaignId =
                    TableProto.Column.newBuilder()
                            .setString(contribute.getContributeId()).build();
            keys.add(campaignId);
            return stub.getRow(CONTRIBUTE, keys);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TableProto.Row row(Contribute contribute) {
        List<TableProto.Column> cols = new ArrayList<TableProto.Column>();
        TableProto.Column contributeId =
                TableProto.Column.newBuilder()
                        .setString(contribute.getContributeId()).build();

        TableProto.Column campaignId =
                TableProto.Column.newBuilder()
                        .setString(contribute.getCampaignId()).build();

        TableProto.Column contributor =
                TableProto.Column.newBuilder()
                        .setString(contribute.getContributor()).build();

        TableProto.Column beneficiary =
                TableProto.Column.newBuilder()
                        .setString(contribute.getBeneficiary()).build();

        TableProto.Column amount =
                TableProto.Column.newBuilder()
                        .setUint32(contribute.getAmount()).build();

        cols.add(contributeId);
        cols.add(campaignId);
        cols.add(contributor);
        cols.add(beneficiary);
        cols.add(amount);

        TableProto.Row row = TableProto.Row.newBuilder()
                .addAllColumns(cols)
                .build();

        return row;
    }
}
