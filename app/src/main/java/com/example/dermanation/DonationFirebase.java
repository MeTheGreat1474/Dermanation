package com.example.dermanation;

public class DonationFirebase {
    private String donationId;
    private String donationTitle;
    private String donationImage;
    private int targetAmount;
    private int progress;
    private String receiverImage;
    private String receiverName;
    private String donationDetail;

    public DonationFirebase() {
    }

    public DonationFirebase(String donationId, String donationTitle, String donationImage, int targetAmount, int progress, String receiverImage, String receiverName, String donationDetail) {
        this.donationId = donationId;
        this.donationTitle = donationTitle;
        this.donationImage = donationImage;
        this.targetAmount = targetAmount;
        this.progress = progress;
        this.receiverImage = receiverImage;
        this.receiverName = receiverName;
        this.donationDetail = donationDetail;
    }

    public String getDonationDetail() {
        return donationDetail;
    }

    public void setDonationDetail(String donationDetail) {
        this.donationDetail = donationDetail;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(int targetAmount) {
        this.targetAmount = targetAmount;
    }

    public String getDonationImage() {
        return donationImage;
    }

    public void setDonationImage(String donationImage) {
        this.donationImage = donationImage;
    }

    public String getDonationTitle() {
        return donationTitle;
    }

    public void setDonationTitle(String donationTitle) {
        this.donationTitle = donationTitle;
    }

    public String getDonationId() {
        return donationId;
    }

    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }
}
