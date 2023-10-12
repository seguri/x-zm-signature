package name.seguri.java.xzmsignature;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ZoomWebhookValidator {
  String createHmacOrThrow(String timestamp, String body)
      throws NoSuchAlgorithmException, InvalidKeyException;

  String createHmac(String timestamp, String body);

  boolean validateOrThrow(String signature, String timestamp, String body)
      throws InvalidKeyException, NoSuchAlgorithmException;

  boolean validate(String signature, String timestamp, String body);
}
