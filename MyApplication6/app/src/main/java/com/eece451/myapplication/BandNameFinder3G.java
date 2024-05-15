package com.eece451.myapplication;

public class BandNameFinder3G {
    public String getBandName(int duplexSpacing) {
        String bandName;

        if (duplexSpacing > 10562 && duplexSpacing < 10838) {
            bandName = "1 (2100)";
        } else if (duplexSpacing > 9662 && duplexSpacing < 9938) {
            bandName = "2 (1900 PCS)";
        } else if (duplexSpacing > 1162 && duplexSpacing < 1438) {
            bandName = "3 (1800 DCS)";
        } else if (duplexSpacing > 1537 && duplexSpacing < 1813) {
            bandName = "4 (AWS-1)";
        } else if (duplexSpacing > 4357 && duplexSpacing < 4633) {
            bandName = "5 (850)";
        } else if (duplexSpacing > 4387 && duplexSpacing < 4663) {
            bandName = "6 (850 Japan)";
        } else if (duplexSpacing > 2237 && duplexSpacing < 2513) {
            bandName = "7 (2600)";
        } else if (duplexSpacing > 2937 && duplexSpacing < 3213) {
            bandName = "8 (900 GSM)";
        } else if (duplexSpacing > 9237 && duplexSpacing < 9513) {
            bandName = "9 (1800 Japan)";
        } else if (duplexSpacing > 3112 && duplexSpacing < 3388) {
            bandName = "10 (AWS-1+)";
        } else if (duplexSpacing > 3712 && duplexSpacing < 3988) {
            bandName = "11 (1500 Lower)";
        } else if (duplexSpacing > 3842 && duplexSpacing < 4118) {
            bandName = "12 (700 a)";
        } else if (duplexSpacing > 4017 && duplexSpacing < 4293) {
            bandName = "13 (700 c)";
        } else if (duplexSpacing > 4117 && duplexSpacing < 4393) {
            bandName = "14 (700 PS)";
        } else if (duplexSpacing > 712 && duplexSpacing < 988) {
            bandName = "19 (800 Japan)";
        } else if (duplexSpacing > 4512 && duplexSpacing < 4788) {
            bandName = "20 (800 DD)";
        } else if (duplexSpacing > 862 && duplexSpacing < 1138) {
            bandName = "21 (1500 Upper)";
        } else if (duplexSpacing > 4662 && duplexSpacing < 4938) {
            bandName = "22 (3500)";
        } else if (duplexSpacing > 5112 && duplexSpacing < 5388) {
            bandName = "25 (1900+)";
        } else if (duplexSpacing > 5762 && duplexSpacing < 6038) {
            bandName = "26 (850+)";
        } else if (duplexSpacing > 6617 && duplexSpacing < 6893) {
            bandName = "32 (1500 L-band)";
        } else if (duplexSpacing > 9500 && duplexSpacing < 9776) {
            bandName = "33 (TD 1900)";
        } else if (duplexSpacing > 10050 && duplexSpacing < 10326) {
            bandName = "34 (TD 2000)";
        } else if (duplexSpacing > 9250 && duplexSpacing < 9526) {
            bandName = "35 (TD PCS Lower)";
        } else if (duplexSpacing > 9650 && duplexSpacing < 9926) {
            bandName = "36 (TD PCS Upper)";
        } else if (duplexSpacing > 9550 && duplexSpacing < 9826) {
            bandName = "37 (TD PCS Center)";
        } else if (duplexSpacing > 12850 && duplexSpacing < 13126) {
            bandName = "38 (TD 2600)";
        } else if (duplexSpacing > 9400 && duplexSpacing < 9676) {
            bandName = "39 (TD 1900+)";
        } else if (duplexSpacing > 11500 && duplexSpacing < 11776) {
            bandName = "40 (TD 2300)";
        } else {
            bandName = "N/A";
        }
        return bandName;
    }
}