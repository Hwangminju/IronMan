package com.example.mjhwa.ironman;

public class EMGData {
    private String emg1;
    private String emg2;
    private String emg3;

    public String getEMG1() {
        return emg1;
    }

    public String getEMG2() {
        return emg2;
    }

    public String getEMG3() {
        return emg3;
    }

    public void setEMG1(String emg1) {
        this.emg1 = emg1;
    }

    public void setEMG2(String emg2) {
        this.emg2 = emg2;
    }

    public void setEMG3(String emg3) {
        this.emg3 = emg3;
    }

    @Override
    public String toString() {
        return "EMG1 : " + emg1 + " " + "EMG2 : " + emg2 + " " + "EMG3" + emg3;
    }
}
