package com.example.dermanation;

public class DonationFirebase {
    private String donationId;
    private String donationTitle;
    private int donationImage;
    private int targetAmount;
    private int progress;
    private int receiverImage;
    private String receiverName;
    private String donationDetail;

    public DonationFirebase() {
    }

    public DonationFirebase(String donationId, String donationTitle, int donationImage, int targetAmount, int progress, int receiverImage, String receiverName, String donationDetail) {
        this.donationId = donationId;
        this.donationTitle = donationTitle;
        this.donationImage = donationImage;
        this.targetAmount = targetAmount;
        this.progress = progress;
        this.receiverImage = receiverImage;
        this.receiverName = receiverName;
        this.donationDetail = donationDetail;
    }

    public String getDonationId() {
        return donationId;
    }

    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }

    public String getDonationTitle() {
        return donationTitle;
    }

    public void setDonationTitle(String donationTitle) {
        this.donationTitle = donationTitle;
    }

    public int getDonationImage() {
        return donationImage;
    }

    public void setDonationImage(int donationImage) {
        this.donationImage = donationImage;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(int targetAmount) {
        this.targetAmount = targetAmount;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(int receiverImage) {
        this.receiverImage = receiverImage;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getDonationDetail() {
        return donationDetail;
    }

    public void setDonationDetail(String donationDetail) {
        this.donationDetail = donationDetail;
    }
}
