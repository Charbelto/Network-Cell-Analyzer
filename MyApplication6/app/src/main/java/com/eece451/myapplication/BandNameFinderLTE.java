package com.eece451.myapplication;

public class BandNameFinderLTE {

    public String getBandName(int frequency) {
        if (frequency >= 0 && frequency <= 599) {
            return "1(2100)";
        } else if (frequency >= 600 && frequency <= 1199) {
            return "2(1900 PCS)";
        } else if (frequency >= 1200 && frequency <= 1949) {
            return "3(1800+)";
        } else if (frequency >= 1950 && frequency <= 2399) {
            return "4(AWS-1)";
        } else if (frequency >= 2400 && frequency <= 2649) {
            return "5(850)";
        } else if (frequency >= 2650 && frequency <= 2749) {
            return "6(850 Japan)";
        } else if (frequency >= 2750 && frequency <= 3449) {
            return "7(2600)";
        } else if (frequency >= 3450 && frequency <= 3799) {
            return "8(900 GSM)";
        } else if (frequency >= 3800 && frequency <= 4149) {
            return "9(1800)";
        } else if (frequency >= 4150 && frequency <= 4749) {
            return "10(AWS-3)";
        } else if (frequency >= 4750 && frequency <= 4949) {
            return "11(1500 Lower)";
        } else if (frequency >= 5010 && frequency <= 5179) {
            return "12(700 a)";
        } else if (frequency >= 5180 && frequency <= 5279) {
            return "13(700 c)";
        } else if (frequency >= 5280 && frequency <= 5379) {
            return "14(700 PS)";
        } else if (frequency >= 5730 && frequency <= 5849) {
            return "17(700 b)";
        } else if (frequency >= 5850 && frequency <= 5999) {
            return "18(800 Lower)";
        } else if (frequency >= 6000 && frequency <= 6149) {
            return "19(800 Upper)";
        } else if (frequency >= 6150 && frequency <= 6449) {
            return "20(800 DD)";
        } else if (frequency >= 6450 && frequency <= 6599) {
            return "21(1500 Upper)";
        } else if (frequency >= 6600 && frequency <= 7399) {
            return "22(3500)";
        } else if (frequency >= 7500 && frequency <= 7699) {
            return "23(2000 S-band)";
        } else if (frequency >= 7700 && frequency <= 8039) {
            return "24(1600 L-band)";
        } else if (frequency >= 8040 && frequency <= 8689) {
            return "25(1900+)";
        } else if (frequency >= 8690 && frequency <= 9039) {
            return "26(850+)";
        } else if (frequency >= 9040 && frequency <= 9209) {
            return "27(800 SMR)";
        } else if (frequency >= 9210 && frequency <= 9659) {
            return "28(700 APT)";
        } else if (frequency >= 9660 && frequency <= 9769) {
            return "29(700 d SDL)";
        } else if (frequency >= 9770 && frequency <= 9869) {
            return "30(2300 WCS)";
        } else if (frequency >= 9870 && frequency <= 9919) {
            return "31(450)";
        } else if (frequency >= 9920 && frequency <= 10359) {
            return "32(1500 L-band)";
        } else if (frequency >= 36000 && frequency <= 36199) {
            return "33(TD 1900)";
        } else if (frequency >= 36200 && frequency <= 36349) {
            return "34(TD 2000)";
        } else if (frequency >= 36350 && frequency <= 36949) {
            return "35(TD PCS Lower)";
        } else if (frequency >= 36950 && frequency <= 37549) {
            return "36(TD PCS Upper)";
        } else if (frequency >= 37550 && frequency <= 37749) {
            return "37(TD PCS Center gap)";
        } else if (frequency >= 37750 && frequency <= 38249) {
            return "38(TD 2600)";
        } else if (frequency >= 38250 && frequency <= 38649) {
            return "39(TD 1900+)";
        } else if (frequency >= 38650 && frequency <= 39649) {
            return "40(TD 2300)";
        } else if (frequency >= 39650 && frequency <= 41589) {
            return "41(TD 2600+)";
        } else if (frequency >= 41590 && frequency <= 43589) {
            return "42(TD 3500)";
        } else if (frequency >= 43590 && frequency <= 45589) {
            return "43(TD 3700)";
        } else if (frequency >= 45590 && frequency <= 46589) {
            return "44(TD 700)";
        } else if (frequency >= 46590 && frequency <= 46789) {
            return "45(TD 1500)";
        } else if (frequency >= 46790 && frequency <= 54539) {
            return "46(TD Unlicensed)";
        } else if (frequency >= 54540 && frequency <= 55239) {
            return "47(TD V2X)";
        } else if (frequency >= 55240 && frequency <= 56739) {
            return "48(TD 3600)";
        } else if (frequency >= 56740 && frequency <= 58239) {
            return "49(TD 3600r)";
        } else if (frequency >= 58240 && frequency <= 59089) {
            return "50(TD 1500+)";
        } else if (frequency >= 59090 && frequency <= 59139) {
            return "51(TD 1500-)";
        } else if (frequency >= 59140 && frequency <= 60139) {
            return "52(TD 3300)";
        } else if (frequency >= 60140 && frequency <= 60254) {
            return "53(TD 2500)";
        } else if (frequency >= 60255 && frequency <= 60304) {
            return "54(TD 1700)";
        } else if (frequency >= 65536 && frequency <= 66435) {
            return "65(2100+)";
        } else if (frequency >= 66436 && frequency <= 67335) {
            return "66(AWS)";
        } else if (frequency >= 67336 && frequency <= 67535) {
            return "67(700 EU)";
        } else if (frequency >= 67536 && frequency <= 67835) {
            return "68(700 ME)";
        } else if (frequency >= 67836 && frequency <= 68335) {
            return "69(DL b38)";
        } else if (frequency >= 68336 && frequency <= 68585) {
            return "70(AWS-4)";
        } else if (frequency >= 68586 && frequency <= 68935) {
            return "71(600)";
        } else if (frequency >= 68936 && frequency <= 68985) {
            return "72(450 PMR/PAMR)";
        } else if (frequency >= 68986 && frequency <= 69035) {
            return "73(450 APAC)";
        } else if (frequency >= 69036 && frequency <= 69465) {
            return "74(L-band)";
        } else if (frequency >= 69466 && frequency <= 70315) {
            return "75(DL b50)";
        } else if (frequency >= 70316 && frequency <= 70365) {
            return "76(DL b51)";
        } else if (frequency >= 70366 && frequency <= 70545) {
            return "85(700 a+)";
        } else if (frequency >= 70546 && frequency <= 70595) {
            return "87(410)";
        } else if (frequency >= 70596 && frequency <= 70645) {
            return "88(410+)";
        } else if (frequency >= 70646 && frequency <= 70655) {
            return "103(NB-IoT)";
        } else if (frequency >= 70656 && frequency <= 70705) {
            return "106(900)";
        } else if (frequency >= 70656 && frequency <= 71055) {
            return "107(DL 600)";
        } else if (frequency >= 71056 && frequency <= 73335) {
            return "108(DL 500)";
        } else {
            return "N/A";
        }
    }
}
