use crate::infrastructure::error::ValidationError;
use jsonschema::JSONSchema;
use once_cell::sync::Lazy;
use serde_json::Value;
use std::collections::HashMap;
use std::fs;
use std::sync::{Arc, Mutex};

static SCHEMA_VALIDATORS: Lazy<Mutex<HashMap<String, Arc<JSONSchema>>>> =
    Lazy::new(|| Mutex::new(HashMap::new()));

pub struct SchemaRegistry;

impl SchemaRegistry {
    pub fn get_validator(schema_path: &str) -> Result<Arc<JSONSchema>, ValidationError> {
        if let Some(cached) = {
            let validators = SCHEMA_VALIDATORS.lock().unwrap();
            validators.get(schema_path).cloned()
        } {
            return Ok(cached);
        }

        let schema_text = fs::read_to_string(schema_path).map_err(ValidationError::from)?;
        let schema_json: Value =
            serde_json::from_str(&schema_text).map_err(ValidationError::from)?;

        // Box::leak gives a &'static reference without unsafe.
        // Memory is intentionally permanent for the process lifetime (static cache).
        let static_ref: &'static Value = Box::leak(Box::new(schema_json));

        let compiled = JSONSchema::compile(static_ref)
            .map_err(|e| ValidationError::Other(e.to_string()))?;

        let compiled = Arc::new(compiled);
        {
            let mut validators = SCHEMA_VALIDATORS.lock().unwrap();
            validators.insert(schema_path.to_owned(), compiled.clone());
        }

        Ok(compiled)
    }

    pub fn clear() {
        let mut vals = SCHEMA_VALIDATORS.lock().unwrap();
        vals.clear();
    }

    pub fn len() -> usize {
        let vals = SCHEMA_VALIDATORS.lock().unwrap();
        vals.len()
    }
}
