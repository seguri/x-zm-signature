# x-zm-signature

A library for verifying Zoom webhook events through the `x-zm-signature` and `x-zm-request-timestamp` headers.

## Installation

The library is distributed on [Maven Central](https://central.sonatype.com/artifact/name.seguri.java/x-zm-signature). Add this dependency:

```
<dependency>
    <groupId>name.seguri.java</groupId>
    <artifactId>x-zm-signature</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Spring Boot usage

If you copy and paste the following code, make sure that `zoom.secret-token` is available, e.g. through `application.properties`, an environment variable `ZOOM_SECRET_TOKEN` , vault, k8s secrets, etc.

```java
@Configuration
@ConfigurationProperties(prefix = "zoom")
class ZoomConfiguration {
    private String secretToken;

    // Getters and setters

    @Bean
    ZoomWebhookValidator zoomWebhookValidator() {
        return new DefaultZoomWebhookValidator(secretToken);
    }
}

@RestController
class ZoomDeauthController {
    @Autowired ZoomWebhookValidator zoomWebhookValidator;

    @PostMapping("/zoom/deauth")
    void deauth(@RequestHeader("x-zm-signature") String signature,
                @RequestHeader("x-zm-request-timestamp") String timestamp,
                @RequestBody String body) {
        if (zoomWebhookValidator.validate(signature, timestamp, body)) {
            // Webhook request came from Zoom
        } else {
            // Webhook request did not come from Zoom
        }
    }
}
```

## Deploying to Sonatype

1. Follow the [Initial Setup](https://central.sonatype.org/publish/publish-guide/#initial-setup)
2. Replace `your-jira-id` and `your-jira-password` with your Jira credentials in `settings-sonatype.xml`
3. Set a proper version (not a SNAPSHOT) in `pom.xml`
4. Run `mvn -s settings-sonatype.xml clean deploy`

## GPG setup

1. Install GPG with `brew install gnupg`
2. Generate a key with `gpg --gen-key`
3. Set `gpg.keyname` as the output of `gpg --list-signatures --keyid-format 0xshort`
4. Submit your key to a key server with `gpg --keyserver hkps://keyserver.ubuntu.com --send-keys <KEY_ID>`

## See also

- [Zoom Developers](https://developers.zoom.us/docs/api/rest/webhook-reference/#verify-with-zooms-header)
- [webhook-sample](https://github.com/zoom/webhook-sample/blob/master/index.js#L31)
- [Sonatype: Publishing my artifact](https://central.sonatype.org/publish/publish-guide)
- [Sonatype: GPG signed components](https://central.sonatype.org/publish/publish-maven/#gpg-signed-components)
