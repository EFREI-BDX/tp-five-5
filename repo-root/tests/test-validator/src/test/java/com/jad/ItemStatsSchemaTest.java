package com.jad;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ItemStatsSchemaTest {

    private static final Pattern VALID_PATTERN = Pattern.compile("item-stats\\..*\\.valid\\.json");
    private static final Pattern INVALID_PATTERN = Pattern.compile("item-stats\\..*\\.invalid\\.json");

    static Stream<String> validFixtures() throws Exception {
        return ItemStatsSchemaTest.listFixturePaths(ItemStatsSchemaTest.VALID_PATTERN);
    }

    private static Stream<String> listFixturePaths(Pattern pattern) throws Exception {
        URL fixturesUrl = Objects.requireNonNull(
                ItemStatsSchemaTest.class.getClassLoader().getResource("fixtures"),
                "Ressource introuvable: fixtures");

        Path fixturesDir = Paths.get(fixturesUrl.toURI());

        return Files.list(fixturesDir)
                .filter(Files::isRegularFile)
                .map(path -> path.getFileName().toString())
                .filter(name -> pattern.matcher(name).matches())
                .sorted(Comparator.naturalOrder())
                .map(name -> "/fixtures/" + name);
    }

    static Stream<String> invalidFixtures() throws Exception {
        return ItemStatsSchemaTest.listFixturePaths(ItemStatsSchemaTest.INVALID_PATTERN);
    }

    @ParameterizedTest
    @MethodSource("validFixtures")
    void validFixturesShouldPassValidation(String fixturePath) throws Exception {
        Schema schema = Utils.loadSchema("/schemas/item-stats.schema.json");
        JSONObject data = Utils.loadJsonResource(fixturePath);
        assertDoesNotThrow(() -> schema.validate(data));
    }

    @ParameterizedTest
    @MethodSource("invalidFixtures")
    void invalidFixturesShouldFailValidation(String fixturePath) throws Exception {
        Schema schema = Utils.loadSchema("/schemas/item-stats.schema.json");
        JSONObject data = Utils.loadJsonResource(fixturePath);
        assertThrows(ValidationException.class, () -> schema.validate(data));
    }
}
