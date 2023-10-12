package name.seguri.java.xzmsignature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ZoomWebhookValidatorTest {
  private final ZoomWebhookValidator sut = new DefaultZoomWebhookValidator("secret");

  static Stream<Arguments> validInputs() {
    return Stream.of(
        Arguments.of(
            "v0=4fbb75111d585a4c58bc193aba49b007ddd7170f9191b0286d9413c1a07a44e8",
            "1697204592920",
            "{\"foo\": \"bar\"}"),
        Arguments.of(
            "v0=f46042d159828f787589618793a3f535b54de02da06ea660545e46c8960b70bf",
            "1697204592920",
            "{\"foo\":\"bar\"}"),
        Arguments.of("v0=48313323faaa3e2551387fedc6a9dbf72f93a6293033e7339e57c844dc1b107d", "", ""),
        Arguments.of(
            "v0=d91ecd49b2475959bcd627b71f5c2dd2c59b4a5712c7b2c41f1d9071422c378b", "null", "null"),
        Arguments.of(
            "v0=d91ecd49b2475959bcd627b71f5c2dd2c59b4a5712c7b2c41f1d9071422c378b", null, null));
  }

  @ParameterizedTest
  @MethodSource("validInputs")
  void createHmac_validInput_success(
      final String signature, final String timestamp, final String data) {
    assertEquals(signature, sut.createHmac(timestamp, data));
  }

  @ParameterizedTest
  @MethodSource("validInputs")
  void validate_validInput_success(
      final String signature, final String timestamp, final String data) {
    assertTrue(sut.validate(signature, timestamp, data));
  }
}
