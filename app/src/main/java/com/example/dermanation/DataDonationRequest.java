package com.example.dermanation;

public class DataDonationRequest {

    private String donationId;
    private String donationTitle;
    private String donationDetail;
    private String receiverName;
    private int targetAmount;
    private float progress;
    private int receiverImage;
    private int donationImage;

    public DataDonationRequest(String donationId, String donationTitle, String donationDetail, float progress, int receiverImage,  String receiverName, int donationImage, int targetAmount) {
        this.donationId = donationId;
        this.donationTitle = donationTitle;
        this.donationDetail = donationDetail;
        this.progress = progress;
        this.receiverImage = receiverImage;
        this.receiverName = receiverName;
        this.donationImage = donationImage;
        this.targetAmount = targetAmount;
    }

    public DataDonationRequest() {

    }

    public String getDonationTitle() {
        return donationTitle;
    }

    public String getDonationDetail() {
        return donationDetail;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public String getDonationId() {
        return donationId;
    }

    public float getProgress() {
        return progress;
    }

    public int getReceiverImage() {
        return receiverImage;
    }

    public int getDonationImage() {
        return donationImage;
    }
}
