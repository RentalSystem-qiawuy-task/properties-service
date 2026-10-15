package fptu.semester8.mss301.propertiesservice.features.building.api;

import fptu.semester8.mss301.propertiesservice.features.building.api.dto.BuildingRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuildingRequestValidationTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void acceptsCoordinatesWithinSupportedRanges() {
        var request = new BuildingRequest("Building", "Address", 1L,
                new BigDecimal("90"), new BigDecimal("180"), null);

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsCoordinatesOutsideSupportedRanges() {
        var request = new BuildingRequest("Building", "Address", 1L,
                new BigDecimal("90.0001"), new BigDecimal("-180.0001"), null);

        assertFalse(validator.validate(request).isEmpty());
    }
}
