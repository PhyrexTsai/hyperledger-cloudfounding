package me.phyrextsai.hyperledger.crowdfunding;

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
            case "create":
                // TODO: create a crowd funding
                log.info("create");
                break;
            case "funding":
                // TODO: add money in one of the crowd funding
                log.info("funding");
                break;
            case "hello":
                // Just print out hello
                System.out.println("hello invoked");
                log.info("hello invoked");
                break;
        }
        log.error("No matching case for function:"+function);
        return null;
    }

    @Override
    public String query(ChaincodeStub stub, String function, String[] args) {
        // TODO: show personal crowd funding details, personal creating, personal funding.
        log.info("query, function:" + function);
        return "Welcome crowd funding";
    }

    @Override
    public String getChaincodeID() {
        return "CrowdFunding";
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Crowd funding! starting " + args);
        log.info("starting");
        new CrowdFunding().start(args);
    }
}
