use super::dto::BaseEvent;
use super::mapper_registry::MapperRegistry;
use super::schema_registry::SchemaRegistry;
use super::schema_validator::{EventSchemaValidator, FileBasedSchemaValidator};
use crate::application::ApplicationService;
use crate::infrastructure::error::ValidationError;
use serde_json::Value;

pub struct Consumer<S: ApplicationService> {
    service: S,
    validator: Box<dyn EventSchemaValidator>,
    mapper_registry: MapperRegistry,
}

impl<S: ApplicationService> Consumer<S> {
    /// Convenience constructor: creates default file-based validator and mapper registry.
    pub fn new(service: S, base_schema_path: impl Into<String>) -> Self {
        Self::with_components(
            service,
            FileBasedSchemaValidator::new(base_schema_path),
            MapperRegistry::with_defaults(),
        )
    }

    /// DIP-compliant constructor: all dependencies injected externally.
    pub fn with_components(
        service: S,
        validator: impl EventSchemaValidator + 'static,
        mapper_registry: MapperRegistry,
    ) -> Self {
        Self {
            service,
            validator: Box::new(validator),
            mapper_registry,
        }
    }

    pub async fn process_json(&self, json_str: &str) -> Result<(), ValidationError> {
        let instance: Value = serde_json::from_str(json_str).map_err(ValidationError::from)?;

        self.validator.validate_base(&instance)?;

        let event: BaseEvent =
            serde_json::from_value(instance).map_err(ValidationError::from)?;

        self.validator
            .validate_payload(&event.payload, &event.event_type)?;

        let domain_event = self.mapper_registry.map(&event)?;

        self.service
            .handle_event(domain_event)
            .await
            .map_err(ValidationError::from)
    }
}

pub fn clear_schema_cache() {
    SchemaRegistry::clear();
}

pub fn schema_cache_len() -> usize {
    SchemaRegistry::len()
}
