use super::schema_registry::SchemaRegistry;
use crate::infrastructure::error::ValidationError;
use serde_json::Value;
use std::path::Path;

/// Port: validates a JSON value against a JSON Schema.
pub trait EventSchemaValidator: Send + Sync {
    fn validate_base(&self, instance: &Value) -> Result<(), ValidationError>;
    fn validate_payload(&self, payload: &Value, event_type: &str) -> Result<(), ValidationError>;
}

/// File-based implementation that reads schemas from disk and caches compiled validators.
pub struct FileBasedSchemaValidator {
    base_schema_path: String,
}

impl FileBasedSchemaValidator {
    pub fn new(base_schema_path: impl Into<String>) -> Self {
        Self {
            base_schema_path: base_schema_path.into(),
        }
    }

    fn payload_schema_path(&self, event_type: &str) -> String {
        let schema_dir = Path::new(&self.base_schema_path)
            .parent()
            .map(|p| p.to_path_buf())
            .unwrap_or_else(|| std::path::PathBuf::from("."));

        let filename = event_type.to_lowercase().replace('_', "-") + ".schema.json";
        schema_dir.join(filename).to_string_lossy().into_owned()
    }
}

impl EventSchemaValidator for FileBasedSchemaValidator {
    fn validate_base(&self, instance: &Value) -> Result<(), ValidationError> {
        let validator = SchemaRegistry::get_validator(&self.base_schema_path)?;
        if let Err(errors) = validator.validate(instance) {
            let msgs: Vec<String> = errors
                .map(|e: jsonschema::ValidationError| e.to_string())
                .collect();
            return Err(ValidationError::Schema(msgs));
        }
        Ok(())
    }

    fn validate_payload(&self, payload: &Value, event_type: &str) -> Result<(), ValidationError> {
        let path = self.payload_schema_path(event_type);
        let validator = SchemaRegistry::get_validator(&path)?;
        if let Err(errors) = validator.validate(payload) {
            let msgs: Vec<String> = errors
                .map(|e: jsonschema::ValidationError| e.to_string())
                .collect();
            return Err(ValidationError::Schema(msgs));
        }
        Ok(())
    }
}

#[cfg(test)]
mod tests {
    use super::FileBasedSchemaValidator;

    #[test]
    fn payload_schema_path_uses_event_type() {
        let v = FileBasedSchemaValidator::new("/tmp/schemas/BaseEvent.schema.json");
        let path = v.payload_schema_path("MATCH_STARTED");
        assert!(path.ends_with("/tmp/schemas/match-started.schema.json"));
    }
}
