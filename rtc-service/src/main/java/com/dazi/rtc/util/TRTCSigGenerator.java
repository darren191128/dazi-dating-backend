package com.dazi.rtc.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.Deflater;

/**
 * TRTC用户签名生成器
 * 参考腾讯云TRTC官方SDK
 */
public class TRTCSigGenerator {
    
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    
    /**
     * 生成UserSig
     * @param sdkAppId 应用ID
     * @param secretKey 密钥
     * @param userId 用户ID
     * @param expire 过期时间（秒）
     * @return UserSig
     */
    public static String genUserSig(int sdkAppId, String secretKey, String userId, long expire) {
        long currentTime = System.currentTimeMillis() / 1000;
        long expireTime = currentTime + expire;
        
        String json = "{" +
            "\"TLS.identifier\":\"" + userId + "\"," +
            "\"TLS.appid\":" + sdkAppId + "," +
            "\"TLS.expire\":" + expire + "," +
            "\"TLS.expire_time\":" + expireTime + "," +
            "\"TLS.account_type\":0," +
            "\"TLS.sdk_appid\":" + sdkAppId + "," +
            "\"TLS.time\":" + currentTime + "," +
            "\"TLS.sr\":\"\"" +
            "}";
        
        String base64UserData = Base64.encodeBase64String(json.getBytes(StandardCharsets.UTF_8));
        
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            byte[] signatureBytes = mac.doFinal(base64UserData.getBytes(StandardCharsets.UTF_8));
            String signature = Base64.encodeBase64String(signatureBytes);
            
            String jsonWithSig = "{" +
                "\"TLS.identifier\":\"" + userId + "\"," +
                "\"TLS.appid\":" + sdkAppId + "," +
                "\"TLS.expire\":" + expire + "," +
                "\"TLS.expire_time\":" + expireTime + "," +
                "\"TLS.account_type\":0," +
                "\"TLS.sdk_appid\":" + sdkAppId + "," +
                "\"TLS.time\":" + currentTime + "," +
                "\"TLS.sr\":\"" + signature + "\"" +
                "}";
            
            byte[] compressed = compress(jsonWithSig.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64URLSafeString(compressed);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("生成UserSig失败", e);
        }
    }
    
    private static byte[] compress(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        
        byte[] buffer = new byte[data.length];
        int compressedSize = deflater.deflate(buffer);
        deflater.end();
        
        return Arrays.copyOf(buffer, compressedSize);
    }
}
