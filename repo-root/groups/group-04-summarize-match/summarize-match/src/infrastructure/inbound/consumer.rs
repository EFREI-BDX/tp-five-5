use crate::application::ApplicationService;
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::BaseEvent;
use crate::infrastructure::inbound::mapper_registry::MapperRegistry;
use crate::infrastructure::inbound::schema_registry::SchemaRegistry;
use serde_json::Value;

pub struct Consumer<S: ApplicationService> {
    service: S,
    base_schema_path: String,
    mapper_registry: MapperRegistry,
}

impl<S: ApplicationService> Consumer<S> {
    pub fn new(service: S, base_schema_path: impl Into<String>) -> Self {
        Self {
            service,
            base_schema_path: base_schema_path.into(),
            mapper_registry: MapperRegistry::with_defaults(),
        }
    }

    pub async fn process_json(&self, json_str: &str) -> Result<(), ValidationError> {
        let instance: Value = serde_json::from_str(json_str).map_err(ValidationError::from)?;

        let base_validator = SchemaRegistry::get_validator(&self.base_schema_path)?;
        if let Err(errors) = base_validator.validate(&instance) {
            let msgs: Vec<String> = errors.map(|e: jsonschema::ValidationError| e.to_string()).collect();
            return Err(ValidationError::Schema(msgs));
        }

        let event: BaseEvent = serde_json::from_value(instance).map_err(ValidationError::from)?;

        let payload_schema_path = build_payload_schema_path(&self.base_schema_path, &event.event_type);
        let payload_validator = SchemaRegistry::get_validator(&payload_schema_path)?;

        if let Err(errors) = payload_validator.validate(&event.payload) {
            let msgs: Vec<String> = errors.map(|e: jsonschema::ValidationError| e.to_string()).collect();
            return Err(ValidationError::Schema(msgs));
        }

        let domain_event = self.mapper_registry.map(&event)?;

        self.service
            .handle_event(domain_event)
            .await
            .map_err(ValidationError::from)
    }
}

fn build_payload_schema_path(base_schema_path: &str, event_type: &str) -> String {
    let schema_dir = std::path::Path::new(base_schema_path)
        .parent()
        .map(|p| p.to_path_buf())
        .unwrap_or_else(|| std::path::PathBuf::from("."));

    let payload_filename = event_type.to_lowercase().replace('_', "-") + ".schema.json";
    schema_dir.join(payload_filename).to_string_lossy().into_owned()
}

pub fn clear_schema_cache() {
    SchemaRegistry::clear();
}

pub fn schema_cache_len() -> usize {
    SchemaRegistry::len()
}

#[cfg(test)]
mod tests {
    use super::build_payload_schema_path;

    #[test]
    fn payload_schema_path_uses_event_type() {
        let path = build_payload_schema_path("/tmp/schemas/BaseEvent.schema.json", "MATCH_STARTED");
        assert!(path.ends_with("/tmp/schemas/match-started.schema.json"));
    }
}
