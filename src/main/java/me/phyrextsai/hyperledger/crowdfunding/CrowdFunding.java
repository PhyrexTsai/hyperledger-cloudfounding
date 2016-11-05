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
            case "funding":
                // TODO add money in it
                for (int i = 0; i < args.length; i += 2)
                    stub.putState(args[i], args[i + 1]);
                break;
            case "hello":
                System.out.println("hello invoked");
                log.info("hello invoked");
                break;
            default:
                log.info("This is a crowd funding contract! Please select a function.");
                break;
        }
        log.error("No matching case for function:"+function);
        return null;
    }

    @Override
    public String query(ChaincodeStub stub, String function, String[] args) {
        log.info("query, function:" + function);
        return null;
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
