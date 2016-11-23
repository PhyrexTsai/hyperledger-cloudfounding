package me.phyrextsai.hyperledger.crowdfunding.helper;

import com.google.protobuf.InvalidProtocolBufferException;
import me.phyrextsai.hyperledger.crowdfunding.data.Contribute;
import me.phyrextsai.hyperledger.crowdfunding.interfaces.TableEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.java.shim.ChaincodeStub;
import org.hyperledger.protos.TableProto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by phyrextsai on 2016/11/16.
 */
public class ContributeHelper implements TableEntity<Contribute> {
    private static Log log = LogFactory.getLog(ContributeHelper.class);

    public final static String CONTRIBUTE = "Contribute";

    @Override
    public boolean create(ChaincodeStub stub) {
        List<TableProto.ColumnDefinition> cols = new ArrayList<TableProto.ColumnDefinition>();
        boolean success = true;
        cols.add(TableProto.ColumnDefinition.newBuilder()
                .setName("contributeId")
                .setKey(true)
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
            try {
                stub.deleteTable(CONTRIBUTE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (stub.validateTableName(CONTRIBUTE)) {
                success = stub.createTable(CONTRIBUTE, cols);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
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

    @Override
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

    public Contribute parse(ChaincodeStub stub, String[] args) {
        if (args.length != 3) {
            return null;
        }
        CampaignHelper campaignHelper = new CampaignHelper();
        String contributeId = UUID.randomUUID().toString();
        String campaignId = args[0];
        String contributor = args[1];
        String beneficiary = campaignHelper.get(stub, campaignId).getOwner();
        Integer amount = Integer.parseInt(args[2]);
        return new Contribute(contributeId, campaignId, contributor, beneficiary, amount, false);
    }

    @Override
    public Contribute get(ChaincodeStub stub, String key) {
        try {
            List<TableProto.Column> keys = new ArrayList<TableProto.Column>();
            TableProto.Column campaignId =
                    TableProto.Column.newBuilder()
                            .setString(key).build();
            keys.add(campaignId);
            TableProto.Row tableRow = stub.getRow(CONTRIBUTE, keys);
            return new Contribute(key,
                    tableRow.getColumns(1).getString(),
                    tableRow.getColumns(2).getString(),
                    tableRow.getColumns(3).getString(),
                    tableRow.getColumns(4).getUint32(),
                    tableRow.getColumns(5).getBool());
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

        TableProto.Column refund =
                TableProto.Column.newBuilder()
                        .setBool(contribute.isRefund()).build();

        cols.add(contributeId);
        cols.add(campaignId);
        cols.add(contributor);
        cols.add(beneficiary);
        cols.add(amount);
        cols.add(refund);

        TableProto.Row row = TableProto.Row.newBuilder()
                .addAllColumns(cols)
                .build();

        return row;
    }

    public Contribute doContribute(ChaincodeStub stub, String[] args) {
        Contribute contribute = parse(stub, args);
        contribute.setRefund(false);
        return contribute;
    }
}
