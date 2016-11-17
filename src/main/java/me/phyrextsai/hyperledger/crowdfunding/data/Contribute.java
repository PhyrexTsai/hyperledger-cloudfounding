package me.phyrextsai.hyperledger.crowdfunding.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by phyrextsai on 2016/11/11.
 */
public class Contribute {

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
        this.setRefund(refund);
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

    public Boolean isRefund() {
        return refund;
    }

    public void setRefund(Boolean refund) {
        this.refund = refund;
    }
}
