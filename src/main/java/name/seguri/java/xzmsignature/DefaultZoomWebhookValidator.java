package name.seguri.java.xzmsignature;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class DefaultZoomWebhookValidator implements ZoomWebhookValidator {
  private static final String ALGORITHM = "HmacSHA256";
  private final SecretKeySpec key;

  public DefaultZoomWebhookValidator(final String secretToken) {
    this.key = new SecretKeySpec(secretToken.getBytes(StandardCharsets.UTF_8), ALGORITHM);
  }

  @Override
  public String createHmacOrThrow(final String timestamp, final String body)
      throws NoSuchAlgorithmException, InvalidKeyException {
    final var message = "v0:" + timestamp + ":" + body;
    final Mac mac = Mac.getInstance(ALGORITHM);
    mac.init(key);
    final byte[] digest = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
    return "v0=" + bytesToHex(digest);
  }

  @Override
  public String createHmac(final String timestamp, final String body) {
    try {
      return createHmacOrThrow(timestamp, body);
    } catch (final NoSuchAlgorithmException | InvalidKeyException e) {
      return "";
    }
  }

  @Override
  public boolean validateOrThrow(final String signature, final String timestamp, final String body)
      throws InvalidKeyException, NoSuchAlgorithmException {
    return signature.equals(createHmacOrThrow(timestamp, body));
  }

  @Override
  public boolean validate(final String signature, final String timestamp, final String body) {
    try {
      return signature != null && validateOrThrow(signature, timestamp, body);
    } catch (final NoSuchAlgorithmException | InvalidKeyException e) {
      return false;
    }
  }

  private static String bytesToHex(final byte[] bytes) {
    return IntStream.range(0, bytes.length)
        .mapToObj(i -> String.format("%02x", bytes[i]))
        .collect(Collectors.joining());
  }
}
