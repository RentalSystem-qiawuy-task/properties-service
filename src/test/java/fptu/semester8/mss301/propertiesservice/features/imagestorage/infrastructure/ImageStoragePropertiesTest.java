package fptu.semester8.mss301.propertiesservice.features.imagestorage.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;

class ImageStoragePropertiesTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfiguration.class);

    @Test
    void bindsStoragePropertiesAndDefaultImageSize() {
        contextRunner.withPropertyValues(
                "storage.provider=r2",
                "storage.public-base-url=https://images.example.test",
                "storage.bucket=properties",
                "storage.endpoint=https://r2.example.test",
                "storage.region=auto",
                "storage.access-key=access",
                "storage.secret-key=secret"
        ).run(context -> {
            assertThat(context).hasNotFailed();
            var properties = context.getBean(ImageStorageProperties.class);
            assertThat(properties.provider()).isEqualTo("r2");
            assertThat(properties.publicBaseUrl()).isEqualTo("https://images.example.test");
            assertThat(properties.bucket()).isEqualTo("properties");
            assertThat(properties.endpoint()).isEqualTo("https://r2.example.test");
            assertThat(properties.region()).isEqualTo("auto");
            assertThat(properties.accessKey()).isEqualTo("access");
            assertThat(properties.secretKey()).isEqualTo("secret");
            assertThat(properties.maxSize()).isEqualTo(5_242_880L);
        });
    }

    @Test
    void bindsImageSizeOverride() {
        contextRunner.withPropertyValues("storage.image.max-size=1024")
                .run(context -> assertThat(context.getBean(ImageStorageProperties.class).maxSize()).isEqualTo(1024));
    }

    @Test
    void rejectsNonPositiveImageSize() {
        contextRunner.withPropertyValues("storage.image.max-size=0")
                .run(context -> assertThat(context).hasFailed());
    }

    @EnableConfigurationProperties(ImageStorageProperties.class)
    static class TestConfiguration {
        @org.springframework.context.annotation.Bean
        MethodValidationPostProcessor methodValidationPostProcessor() {
            return new MethodValidationPostProcessor();
        }
    }
}
