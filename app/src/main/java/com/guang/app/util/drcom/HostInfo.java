package com.guang.app.util.drcom;

import java.io.Serializable;

/**
 * Created by lin on 2017-01-10-010.
 * Modify by guang on 2017-03-21
 * hostos 机器操作系统 随意，已写死
 * hostname 机器名，可随意，转Byte[]后长度<=32就好，故写死在DrcomTask里了
 * ip 从第一个challenge包的返回里获取
 */
public class HostInfo implements Serializable{
    private final byte[] macBytes = new byte[6];  //mac地址，可随意
    private String username;    //学号
    private String password;    //密码

    public HostInfo(){}

    public HostInfo(String username, String password, String macHex) {
        this.username = username;
        this.password = password;
        checkHexToDashMac(macHex);
    }

    private void checkHexToDashMac(String mac) {
        if (mac.contains("-")) {
            mac = mac.replaceAll("-", "");
        }
        if (mac.contains(":")) {
            mac = mac.replaceAll(":", "");
        }
        if (mac.length() != 12) {
            throw new RuntimeException("MAC 地址格式错误。应为 xx:xx:xx:xx:xx:xx 或 xx-xx-xx-xx-xx-xx 或 xxxxxxxxxxxx 格式的 16 进制: " + mac);
        }
        try {
            Long.parseLong(mac, 16);
        } catch (NumberFormatException e) {
            throw new RuntimeException("MAC 地址格式错误。应为 xx:xx:xx:xx:xx:xx 或 xx-xx-xx-xx-xx-xx 或 xxxxxxxxxxxx 格式的 16 进制: " + mac);
        }
        StringBuilder sb = new StringBuilder(18);
        for (int i = 0; i < 12; i++) {
            sb.append(mac.charAt(i++)).append(mac.charAt(i)).append("-");
        }
        String macHexDash = sb.substring(0, 17);

        String[] split = macHexDash.split("-");
        for (int i = 0; i < split.length; i++) {
            macBytes[i] = (byte) Integer.parseInt(split[i], 16);
        }
    }


    public byte[] getMacBytes() {
        return macBytes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}