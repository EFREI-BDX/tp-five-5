use crate::infrastructure::error::ValidationError;
use jsonschema::JSONSchema;
use once_cell::sync::Lazy;
use serde_json::Value;
use std::collections::HashMap;
use std::fs;
use std::sync::{Arc, Mutex};

static SCHEMA_DOCS: Lazy<Mutex<HashMap<String, Box<Value>>>> =
    Lazy::new(|| Mutex::new(HashMap::new()));
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
        let schema_json: Value = serde_json::from_str(&schema_text).map_err(ValidationError::from)?;

        // jsonschema::compile may keep internal references to the schema Value.
        // Keep schema ownership in SCHEMA_DOCS while compiling a stable validator.
        let boxed_schema = Box::new(schema_json);
        let raw = Box::into_raw(boxed_schema);
        let schema_static: &'static Value = unsafe { &*raw };

        let compiled = JSONSchema::compile(schema_static)
            .map_err(|e| ValidationError::Other(e.to_string()))?;

        let boxed_schema = unsafe { Box::from_raw(raw) };

        {
            let mut docs = SCHEMA_DOCS.lock().unwrap();
            docs.insert(schema_path.to_owned(), boxed_schema);
        }

        let compiled = Arc::new(compiled);
        {
            let mut validators = SCHEMA_VALIDATORS.lock().unwrap();
            validators.insert(schema_path.to_owned(), compiled.clone());
        }

        Ok(compiled)
    }

    pub fn clear() {
        let mut docs = SCHEMA_DOCS.lock().unwrap();
        docs.clear();
        let mut vals = SCHEMA_VALIDATORS.lock().unwrap();
        vals.clear();
    }

    pub fn len() -> usize {
        let vals = SCHEMA_VALIDATORS.lock().unwrap();
        vals.len()
    }
}
