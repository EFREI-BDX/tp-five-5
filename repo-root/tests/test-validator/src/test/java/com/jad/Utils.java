package com.jad;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

enum Utils {
    ;

    static Schema loadSchema(final String schemaPath) {
        InputStream inputStream = Utils.class.getResourceAsStream(schemaPath);
        if (inputStream == null) throw new IllegalArgumentException("Schema not found: " + schemaPath);
        JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
        return SchemaLoader.load(rawSchema);

    }

    static JSONObject loadJsonResource(String resourcePath) throws Exception {
        InputStream inputStream = Utils.class.getResourceAsStream(resourcePath);
        if (inputStream == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);
        return new JSONObject(new JSONTokener(inputStream));
    }
}
